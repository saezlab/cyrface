package uk.ac.ebi.cyrface.internal.examples.commandLine;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class CustomPlotCommandTaskFactory implements TaskFactory {

	private CyServiceRegistrar cyServiceRegistrar;
	
	public CustomPlotCommandTaskFactory (CyServiceRegistrar cyServiceRegistrar) {
		this.cyServiceRegistrar = cyServiceRegistrar;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		CustomPlotCommandTask task = new CustomPlotCommandTask(cyServiceRegistrar);
		TaskIterator iterator = new TaskIterator(1, task);
		return iterator;
	}

	@Override
	public boolean isReady() {
		return false;
	}

}
