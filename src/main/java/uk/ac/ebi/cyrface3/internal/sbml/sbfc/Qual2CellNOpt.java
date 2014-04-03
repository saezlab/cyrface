//package uk.ac.ebi.cyrface3.internal.sbml.sbfc;
//
//import org.sbfc.api.GeneralConverter;
//import org.sbfc.api.GeneralModel;
//import org.sbml.jsbml.SBMLDocument;
//import org.sbml.jsbml.xml.XMLException;
//
//import uk.ac.ebi.cyrface3.internal.sbml.simplenet.Network;
//import uk.ac.ebi.cyrface3.internal.sbml.simplenet.NetworkException;
//
//import com.thoughtworks.xstream.converters.ConversionException;
//
//public class Qual2CellNOpt extends GeneralConverter
//{
//	@Override
//	public GeneralModel convert(GeneralModel model) throws ConversionException
//	{
//		try
//		{
//			SBMLQualModel inModel = (SBMLQualModel)model;
//			
//			SBMLDocument doc = inModel.getSBMLDocument();
//
//			QualImportHelper qual2sif = new QualImportHelper();
//			Network net;
//			net = qual2sif.doImport(doc);
//			CellNOptModel outModel = new CellNOptModel(net);
//			return outModel;
//		}
//		catch (NetworkException e)
//		{
//			throw new ConversionException(e);
//		}
//		catch (XMLException e)
//		{
//			throw new ConversionException(e);
//		}
//	}
//
//	@Override
//	public String getResultExtension()
//	{
//		return ".sif";
//	}
//
//	@Override
//	public GeneralModel getInputModel()
//	{
//		return new SBMLQualModel();
//	}
//
//}
