package uk.ac.ebi.cyrface3.internal.examples.dataRail.contextMenus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CyMenuItem;
import org.cytoscape.application.swing.CyNodeViewContextMenuFactory;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;

import uk.ac.ebi.cyrface3.internal.Storage;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailAttributes;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailModel;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailVisualStyle;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.menu.RFunctionsModel;

public class ContextMenuFactory implements CyNodeViewContextMenuFactory{
	/**
	 * This class is used for creating the context-menus for all nodes,
	 * but only the context-menus for the work-flow-nodes provide services.
	 */
	private Storage storage;
	private long nodeSUID;
	private CyNetworkView view;
	private CyNetwork network;
	private DataRailVisualStyle dataRailVisualStyle;
	private DataRailModel model;
	private CyTable defaultNodeTable;
	private long[] workflowNodesSUIDs;
	
	public ContextMenuFactory(Storage storage) {
		this.storage = storage;
		dataRailVisualStyle = new DataRailVisualStyle(storage);
	}

	public CyMenuItem createMenuItem(CyNetworkView netView,	View<CyNode> nodeView) {
		CyApplicationManager _appManager = storage.getCyApplicationManager();
		
		view = _appManager.getCurrentNetworkView();
		network = _appManager.getCurrentNetwork();
		defaultNodeTable = network.getDefaultNodeTable();
		nodeSUID = nodeView.getModel().getSUID();
		model = storage.getDataRailModel();
		
		boolean nodeIsPartOfWorkflow = false;
		
			// in this long-array are the SUIDs of the work-flow saved
		workflowNodesSUIDs = storage.getWorkflowNodesSUIDs();
		
			// check, if the node-SUID of the right-click-selected node is in the array
		for(int i=0; i<workflowNodesSUIDs.length; i++){
				// if true: get the work-flow service for this node in the following code
			if(workflowNodesSUIDs[i]==nodeSUID){
				nodeIsPartOfWorkflow = true;
				break;
			}else{
				nodeIsPartOfWorkflow = false;
			}
		}
		
		if(nodeIsPartOfWorkflow == true){
			CyMenuItem cyMenuItem = null;
			JMenuItem menuItem = null;
			
// ---------------------------------- midasFileNode --------------------------------------		
			// TODO more information in the tool-tips
			if(nodeSUID==workflowNodesSUIDs[0]){
				menuItem = new JMenuItem("CyRFace 3: Set MIDAS-file ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						JFileChooser fc = new JFileChooser(); 
						FileFilter filter = new FileNameExtensionFilter("csv-files", "csv");
						fc.addChoosableFileFilter(filter);
						fc.setFileFilter(filter);
						int browserReturn = fc.showOpenDialog(null);
						if (browserReturn == JFileChooser.APPROVE_OPTION){
							String midasFilePath = fc.getSelectedFile().getAbsolutePath();
							model.setMidasFilePath(midasFilePath);
							
							network.getDefaultNodeTable().getRow(nodeSUID).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
							dataRailVisualStyle.applyVisualStyle();
							view.updateView();
						}
					}
				});
				menuItem.setToolTipText("Browse the input MIDAS file.");
				cyMenuItem = new CyMenuItem(menuItem, 0);
				
// ---------------------------------- loadMidasNode --------------------------------------	
			}else if(nodeSUID==workflowNodesSUIDs[1]){
				menuItem = new JMenuItem("CyRFace 3: Load MIDAS ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						CyRow step1Row = defaultNodeTable.getRow(workflowNodesSUIDs[0]);
						boolean isPreviousStepDefined = step1Row.get(DataRailAttributes.NODE_STATUS, String.class).
															equals(DataRailAttributes.NODE_STATUS_DEFINED);
						if(isPreviousStepDefined==true){
							try {
								model.getRCommand().loadMidasFile(model.getMidasFilePath());
								
								network.getDefaultNodeTable().getRow(nodeSUID).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
								network.getDefaultNodeTable().getRow(workflowNodesSUIDs[2]).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
								view.updateView();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
						}else{
							JOptionPane.showMessageDialog(null, "<HTML>Please set the path to the MIDAS-File in the context<br />"
									+ "menu of the first node named \"MIDAS\".<HTML />");
						}
					}
				});
				menuItem.setToolTipText("Loads MIDAS file converting it into a CNO list.");
				cyMenuItem = new CyMenuItem(menuItem, 0);
				
// ------------------------------------ cnoListNode ---------------------------------------	
			}else if(nodeSUID==workflowNodesSUIDs[2]){ 
				menuItem = new JMenuItem("CyRFace 3: Plot MIDAS ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CyRow step2Row = defaultNodeTable.getRow(workflowNodesSUIDs[1]);
						boolean isPreviousStepDefined = step2Row.get(DataRailAttributes.NODE_STATUS, String.class).
															equals(DataRailAttributes.NODE_STATUS_DEFINED);
						if(isPreviousStepDefined==true){
							try {
								
								File cnoListPlot = model.getRCommand().plotCnoList( RFunctionsModel.varCnoList );
								
								model.setCnoListPlot(cnoListPlot);
								
//								new PlotsDialog(cnoListPlot); // TODO when batik-jars are working
								
								network.getDefaultNodeTable().getRow(nodeSUID).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
								view.updateView();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
						}else{
							JOptionPane.showMessageDialog(null, "<HTML>Please make sure, that the MIDAS-file is loaded using<br />"
									+ "the menu of the second node named \"load MIDAS\".<HTML />");
						}
					}
				});
				menuItem.setToolTipText("Plot the MIDAS."); // TODO what is the cno list? - explain here...
				cyMenuItem = new CyMenuItem(menuItem, 0);
				
// ----------------------------------- normalizeNode ---------------------------------------		
			}else if(nodeSUID==workflowNodesSUIDs[3]){
				menuItem = new JMenuItem("CyRFace 3: Normalize MIDAS ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						CyRow step3Row = defaultNodeTable.getRow(workflowNodesSUIDs[2]);
						boolean isPreviousStepDefined = step3Row.get(DataRailAttributes.NODE_STATUS, String.class).
															equals(DataRailAttributes.NODE_STATUS_DEFINED);
						if(isPreviousStepDefined==true){
//							new NormalizeCnoList(model);
//							
//							try {
//								model.getRCommand().normaliseCnoList(model);
//								File normalizedMidasFile = File.createTempFile(FilenameUtils.getName(model.getMidasFilePath())+"_normalized", ".csv");
//								normalizedMidasFile.delete();
//								model.getRCommand().writeNormalizedMIDAS(normalizedMidasFile.getAbsolutePath());
//								model.setNormalizedMidasFile(normalizedMidasFile);
//								
//							} catch (Exception e1) {
//								e1.printStackTrace();
//							}
							
							network.getDefaultNodeTable().getRow(nodeSUID).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
							network.getDefaultNodeTable().getRow(workflowNodesSUIDs[4]).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
							view.updateView();
						}else{
							JOptionPane.showMessageDialog(null, "<HTML>	Please make sure, that the MIDAS-file is loaded using<br />"
									+ "the menu of the second node named \"load MIDAS\".<HTML />");
						}
					}
				});
				menuItem.setToolTipText("Normalize the MIDAS.");
				cyMenuItem = new CyMenuItem(menuItem, 0);
				
// -------------------------------- cnoListNormalizedNode ------------------------------------	
			}else if(nodeSUID==workflowNodesSUIDs[4]){
				menuItem = new JMenuItem("CyRFace 3: Plot MIDAS ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						CyRow step4Row = defaultNodeTable.getRow(workflowNodesSUIDs[3]);
						boolean isPreviousStepDefined = step4Row.get(DataRailAttributes.NODE_STATUS, String.class).
															equals(DataRailAttributes.NODE_STATUS_DEFINED);
						if(isPreviousStepDefined==true){
							File cnoListPlot;
							try {
								cnoListPlot = model.getRCommand().plotCnoList( RFunctionsModel.varNormCnoList );
								model.setCnoListNormalisePlot(cnoListPlot);
//								new PlotsDialog(cnoListPlot, model.getNormalizedMidasFile()); // TODO when batik-jars are working
								
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, "<HTML>	Sorry, he plotting is not implemented yet.<HTML />");
//								e1.printStackTrace(); // TODO uncomment, if creating the plots works
							}
						}else{
							JOptionPane.showMessageDialog(null, "<HTML>	Please make sure, that the MIDAS-file is is normalized using<br />"
									+ "	the menu of the fourth node named \"Normalize\".<HTML />");
						}
					}
				});
				menuItem.setToolTipText("Plot the MIDAS.");
				cyMenuItem = new CyMenuItem(menuItem, 0);
				
// ------------------------------------ optimizeNode ----------------------------------------	
			}else if(nodeSUID==workflowNodesSUIDs[5]){	
				menuItem = new JMenuItem("CyRFace 3: Optimize ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CyRow step5Row = defaultNodeTable.getRow(workflowNodesSUIDs[4]);
						boolean isPreviousStepDefined = step5Row.get(DataRailAttributes.NODE_STATUS, String.class).
															equals(DataRailAttributes.NODE_STATUS_DEFINED);
						if(isPreviousStepDefined==true){
						
							JFileChooser fc = new JFileChooser(); 
							FileFilter filter = new FileNameExtensionFilter("sif-, xml- or sbml-files", "csv", "sif", "sbml");
							fc.addChoosableFileFilter(filter);
							fc.setFileFilter(filter);
							int browserReturn = fc.showSaveDialog(null);
							if (browserReturn == JFileChooser.APPROVE_OPTION){
								
//								model.setPknModelFile(fc.getSelectedFile().getAbsolutePath());
//								new OptimizeCnoList(model);
//								
								network.getDefaultNodeTable().getRow(nodeSUID).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
								network.getDefaultNodeTable().getRow(workflowNodesSUIDs[6]).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
								view.updateView();
							}
						}else{
							JOptionPane.showMessageDialog(null, "<HTML>	Please make sure, that the MIDAS-file is is normalized using<br />"
									+ "	the menu of the fourth node named \"Normalize\".<HTML />");
						}
					}
				});
				menuItem.setToolTipText("Optimize the MIDAS.");
				cyMenuItem = new CyMenuItem(menuItem, 0);
				
// -------------------------------- cnoListOptimizedNode ------------------------------------	
			}else if(nodeSUID==workflowNodesSUIDs[6]){
				menuItem = new JMenuItem("CyRFace 3: Plot MIDAS ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						CyRow step6Row = defaultNodeTable.getRow(workflowNodesSUIDs[5]);
						boolean isPreviousStepDefined = step6Row.get(DataRailAttributes.NODE_STATUS, String.class).
															equals(DataRailAttributes.NODE_STATUS_DEFINED);
						if(isPreviousStepDefined==true){
							try{
								
								File cnoListPlot = model.getRCommand().cutAndPlot();
								
								model.setCnoListOptimisedPlot(cnoListPlot);
								
	//							PlotsDialog plots = new PlotsDialog(cnoListPlot, model.getOptimizedMidasFile());
								
							}catch(Exception e2){
								e2.printStackTrace();
							}
						}else{
							JOptionPane.showMessageDialog(null, "<HTML>	Please make sure, that the MIDAS-file is is optimized using<br />"
									+ "	the menu of the sixth node named \"Optimize\".<HTML />");
						}
					}
				});
				menuItem.setToolTipText("Plot optimized CNO-list."); 
				cyMenuItem = new CyMenuItem(menuItem, 0);
			}
			return cyMenuItem;
			
// ------------------------ if the node is not part of the worklow --------------------------	
		}else{
			JMenuItem menuItem = new JMenuItem("This Node is not part of the CyRFace 3 Workflow");
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//TODO
					JOptionPane.showMessageDialog(null, "MyNodeViewContextMenuFactory action worked.");
					
				}
			});
			return new CyMenuItem(menuItem, 0);
		}
	}
	

}
