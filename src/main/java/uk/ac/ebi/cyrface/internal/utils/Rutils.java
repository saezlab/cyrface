package uk.ac.ebi.cyrface.internal.utils;

public class Rutils {
	
	/**
	 * Converts the Java primary list into an R list format (e.g. c(1.0,2.0))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(double[] values){
		StringBuilder rValuesList = new StringBuilder("c(");
		for(int i=0; i<values.length; i++){
			rValuesList.append(values[i]);
			
			if( i<(values.length-1) )
				rValuesList.append(",");
		}
		rValuesList.append(")");
		
		return rValuesList.toString();
	}
	
	/**
	 * Converts the Java primary list into an R list format (e.g. c(1.0,2.0))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(float[] values){
		StringBuilder rValuesList = new StringBuilder("c(");
		for(int i=0; i<values.length; i++){
			rValuesList.append(values[i]);
			
			if( i<(values.length-1) )
				rValuesList.append(",");
		}
		rValuesList.append(")");
		
		return rValuesList.toString();
	}
	
	/**
	 * Converts the Java primary list into an R list format (e.g. c(1,2,3,4))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(int[] values){
		StringBuilder rValuesList = new StringBuilder("c(");
		for(int i=0; i<values.length; i++){
			rValuesList.append(values[i]);
			
			if( i<(values.length-1) )
				rValuesList.append(",");
		}
		rValuesList.append(")");
		
		return rValuesList.toString();
	}
	
	/**
	 * Converts the Java primary list into an R list format (e.g. c("Hello","world"))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(String[] values){
		StringBuilder rValuesList = new StringBuilder("c(");
		for(int i=0; i<values.length; i++){
			rValuesList.append("\"");
			rValuesList.append(values[i]);
			rValuesList.append("\"");
			
			if( i<(values.length-1) )
				rValuesList.append(",");
		}
		rValuesList.append(")");
		
		return rValuesList.toString();
	}
	
	/**
	 * Converts the Java primary list into an R list format (e.g. c('H','e','l','l','o'))
	 * 
	 * @param values
	 * @return
	 */
	public static String getAsRList(char[] values){
		StringBuilder rValuesList = new StringBuilder("c(");
		for(int i=0; i<values.length; i++){
			rValuesList.append("'");
			rValuesList.append(values[i]);
			rValuesList.append("'");
			
			if( i<(values.length-1) )
				rValuesList.append(",");
		}
		rValuesList.append(")");
		
		return rValuesList.toString();
	}

}
