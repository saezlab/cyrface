package uk.ac.ebi.cyrface3.internal.utils;

public enum RPlotsTypes {

	PLOT ("plot"),
	PLOT_TIME_SERIES ("plot.ts"),
	HISTOGRAM ("hist");
	
	private String plotName;
	
	private RPlotsTypes(String plotName){
		this.plotName = plotName; 
	}
	
	/**
	 * rValues is a list already formated as an R list (e.g. c(1,2,3,4)) 
	 * 
	 * @param rValues
	 * @return
	 */
	public String getPlotRCommand(String rValues){
		StringBuilder command = new StringBuilder();
		
		command.append(plotName);
		command.append("(");
		command.append(rValues);
		command.append(")");
		
		return command.toString();
	}
}
