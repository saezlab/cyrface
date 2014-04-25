package uk.ac.ebi.cyrface.internal.examples.dataRail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

import uk.ac.ebi.cyrface.internal.examples.dataRail.menu.ContextMenuFactory;

public class DataRailWorkFlow {
	
	private CyServiceRegistrar cyServiceRegistrar;
	
	private DataRailModel model;
	private List<CyNode> workflowNodes;
	private ContextMenuFactory contextMenuFactory;
	
	private CyNetwork network;
	private CyNetworkView view;
	
	public DataRailWorkFlow (CyServiceRegistrar cyServiceRegistrar) {
		this.cyServiceRegistrar = cyServiceRegistrar;
		this.workflowNodes = new ArrayList<CyNode>();
	}
	
	public void start () throws Exception {
		model = new DataRailModel (cyServiceRegistrar);
		
		// Create CyNetwork and name it
		network = cyServiceRegistrar.getService(CyNetworkFactory.class).createNetwork();
		network.getRow(network).set(CyNetwork.NAME, "DataRail");
		
		// Create Wroflows Nodes and Edges
		createNodes();
		createEdges();
		
		// Set Nodes Attributes
		setNodesAttributes();
		
		// Create Network View
		view = cyServiceRegistrar.getService(CyNetworkViewFactory.class).createNetworkView(network);
		cyServiceRegistrar.getService(CyNetworkManager.class).addNetwork(network);
		cyServiceRegistrar.getService(CyNetworkViewManager.class).addNetworkView(view);
		
		// Set nodes layout
		setNodesPositions();
		
		view.fitContent();
		view.updateView();
		
		// Create and Register Nodes Context Menu
		contextMenuFactory = new ContextMenuFactory(cyServiceRegistrar, workflowNodes, model);
		Properties props = new Properties();
		props.put("preferredMenu", "Apps.Cyrface");
		cyServiceRegistrar.registerAllServices(contextMenuFactory, props);
		
		DataRailVisualStyle dataRailVisualStyle = new DataRailVisualStyle(cyServiceRegistrar);
		dataRailVisualStyle.applyVisualStyle();
	}
			
		
	private void createNodes () {
		for (int i = 0; i < 7; i++) {
			CyNode node = network.addNode();
			workflowNodes.add(node);
		}
	}
	
	private void createEdges () {
		network.addEdge(workflowNodes.get(0), workflowNodes.get(1), true);
		network.addEdge(workflowNodes.get(1), workflowNodes.get(2), true);
		network.addEdge(workflowNodes.get(2), workflowNodes.get(3), true);
		network.addEdge(workflowNodes.get(3), workflowNodes.get(4), true);
		network.addEdge(workflowNodes.get(4), workflowNodes.get(5), true);
		network.addEdge(workflowNodes.get(5), workflowNodes.get(6), true);
	}
	
	private void setNodesPositions () {
		for (int i = 0; i < workflowNodes.size(); i++) {
			View<CyNode> nodeView = view.getNodeView(workflowNodes.get(i));
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP * (i+1));
			nodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
		}
	}
	
	private void setNodesAttributes () {
		CyTable defaultNodeTable = network.getDefaultNodeTable();
		defaultNodeTable.createColumn(DataRailAttributes.NODE_TYPE, String.class, false);
		defaultNodeTable.createColumn(DataRailAttributes.NODE_LABEL, String.class, false);
		defaultNodeTable.createColumn(DataRailAttributes.NODE_STATUS, String.class, false);

		for (int i = 0; i < workflowNodes.size(); i++) {
			CyNode node = workflowNodes.get(i);
			
			String nodeName = "", nodeType = "", nodeStatus = "", nodeLabel = "";
			
			switch (i) {
				case 0: 
					nodeName = "Step " + (i+1);
					nodeType = DataRailAttributes.NODE_TYPE_OBJECT;
					nodeStatus = DataRailAttributes.NODE_STATUS_UNDEFINED;
					nodeLabel = DataRailAttributes.NODE_LABEL_MIDAS_FILE;
					break;
					
				case 1: ; 
					nodeName = "Step " + (i+1);
					nodeType = DataRailAttributes.NODE_TYPE_FUNCTION;
					nodeStatus = DataRailAttributes.NODE_STATUS_UNDEFINED;
					nodeLabel = DataRailAttributes.NODE_LABEL_LOAD_MIDAS;
					break;
					
				case 2: 
					nodeName = "Step " + (i+1);
					nodeType = DataRailAttributes.NODE_TYPE_OBJECT;
					nodeStatus = DataRailAttributes.NODE_STATUS_UNDEFINED;
					nodeLabel = DataRailAttributes.NODE_LABEL_CNO_LIST;
					break;
					
				case 3: 
					nodeName = "Step " + (i+1);
					nodeType = DataRailAttributes.NODE_TYPE_FUNCTION;
					nodeStatus = DataRailAttributes.NODE_STATUS_UNDEFINED;
					nodeLabel = DataRailAttributes.NODE_LABEL_NORMALIZE;
					break;
					
				case 4: 
					nodeName = "Step " + (i+1);
					nodeType = DataRailAttributes.NODE_TYPE_OBJECT;
					nodeStatus = DataRailAttributes.NODE_STATUS_UNDEFINED;
					nodeLabel = DataRailAttributes.NODE_LABEL_NORMALIZED_CNO_LIST;
					break;
					
				case 5: 
					nodeName = "Step " + (i+1);
					nodeType = DataRailAttributes.NODE_TYPE_FUNCTION;
					nodeStatus = DataRailAttributes.NODE_STATUS_UNDEFINED;
					nodeLabel = DataRailAttributes.NODE_LABEL_OPTMIZE;
					break;
					
				case 6:
					nodeName = "Step " + (i+1);
					nodeType = DataRailAttributes.NODE_TYPE_OBJECT;
					nodeStatus = DataRailAttributes.NODE_STATUS_UNDEFINED;
					nodeLabel = DataRailAttributes.NODE_LABEL_OPTMIZED_CNO_LIST;
					break;
					
				default:;
			}
			
			network.getRow(node).set(CyNetwork.NAME, nodeName);
			network.getRow(node).set(DataRailAttributes.NODE_TYPE, nodeType);
			network.getRow(node).set(DataRailAttributes.NODE_STATUS, nodeStatus);
			network.getRow(node).set(DataRailAttributes.NODE_LABEL, nodeLabel);
		}
	}
}
