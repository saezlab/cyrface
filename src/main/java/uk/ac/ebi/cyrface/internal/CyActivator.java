package uk.ac.ebi.cyrface.internal;

import java.util.Properties;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

import uk.ac.ebi.cyrface.internal.examples.dataRail.DataRailWorkFlow;
import uk.ac.ebi.cyrface.internal.examples.dataRail.menu.ContextMenuFactory;
import uk.ac.ebi.cyrface.internal.examples.dataRail.menu.DataRailCyMenu;

public class CyActivator extends AbstractCyActivator {

	public BundleContext bundleContext;
	
	public CySwingApplication cySwingApplication;
	public CyApplicationManager cyApplicationManager;
	public CyAppAdapter cyAppAdapter;
	public DialogTaskManager dialogTaskManager;
	public SynchronousTaskManager synchronousTaskManager;
	
	public CyNetworkFactory cyNetworkFactory;
	public CyNetworkViewFactory cyNetworkViewFactory;
	public CyNetworkManager cyNetworkManager;
	public CyNetworkViewManager cyNetworkViewManager;
	
	public CyEventHelper cyEventHelper;
	
	public void start(BundleContext bc) throws Exception {
		bundleContext = bc;
		
		/* Get Cytoscape Services */
		cyAppAdapter = getService(bc, CyAppAdapter.class);		

		cySwingApplication = getService(bc, CySwingApplication.class);
		cyApplicationManager = getService(bc, CyApplicationManager.class);
		
		dialogTaskManager = getService(bc, DialogTaskManager.class);
		synchronousTaskManager = getService(bc, SynchronousTaskManager.class);
		
		cyNetworkFactory = getService(bc, CyNetworkFactory.class);
		cyNetworkViewFactory = getService(bc, CyNetworkViewFactory.class);
		cyNetworkManager = getService(bc, CyNetworkManager.class);
		cyNetworkViewManager = getService(bc, CyNetworkViewManager.class);
		
		cyEventHelper = getService(bc, CyEventHelper.class);
		
		DataRailWorkFlow dataRail = new DataRailWorkFlow(this);
		DataRailCyMenu menuClick = new DataRailCyMenu(this, dataRail);
		
		Properties properties = new Properties();
		registerAllServices(bc, menuClick, properties);
		registerAllServices(bc, dataRail, properties);
		
	}
	
	public void registerNodeContexMenu (ContextMenuFactory contextMenuFactory) {
		Properties contextMenuFactoryProps = new Properties();
		contextMenuFactoryProps.setProperty("title", "DataRail Workflow");
		registerAllServices(bundleContext, contextMenuFactory, contextMenuFactoryProps);
	}
	
}
