package uk.ac.ebi.cyrface.internal.examples.commandLine;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

import uk.ac.ebi.cyrface.internal.rinterface.rserve.RserveHandler;

public class CustomCommandTask extends AbstractTask implements ObservableTask {

	private CyServiceRegistrar cyServiceRegistrar;
	private StringBuilder outputString;
	
	@Tunable(description="command")
    public String command = "print('Hello World')";
	
	
	public CustomCommandTask (CyServiceRegistrar cyServiceRegistrar) {
		this.cyServiceRegistrar = cyServiceRegistrar;
		this.outputString = new StringBuilder();
	}
	
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		RserveHandler handler = new RserveHandler(cyServiceRegistrar);

		String commandString = ((String) command).trim();
		
		String[] output = ((RserveHandler)handler).executeCaptureOutput(commandString);

		for (String outputLine : output) {
			outputString.append(outputLine);
			outputString.append("\n");
		}
	}

	@Override
	public <R> R getResults(Class<? extends R> type) {
		return type.cast(outputString.toString());
	}
}
