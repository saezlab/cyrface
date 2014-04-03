package uk.ac.ebi.cyrface2.internal.examples.dataRail.menu;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.AbstractCyAction;

import uk.ac.ebi.cyrface2.internal.CyActivator;
import uk.ac.ebi.cyrface2.internal.examples.dataRail.DataRailWorkFlow;

@SuppressWarnings("serial")
public class DataRailCyMenu extends AbstractCyAction {
	
	private DataRailWorkFlow dataRail;
	
	public DataRailCyMenu (CyActivator activator, DataRailWorkFlow dataRail) {
		super("DataRail", activator.cyApplicationManager, null, null);

		this.dataRail = dataRail;
		
		setPreferredMenu("Apps.Cyrface");		
	}

	public void actionPerformed (ActionEvent event) {
		try{
			dataRail.start();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
