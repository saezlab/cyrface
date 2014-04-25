package uk.ac.ebi.cyrface.internal.examples.commandLine;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class CustomCommandTaskFactory implements TaskFactory {

	private CyServiceRegistrar cyServiceRegistrar;
	
	public CustomCommandTaskFactory (CyServiceRegistrar cyServiceRegistrar) {
		this.cyServiceRegistrar = cyServiceRegistrar;
	}
	
	@Override
	public TaskIterator createTaskIterator() {
		CustomCommandTask customCommandTask = new CustomCommandTask(cyServiceRegistrar);
		TaskIterator iterator = new TaskIterator(1, customCommandTask);
		return iterator;
	}

	@Override
	public boolean isReady() {
		return false;
	}

}
