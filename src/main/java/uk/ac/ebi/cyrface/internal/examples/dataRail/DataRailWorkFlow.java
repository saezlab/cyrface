package uk.ac.ebi.cyrface.internal.examples.dataRail;

import java.util.ArrayList;
import java.util.List;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

import uk.ac.ebi.cyrface.internal.CyActivator;
import uk.ac.ebi.cyrface.internal.examples.dataRail.menu.ContextMenuFactory;

public class DataRailWorkFlow {
	
	private CyActivator activator;
	
	private DataRailModel model;
	
	private CyNode midasFileNode;
	private CyNode loadMidasNode;
	private CyNode cnoListNode;
	private CyNode normalizeNode;
	private CyNode cnoListNormalizedNode;
	private CyNode optimizeNode;
	private CyNode cnoListOptimizedNode;
	
	private CyNetworkFactory networkFactory;
	private CyNetworkViewFactory networkViewFactory;
	private ContextMenuFactory contextMenuFactory;
	
	private CyRootNetworkManager rootNetworkManager;
	private CyNetworkViewManager networkViewManager;
	private CyNetworkManager networkManager;
	
	private CyRootNetwork dataRailRoot;
	private CyNetwork dataRailRootModel;
	private CySubNetwork dataRailNetwork;
	private CyNetworkView view;
	
	
	public DataRailWorkFlow (CyActivator activator) {
		this.activator = activator;
		
		networkFactory = activator.cyAppAdapter.getCyNetworkFactory();
		networkViewFactory = activator.cyAppAdapter.getCyNetworkViewFactory();
		
		networkManager = activator.cyAppAdapter.getCyNetworkManager();
		networkViewManager = activator.cyAppAdapter.getCyNetworkViewManager();
		rootNetworkManager = activator.cyAppAdapter.getCyRootNetworkManager();
	}
	
	
	public void start () throws Exception {
		model = new DataRailModel (activator);
	
		
		dataRailRootModel = networkFactory.createNetwork();
		
		dataRailRoot = rootNetworkManager.getRootNetwork(dataRailRootModel);
		dataRailRoot.getRow(dataRailRoot).set(CyRootNetwork.NAME, "DataRail");
		
		dataRailNetwork = dataRailRoot.addSubNetwork();
		networkManager.addNetwork(dataRailNetwork);
		dataRailNetwork.getRow(dataRailNetwork).set(CySubNetwork.NAME, "workflow");

		view = networkViewFactory.createNetworkView(dataRailNetwork);
		networkViewManager.addNetworkView(view);
		
		List<Long> workflowNodesSUIDs = createNodes();
		createEdges();
		
		view.fitContent();		
		view.updateView();
		
		contextMenuFactory = new ContextMenuFactory(activator, workflowNodesSUIDs, model);
		activator.registerNodeContexMenu(contextMenuFactory);
		
		DataRailVisualStyle dataRailVisualStyle = new DataRailVisualStyle(this.activator);
		dataRailVisualStyle.applyVisualStyle();
	}
			
		
	private List<Long> createNodes () {

		CyTable defaultNodeTable = dataRailNetwork.getDefaultNodeTable();
		defaultNodeTable.createColumn(DataRailAttributes.NODE_TYPE, String.class, false);
		defaultNodeTable.createColumn(DataRailAttributes.NODE_LABEL, String.class, false);
		defaultNodeTable.createColumn(DataRailAttributes.NODE_STATUS, String.class, false);
		List<Long> workflowNodesSUIDs = new ArrayList <Long>();
		
		
		// create the node of the dataRail
		CyNode node = dataRailRoot.addNode();
		workflowNodesSUIDs.add(node.getSUID());
		midasFileNode = dataRailRoot.getNode(node.getSUID());
		dataRailNetwork.addNode(midasFileNode);
		view.updateView();
		
		// place the node in the network
		View<CyNode> midasFileNodeView = view.getNodeView(midasFileNode);
		midasFileNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP * 1);
		midasFileNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
	
		// set the attributes of the node
		defaultNodeTable.getRow(midasFileNode.getSUID()).set("name", "Step 1");
		defaultNodeTable.getRow(midasFileNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_OBJECT);
		defaultNodeTable.getRow(midasFileNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(midasFileNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_MIDAS_FILE);

		
		// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs.add(node.getSUID());
		loadMidasNode = dataRailRoot.getNode(node.getSUID());
		dataRailNetwork.addNode(loadMidasNode);
		view.updateView();
		
		// place the node in the network
		View<CyNode> loadMidasNodeView = view.getNodeView(loadMidasNode);
		loadMidasNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP * 2);
		loadMidasNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
	
		// set the attributes of the node
		defaultNodeTable.getRow(loadMidasNode.getSUID()).set("name", "Step 2");
		defaultNodeTable.getRow(loadMidasNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_FUNCTION);
		defaultNodeTable.getRow(loadMidasNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(loadMidasNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_LOAD_MIDAS);

		
		// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs.add(node.getSUID());
		cnoListNode = dataRailRoot.getNode(node.getSUID());
		dataRailNetwork.addNode(cnoListNode);
		view.updateView();
		
		// place the node in the network
		View<CyNode> cnoListNodeView = view.getNodeView(cnoListNode);
		cnoListNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP * 3);
		cnoListNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
	
		// set the attributes of the node
		defaultNodeTable.getRow(cnoListNode.getSUID()).set("name", "Step 3");
		defaultNodeTable.getRow(cnoListNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_OBJECT);
		defaultNodeTable.getRow(cnoListNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(cnoListNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_CNO_LIST);


		// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs.add(node.getSUID());
		normalizeNode = dataRailRoot.getNode(node.getSUID());
		dataRailNetwork.addNode(normalizeNode);
		view.updateView();
		
		//place the node in the network
		View<CyNode> normalizeNodeView = view.getNodeView(normalizeNode);
		normalizeNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP * 4);
		normalizeNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
		
		// set the attributes of the node
		defaultNodeTable.getRow(normalizeNode.getSUID()).set("name", "Step 4");
		defaultNodeTable.getRow(normalizeNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_FUNCTION);
		defaultNodeTable.getRow(normalizeNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(normalizeNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_NORMALIZE);


		// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs.add(node.getSUID());
		cnoListNormalizedNode = dataRailRoot.getNode(node.getSUID());
		dataRailNetwork.addNode(cnoListNormalizedNode);
		view.updateView();
		
		//place the node in the network
		View<CyNode> cnoListNormalizedNodeView = view.getNodeView(cnoListNormalizedNode);
		cnoListNormalizedNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP * 5);
		cnoListNormalizedNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
		
		// set the attributes of the node
		defaultNodeTable.getRow(cnoListNormalizedNode.getSUID()).set("name", "Step 5");
		defaultNodeTable.getRow(cnoListNormalizedNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_OBJECT);
		defaultNodeTable.getRow(cnoListNormalizedNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(cnoListNormalizedNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_NORMALIZED_CNO_LIST);

		
		// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs.add(node.getSUID());
		optimizeNode = dataRailRoot.getNode(node.getSUID());
		dataRailNetwork.addNode(optimizeNode);
		view.updateView();
		
		//place the node in the network
		View<CyNode> optimizeNodeView = view.getNodeView(optimizeNode);
		optimizeNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP * 6);
		optimizeNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
		
		// set the attributes of the node
		defaultNodeTable.getRow(optimizeNode.getSUID()).set("name", "Step 6");
		defaultNodeTable.getRow(optimizeNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_FUNCTION);
		defaultNodeTable.getRow(optimizeNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(optimizeNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_OPTMIZE);

		
		// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs.add(node.getSUID());
		cnoListOptimizedNode = dataRailRoot.getNode(node.getSUID());
		dataRailNetwork.addNode(cnoListOptimizedNode);
		view.updateView();
		
		//place the node in the network
		View<CyNode> cnoListOptimizedNodeView = view.getNodeView(cnoListOptimizedNode);
		cnoListOptimizedNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP * 7);
		cnoListOptimizedNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
		
		// set the attributes of the node
		defaultNodeTable.getRow(cnoListOptimizedNode.getSUID()).set("name", "Step 7");
		defaultNodeTable.getRow(cnoListOptimizedNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_OBJECT);
		defaultNodeTable.getRow(cnoListOptimizedNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(cnoListOptimizedNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_OPTMIZED_CNO_LIST);
		
		return workflowNodesSUIDs;
	}
		
	private void createEdges () {
		dataRailNetwork.addEdge(midasFileNode, loadMidasNode, true);
		dataRailNetwork.addEdge(loadMidasNode, cnoListNode, true);
		dataRailNetwork.addEdge(cnoListNode, normalizeNode, true);
		dataRailNetwork.addEdge(normalizeNode, cnoListNormalizedNode, true);
		dataRailNetwork.addEdge(cnoListNormalizedNode, optimizeNode, true);
		dataRailNetwork.addEdge(optimizeNode, cnoListOptimizedNode, true);
	}
	
}
