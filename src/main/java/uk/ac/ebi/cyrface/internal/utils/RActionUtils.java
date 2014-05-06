package uk.ac.ebi.cyrface.internal.utils;


public class RActionUtils {

	public static final String CAPTURE_FUNCTION_PREFIX = "messages <- capture.output(";
	public static final String CAPTURE_FUNCTION_SUFFIX = ")";
	
	public static final String PLOT_DEVICE = "svg";
	
	
	public static String getWindowsCorrectPath (String filePath) {
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
			return filePath.replace('\\', '/');
		return filePath;
	}
	
	public static String captureOutput (String command) {
		StringBuilder wrap = new StringBuilder();
		
		wrap.append("messages <- capture.output(");
		wrap.append(command);
		wrap.append(")");
		
		return wrap.toString();
	}
	
	public static String processROutput (String[] output) {
		StringBuilder messages = new StringBuilder();
		
		for(String message : output){
			String aux = message.trim();
			aux = aux.substring(3); // Remove the [1] prefix in each CNOR output line
			aux = aux.substring(2, aux.length()-1); // Remove quotes from the beginning and the end of the line
			
			messages.append(aux);
			messages.append("\n");
		}
		
		return messages.toString();
	}
}
