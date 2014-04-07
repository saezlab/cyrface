package uk.ac.ebi.cyrface2.internal.sbml.sbfc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.ext.qual.FunctionTerm;
import org.sbml.jsbml.ext.qual.Input;
import org.sbml.jsbml.ext.qual.InputTransitionEffect;
import org.sbml.jsbml.ext.qual.Output;
import org.sbml.jsbml.ext.qual.OutputTransitionEffect;
import org.sbml.jsbml.ext.qual.QualConstant;
import org.sbml.jsbml.ext.qual.QualitativeModel;
import org.sbml.jsbml.ext.qual.QualitativeSpecies;
import org.sbml.jsbml.ext.qual.Sign;
import org.sbml.jsbml.ext.qual.Transition;
import org.sbml.jsbml.xml.stax.SBMLWriter;

import uk.ac.ebi.cyrface2.internal.sbml.simplenet.Edge;
import uk.ac.ebi.cyrface2.internal.sbml.simplenet.EdgeImpl;
import uk.ac.ebi.cyrface2.internal.sbml.simplenet.Network;
import uk.ac.ebi.cyrface2.internal.sbml.simplenet.Node;

/**
 *  Proof of concept SBML-qual generator.
 */
public class QualExportHelper
{
	SBMLDocument sbml;
	
	private interface Op
	{
		abstract String getOperation();
		abstract List<Op> getChildren();		
		abstract ASTNode asMath();
	}

	private static abstract class Gate implements Op
	{
	}
	
	private static class OrGate extends Gate
	{
		List<Op> children = new ArrayList<Op>();

		public OrGate (List<EdgeImpl> incoming, TransitionProxy parent)
		{
			for (Edge e : incoming)
			{
				children.add(parent.makeOp(e));
			}			
		}

		@Override
		public String getOperation()
		{
			return "or";
		}

		@Override
		public List<Op> getChildren()
		{
			return children;
		}

		public ASTNode asMath()
		{
			ASTNode result = new ASTNode(ASTNode.Type.LOGICAL_OR);
			for (Op child : getChildren())
			{
				result.addChild(child.asMath());
			}
			return result;
		}

	}

	private static class NotGate extends Gate
	{
		Op child = null;
		
		NotGate (Op c)
		{
			child = c;
		}

		@Override
		public String getOperation()
		{
			return "not";
		}

		@Override
		public List<Op> getChildren()
		{
			ArrayList<Op> result = new ArrayList<Op>();
			result.add(child);
			return result;
		}

		@Override
		public ASTNode asMath()
		{
			ASTNode result = new ASTNode(ASTNode.Type.LOGICAL_NOT);
			result.addChild(child.asMath());
			return result;
		}
	}
	
	private static class AndGate extends Gate
	{
		private Node gate;
		List<Op> children = new ArrayList<Op>();
		
		AndGate (Node n, TransitionProxy parent)
		{
			this.gate = n;
			
			for (Edge e : n.getIncoming())
			{
				children.add(parent.makeOp(e));
			}
		}
		
		@Override
		public String getOperation()
		{
			return "and";
		}

		@Override
		public List<Op> getChildren()
		{
			return children;
		}

		public ASTNode asMath()
		{
			ASTNode result = new ASTNode(ASTNode.Type.LOGICAL_AND);
			result.setId(gate.getId());
			for (Op child : getChildren())
			{
				result.addChild(child.asMath());
			}
			return result;
		}

	}
	
	private static class Leaf implements Op
	{
		private boolean isNegative;
		private TransitionProxy parent;
		
		Node n;
		
		Leaf (Node n, boolean isNegative, TransitionProxy parent)
		{
			this.n = n;
			this.isNegative = isNegative;
			this.parent = parent;
			
			parent.inputs.add(n);
			if (isNegative) parent.negativeInputs.add(n);
		}

		@Override
		public String getOperation()
		{
			return isNegative ? "lt" : "geq";
		}

		@Override
		public List<Op> getChildren()
		{
			return Collections.emptyList();
		}
		
//		public Xml asXml()
//		{
//			return Xml.elt (getOperation(), n.getId());
//		}


		@Override
		public ASTNode asMath()
		{
			// TODO Auto-generated method stub
			ASTNode result = new ASTNode(isNegative ? ASTNode.Type.RELATIONAL_LT : ASTNode.Type.RELATIONAL_GEQ);
			
			ASTNode n1 = new ASTNode(ASTNode.Type.NAME);
			// QS id
			n1.setName(n.getId());
			ASTNode n2 = new ASTNode(ASTNode.Type.NAME);
			// input id
			n2.setName("theta_" + parent.id + "_" + n.getId());
			
			result.addChild(n1);
			result.addChild(n2);
			
			return result;
		}
	}
	
	private static class TransitionProxy
	{
		private final String id;
		private static int counter = 1;
		private final Set<Node> inputs = new HashSet<Node>();
		private final Node output;
		private final Set<Node> negativeInputs = new HashSet<Node>();
		private Op operation;
		
		/**
		 * Create a transition for the given output node. 
		 * <p>
		 * The transition contians a boolean expression, by transforming the network that leads to the
		 * given destination node. If there are multiple edges pointing towards dest, they
		 * are combined into an OR operation. If incoming edges come from an AND gate, then
		 * this is added to the expression as well.
		 * <p>
		 * Each leaf node that is part of the tree pointing to dest, is added to the list of
		 * inputs of this transition.
		 * 
		 * @param output the transition output or destination node.
		 */
		TransitionProxy (Node output)
		{
			this.id = "t" + counter++;
			this.output = output;
			makeTree (output);
		}
		
		public String getId() { return id; }
		public Set<Node> getInputs() { return inputs; }
//		public Node getOutput () { return output; }
		public Op getOperation() { return operation; }
		public boolean isNegative(Node n)
		{
			return negativeInputs.contains(n);
		}
		
		private void makeTree (Node dest)
		{
			if (dest.getIncoming().size() == 1)
			{
				Edge e = dest.getIncoming().get(0);
				operation = makeOp (e);
			}
			else
			{
				operation = new OrGate (dest.getIncoming(), this);
			}
		}

		public Op makeOp(Edge e)
		{
			boolean isNegative = e.getPredictate().equals ("-1");
			Node n = e.getSrc();
			if (isGate(n))
			{
				if (isNegative)
					return new NotGate(new AndGate(n, this));
				else
					return new AndGate(n, this);
			}
			else
			{
				return new Leaf(n, isNegative, this);
			}
		}
		
	}
	
	Stack <Node> remainingGates = new Stack<Node>();
	
	List <Node> species = new ArrayList<Node>();
	List <TransitionProxy> transitions = new ArrayList<TransitionProxy>();
	Map <Node, TransitionProxy> transitionByOutput = new HashMap<Node, TransitionProxy>();
	Map <Node, TransitionProxy> transitionByGate = new HashMap<Node, TransitionProxy>();
	
	private void createSbml()
	{
		sbml = new SBMLDocument(3, 1);
		
		sbml.getSBMLDocumentAttributes().put(QualConstant.shortLabel + ":required", "true");
		sbml.addNamespace(QualConstant.shortLabel, "xmlns", QualConstant.namespaceURI);
		
		Model model = sbml.createModel("model1");

		QualitativeModel extendeModel = new QualitativeModel(model);
		model.addExtension(QualConstant.namespaceURI, extendeModel);
//				(QualitativeModel) model.getExtension(QualConstant.namespaceURI);
		Compartment mainCompartment = model.createCompartment("main");
		mainCompartment.setConstant(true);
		
		for (Node n : species)
		{
			QualitativeSpecies s = extendeModel.createQualitativeSpecies(n.getId());
			s.setCompartment(mainCompartment);
			s.setConstant(false);
			
			if (n.hasAttribute("URI"))
			{
//				TODO export identifiers
			}
		}
		
		int c = 1;
		for (TransitionProxy t : transitions)
		{
			Transition tr = extendeModel.createTransition();
			String trId = "t" + c++; 
			tr.setId(trId);
			for (Node n : t.inputs)
			{
				Input i = tr.createInput();
				i.setQualitativeSpecies(n.getId());
				i.setTransitionEffect(InputTransitionEffect.none);
				i.setSign(t.isNegative(n) ? Sign.negative : Sign.positive);
				i.setId("theta_" + trId + "_" + n.getId());
				i.setThresholdLevel(1);
			}			
			Node n = t.output;
			Output o = tr.createOutput();
			o.setQualitativeSpecies(n.getId());
			o.setTransitionEffect(OutputTransitionEffect.assignmentLevel);
			
			// define the default function term
			FunctionTerm df = new FunctionTerm();
			df.setResultLevel(0);
			df.setDefaultTerm(true);

			// define a function term
			FunctionTerm ft = new FunctionTerm();
			ft.setResultLevel(1);
			ft.setMath(t.operation.asMath());
			tr.getListOfFunctionTerms().add(df);
			tr.getListOfFunctionTerms().add(ft);
		}
	}
	
	public void fromSif(Network sif)
	{
		transformNetwork (sif);
		createSbml();
	}

	private static boolean isGate (Node n)
	{
		return n.getId().startsWith("and");
	}
	
	private void transformNetwork(Network sif)
	{
		for (Node n : sif.getNodes())
		{
			if (!n.getId().startsWith("and"))
			{
				species.add(n);
			}
		}

		for (Edge e : sif.getEdges())
		{
			Node output = e.getDest();
//			System.out.println (output.getId());
			if (!isGate(output))
			{
				if (transitionByOutput.containsKey(output))
				{
					continue;
				}
				else
				{
					TransitionProxy t = new TransitionProxy(output);					
					transitionByOutput.put(output, t);
					transitions.add (t);
				}
			}
		}		
	}

	private void toFile (File fout) throws IOException, SBMLException, XMLStreamException
	{
		new SBMLWriter().write(sbml, fout);
	}

	public static void qualFromNetwork (Network net, File out) throws SBMLException, IOException, XMLStreamException
	{
		QualExportHelper qual2sif = new QualExportHelper();
		qual2sif.doExport (net, out);		
	}

	private void doExport(Network net, File out) throws SBMLException, IOException, XMLStreamException
	{		
		QualExportHelper qm = new QualExportHelper();
		qm.fromSif(net);
		qm.toFile(out);	
	}

	public SBMLDocument getSbml()
	{
		return sbml;
	}

}
