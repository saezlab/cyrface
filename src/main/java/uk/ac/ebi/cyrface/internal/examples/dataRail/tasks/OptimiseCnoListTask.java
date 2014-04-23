package uk.ac.ebi.cyrface.internal.examples.dataRail.tasks;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.sbfc.api.GeneralModel;

import uk.ac.ebi.cyrface.internal.examples.dataRail.DataRailAttributes;
import uk.ac.ebi.cyrface.internal.examples.dataRail.DataRailModel;
import uk.ac.ebi.cyrface.internal.sbml.sbfc.Qual2CellNOpt;
import uk.ac.ebi.cyrface.internal.sbml.sbfc.SBMLQualModel;

public class OptimiseCnoListTask extends AbstractTask {
	
	private List<CyNode> workflowNodes;
	private DataRailModel model;
	
	private CyNetworkView view;
	private CyNetwork network;
	
	
	public OptimiseCnoListTask (DataRailModel model, List<CyNode> workflowNodes, CyNetwork network, CyNetworkView view) {
		this.workflowNodes = workflowNodes;
		this.model = model;
		this.network = network;
		this.view = view;
	}
	
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		taskMonitor.setTitle("Running CellNOptR optimization");
		
		taskMonitor.setProgress(0.1);
		taskMonitor.setStatusMessage("Running optimization... This may take a few minutes.");
		
		String extension = FilenameUtils.getExtension(model.getPknModelFile());
		if( extension.equals("xml") || extension.equals("sbml") ){
			SBMLQualModel smblModel = new SBMLQualModel();
			smblModel.setModelFromFile(model.getPknModelFile());
			
			Qual2CellNOpt qual2cno = new Qual2CellNOpt();
			GeneralModel convertedModel = qual2cno.convert(smblModel);
			
			File sifModel = File.createTempFile(FilenameUtils.getName(model.getPknModelFile()), ".sif");
			convertedModel.modelToFile(sifModel.getAbsolutePath());
			
			model.setPknModelFile(sifModel.getAbsolutePath());
		}
		
		model.getRCommand().optmise(model.getPknModelFile());
		
		File optimizedMidasFile = File.createTempFile(FilenameUtils.getName(model.getMidasFilePath()) + "_optimized", ".csv");
		optimizedMidasFile.delete();
		model.getRCommand().writeOptimizedMIDAS(optimizedMidasFile.getAbsolutePath());
		model.setOptimizedMidasFile(optimizedMidasFile);
		
		network.getRow(workflowNodes.get(5)).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
		network.getRow(workflowNodes.get(6)).set(DataRailAttributes.NODE_STATUS, DataRailAttributes.NODE_STATUS_DEFINED);
		
		view.updateView();
		
		taskMonitor.setStatusMessage("Normalization done.");
		taskMonitor.setProgress(1.0);
	}

}
