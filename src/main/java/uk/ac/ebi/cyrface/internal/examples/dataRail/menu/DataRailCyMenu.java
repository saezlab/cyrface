package uk.ac.ebi.cyrface.internal.examples.dataRail.menu;

import java.awt.event.ActionEvent;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.service.util.CyServiceRegistrar;

import uk.ac.ebi.cyrface.internal.examples.dataRail.DataRailWorkFlow;

@SuppressWarnings("serial")
public class DataRailCyMenu extends AbstractCyAction {
	
	private DataRailWorkFlow dataRail;
	
	public DataRailCyMenu (CyServiceRegistrar cyServiceRegistrar, DataRailWorkFlow dataRail) {
		super("DataRail", cyServiceRegistrar.getService(CyApplicationManager.class), null, null);

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
