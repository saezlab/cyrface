package uk.ac.ebi.cyrface3.internal.examples.dataRail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class ContextMenuFactory implements CyNodeViewContextMenuFactory{
	
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
//		model = storage.getDataRailModel();
		
		boolean nodeIsPartOfWorkflow = false;
		
		workflowNodesSUIDs = storage.getWorkflowNodesSUIDs();
		
		for(int i=0; i<workflowNodesSUIDs.length; i++){
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
//							model.setMidasFilePath(midasFilePath);
							
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
//								model.getRCommand().loadMidasFile(model.getMidasFilePath());
								
								network.getDefaultNodeTable().getRow(nodeSUID).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
								network.getDefaultNodeTable().getRow(workflowNodesSUIDs[2]).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
								view.updateView();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
						}else{
							JOptionPane.showMessageDialog(null, "<HTML>Please set the path to the MIDAS-File in the context<br />"
									+ "menu of the first node named \"MIDAS\" first.<HTML />");
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
								
//								File cnoListPlot = model.getRCommand().plotCnoList( RFunctionsModel.varCnoList );
//								
//								model.setCnoListPlot(cnoListPlot);
//								
//								new PlotsDialog(cnoListPlot); 
								
								network.getDefaultNodeTable().getRow(nodeSUID).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
								view.updateView();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
						}else{
							JOptionPane.showMessageDialog(null, "<HTML>Please make sure, that the MIDAS-file is loaded using<br />"
									+ "the menu of the second node named \"load MIDAS\" first.<HTML />");
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
						//TODO
						JOptionPane.showMessageDialog(null, "MyNodeViewContextMenuFactory action worked.");
						
					}
				});
				menuItem.setToolTipText("Normalize the MIDAS.");
				cyMenuItem = new CyMenuItem(menuItem, 0);
				
// -------------------------------- cnoListNormalizedNode ------------------------------------	
			}else if(nodeSUID==workflowNodesSUIDs[4]){
				menuItem = new JMenuItem("CyRFace 3: Plot MIDAS ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//TODO
						JOptionPane.showMessageDialog(null, "MyNodeViewContextMenuFactory action worked.");
						
					}
				});
				menuItem.setToolTipText("Plot the MIDAS.");
				cyMenuItem = new CyMenuItem(menuItem, 0);
				
// ------------------------------------ optimizeNode ----------------------------------------	
			}else if(nodeSUID==workflowNodesSUIDs[5]){	
				menuItem = new JMenuItem("CyRFace 3: Optimize ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//TODO
						JOptionPane.showMessageDialog(null, "MyNodeViewContextMenuFactory action worked.");
						
					}
				});
				menuItem.setToolTipText("Optimize the MIDAS.");
				cyMenuItem = new CyMenuItem(menuItem, 0);
				
// -------------------------------- cnoListOptimizedNode ------------------------------------	
			}else if(nodeSUID==workflowNodesSUIDs[6]){
				menuItem = new JMenuItem("CyRFace 3: Plot MIDAS ...");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//TODO
						JOptionPane.showMessageDialog(null, "MyNodeViewContextMenuFactory action worked.");
						
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
