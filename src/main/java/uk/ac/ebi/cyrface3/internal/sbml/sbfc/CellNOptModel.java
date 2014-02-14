//package uk.ac.ebi.cyrface3.internal.sbml.sbfc;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.StringWriter;
//
//import uk.ac.ebi.cyrface3.internal.sbml.simplenet.Network;
//import uk.ac.ebi.cyrface3.internal.sbml.simplenet.SifFormat;
//
//public class CellNOptModel implements GeneralModel
//{
//	private Network net;
//	
//	public CellNOptModel()
//	{
//		net = null;
//	}
//	
//	public CellNOptModel(Network value)
//	{
//		net = value;
//	}
//
//	@Override
//	public void setModelFromFile(String fileName) throws ReadModelException
//	{
//		File f = new File (fileName);
//		
//		try
//		{
//			net = new SifFormat().readFromFile(f);
//			File idFile = new File (fileName.replace(".sif", ".ids"));
//			if (idFile.exists())
//			{
//				// TODO: also read network attributes.
//			}
//			
//		}
//		catch (IOException e)
//		{
//			throw new ReadModelException(e);
//		}
//	}
//
//	@Override
//	public void setModelFromString(String modelString)
//	{
//		throw new UnsupportedOperationException();
//		//TODO
//	}
//
//	@Override
//	public void modelToFile(String fileName) throws WriteModelException
//	{
//		try
//		{
//			new SifFormat().writeToFile(net, new File (fileName));
//		}
//		catch (IOException e)
//		{
//			throw new WriteModelException(e);
//		}
//	}
//
//	@Override
//	public String modelToString() throws WriteModelException
//	{
//		try
//		{
//			StringWriter sw = new StringWriter();
//			new SifFormat().write(net, sw);
//			return sw.toString();
//		}
//		catch (IOException e)
//		{
//			throw new WriteModelException(e);
//		}
//		
//	}
//
//	@Override
//	public String[] getExtensions()
//	{
//		return new String[] { ".sif" };
//	}
//
//	@Override
//	public boolean isCorrectType(File f)
//	{
//		// we have no way of being absolutely sure, so just return true...
//		return true;
//	}
//
//	@Override
//	public String getURI()
//	{
//		// no official type defined.
//		return null;
//	}
//
//	public Network getNetwork()
//	{
//		return net;
//	}
//
//}
