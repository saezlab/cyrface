package uk.ac.ebi.cyrface3.internal;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;

import uk.ac.ebi.cyrface3.internal.examples.dataRail.ContextMenuFactory;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailVisualStyle;

public class Storage {

	private CySwingApplication cytoscapeDesktopService;
	private CyApplicationManager cyApplicationManager;
	private CyAppAdapter cyAppAdapter;
	private String visualStyleName = "DataRailVisualStyle";
	private DataRailVisualStyle dataRailVisualStyle = null;
	private ContextMenuFactory contextMenuFactory;
//	private DataRailModel dataRailModel;
	
	private long[] workflowNodesSUIDs;
	
// 	------------------------------------------------------------------------------------
	public void setCytoscapeDesktopService(CySwingApplication cytoscapeDesktopService){
		this.cytoscapeDesktopService = cytoscapeDesktopService;}
	
	public CySwingApplication getCytoscapeDesktopService(){
		return cytoscapeDesktopService;}
	
// 	------------------------------------------------------------------------------------
//	public void setDataRailModel(DataRailModel dataRailModel){
//		this.dataRailModel = dataRailModel;}
//	
//	public DataRailModel getDataRailModel(){
//		return dataRailModel;}
	
// 	------------------------------------------------------------------------------------
	public void setContextMenuFactory(ContextMenuFactory contextMenuFactory){
		this.contextMenuFactory = contextMenuFactory;}
	
	public ContextMenuFactory getContextMenuFactory(){
		return contextMenuFactory;}
	
// 	------------------------------------------------------------------------------------
	public void setCyApplicationManager(CyApplicationManager cyApplicationManager){
		this.cyApplicationManager = cyApplicationManager;}
	
	public CyApplicationManager getCyApplicationManager(){
		return cyApplicationManager;}
	
// 	------------------------------------------------------------------------------------
	public void setCyAppAdapter(CyAppAdapter cyAppAdapter){
		this.cyAppAdapter = cyAppAdapter;}
	
	public CyAppAdapter getCyAppAdapter(){
		return cyAppAdapter;}
	
// 	------------------------------------------------------------------------------------
	public String getVisualStyleName(){
		return visualStyleName;}
	
// 	------------------------------------------------------------------------------------
	public void setWorkflowNodesSUIDs(long[] workflowNodesSUIDs){
		this.workflowNodesSUIDs = workflowNodesSUIDs;}
		
	public long[] getWorkflowNodesSUIDs(){
		return workflowNodesSUIDs;}
	
//	 ------------------------------------------------------------------------------------
	public void setVisualStyleObject(DataRailVisualStyle dataRailVisualStyle){
		this.dataRailVisualStyle = dataRailVisualStyle;}
	
	public DataRailVisualStyle getVisualStyleObject(){
		return dataRailVisualStyle;}
}
