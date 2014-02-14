package uk.ac.ebi.cyrface3.internal.examples.dataRail.menu;

import java.io.File;
import java.io.FileOutputStream;

import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailModel;
import uk.ac.ebi.cyrface3.internal.rinterface.RserveHandler;
import uk.ac.ebi.cyrface3.internal.utils.BioconductorPackagesEnum;

public class RFunctionsModel {
	
	private RserveHandler handler;
	
	// R variables names
	public static String varCnoList = "cnolist";
	public static String varNormCnoList = "normCnoList";
	public static String varPknModel = "pknModel";
	public static String varModel = "model";
	public static String varOptResult = "result";
	
	public RFunctionsModel(DataRailModel model) throws Exception {
		try {
			this.handler = new RserveHandler();
		} catch (Exception e) {
			throw(e);
		}
	
		try{
			initializePackages();
		}catch(Exception e){
			throw(e);
		}
	}
	
	public void initializePackages() throws Exception{
		try{
			handler.installBioconductorPackage(BioconductorPackagesEnum.RBGL);
			handler.installBioconductorPackage(BioconductorPackagesEnum.CELLNOPTR);
			
			handler.libraryPackage(BioconductorPackagesEnum.CELLNOPTR);
		}catch(Exception e){
			throw(e);
		}
	}
	
	public void loadMidasFile(String midasFilePath) throws Exception{
		try{
			handler.execute(varCnoList + "=CNOlist(\"" + getWindowsCorrectPath(midasFilePath) + "\")");
		} catch(Exception e){
			throw(e);
		}
	}
	
	public File plotCnoList(String varCnoList) throws Exception{
		File plotImg = null;

		try{
			handler.execute("try(svg(filename='" + varCnoList + ".svg'))");
			handler.execute("plotCNOlist(" + varCnoList + ")");
			handler.execute("dev.off()");
			byte[] plot = handler.executeReceiveBytes("r=readBin('" + varCnoList + ".svg','raw',1024*1024); unlink('" + varCnoList +".svg'); r");

			plotImg = File.createTempFile(varCnoList, ".svg");
			FileOutputStream fos = new FileOutputStream(plotImg);

			fos.write( plot );

			fos.flush();
			fos.close();
		} catch(Exception e){
			throw(e);
		}
		
		return plotImg;
	}
	
	// EC50, detection and saturation
	public void normaliseCnoList(DataRailModel model) throws Exception{
		try{
			handler.execute(varNormCnoList + "=normaliseCNOlist(" + varCnoList + ", EC50Data=" + model.getEc50() + ", detection=" + model.getDetection() + ", saturation=" + (Double.isInfinite(model.getSaturation())? "Inf" : model.getSaturation()) + ")");
		} catch(Exception e){
			throw(e);
		}
	}
	
	public void writeNormalizedMIDAS(String file) throws Exception{
		try{
			handler.execute("writeMIDAS("+varNormCnoList+",\""+getWindowsCorrectPath(file)+"\")");
		} catch(Exception e){
			throw(e);
		}
	}
	
	public void writeOptimizedMIDAS(String file) throws Exception{
		try{
			handler.execute("writeMIDAS("+varNormCnoList+",\""+getWindowsCorrectPath(file)+"\")");
		} catch(Exception e){
			throw(e);
		}
	}
	
	public void optmise(String pknModelFile) throws Exception{
		try{
			handler.execute(varPknModel + "=readSIF(\"" + getWindowsCorrectPath(pknModelFile) + "\")");
			
			handler.execute(varModel + "=preprocessing(" + varNormCnoList + "," + varPknModel + ")");
			
			handler.execute(varOptResult + "=gaBinaryT1(" + varNormCnoList + "," + varModel + ",verbose=FALSE)");
			
		} catch(Exception e){
			throw(e);
		}
	}
	
	public File cutAndPlot() throws Exception{
		File plotImg = null;

		try{
			handler.execute("try(png(filename='"+ varOptResult +".png'))");
			handler.execute("cutAndPlot(" + varNormCnoList + "," + varModel + ",list(" + varOptResult + "$bString))");
			handler.execute("dev.off()");
			byte[] plot = handler.executeReceiveBytes("r=readBin('"+ varOptResult +".png','raw',1024*1024); unlink('"+ varOptResult +".png'); r");

			plotImg = File.createTempFile("CnoListPlot", ".png");
			FileOutputStream fos = new FileOutputStream(plotImg);

			fos.write( plot );

			fos.flush();
			fos.close();
		} catch(Exception e){
			throw(e);
		}
		
		return plotImg;
	}
	
	public static String getWindowsCorrectPath(String filePath){
		if( System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			return filePath.replace('\\', '/');
		
		return filePath;
	}
}
