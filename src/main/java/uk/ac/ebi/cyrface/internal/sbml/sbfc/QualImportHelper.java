package uk.ac.ebi.cyrface.internal.sbml.sbfc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.ASTNode.Type;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.ext.qual.FunctionTerm;
import org.sbml.jsbml.ext.qual.Input;
import org.sbml.jsbml.ext.qual.Output;
import org.sbml.jsbml.ext.qual.QualConstant;
import org.sbml.jsbml.ext.qual.QualitativeModel;
import org.sbml.jsbml.ext.qual.QualitativeSpecies;
import org.sbml.jsbml.ext.qual.Sign;
import org.sbml.jsbml.ext.qual.Transition;
import org.sbml.jsbml.xml.stax.SBMLReader;

import uk.ac.ebi.cyrface.internal.sbml.simplenet.Network;
import uk.ac.ebi.cyrface.internal.sbml.simplenet.NetworkException;
import uk.ac.ebi.cyrface.internal.sbml.simplenet.NetworkImpl;
import uk.ac.ebi.cyrface.internal.sbml.simplenet.Node;

/**
 * Can read an SBML-qual file and convert it to a {@link Network}. 
 * This conversion is not completely generic, it relies on some conventions used by the path2models project. 
 */
public class QualImportHelper
{
	public static Network networkFromQual (File in) throws XMLStreamException, IOException, NetworkException
	{
		QualImportHelper qual2sif = new QualImportHelper();
		return qual2sif.doImport (in);
	}
	
	private List<String> warnings = new ArrayList<String>();
	
	private void warn(String msg)
	{
		warnings.add(msg);
//		System.out.println (msg);
	}
	

	
	private Network doImport(File in) throws XMLStreamException, IOException, NetworkException
	{
		System.out.println ("Reading: " + in.getAbsolutePath());
		SBMLDocument doc;
		doc = new SBMLReader().readSBML(in.getAbsolutePath());
		return doImport(doc);
	}

	public Network doImport(SBMLDocument doc) throws NetworkException
	{
		Model model = doc.getModel();
		QualitativeModel qualModel = (QualitativeModel) model.getExtension(QualConstant.namespaceURI);
		if (qualModel == null) throw new SBMLException("Could not find qual model in SBML file");
		Network net = doImport (qualModel);
		return net;
	}

	private Network doImport(QualitativeModel extendeModel)
	{
		Network result = new NetworkImpl();
	
		// first make suitable id's for all qualitative species.		
		for (QualitativeSpecies s : extendeModel.getListOfQualitativeSpecies())
		{
			List<String> keggBqbiolIsURIs = s.filterCVTerms(CVTerm.Qualifier.BQB_IS, "kegg");

            if (keggBqbiolIsURIs.size() == 1) 
            {
                String keggId = keggBqbiolIsURIs.get(0);
                Node n = result.createOrGetNode(s.getId());
                n.setAttribute("URI", keggId);
            }
            else if (keggBqbiolIsURIs.size() > 1)
            {
            	warn ("Warning: Multiple KEGG id found for QS " + s.getId());
            }
            else
            {
            	warn ("Warning: No KEGG id found for QS " + s.getId());
            }
		}
		
//		System.out.println ("Number of transitions: " + extendeModel.getListOfTransitions().size());
		for (Transition t : extendeModel.getListOfTransitions())
		{
			List<Node> inputs = new ArrayList<Node>();
			List<Node> outputs = new ArrayList<Node>();
			
			for (Input i : t.getListOfInputs())
			{
				QualitativeSpecies is = extendeModel.getQualitativeSpecies(i.getQualitativeSpecies());
				inputs.add(result.createOrGetNode(is.getId()));
			}
			for (Output o : t.getListOfOutputs())
			{
				QualitativeSpecies os = extendeModel.getQualitativeSpecies(o.getQualitativeSpecies());
				outputs.add(result.createOrGetNode(os.getId()));
			}
			
			if (inputs.size() == 1 && outputs.size() == 1)
			{
				Input i = t.getListOfInputs().get(0);

				Sign inputSign = Sign.unknown;
				if (i.isSetSign()) inputSign = i.getSign();
				String predicate = inputSign == Sign.negative ? "-1" : "1";				
				result.createEdge(inputs.get(0), outputs.get(0), predicate);
			}
			else if (outputs.size() == 1)
			{
				FunctionTerm foundFt = null;
				for (FunctionTerm ft : t.getListOfFunctionTerms())
				{
					if (ft.getResultLevel() == 1)
					{
						foundFt = ft;
						break;
					}
				}
				
				parseMath (foundFt.getMath(), result, outputs.get(0), inputs);
				
//				throw new UnsupportedOperationException("Can not convert non-linear transition yet");
			}
			else
			{
				warn ("Skipping transition " + t.getId() + " with multiple outputs.");
			}
		}
		
		return result;
	}
	
	private void parseMath(ASTNode math, Network result, Node parent, List<? extends Node> inputs)
	{
		switch (math.getType())
		{
			case RELATIONAL_GEQ:
			case RELATIONAL_LT:
			{
				// We expect the nodes below GEQ or LT to be an input id and a qualitative species id.
				// If not, emit a warning and bail out.
				ASTNode left = math.getLeftChild();
				ASTNode right = math.getRightChild();
				if (left.getType() != ASTNode.Type.NAME || right.getType() != ASTNode.Type.NAME) 
				{
					warn ("FunctionTerm too complex to represent in CellNOpt!");
					return;
				}
				// TODO: right now hard-coded on the left. Check that left and right aren't swapped. 
				Node input = result.createOrGetNode(left.getName());
				result.createEdge(input, parent, math.getType() == Type.RELATIONAL_GEQ ? "1" : "-1");	
			}
			break;
			case LOGICAL_AND:
				for (ASTNode child : math.getChildren())
				{
					// See if the math node carries a suitable Id. 
					// For round-trip conversion, it's useful to re-use the same gate number as we had before
					String nodeId = math.getId();
					// If the node Id doesn't start with "and", it's not suitable for our purposes and we have to generate a new id.
					if (nodeId != null && !nodeId.startsWith("and")) nodeId = null;
					if (nodeId == null)
					{
						// generate a suitable Node Id. 
						int count = 1;
						nodeId = "and" + count++;
						while (result.exists (nodeId)) nodeId = "and" + count++;
					}
					Node gate = result.createOrGetNode(nodeId);					
					result.createEdge (parent, gate, "1");
					parseMath (child, result, gate, inputs);
				}
			break;
			case LOGICAL_OR:	
				// OR is simple: each child represents an edge between parent and whatever comes further down the math expression.
				for (ASTNode child : math.getChildren())
				{
					parseMath (child, result, parent, inputs);
				}
			break;
			default:
				warn ("FunctionTerm too complex to represent in CellNOpt!");
				break;
		}
	}

}