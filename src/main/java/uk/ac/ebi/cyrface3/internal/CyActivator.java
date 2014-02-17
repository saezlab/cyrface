package uk.ac.ebi.cyrface3.internal;

import java.util.Properties;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailVisualStyle;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.contextMenus.ContextMenuFactory;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.menu.DataRailCyMenu;

public class CyActivator extends AbstractCyActivator {

	public void start(BundleContext context) throws Exception {
			// these services provide access to the main classes of the Cytoscape 3 API
		CySwingApplication cytoscapeDesktopService = getService(context, CySwingApplication.class);
		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		CyAppAdapter cyAppAdapter = getService(context, CyAppAdapter.class);
		
			// The storage class is my model for the whole project.
		Storage storage = new Storage();
			// the services are saved in the storage for simple and unique access
		storage.setCytoscapeDesktopService(cytoscapeDesktopService);
		storage.setCyApplicationManager(cyApplicationManager);
		storage.setCyAppAdapter(cyAppAdapter);
		
			// To be able to use the context-menus in Cytoscape, 
			// the context-menu-factory must be initialized and registered. 
		ContextMenuFactory contextMenuFactory = new ContextMenuFactory(storage);
		storage.setContextMenuFactory(contextMenuFactory);
		
			// The visual style of the data rail is initialized here
		DataRailVisualStyle dataRailVisualStyle = new DataRailVisualStyle(storage);
			// For an easy usage it is saved in the storage
		storage.setVisualStyleObject( dataRailVisualStyle);
		
			// This class links the menu to the cytoscape-menu for starting the Rserve and the work-flow
		DataRailCyMenu menuClick = new DataRailCyMenu("DataRail", storage);
		
			// Here all Cytoscape-extern (plugin-intern) services are registered
		Properties properties = new Properties();
		registerAllServices(context, menuClick, properties);
		registerAllServices(context, dataRailVisualStyle, properties);
		registerAllServices(context, contextMenuFactory, properties);
	}
}
