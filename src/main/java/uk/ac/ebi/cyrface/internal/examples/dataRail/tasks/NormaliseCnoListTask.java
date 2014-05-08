package uk.ac.ebi.cyrface.internal.examples.dataRail.tasks;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

import uk.ac.ebi.cyrface.internal.examples.dataRail.DataRailAttributes;
import uk.ac.ebi.cyrface.internal.examples.dataRail.DataRailModel;
import uk.ac.ebi.cyrface.internal.utils.Rutils;

public class NormaliseCnoListTask extends AbstractTask {

	private List<CyNode> workflowNodes;
	private DataRailModel model;
	
	private CyNetworkView view;
	private CyNetwork network;
	
	@Tunable(description="EC50")
    public double ec50 = 0.5;
	
	@Tunable(description="Saturation")
    public double saturation = Double.POSITIVE_INFINITY;
	
	@Tunable(description="Detection")
    public double detection = 0.0;
	
	public NormaliseCnoListTask (DataRailModel model, List<CyNode> workflowNodes, CyNetwork network, CyNetworkView view) {
		this.workflowNodes = workflowNodes;
		this.model = model;
		this.network = network;
		this.view = view;
	}
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Running data normalization");
		
		taskMonitor.setProgress(0.1);
		taskMonitor.setStatusMessage("Normalizing MIDAS...");
		
		model.getRCommand().normaliseCnoList(model);
		
		File normalizedMidasFile = File.createTempFile(FilenameUtils.getName(model.getMidasFilePath())+"_normalized", ".csv");
		normalizedMidasFile.delete();
		model.getRCommand().writeNormalizedMIDAS(Rutils.getWindowsCorrectPath(normalizedMidasFile.getAbsolutePath()));
		model.setNormalizedMidasFile(normalizedMidasFile);
		
		network.getRow(workflowNodes.get(3)).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
		network.getRow(workflowNodes.get(4)).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
		
		view.updateView();
		
		taskMonitor.setStatusMessage("Normalization done.");
		taskMonitor.setProgress(1.0);
	}
}
