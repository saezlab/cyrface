package uk.ac.ebi.cyrface2.internal;

import java.util.Properties;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

import uk.ac.ebi.cyrface2.internal.examples.dataRail.DataRailWorkFlow;
import uk.ac.ebi.cyrface2.internal.examples.dataRail.menu.ContextMenuFactory;
import uk.ac.ebi.cyrface2.internal.examples.dataRail.menu.DataRailCyMenu;

public class CyActivator extends AbstractCyActivator {

	public BundleContext bundleContext;
	
	public CySwingApplication cytoscapeDesktopService;
	public CyApplicationManager cyApplicationManager;
	public CyAppAdapter cyAppAdapter;
	public DialogTaskManager dialogTaskManager;
	public SynchronousTaskManager synchronousTaskManager;
	
	public void start(BundleContext bc) throws Exception {
		bundleContext = bc;
		
		/* Get Cytoscape Services */
		cytoscapeDesktopService = getService(bc, CySwingApplication.class);
		cyApplicationManager = getService(bc, CyApplicationManager.class);
		cyAppAdapter = getService(bc, CyAppAdapter.class);		
		dialogTaskManager = getService(bc, DialogTaskManager.class);
		synchronousTaskManager = getService(bc, SynchronousTaskManager.class);
		
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
