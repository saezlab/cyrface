package uk.ac.ebi.cyrface2.internal.rinterface;

import org.cytoscape.work.TaskIterator;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

import uk.ac.ebi.cyrface2.internal.CyActivator;
import uk.ac.ebi.cyrface2.internal.utils.BioconductorPackagesEnum;
import uk.ac.ebi.cyrface2.internal.utils.Rutils;

public class RserveHandler extends RHandler{

	private static RConnection connection = null;
	private CyActivator activator;

	
	public RserveHandler(CyActivator activator) {
		super("Rserve");
		
		this.activator = activator;
		
		establishConnection();
	}

	/**
	 * Closes the connection with R, if not possible an exception is thrown.
	 * 
	 * @throws Exception
	 */
	public void closeConnection() {
		try {
			if( connection == null ){
				connection.close();
				connection = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Establishes connection with R, if not established yet. An exception is thrown if not possible.
	 * 
	 * @throws Exception
	 */
	public void establishConnection() {
		try {
			
			activator.dialogTaskManager.execute(new TaskIterator(new StartServeTask()));

			if( connection == null ){
				connection = new RConnection();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkInstalledPackge(BioconductorPackagesEnum packageName) throws Exception{
		return checkInstalledPackge(packageName.getPackageName());
	}

	public boolean checkInstalledPackge(String packageName) throws Exception{
		REXP rexp = null;
		rexp = connection.parseAndEval("is.installed <- function(mypkg) is.element(mypkg, installed.packages()[,1])");
		rexp = connection.parseAndEval("is.installed('"+ packageName  +"')");

		boolean isPackageInstalled = (rexp.asInteger() == 1) ? true : false;

		return isPackageInstalled;			
	}


	@Override
	public void installBioconductorPackage(BioconductorPackagesEnum packageName) throws Exception{
		installBioconductorPackage(packageName.getPackageName());
	}

	public void installBioconductorPackage(String packageName) throws Exception{
		REXP rexp = null;
		
		rexp = connection.parseAndEval("source(\"http://bioconductor.org/biocLite.R\")");

		if( !checkInstalledPackge(packageName) )
			rexp = connection.parseAndEval("biocLite('" + packageName + "', suppressUpdates=TRUE)");

		libraryPackage(packageName);
	}

	@Override
	public void libraryPackage(BioconductorPackagesEnum packageName) throws Exception {
		libraryPackage(packageName.getPackageName());
	}

	public void libraryPackage(String packageName) throws Exception{
		REXP rexp = connection.parseAndEval( "library(\"" + packageName + "\")" );

	}

	@Override
	public void execute(String command) throws Exception{
		REXP rexp = connection.parseAndEval(command);

	}

	public int executeReceiveInteger(String command) throws Exception{
		REXP rexp = connection.parseAndEval(command);

		return rexp.asInteger();

	}

	public int[] executeReceiveIntegers(String command) throws Exception{
		REXP rexp = connection.parseAndEval(command);

		return rexp.asIntegers();

	}

	public double executeReceiveDouble(String command) throws Exception{
		REXP rexp = connection.parseAndEval(command);

		return rexp.asDouble();

	}

	public double[] executeReceiveDoubles(String command) throws Exception{
		REXP rexp = connection.parseAndEval(command);

		return rexp.asDoubles();

	}

	public String executeReceiveString(String command) throws Exception{
		REXP rexp = connection.parseAndEval(command);
		return rexp.asString();

	}

	public String[] executeReceiveStrings(String command) throws Exception{
		REXP rexp = connection.parseAndEval(command);
		return rexp.asStrings();

	}

	public byte[] executeReceiveBytes(String command) throws Exception{
		REXP rexp = connection.parseAndEval(command);
		return rexp.asBytes();

	}

	/**
	 * rValues MUST BE a list already formated as an R list (e.g. c(1,2,3,4)) 
	 * 
	 * or simply a value (e.g. 4.0)
	 * 
	 * @param rValues
	 * @return
	 */
	public void storeVariable(String rValues, String varName)throws Exception{
		REXP rexp = connection.parseAndEval(varName +"="+ rValues);

	}

	public void storeVariable(double[] values, String varName)throws Exception{
		REXP rexp = connection.parseAndEval(varName +"="+ Rutils.getAsRList(values)); // parse Sting and evaluate in R

	}

	public void storeVariable(int[] values, String varName)throws Exception{
		REXP rexp = connection.parseAndEval(varName +"="+ Rutils.getAsRList(values));

	}

	public void storeVariable(float[] values, String varName)throws Exception{
		REXP rexp = connection.parseAndEval(varName +"="+ Rutils.getAsRList(values));
	}

	public void executeCheckExceptions(String command) throws Exception{
		REXP rexp = connection.parseAndEval("try(" + command + ",silent=TRUE)");

		if (rexp.inherits("try-error")) throw new Exception(rexp.asString(), new Throwable("Error running " + command.substring(0, 20) + "! Check if the selected model and data are the correct ones."));
	}

	public String[] executeCaptureOutput(String command) throws Exception{
		REXP rexp = null;

		rexp = connection.parseAndEval( "output <- capture.output(" + command + ")");
		return rexp.asStrings();

	}

	public boolean isConnectionEstablished(){
		return connection == null ? false : true;
	}

}