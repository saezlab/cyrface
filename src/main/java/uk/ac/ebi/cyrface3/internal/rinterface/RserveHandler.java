package uk.ac.ebi.cyrface3.internal.rinterface;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

import uk.ac.ebi.cyrface3.internal.utils.BioconductorPackagesEnum;
import uk.ac.ebi.cyrface3.internal.utils.ExceptionHandler;
import uk.ac.ebi.cyrface3.internal.utils.Rutils;

public class RserveHandler extends RHandler{

	private static RConnection connection = null;
	
	public RserveHandler() {
		super("Rserve");
		
		try{
			establishConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the connection with R, if not possible an exception is thrown.
	 * 
	 * @throws Exception
	 */
	public void closeConnection() throws Exception{
		try {
			if( connection == null ){
				connection.close();
				connection = null;
			}
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, null, e );
		}
	}
	
	/**
	 * Establishes connection with R, if not established yet. An exception is thrown if not possible.
	 * 
	 * @throws Exception
	 */
	public void establishConnection() throws Exception{
		REXP rexp = null;
		try {
			new Rserve();
			if( connection == null ){
				connection = new RConnection();
			}

			installBioconductor();
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public boolean checkInstalledPackge(BioconductorPackagesEnum packageName) throws Exception{
		return checkInstalledPackge(packageName.getPackageName());
	}
	
	public boolean checkInstalledPackge(String packageName) throws Exception{
		REXP rexp = null;
		try {
			rexp = connection.parseAndEval("is.installed <- function(mypkg) is.element(mypkg, installed.packages()[,1])");
			rexp = connection.parseAndEval("is.installed('"+ packageName  +"')");
			
			boolean isPackageInstalled = (rexp.asInteger() == 1) ? true : false;
			
			return isPackageInstalled;			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}

	public void installBioconductor() throws Exception{
		REXP rexp = null;
		
		try {
			if( !checkInstalledPackge(BioconductorPackagesEnum.BIOCONDUCTOR.getPackageName()) ){
				rexp = connection.parseAndEval("source(\"http://www.bioconductor.org/biocLite.R\")");
			}
				
			libraryPackage(BioconductorPackagesEnum.BIOCONDUCTOR.getPackageName());
			rexp = connection.parseAndEval("library('BiocInstaller')");
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}

	@Override
	public void installBioconductorPackage(BioconductorPackagesEnum packageName) throws Exception{
		installBioconductorPackage(packageName.getPackageName());
	}
	
	public void installBioconductorPackage(String packageName) throws Exception{
		REXP rexp = null;
		try {
			if( !checkInstalledPackge( BioconductorPackagesEnum.BIOCONDUCTOR ) )
				installBioconductor();
			
			if( !checkInstalledPackge(packageName) )
				rexp = connection.parseAndEval("biocLite('" + packageName + "', suppressUpdates=TRUE)");
			
			libraryPackage(packageName);
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		} 
	}

	@Override
	public void libraryPackage(BioconductorPackagesEnum packageName) throws Exception {
		libraryPackage(packageName.getPackageName());
	}
	
	public void libraryPackage(String packageName) throws Exception{
		REXP rexp = null;
		try {
			rexp = connection.parseAndEval( "library(\"" + packageName + "\")" );
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	@Override
	public void execute(String command) throws Exception{
		REXP rexp = null;
		try {
			 rexp = connection.parseAndEval(command);
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e);
		}
	}
	
	public int executeReceiveInteger(String command) throws Exception{
		REXP rexp = null;
		
		try {
			rexp = connection.parseAndEval(command);
			
			return rexp.asInteger();
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public int[] executeReceiveIntegers(String command) throws Exception{
		REXP rexp = null;
		
		try {
			rexp = connection.parseAndEval(command);
			
			return rexp.asIntegers();
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public double executeReceiveDouble(String command) throws Exception{
		REXP rexp = null;
		
		try {
			rexp = connection.parseAndEval(command);
			
			return rexp.asDouble();
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public double[] executeReceiveDoubles(String command) throws Exception{
		REXP rexp = null;
		
		try {
			rexp = connection.parseAndEval(command);
			
			return rexp.asDoubles();
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public String executeReceiveString(String command) throws Exception{
		REXP rexp = null;
		
		try {
			rexp = connection.parseAndEval(command);
			return rexp.asString();
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public String[] executeReceiveStrings(String command) throws Exception{
		REXP rexp = null;
		
		try {
			rexp = connection.parseAndEval(command);
			return rexp.asStrings();
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public byte[] executeReceiveBytes(String command) throws Exception{
		REXP rexp = null;
		
		try {
			rexp = connection.parseAndEval(command);
			return rexp.asBytes();
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
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
		REXP rexp = null;
		try {
			rexp = connection.parseAndEval(varName +"="+ rValues);
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public void storeVariable(double[] values, String varName)throws Exception{
		REXP rexp = null;
		try {
			rexp = connection.parseAndEval(varName +"="+ Rutils.getAsRList(values)); // parse Sting and evaluate in R
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public void storeVariable(int[] values, String varName)throws Exception{
		REXP rexp = null;
		try {
			rexp = connection.parseAndEval(varName +"="+ Rutils.getAsRList(values));
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public void storeVariable(float[] values, String varName)throws Exception{
		REXP rexp = null;
		try {
			rexp = connection.parseAndEval(varName +"="+ Rutils.getAsRList(values));
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public void executeCheckExceptions(String command) throws Exception{
		REXP rexp = connection.parseAndEval("try(" + command + ",silent=TRUE)");
		
		if (rexp.inherits("try-error")) throw new Exception(rexp.asString(), new Throwable("Error running " + command.substring(0, 20) + "! Check if the selected model and data are the correct ones."));
	}
	
	public String[] executeCaptureOutput(String command) throws Exception{
		REXP rexp = null;
		
		try {
			rexp = connection.parseAndEval( "output <- capture.output(" + command + ")");
			return rexp.asStrings();
			
		} catch (Exception e) {
			throw ExceptionHandler.handleRserveException( this, rexp, e );
		}
	}
	
	public boolean isConnectionEstablished(){
		return connection == null ? false : true;
	}

}

