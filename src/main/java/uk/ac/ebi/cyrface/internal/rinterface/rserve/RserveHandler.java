package uk.ac.ebi.cyrface.internal.rinterface.rserve;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

import uk.ac.ebi.cyrface.internal.rinterface.RHandler;
import uk.ac.ebi.cyrface.internal.utils.BioconductorPackagesEnum;
import uk.ac.ebi.cyrface.internal.utils.RActionUtils;
import uk.ac.ebi.cyrface.internal.utils.Rutils;

public class RserveHandler extends RHandler {

	private RConnection connection = null;

	
	public RserveHandler (CyServiceRegistrar cyServiceRegistrar) throws Exception {
		super(cyServiceRegistrar, "Rserve");
		
		establishConnection();
	}

	public void closeConnection () throws Exception {
		if (connection == null) {
			connection.close();
			connection = null;
		}
	}

	private void establishConnection() throws Exception {
		if (!isRserveRunning()) {
			StartRServeTask startTask = new StartRServeTask();
			StartRServeTaskObservable startTaskObservable = new StartRServeTaskObservable();
			
			cyServiceRegistrar.getService(DialogTaskManager.class).execute(new TaskIterator(startTask), startTaskObservable);
			while (!startTaskObservable.isComplete()) { Thread.sleep(100); }
		}
		
		connection = new RConnection();
	}

	public boolean checkInstalledPackge(BioconductorPackagesEnum packageName) throws Exception {
		return checkInstalledPackge(packageName.getPackageName());
	}

	public boolean checkInstalledPackge(String packageName) throws Exception {
		REXP rexp = connection.parseAndEval("is.installed <- function(mypkg) is.element(mypkg, installed.packages()[,1])");
		rexp = connection.parseAndEval("is.installed('"+ packageName  +"')");

		return (rexp.asInteger() == 1) ? true : false;			
	}


	@Override
	public void installBioconductorPackage(BioconductorPackagesEnum packageName) throws Exception {
		installBioconductorPackage(packageName.getPackageName());
	}

	public void installBioconductorPackage(String packageName) throws Exception {
		if (!checkInstalledPackge(packageName)) {
			connection.parseAndEval("source(\"http://bioconductor.org/biocLite.R\")");
			connection.parseAndEval("biocLite('" + packageName + "', ask = F)");
		}

		libraryPackage(packageName);
	}
	
	public void upgradeBioconductor() throws Exception {
		connection.parseAndEval("source(\"http://bioconductor.org/biocLite.R\")");
		connection.parseAndEval("biocLite(ask = F)");
		connection.parseAndEval("biocLite('BiocUpgrade', ask=F)");
	}

	@Override
	public void libraryPackage(BioconductorPackagesEnum packageName) throws Exception {
		libraryPackage(packageName.getPackageName());
	}

	public void libraryPackage(String packageName) throws Exception {
		REXP rexp = connection.parseAndEval( "library(\"" + packageName + "\")" );

	}

	@Override
	public void execute(String command) throws Exception {
		REXP rexp = connection.parseAndEval(command);

	}
	
	public File executeReceivePlotFile (String command, String plotName) throws Exception {
		execute("try(" + RActionUtils.PLOT_DEVICE + "(filename='" + plotName + "." + RActionUtils.PLOT_DEVICE + "'))");
		execute(command);
		execute("dev.off()");
		
		byte[] plot = executeReceiveBytes("r=readBin('" + plotName + "." + RActionUtils.PLOT_DEVICE + "','raw',1024*1024); unlink('test." + RActionUtils.PLOT_DEVICE + "'); r");
		
		File plotImg = File.createTempFile("plotName", "." + RActionUtils.PLOT_DEVICE);
		FileOutputStream fos = new FileOutputStream(plotImg);

		fos.write( plot );

		fos.flush();
		fos.close();
		
		return plotImg;
	}
	
	public int executeReceiveInteger(String command) throws Exception {
		REXP rexp = connection.parseAndEval(command);

		return rexp.asInteger();

	}

	public int[] executeReceiveIntegers(String command) throws Exception {
		REXP rexp = connection.parseAndEval(command);

		return rexp.asIntegers();

	}

	public double executeReceiveDouble(String command) throws Exception {
		REXP rexp = connection.parseAndEval(command);

		return rexp.asDouble();

	}

	public double[] executeReceiveDoubles(String command) throws Exception {
		REXP rexp = connection.parseAndEval(command);

		return rexp.asDoubles();

	}

	public String executeReceiveString(String command) throws Exception {
		REXP rexp = connection.parseAndEval(command);
		return rexp.asString();

	}

	public String[] executeReceiveStrings(String command) throws Exception {
		REXP rexp = connection.parseAndEval(command);
		return rexp.asStrings();

	}

	public byte[] executeReceiveBytes(String command) throws Exception {
		REXP rexp = connection.parseAndEval(command);
		return rexp.asBytes();

	}

	public void storeVariable(String rValues, String varName) throws Exception {
		REXP rexp = connection.parseAndEval(varName +"="+ rValues);

	}

	public void storeVariable(double[] values, String varName) throws Exception {
		REXP rexp = connection.parseAndEval(varName +"="+ Rutils.getAsRList(values)); // parse Sting and evaluate in R

	}

	public void storeVariable(int[] values, String varName) throws Exception {
		REXP rexp = connection.parseAndEval(varName +"="+ Rutils.getAsRList(values));

	}

	public void storeVariable(float[] values, String varName) throws Exception {
		REXP rexp = connection.parseAndEval(varName +"="+ Rutils.getAsRList(values));
	}

	public void executeCheckExceptions(String command) throws Exception {
		REXP rexp = connection.parseAndEval("try(" + command + ",silent=TRUE)");

		if (rexp.inherits("try-error")) throw new Exception(rexp.asString(), new Throwable("Error running " + command.substring(0, 20) + "! Check if the selected model and data are the correct ones."));
	}

	public String[] executeCaptureOutput(String command) throws Exception {
		REXP rexp = connection.parseAndEval( "output <- capture.output(" + command + ")");
		return rexp.asStrings();
	}
	
	public String executeParseOutput(String command) throws Exception {
		return RActionUtils.processROutput(executeCaptureOutput(command));
	}
	
	public File getFile (String fileName) throws Exception {
		String readBinCommand = "r <- readBin('" + fileName + "', 'raw', 1024*1024); r";
		byte[] fileByteArray = executeReceiveBytes(readBinCommand);
		
		String prefix = FilenameUtils.removeExtension(fileName);
		String sufix = "." + FilenameUtils.getExtension(fileName);
		
		File file = File.createTempFile(prefix, sufix);
		FileOutputStream fos = new FileOutputStream(file);

		fos.write(fileByteArray);

		fos.flush();
		fos.close();
		
		return file;
	}

	public boolean isConnectionEstablished(){
		return connection == null ? false : true;
	}
	
	public boolean isRserveRunning () {
		try {
			RConnection connection = new RConnection();
			connection.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}