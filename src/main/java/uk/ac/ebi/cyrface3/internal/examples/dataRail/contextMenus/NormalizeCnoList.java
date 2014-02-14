package uk.ac.ebi.cyrface3.internal.examples.dataRail.contextMenus;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailAttributes;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailModel;

class NormalizeCnoList{

	private DataRailModel model;

	
	public NormalizeCnoList(DataRailModel model){
		this.model = model;
		JTextField ec50 = new JTextField(model.getEc50().toString());
		JTextField saturation = new JTextField(model.getSaturation().toString());
		JTextField detection = new JTextField(model.getDetection().toString());
		final JComponent[] inputs = new JComponent[] {
			new JLabel("EC50Data"),
			ec50,
			new JLabel("Detection"),
			detection,
			new JLabel("Saturation"),
			saturation
		};
		JOptionPane.showMessageDialog(null, inputs, "Normalization Function Arguments", JOptionPane.PLAIN_MESSAGE); // TODO is null ok?
		saveNormalizationArgs(ec50.getText(), saturation.getText(), detection.getText());
	}

	public void saveNormalizationArgs(String ec50String, String saturationString, String detectionString){
		Double ec50, saturation, detection;
		
		try{
			ec50 = Double.valueOf(ec50String);
		}catch(Exception e){
			ec50 = DataRailAttributes.ec50_default; 
		}
		
		try{
			saturation = Double.valueOf(saturationString);
		}catch(Exception e){
			saturation = DataRailAttributes.saturation_default; 
		}
		
		try{
			detection = Double.valueOf(detectionString);
		}catch(Exception e){
			detection = DataRailAttributes.detection_default; 
		}
		
		model.setEc50(ec50);
		model.setSaturation(saturation);
		model.setDetection(detection);
	}
		
}	
