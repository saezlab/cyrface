package uk.ac.ebi.cyrface3.internal.examples.dataRail.menu;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.AbstractCyAction;

import uk.ac.ebi.cyrface3.internal.Storage;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailModel;
import uk.ac.ebi.cyrface3.internal.examples.dataRail.DataRailWorkFlow;

/**
 * Creates a new menu item under Apps menu section.
 */
public class DataRailCyMenu extends AbstractCyAction {
	private static final long serialVersionUID = 1681188245543353660L;
	private Storage storage;
	
	public DataRailCyMenu(final String menuTitle, Storage storage) {
		super(menuTitle, storage.getCyApplicationManager(), null, null);
		setPreferredMenu("Apps.CyRFace 3");
		this.storage = storage;
	}

	public void actionPerformed(ActionEvent e) {
		try{
			DataRailModel dataRailModel = new DataRailModel();
			storage.setDataRailModel(dataRailModel);
			
			new DataRailWorkFlow(storage);
		}catch(Exception e1){
			e1.printStackTrace();
		}
	}
}
