//package uk.ac.ebi.cyrface3.internal.sbml.sbfc;
//
//import java.io.File;
//import java.io.IOException;
//
//import javax.xml.stream.XMLStreamException;
//
//import org.sbfc.api.GeneralModel;
//import org.sbfc.exceptions.ReadModelException;
//import org.sbfc.exceptions.WriteModelException;
//import org.sbml.jsbml.JSBML;
//import org.sbml.jsbml.Model;
//import org.sbml.jsbml.SBMLDocument;
//import org.sbml.jsbml.SBMLException;
//
//
///**
// * Class creating the link between GeneralModel and the SBML definition in JSBML
// *
// * @author Martijn
// * 	
// * Temporarily created custom version using a newer version of JSBML, which supports SBML-Qual
// * TODO: merge back with org.sbfc.model.sbml.SBMLModel.
// */
//public class SBMLQualModel implements GeneralModel {
//
//	private SBMLDocument document;
//	private String fileName;
//	
//	/**
//	 * Class constructor
//	 */
//	public SBMLQualModel() {
//		super();
//	}
//
//	public SBMLQualModel(SBMLDocument value)
//	{
//		document = value;
//	}
//	
//	public Model getModel() {
//		if (document != null) {
//			return document.getModel();
//		}
//		
//		return null;
//	}
//
//	public SBMLDocument getSBMLDocument() {
//		return document;
//	}
//	
//	public void setModelFromFile(String fileName) throws ReadModelException{
//		this.document = modelFromFile(fileName);
//	}
//	
//
//	public void setModelFromString(String modelString) throws ReadModelException {
//		this.document = modelFromString(modelString);
//	}
//
//	@Override
//	public String[] getExtensions() {
//		return new String[] { ".xml", ".sbml" };
//	}
//
//
//	public void modelToFile(String fileName) throws WriteModelException {
//
//		try {
//			JSBML.writeSBML(document, fileName);
//		} catch (XMLStreamException e) {
//			throw new WriteModelException(e);
//		} catch (IOException e) {
//			throw new WriteModelException(e);
//		} catch (SBMLException e) {
//			throw new WriteModelException(e);
//		}
//	}
//
//
//	public SBMLDocument modelFromFile(String fileName) throws ReadModelException {
//		
//		try {
//			document = JSBML.readSBML(fileName);
//			this.fileName = fileName;
//		} catch (XMLStreamException e) {
//			throw new ReadModelException(e);
//		} catch (IOException e) {
//			throw new ReadModelException(e);
//		}
//		return document;
//	}
//
//
//	public SBMLDocument modelFromString(String modelString) throws ReadModelException {
//		
//		try {
//			document = JSBML.readSBMLFromString(modelString);
//			fileName = null;
//		} catch (XMLStreamException e) {
//			throw new ReadModelException(e);
//		}
//		return document;
//	}
//
//	@Override
//	public String modelToString() throws WriteModelException {
//		String reString =null;
//		
//		try {
//			reString = JSBML.writeSBMLToString(document);
//		} catch (XMLStreamException e) {
//			throw new WriteModelException(e);
//		} catch (SBMLException e) {
//			throw new WriteModelException(e);
//		}
//
//		return reString;
//	}
//
//
//	/**
//	 * Returns the file name that was used to set this {@link GeneralModel} if
//	 * it was set using the {@link #setModelFromFile(String)} method, null otherwise.
//	 * 
//	 * @return the file name that was used to set this {@link GeneralModel} if
//	 * it was set using the {@link #setModelFromFile(String)} method, null otherwise.
//	 */
//	public String getModelFileName() {
//		return fileName;
//	}
//
//
//	@Override
//	public boolean isCorrectType(File f) {
//		return true;
//	}
//
//
//	@Override
//	public String getURI() {
//		return "http://identifiers.org/combine.specifications/sbml";
//	}
//}
