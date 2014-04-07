package uk.ac.ebi.cyrface2.internal.examples.dataRail.menu;

import java.io.File;
import java.io.FileOutputStream;

import uk.ac.ebi.cyrface2.internal.CyActivator;
import uk.ac.ebi.cyrface2.internal.examples.dataRail.DataRailModel;
import uk.ac.ebi.cyrface2.internal.rinterface.RserveHandler;
import uk.ac.ebi.cyrface2.internal.utils.BioconductorPackagesEnum;

public class RFunctionsModel {
	
	private CyActivator activator;
	
	private RserveHandler handler;
	
	public static String varCnoList = "cnolist";
	public static String varNormCnoList = "normCnoList";
	public static String varPknModel = "pknModel";
	public static String varModel = "model";
	public static String varOptResult = "result";
	
	
	public RFunctionsModel (DataRailModel model, CyActivator activator) throws Exception {
		this.activator = activator;
		
		this.handler = new RserveHandler(activator);
		
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
		handler.execute(varCnoList + "=CNOlist(\"" + getWindowsCorrectPath(midasFilePath) + "\")");
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
		handler.execute(varNormCnoList + "=normaliseCNOlist(" + varCnoList + ", EC50Data=" + model.getEc50() + ", detection=" + model.getDetection() + ", saturation=" + (Double.isInfinite(model.getSaturation())? "Inf" : model.getSaturation()) + ")");
	}
	
	public void writeNormalizedMIDAS(String file) throws Exception {
		handler.execute("writeMIDAS("+varNormCnoList+",\""+getWindowsCorrectPath(file)+"\")");
	}
	
	public void writeOptimizedMIDAS(String file) throws Exception {
		handler.execute("writeMIDAS("+varNormCnoList+",\""+getWindowsCorrectPath(file)+"\")");
	}
	
	public void optmise(String pknModelFile) throws Exception {
		handler.execute(varPknModel + "=readSIF(\"" + getWindowsCorrectPath(pknModelFile) + "\")");
		
		handler.execute(varModel + "=preprocessing(" + varNormCnoList + "," + varPknModel + ")");
		
		handler.execute(varOptResult + "=gaBinaryT1(" + varNormCnoList + "," + varModel + ",verbose=FALSE)");
	}
	
	public File cutAndPlot() throws Exception {
		File plotImg = null;

		handler.execute("try(png(filename='"+ varOptResult +".png'))");
		handler.execute("cutAndPlot(" + varNormCnoList + "," + varModel + ",list(" + varOptResult + "$bString))");
		handler.execute("dev.off()");
		byte[] plot = handler.executeReceiveBytes("r=readBin('"+ varOptResult +".png','raw',1024*1024); unlink('"+ varOptResult +".png'); r");

		plotImg = File.createTempFile("CnoListPlot", ".png");
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
