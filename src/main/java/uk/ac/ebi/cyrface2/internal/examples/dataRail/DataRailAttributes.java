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
	public static Double ec50_default = 0.5;
	public static String ec50_tooltip = "Parameter for the scaling of the data between 0 and 1, default=0.5";
	
	public static Double saturation_default = Double.POSITIVE_INFINITY;
	public static String saturation_tooltip = "Saturation level of the instrument, everything over this will be treated as NA, default to Inf.";
	
	public static Double detection_default = 0.0;
	public static String detection_tooltip = "Minimum detection level of the instrument, everything smaller will be treated as noise (NA), default to 0";
}
