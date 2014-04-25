package uk.ac.ebi.cyrface.internal.examples.dataRail.menu;

import java.io.File;
import java.io.FileOutputStream;

import org.cytoscape.service.util.CyServiceRegistrar;

import uk.ac.ebi.cyrface.internal.examples.dataRail.DataRailModel;
import uk.ac.ebi.cyrface.internal.rinterface.rserve.RserveHandler;
import uk.ac.ebi.cyrface.internal.utils.BioconductorPackagesEnum;

public class RFunctionsModel {
	
	private CyServiceRegistrar cyServiceRegistrar;
	
	private RserveHandler handler;
	
	public static String VAR_CNO_LIST = "cnolist";
	public static String VAR_NORM_CNO_LIST= "normCnoList";
	public static String VAR_PKN_MODEL = "pknModel";
	public static String VAR_MODEL = "model";
	public static String VAR_OPT_RESULT = "result";
	
	
	public RFunctionsModel (DataRailModel model, CyServiceRegistrar cyServiceRegistrar) throws Exception {
		this.cyServiceRegistrar = cyServiceRegistrar;
		
		this.handler = new RserveHandler(cyServiceRegistrar);
		
		initializePackages();
	}
	
	public void initializePackages() throws Exception {
		if (handler.isConnectionEstablished()) {
			handler.installBioconductorPackage(BioconductorPackagesEnum.RBGL);
			handler.installBioconductorPackage(BioconductorPackagesEnum.CELLNOPTR);
		
			handler.libraryPackage(BioconductorPackagesEnum.CELLNOPTR);
		}
	}
	
	public void loadMidasFile(String midasFilePath) throws Exception{
		handler.execute(VAR_CNO_LIST + "=CNOlist(\"" + getWindowsCorrectPath(midasFilePath) + "\")");
	}
	
	public File plotCnoList(String varCnoList) throws Exception {
		File plotImg = null;

		handler.execute("try(svg(filename='" + varCnoList + ".svg'))");
		handler.execute("plotCNOlist(" + varCnoList + ")");
		handler.execute("dev.off()");
		byte[] plot = handler.executeReceiveBytes("r=readBin('" + varCnoList + ".svg','raw',1024*1024); unlink('" + varCnoList +".svg'); r");

		plotImg = File.createTempFile(varCnoList, ".svg");
		FileOutputStream fos = new FileOutputStream(plotImg);

		fos.write(plot);

		fos.flush();
		fos.close();
			
		return plotImg;
	}
	
	public void normaliseCnoList (DataRailModel model) throws Exception {
		handler.execute(VAR_NORM_CNO_LIST + "=normaliseCNOlist(" + VAR_CNO_LIST + ", EC50Data=" + model.getEc50() + ", detection=" + model.getDetection() + ", saturation=" + (Double.isInfinite(model.getSaturation())? "Inf" : model.getSaturation()) + ")");
	}
	
	public void writeNormalizedMIDAS(String file) throws Exception {
		handler.execute("writeMIDAS("+VAR_NORM_CNO_LIST+",\""+getWindowsCorrectPath(file)+"\")");
	}
	
	public void writeOptimizedMIDAS(String file) throws Exception {
		handler.execute("writeMIDAS("+VAR_NORM_CNO_LIST+",\""+getWindowsCorrectPath(file)+"\")");
	}
	
	public void optmise(String pknModelFile) throws Exception {
		handler.execute(VAR_PKN_MODEL + "=readSIF(\"" + getWindowsCorrectPath(pknModelFile) + "\")");
		
		handler.execute(VAR_MODEL + "=preprocessing(" + VAR_NORM_CNO_LIST + "," + VAR_PKN_MODEL + ")");
		
		handler.execute(VAR_OPT_RESULT + "=gaBinaryT1(" + VAR_NORM_CNO_LIST + "," + VAR_MODEL + ",verbose=FALSE)");
	}
	
	public File cutAndPlot() throws Exception {
		File plotImg = null;

		handler.execute("try(svg(filename='"+ VAR_OPT_RESULT +".svg'))");
		handler.execute("cutAndPlot(" + VAR_NORM_CNO_LIST + "," + VAR_MODEL + ",list(" + VAR_OPT_RESULT + "$bString))");
		handler.execute("dev.off()");
		byte[] plot = handler.executeReceiveBytes("r=readBin('"+ VAR_OPT_RESULT +".svg','raw',1024*1024); unlink('"+ VAR_OPT_RESULT +".svg'); r");

		plotImg = File.createTempFile("CnoListPlot", ".svg");
		FileOutputStream fos = new FileOutputStream(plotImg);

		fos.write( plot );

		fos.flush();
		fos.close();
			
		return plotImg;
	}
	
	public static String getWindowsCorrectPath(String filePath) {
		if( System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			return filePath.replace('\\', '/');
		
		return filePath;
	}
}
