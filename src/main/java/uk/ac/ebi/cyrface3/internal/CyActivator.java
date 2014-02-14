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
		CySwingApplication cytoscapeDesktopService = getService(context, CySwingApplication.class);
		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		CyAppAdapter cyAppAdapter = getService(context, CyAppAdapter.class);
		
		Storage storage = new Storage();
		storage.setCytoscapeDesktopService(cytoscapeDesktopService);
		storage.setCyApplicationManager(cyApplicationManager);
		storage.setCyAppAdapter(cyAppAdapter);
		
		ContextMenuFactory contextMenuFactory = new ContextMenuFactory(storage);
		storage.setContextMenuFactory(contextMenuFactory);
		
		DataRailVisualStyle dataRailVisualStyle = new DataRailVisualStyle(storage);
		storage.setVisualStyleObject( dataRailVisualStyle);
		
		DataRailCyMenu menuClick = new DataRailCyMenu("DataRail", storage);
		
		Properties properties = new Properties();
		registerAllServices(context, menuClick, properties);
		registerAllServices(context, dataRailVisualStyle, properties);
		registerAllServices(context, contextMenuFactory, properties);
	}
}
