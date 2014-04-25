package uk.ac.ebi.cyrface.internal;

import java.util.Properties;

import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.ServiceProperties;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

import uk.ac.ebi.cyrface.internal.examples.commandLine.CustomCommandTaskFactory;
import uk.ac.ebi.cyrface.internal.examples.commandLine.CustomPlotCommandTaskFactory;
import uk.ac.ebi.cyrface.internal.examples.dataRail.DataRailWorkFlow;
import uk.ac.ebi.cyrface.internal.examples.dataRail.menu.DataRailCyMenu;

public class CyActivator extends AbstractCyActivator {

	public BundleContext bundleContext;
	public CyServiceRegistrar cyServiceRegistrar;
	
	
	public void start(BundleContext bundleContext) throws Exception {
		this.bundleContext = bundleContext;
		
		cyServiceRegistrar = getService(bundleContext, CyServiceRegistrar.class);
		
		/* Register DataRail Workflow */
		DataRailWorkFlow dataRail = new DataRailWorkFlow(cyServiceRegistrar);
		DataRailCyMenu menuClick = new DataRailCyMenu(cyServiceRegistrar, dataRail);
		
		Properties properties = new Properties();
		registerAllServices(bundleContext, menuClick, properties);
		registerAllServices(bundleContext, dataRail, properties);
		
		registerCyrfaceCommands();
	}
	
	private void registerCyrfaceCommands () {
		/* Custom Command */
		Properties customCommandProps = new Properties();
		customCommandProps.setProperty(ServiceProperties.COMMAND, "run");
		customCommandProps.setProperty(ServiceProperties.COMMAND_NAMESPACE, "cyrface");
		
		CustomCommandTaskFactory customCommand = new CustomCommandTaskFactory(cyServiceRegistrar);
		registerService(bundleContext, customCommand, TaskFactory.class, customCommandProps);
		
		/* Custom Plot Command */
		Properties customPlotCommandProps = new Properties();
		customPlotCommandProps.setProperty(ServiceProperties.COMMAND, "plot");
		customPlotCommandProps.setProperty(ServiceProperties.COMMAND_NAMESPACE, "cyrface");
		
		CustomPlotCommandTaskFactory customPlotCommand = new CustomPlotCommandTaskFactory(cyServiceRegistrar);
		registerService(bundleContext, customPlotCommand, TaskFactory.class, customPlotCommandProps);
	}
	
}
