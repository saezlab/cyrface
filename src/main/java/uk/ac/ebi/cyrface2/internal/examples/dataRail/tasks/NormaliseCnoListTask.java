package uk.ac.ebi.cyrface2.internal.examples.dataRail.tasks;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

import uk.ac.ebi.cyrface2.internal.examples.dataRail.DataRailAttributes;
import uk.ac.ebi.cyrface2.internal.examples.dataRail.DataRailModel;

public class NormaliseCnoListTask extends AbstractTask {

	private CyTable defaultNodeTable;
	private List<Long> workflowNodesSUIDs;
	private DataRailModel model;
	
	private CyNetworkView view;
	private CyNetwork network;
	
	@Tunable(description="EC50")
    public double ec50 = 0.5;
	
	@Tunable(description="Saturation")
    public double saturation = Double.POSITIVE_INFINITY;
	
	@Tunable(description="Detection")
    public double detection = 0.0;
	
	public NormaliseCnoListTask (DataRailModel model, List<Long> workflowNodesSUIDs, CyTable defaultNodeTable, CyNetwork network, CyNetworkView view) {
		this.defaultNodeTable = defaultNodeTable;
		this.workflowNodesSUIDs = workflowNodesSUIDs;
		this.model = model;
		this.network = network;
		this.view = view;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setProgress(0.0);
		taskMonitor.setStatusMessage("Normalizing MIDAS...");
		
		model.getRCommand().normaliseCnoList(model);
		
		File normalizedMidasFile = File.createTempFile(FilenameUtils.getName(model.getMidasFilePath())+"_normalized", ".csv");
		normalizedMidasFile.delete();
		model.getRCommand().writeNormalizedMIDAS(normalizedMidasFile.getAbsolutePath());
		model.setNormalizedMidasFile(normalizedMidasFile);
		
		network.getDefaultNodeTable().getRow(workflowNodesSUIDs.get(3)).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
		network.getDefaultNodeTable().getRow(workflowNodesSUIDs.get(4)).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
		
		view.updateView();
		
		taskMonitor.setProgress(1.0);
		taskMonitor.setStatusMessage("Normalization done.");
	}
}
