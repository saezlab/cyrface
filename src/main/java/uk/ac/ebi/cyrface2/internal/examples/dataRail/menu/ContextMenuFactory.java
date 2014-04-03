package uk.ac.ebi.cyrface2.internal.examples.dataRail.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.cytoscape.application.swing.CyMenuItem;
import org.cytoscape.application.swing.CyNodeViewContextMenuFactory;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;

import uk.ac.ebi.cyrface2.internal.CyActivator;
import uk.ac.ebi.cyrface2.internal.examples.dataRail.DataRailAttributes;
import uk.ac.ebi.cyrface2.internal.examples.dataRail.DataRailModel;
import uk.ac.ebi.cyrface2.internal.examples.dataRail.DataRailVisualStyle;

public class ContextMenuFactory implements CyNodeViewContextMenuFactory {
	
	private CyActivator activator;
	
	private DataRailVisualStyle dataRailVisualStyle;
	private DataRailModel model;
	
	private CyNetworkView view;
	private CyNetwork network;
	private CyTable defaultNodeTable;
	
	private List<Long> workflowNodesSUIDs;
	
	public ContextMenuFactory(CyActivator activator, List<Long> workflowNodesSUIDs, DataRailModel model) {
		this.activator = activator;
		this.model = model;
		this.workflowNodesSUIDs = workflowNodesSUIDs;
	}

	public CyMenuItem createMenuItem (CyNetworkView netView, View<CyNode> nodeView) {
		
		view = activator.cyApplicationManager.getCurrentNetworkView();
		network = activator.cyApplicationManager.getCurrentNetwork();
		defaultNodeTable = network.getDefaultNodeTable();
		
		final long nodeSUID = nodeView.getModel().getSUID();
		CyMenuItem menuItem;
		
		switch (workflowNodesSUIDs.indexOf(nodeSUID)) {
			case 0: 
				menuItem = createBrowseMidasMenu(nodeSUID); break;
			case 1: 
				menuItem = createLoadMidasMenu(nodeSUID); break;
			case 2: 
				menuItem = createCNOListMenu(nodeSUID); break;
			case 3: 
				menuItem = createNormaliseMenu(nodeSUID); break;
			case 4: 
				menuItem = createCNOListNormalisedMenu(nodeSUID); break;
			case 5: 
				menuItem = createOptimiseMenu(nodeSUID); break;
			case 6: 
				menuItem = createOptimisedCNOListMenu(nodeSUID); break;
			default: 
				menuItem = null; break;
		}
		
		return menuItem;
	}
	
	private CyMenuItem createBrowseMidasMenu (final long nodeSUID) {
		JMenuItem menuItem = new JMenuItem("Set MIDAS-file ...");
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
		return new CyMenuItem(menuItem, 0);
	}
	
	private CyMenuItem createLoadMidasMenu (final long nodeSUID) {
		JMenuItem menuItem = new JMenuItem("Load MIDAS ...");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CyRow step1Row = defaultNodeTable.getRow(workflowNodesSUIDs.get(0));
				boolean isPreviousStepDefined = step1Row.get(DataRailAttributes.NODE_STATUS, String.class).
													equals(DataRailAttributes.NODE_STATUS_DEFINED);
				if (isPreviousStepDefined) {
					try {
						model.getRCommand().loadMidasFile(model.getMidasFilePath());
						
						network.getDefaultNodeTable().getRow(nodeSUID).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
						network.getDefaultNodeTable().getRow(workflowNodesSUIDs.get(2)).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
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
		return new CyMenuItem(menuItem, 0);
	}
	
	private CyMenuItem createCNOListMenu (final long nodeSUID) {
		JMenuItem menuItem = new JMenuItem("Plot MIDAS ...");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CyRow step2Row = defaultNodeTable.getRow(workflowNodesSUIDs.get(1));
				boolean isPreviousStepDefined = step2Row.get(DataRailAttributes.NODE_STATUS, String.class).
													equals(DataRailAttributes.NODE_STATUS_DEFINED);
				if(isPreviousStepDefined==true){
					try {
						
						File cnoListPlot = model.getRCommand().plotCnoList( RFunctionsModel.varCnoList );
						
						model.setCnoListPlot(cnoListPlot);
						
//						new PlotsDialog(cnoListPlot); 
						
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
		menuItem.setToolTipText("Plot the MIDAS.");
		return new CyMenuItem(menuItem, 0);
	}
	
	private CyMenuItem createNormaliseMenu (final long nodeSUID) {
		JMenuItem menuItem = new JMenuItem("Normalize MIDAS ...");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				JOptionPane.showMessageDialog(null, "MyNodeViewContextMenuFactory action worked.");
				
			}
		});
		
		menuItem.setToolTipText("Normalize the MIDAS.");
		return new CyMenuItem(menuItem, 0);
	}
	
	private CyMenuItem createCNOListNormalisedMenu (final long nodeSUID) {
		JMenuItem menuItem = new JMenuItem("Plot MIDAS ...");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				JOptionPane.showMessageDialog(null, "MyNodeViewContextMenuFactory action worked.");
				
			}
		});
		menuItem.setToolTipText("Plot the MIDAS.");
		return new CyMenuItem(menuItem, 0);
	}
	
	private CyMenuItem createOptimiseMenu (final long nodeSUID) {
		JMenuItem menuItem = new JMenuItem("Optimize ...");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				JOptionPane.showMessageDialog(null, "MyNodeViewContextMenuFactory action worked.");
				
			}
		});
		menuItem.setToolTipText("Optimize the MIDAS.");
		return new CyMenuItem(menuItem, 0);
	}
	
	private CyMenuItem createOptimisedCNOListMenu (final long nodeSUID) {
		JMenuItem menuItem = new JMenuItem("Plot MIDAS ...");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				JOptionPane.showMessageDialog(null, "MyNodeViewContextMenuFactory action worked.");
				
			}
		});
		menuItem.setToolTipText("Plot optimized CNO-list."); 
		return new CyMenuItem(menuItem, 0);
	}
}
