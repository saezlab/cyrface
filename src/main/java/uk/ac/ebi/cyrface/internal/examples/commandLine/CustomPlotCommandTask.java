package uk.ac.ebi.cyrface.internal.examples.commandLine;

import java.io.File;

import javax.swing.ImageIcon;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

import rcaller.RCaller;
import uk.ac.ebi.cyrface.internal.utils.PlotsDialog;

public class CustomPlotCommandTask extends AbstractTask implements ObservableTask {

	private CyServiceRegistrar cyServiceRegistrar;
	
	private StringBuilder outputString;
	
	@Tunable(description="command")
    public String command = "boxplot(cars)";
	
	
	public CustomPlotCommandTask (CyServiceRegistrar cyServiceRegistrar) {
		this.cyServiceRegistrar = cyServiceRegistrar;
		this.outputString = new StringBuilder();
	}
	
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		String commandString = ((String)command).trim();

		RCaller caller = new RCaller();
		caller.setRscriptExecutable("/usr/bin/Rscript");
		caller.cleanRCode();

		File file = caller.getRCode().startPlot();
		caller.getRCode().addRCode(commandString);
		caller.getRCode().endPlot();
		caller.runOnly();

		ImageIcon plot = caller.getRCode().getPlot(file);
		
		PlotsDialog dialog = new PlotsDialog(file);
		dialog.display();
	}

	@Override
	public <R> R getResults(Class<? extends R> type) {
		return type.cast(outputString.toString());
	}

}
