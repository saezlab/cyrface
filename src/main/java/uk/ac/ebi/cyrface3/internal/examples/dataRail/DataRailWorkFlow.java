package uk.ac.ebi.cyrface3.internal.examples.dataRail;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.application.CyApplicationManager;
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

import uk.ac.ebi.cyrface3.internal.Storage;

public class DataRailWorkFlow {
	
	private CyNode midasFileNode;
	private CyNode loadMidasNode;
	private CyNode cnoListNode;
	private CyNode normalizeNode;
	private CyNode cnoListNormalizedNode;
	private CyNode optimizeNode;
	private CyNode cnoListOptimizedNode;
	
	private Storage storage;
	private CyAppAdapter _appAdapter;
	private CyApplicationManager _appManager;
	private CyNetworkFactory networkFactory;
	private CyNetworkManager networkManager;
	private CyRootNetworkManager rootNetworkManager;
	private CyNetworkViewFactory networkViewFactory;
	private CyNetworkViewManager networkViewManager;
	private ContextMenuFactory contextMenuFactory;
	
	private CyRootNetwork dataRailRoot;
	private CyNetwork dataRailRootModel;
	private CySubNetwork dataRailNetwork;
	private CyNetworkView view;
	
	public DataRailWorkFlow(Storage storage){
		this.storage=storage;
		init();
		createModel();
	}
	
	private void init(){
		_appAdapter = storage.getCyAppAdapter();
		_appManager = storage.getCyApplicationManager();
		networkFactory = _appAdapter.getCyNetworkFactory();
		networkManager = _appAdapter.getCyNetworkManager();
		rootNetworkManager = _appAdapter.getCyRootNetworkManager();
		networkViewFactory =_appAdapter.getCyNetworkViewFactory();
		networkViewManager = _appAdapter.getCyNetworkViewManager();
		contextMenuFactory = storage.getContextMenuFactory();
	}
	
	private void createModel(){
		createRootModel();
		createRootNetwork();
		createSubNetwork();
		createSubNetworkView();
		createNodes();
		createEdges();
		viewFitContent();
				
		DataRailVisualStyle dataRailVisualStyle = storage.getVisualStyleObject();
		dataRailVisualStyle.createDataRailVisualStyle();
		dataRailVisualStyle.applyVisualStyle();
		
		view.updateView();
	}
	
	private void createRootModel(){
			// a model-network for the data-rail-root-network
		dataRailRootModel = networkFactory.createNetwork();
	
	}

	private void createRootNetwork(){
			// the actual data-rail-root-network
		dataRailRoot = rootNetworkManager.getRootNetwork(dataRailRootModel);
			// set the name for the root-network
		dataRailRoot.getRow(dataRailRoot).set(CyRootNetwork.NAME, "DataRail");
	}
	
	
	private void createSubNetwork(){
			// add a subnetwork to the data-rail-root-network and save the reference
		dataRailNetwork = dataRailRoot.addSubNetwork();
		
			// register the subnetwork to the Cytoscape-network-manager
		networkManager.addNetwork(dataRailNetwork);
		
			// set the name for the data-rail-subnetwork
		dataRailNetwork.getRow(dataRailNetwork).set(CySubNetwork.NAME, "rail");
	}
	
	private void createSubNetworkView(){
			// create the view for the data-rail-subnetwork
		view = networkViewFactory.createNetworkView(dataRailNetwork);
		
			// register the view to the Cytoscape-view-manager
		networkViewManager.addNetworkView(view);
	}
		
	private void createNodes(){

		CyTable defaultNodeTable = dataRailNetwork.getDefaultNodeTable();
		defaultNodeTable.createColumn(DataRailAttributes.NODE_TYPE, String.class, false);
		defaultNodeTable.createColumn(DataRailAttributes.NODE_LABEL, String.class, false);
		defaultNodeTable.createColumn(DataRailAttributes.NODE_STATUS, String.class, false);
		long[] workflowNodesSUIDs = new long[7];
		
			// create the node of the dataRail
		CyNode node = dataRailRoot.addNode();
		workflowNodesSUIDs[0] = node.getSUID();
		midasFileNode = dataRailRoot.getNode(workflowNodesSUIDs[0]);
		dataRailNetwork.addNode(midasFileNode);
		view.updateView();
		
			//place the node in the network
		View<CyNode> midasFileNodeView = view.getNodeView(midasFileNode);
		midasFileNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP*1);
		midasFileNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
	
			// set the attributes of the node
		defaultNodeTable.getRow(midasFileNode.getSUID()).set("name", "Step 1");
		defaultNodeTable.getRow(midasFileNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_OBJECT);
		defaultNodeTable.getRow(midasFileNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(midasFileNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_MIDAS_FILE);
// --------------------------------------------------------------------------------------------------------------------------------		
			// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs[1] = node.getSUID();
		loadMidasNode = dataRailRoot.getNode(workflowNodesSUIDs[1]);
		dataRailNetwork.addNode(loadMidasNode);
		view.updateView();
		
			//place the node in the network
		View<CyNode> loadMidasNodeView = view.getNodeView(loadMidasNode);
		loadMidasNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP*2);
		loadMidasNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
	
			// set the attributes of the node
		defaultNodeTable.getRow(loadMidasNode.getSUID()).set("name", "Step 2");
		defaultNodeTable.getRow(loadMidasNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_FUNCTION);
		defaultNodeTable.getRow(loadMidasNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(loadMidasNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_LOAD_MIDAS);
// --------------------------------------------------------------------------------------------------------------------------------		
			// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs[2] = node.getSUID();
		cnoListNode = dataRailRoot.getNode(workflowNodesSUIDs[2]);
		dataRailNetwork.addNode(cnoListNode);
		view.updateView();
		
			//place the node in the network
		View<CyNode> cnoListNodeView = view.getNodeView(cnoListNode);
		cnoListNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP*3);
		cnoListNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
	
			// set the attributes of the node
		defaultNodeTable.getRow(cnoListNode.getSUID()).set("name", "Step 3");
		defaultNodeTable.getRow(cnoListNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_OBJECT);
		defaultNodeTable.getRow(cnoListNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(cnoListNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_CNO_LIST);
// --------------------------------------------------------------------------------------------------------------------------------		
			// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs[3] = node.getSUID();
		normalizeNode = dataRailRoot.getNode(workflowNodesSUIDs[3]);
		dataRailNetwork.addNode(normalizeNode);
		view.updateView();
		
			//place the node in the network
		View<CyNode> normalizeNodeView = view.getNodeView(normalizeNode);
		normalizeNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP*4);
		normalizeNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
		
			// set the attributes of the node
		defaultNodeTable.getRow(normalizeNode.getSUID()).set("name", "Step 4");
		defaultNodeTable.getRow(normalizeNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_FUNCTION);
		defaultNodeTable.getRow(normalizeNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(normalizeNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_NORMALIZE);
// --------------------------------------------------------------------------------------------------------------------------------		
			// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs[4] = node.getSUID();
		cnoListNormalizedNode = dataRailRoot.getNode(workflowNodesSUIDs[4]);
		dataRailNetwork.addNode(cnoListNormalizedNode);
		view.updateView();
		
			//place the node in the network
		View<CyNode> cnoListNormalizedNodeView = view.getNodeView(cnoListNormalizedNode);
		cnoListNormalizedNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP*5);
		cnoListNormalizedNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
		
			// set the attributes of the node
		defaultNodeTable.getRow(cnoListNormalizedNode.getSUID()).set("name", "Step 5");
		defaultNodeTable.getRow(cnoListNormalizedNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_OBJECT);
		defaultNodeTable.getRow(cnoListNormalizedNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(cnoListNormalizedNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_NORMALIZED_CNO_LIST);
// --------------------------------------------------------------------------------------------------------------------------------		
			// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs[5] = node.getSUID();
		optimizeNode = dataRailRoot.getNode(workflowNodesSUIDs[5]);
		dataRailNetwork.addNode(optimizeNode);
		view.updateView();
		
			//place the node in the network
		View<CyNode> optimizeNodeView = view.getNodeView(optimizeNode);
		optimizeNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP*6);
		optimizeNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
		
			// set the attributes of the node
		defaultNodeTable.getRow(optimizeNode.getSUID()).set("name", "Step 6");
		defaultNodeTable.getRow(optimizeNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_FUNCTION);
		defaultNodeTable.getRow(optimizeNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(optimizeNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_OPTMIZE);
// --------------------------------------------------------------------------------------------------------------------------------		
			// create the node of the dataRail
		node = dataRailRoot.addNode();
		workflowNodesSUIDs[6] = node.getSUID();
		cnoListOptimizedNode = dataRailRoot.getNode(workflowNodesSUIDs[6]);
		dataRailNetwork.addNode(cnoListOptimizedNode);
		view.updateView();
		
			//place the node in the network
		View<CyNode> cnoListOptimizedNodeView = view.getNodeView(cnoListOptimizedNode);
		cnoListOptimizedNodeView.setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, DataRailAttributes.VERTICAL_NODE_COORDINATE);
		cnoListOptimizedNodeView.setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, DataRailAttributes.VERTICAL_NODE_STEP*7);
		
			// set the attributes of the node
		defaultNodeTable.getRow(cnoListOptimizedNode.getSUID()).set("name", "Step 7");
		defaultNodeTable.getRow(cnoListOptimizedNode.getSUID()).set(DataRailAttributes.NODE_TYPE, DataRailAttributes.NODE_TYPE_OBJECT);
		defaultNodeTable.getRow(cnoListOptimizedNode.getSUID()).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_UNDEFINED);
		defaultNodeTable.getRow(cnoListOptimizedNode.getSUID()).set(DataRailAttributes.NODE_LABEL, DataRailAttributes.NODE_LABEL_OPTMIZED_CNO_LIST);
		
		view.updateView();
		storage.setWorkflowNodesSUIDs(workflowNodesSUIDs);
	}
		
	private void createEdges(){
		
		dataRailNetwork.addEdge(midasFileNode, loadMidasNode, true);
		dataRailNetwork.addEdge(loadMidasNode, cnoListNode, true);
		dataRailNetwork.addEdge(cnoListNode, normalizeNode, true);
		dataRailNetwork.addEdge(normalizeNode, cnoListNormalizedNode, true);
		dataRailNetwork.addEdge(cnoListNormalizedNode, optimizeNode, true);
		dataRailNetwork.addEdge(optimizeNode, cnoListOptimizedNode, true);
	}
	
	private void viewFitContent(){
		view.fitContent();
	}
}
