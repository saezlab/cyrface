package uk.ac.ebi.cyrface2.internal.examples.dataRail;

public class DataRailAttributes {

	public static Double VERTICAL_NODE_COORDINATE = 50.0;
	public static Double VERTICAL_NODE_STEP = 100.0;
	
	public static String NODE_IDENTIFICATION = "SUID";
	
	public static String NODE_TYPE = "Type";
	public static String NODE_TYPE_OBJECT = "Object";
	public static String NODE_TYPE_FUNCTION = "Function";
	
	public static String NODE_STATUS = "status";
	public static String NODE_STATUS_DEFINED = "Defined";
	public static String NODE_STATUS_UNDEFINED = "Undefined";
	
	public static String NODE_LABEL = "Label";
	public static String NODE_LABEL_MIDAS_FILE = "MIDAS";
	public static String NODE_LABEL_LOAD_MIDAS = "Load MIDAS";
	public static String NODE_LABEL_CNO_LIST = "MIDAS";
	public static String NODE_LABEL_NORMALIZE = "Normalize";
	public static String NODE_LABEL_NORMALIZED_CNO_LIST = "MIDAS";
	public static String NODE_LABEL_OPTMIZE = "Optimize";
	public static String NODE_LABEL_OPTMIZED_CNO_LIST = "Optmized CNO List";
	
	public static String EDGE_INTERACTION = "outputs";
	
	public static String WORKFLOW_VISUAL_STYLE = "DataRail";
	
	// Normalization Args
	public static Double EC50_DEFAULT = 0.5;
	public static String EC50_TOOL_TIP = "Parameter for the scaling of the data between 0 and 1, default=0.5";
	
	public static Double SATURATION_DEFAULT = Double.POSITIVE_INFINITY;
	public static String SATURATION_TOOLTIP = "Saturation level of the instrument, everything over this will be treated as NA, default to Inf.";
	
	public static Double DETECTION_DEFAULT = 0.0;
	public static String DETECTION_TOOL_TIP = "Minimum detection level of the instrument, everything smaller will be treated as noise (NA), default to 0";
	
	
	// Nodes Context Menus Text
	public static String SET_MIDAS_CONTEXT_MENU_NAME = "Set MIDAS-file...";
	public static String SET_MIDAS_CONTEXT_MENU_TOOL_TIP = "Browse the input MIDAS file.";
	
	public static String LOAD_MIDAS_CONTEXT_MENU_NAME = "Load MIDAS...";
	public static String LOAD_MIDAS_CONTEXT_MENU_TOOL_TIP = "Loads MIDAS file converting it into a CNO list.";
	public static String LOAD_MIDAS_ERROR_MESSAGE = "<HTML>Please set the path to the MIDAS-File in the context<br /> menu of the first node named \"MIDAS\" first.<HTML />";
	
	public static String CNO_LIST_CONTEXT_MENU_NAME = "Plot MIDAS...";
	public static String CNO_LIST_CONTEXT_MENU_TOOL_TIP = "Plot the MIDAS.";
	public static String CNO_LIST_ERROR_MESSAGE = "<HTML>Please make sure, that the MIDAS-file is loaded using<br /> the menu of the second node named \"load MIDAS\" first.<HTML />";
	
	public static String NORMALISE_CNO_CONTEXT_MENU_NAME = "Normalise MIDAS...";
	public static String NORMALISE_CNO_CONTEXT_MENU_TOOL_TIP = "Normalise the data-set according to the selected arguments.";
	
	public static String NORMALISED_CNO_CONTEXT_MENU_NAME = "Plot MIDAS...";
	public static String NORMALISED_CNO_CONTEXT_MENU_TOOL_TIP = "Plot the normalised MIDAS.";
	
	public static String OPTMISE_CONTEXT_MENU_NAME = "Optimize...";
	public static String OPTMISE_CONTEXT_MENU_TOOL_TIP = "Optimize the normalised MIDAS against the selected network. The output will be plot showing how well the network explains the data.";
	
	public static String PLOT_OPTIMISED_CONTEXT_MENU_NAME = "Plot MIDAS...";
	public static String PLOT_OPMITSED_CONTEXT_MENU_TOOL_TIP = "Plots the optmisation result.";
}
