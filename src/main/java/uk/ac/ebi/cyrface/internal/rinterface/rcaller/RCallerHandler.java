package uk.ac.ebi.cyrface.internal.rinterface.rcaller;

import java.io.File;

import rcaller.RCaller;
import rcaller.RCode;
import uk.ac.ebi.cyrface.internal.rinterface.RHandler;
import uk.ac.ebi.cyrface.internal.utils.BioconductorPackagesEnum;
import uk.ac.ebi.cyrface.internal.utils.RPlotsTypes;
import uk.ac.ebi.cyrface.internal.utils.Rutils;

public class RCallerHandler extends RHandler {
	
	public RCallerHandler() {
		super("RCaller");
		
	}

	
	public boolean checkInstalledPackge(BioconductorPackagesEnum packageName) throws Exception{
		return checkInstalledPackge(packageName.getPackageName());
	}
	
	public boolean checkInstalledPackge(String packageName) throws Exception{
		RCaller caller = generateCaller();
		
		RCode code = new RCode();
		code.clear();
		
		code.addRCode("result <- require('" + packageName + "')");
		
		caller.setRCode(code);
		caller.runAndReturnResult("result");

		boolean result = caller.getParser().getAsLogicalArray("result")[0];
		
		return result;
	}
	
	/**
	 * 
	 * Installs Bioconductor, by running source("http://www.bioconductor.org/biocLite.R"). Required in order to install Bioconductor packages.
	 * 
	 * @throws Exception
	 */
	public void installBioconductor() throws Exception{
		RCaller caller = generateCaller();
		
		RCode code = new RCode();
		code.clear();
		
		code.addRCode("source(\"http://www.bioconductor.org/biocLite.R\")");
		
		code.addRCode("library('BiocInstaller')");
		
		caller.setRCode(code);
		caller.runOnly();
	}
	
	public void installBioconductorPackage(BioconductorPackagesEnum packageName) throws Exception{
		installBioconductorPackage(packageName.getPackageName());
	}
	
	public void installBioconductorPackage(String packageName) throws Exception{
		RCaller caller = generateCaller();
		
		RCode code = new RCode();
		code.clear();
		
		if( !checkInstalledPackge(packageName) )
			installBioconductor();
		
		if( !checkInstalledPackge(packageName) )
			code.addRCode("biocLite('" + packageName + "', suppressUpdates=TRUE)");
		
		caller.setRCode(code);
		caller.runOnly();
	}
	
	public void libraryPackage(BioconductorPackagesEnum packageName) throws Exception{
		libraryPackage(packageName.getPackageName());
	}
	
	public void libraryPackage(String packageName) throws Exception{
		RCaller caller = generateCaller();
		
		RCode code = new RCode();
		code.clear();
		
		code.addRCode("library(\"" + packageName + "\")");
		
		caller.setRCode(code);
		caller.runOnly();
	}
	
	@Override
	public void execute(String command) throws Exception {
		RCaller caller = generateCaller();
		
		RCode code = new RCode();
		code.clear();
		
		code.addRCode(command);

		caller.setRCode(code);
		caller.runOnly();
	}
	
	private RCaller executeReceiveCaller(String command, String variable) throws Exception {
		RCaller caller = generateCaller();
		
		RCode code = new RCode();
		code.clear();
		
		code.addRCode(command);

		caller.setRCode(code);
		caller.runAndReturnResult(variable);
		
		return caller;
	}
	
	public int executeReceiveInteger(String command, String variable) throws Exception{
		RCaller caller = executeReceiveCaller(command, variable);
		
		return caller.getParser().getAsIntArray(variable)[0];
	}
	
	public int[] executeReceiveIntegers(String command, String variable) throws Exception{
		RCaller caller = executeReceiveCaller(command, variable);
		
		return caller.getParser().getAsIntArray(variable);
	}
	
	public double executeReceiveDouble(String command, String variable) throws Exception{
		RCaller caller = executeReceiveCaller(command, variable);
		
		return caller.getParser().getAsDoubleArray(variable)[0];
	}
	
	public double[] executeReceiveDoubles(String command, String variable) throws Exception{
		RCaller caller = executeReceiveCaller(command, variable);
		
		return caller.getParser().getAsDoubleArray(variable);
	}
	
	public String executeReceiveString(String command, String variable) throws Exception{
		RCaller caller = executeReceiveCaller(command, variable);
		
		return caller.getParser().getAsStringArray(variable)[0];
	}
	
	public String[] executeReceiveStrings(String command, String variable) throws Exception{
		RCaller caller = executeReceiveCaller(command, variable);
		
		return caller.getParser().getAsStringArray(variable);
	}
	
	public File executeReceivePlotFile(String command) throws Exception{
		RCaller caller = generateCaller();
		
		RCode code = new RCode();
		code.clear();
		
		File file = caller.getRCode().startPlot();
		caller.getRCode().addRCode(command);
		caller.getRCode().endPlot();
        caller.runOnly();
        
        return file;
	}
	
	public File executeReceivePlotFile(double[] values, RPlotsTypes plotType ) throws Exception{
		String rValues = Rutils.getAsRList(values);
		String command = plotType.getPlotRCommand(rValues);
		
		return executeReceivePlotFile(command);
	}
	
	public File executeReceivePlotFile(float[] values, RPlotsTypes plotType ) throws Exception{
		String rValues = Rutils.getAsRList(values);
		String command = plotType.getPlotRCommand(rValues);
		
		return executeReceivePlotFile(command);
	}
	
	public File executeReceivePlotFile(int[] values, RPlotsTypes plotType ) throws Exception{
		String rValues = Rutils.getAsRList(values);
		String command = plotType.getPlotRCommand(rValues);
		
		return executeReceivePlotFile(command);
	}
	
	
	@Override
	public boolean isConnectionEstablished() {
		try{
			generateCaller();
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	

	private RCaller generateCaller(){
		RCaller caller = new RCaller();
		caller.setRscriptExecutable("/usr/bin/RScript");

		return caller;
	}

}
