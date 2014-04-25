package uk.ac.ebi.cyrface.internal.rinterface.rserve;

import org.cytoscape.work.FinishStatus;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskObserver;

public class StartRServeTaskObservable implements TaskObserver {

	boolean taskComplete = false;
	
	@Override
	public void taskFinished (ObservableTask task) {
		taskComplete = true;
	}

	public boolean isComplete() { 
		return taskComplete; 
	}
	
	@Override
	public void allFinished(FinishStatus finishStatus) {}

}
