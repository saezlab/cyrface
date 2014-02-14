//package uk.ac.ebi.cyrface3.internal.examples.dataRail.contextMenus;
//
//import java.io.File;
//
//import org.apache.commons.io.FilenameUtils;
//import org.sbfc.api.GeneralModel;
//
//import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailModel;
//import uk.ac.ebi.cyrface3.internal.sbml.sbfc.Qual2CellNOpt;
//import uk.ac.ebi.cyrface3.internal.sbml.sbfc.SBMLQualModel;
//
//public class OptimizeCnoList {
//
//	private DataRailModel model;
//	
//	public OptimizeCnoList(){
//		this.model = model;
//	}
//	
//	public void optimizeCnoList(){
//		try{
//			String extension = FilenameUtils.getExtension(model.getPknModelFile());
//			if( extension.equals("xml") || extension.equals("sbml") ){
//				SBMLQualModel smblModel = new SBMLQualModel();
//				smblModel.setModelFromFile(model.getPknModelFile());
//				
//				Qual2CellNOpt qual2cno = new Qual2CellNOpt();
//				GeneralModel convertedModel = qual2cno.convert(smblModel);
//				
//				File sifModel = File.createTempFile(FilenameUtils.getName(model.getPknModelFile()), ".sif");
//				convertedModel.modelToFile(sifModel.getAbsolutePath());
//				
//				model.setPknModelFile(sifModel.getAbsolutePath());
//			}
//			
//			model.getRCommand().optmise(model.getPknModelFile());
//			
//			File optimizedMidasFile = File.createTempFile(FilenameUtils.getName(model.getMidasFilePath())+"_optimized", ".csv");
//			optimizedMidasFile.delete();
//			model.getRCommand().writeOptimizedMIDAS(optimizedMidasFile.getAbsolutePath());
//			model.setOptimizedMidasFile(optimizedMidasFile);
//			
//		}catch(Exception e2){
//			e2.printStackTrace();
//		}
//	}
//	
//}
