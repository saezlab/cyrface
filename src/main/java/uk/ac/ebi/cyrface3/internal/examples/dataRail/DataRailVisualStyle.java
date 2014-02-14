package uk.ac.ebi.cyrface3.internal.examples.dataRail;

import java.awt.Color;
import java.util.Iterator;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.view.presentation.property.ArrowShapeVisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.presentation.property.values.NodeShape;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualPropertyDependency;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

import uk.ac.ebi.cyrface3.internal.Storage;

public class DataRailVisualStyle {
	
	/**
	 * 
	 * 
	 * TODO Correct implementation -> missing integration in context!!
	 * 
	 * 
	 */
	
	private Storage storage;
	private CyAppAdapter _appAdapter;
	private VisualStyle vs;
	private VisualStyleFactory vsFactory;
	private String visualStyleName;
	private VisualMappingManager vmManager;
	private VisualMappingFunctionFactory vmFunctionFactoryD;
	private VisualMappingFunctionFactory vmFunctionFactoryP;
	
	public DataRailVisualStyle(Storage storage){
		this.storage = storage;
		init();
	}
	
	private void init(){
		visualStyleName = storage.getVisualStyleName();
		_appAdapter = storage.getCyAppAdapter();
		vmManager = _appAdapter.getVisualMappingManager(); 
		vsFactory = _appAdapter.getVisualStyleFactory();
		vmFunctionFactoryD = _appAdapter.getVisualMappingFunctionDiscreteFactory(); 
		vmFunctionFactoryP = _appAdapter.getVisualMappingFunctionPassthroughFactory();
	}
	
	public void createDataRailVisualStyle(){
		// iterator walks through the visual styles
		@SuppressWarnings("rawtypes")
		Iterator it = vmManager.getAllVisualStyles().iterator(); 
		boolean styleAlreadyExisting=false;
			// iterating as long there is a visual style left to check
		while (it.hasNext()) { 
				// set current visual style for testing
			VisualStyle curVS = (VisualStyle) it.next(); 
				// test if visual style is already existing
			if (curVS.getTitle().equalsIgnoreCase(visualStyleName)) { 
					//style is already existing
				styleAlreadyExisting=true;
				break; // if it is found, jump out of the loop
			}
		}
		
		if(styleAlreadyExisting==false){
			
			vs = vsFactory.createVisualStyle(visualStyleName);
// --------------------------------------------- node labels -----------------------------------------
			PassthroughMapping<String, String> pNodeLabels = (PassthroughMapping<String, String>)
					vmFunctionFactoryP.createVisualMappingFunction("label", String.class, 
							BasicVisualLexicon.NODE_LABEL);
			
			vs.addVisualMappingFunction(pNodeLabels);
			
// --------------------------------------------- node shapes -----------------------------------------
			DiscreteMapping<String, NodeShape> dNodeShapeMapping = (DiscreteMapping<String, NodeShape>) 
					vmFunctionFactoryD.createVisualMappingFunction("type", String.class,
							BasicVisualLexicon.NODE_SHAPE);
			
			dNodeShapeMapping.putMapValue("Object", NodeShapeVisualProperty.RECTANGLE);
			dNodeShapeMapping.putMapValue("Function", NodeShapeVisualProperty.HEXAGON);
			vs.addVisualMappingFunction(dNodeShapeMapping);
			
// --------------------------------------------- node color -----------------------------------------
			float[] hsbvalsDefined = new float[3];
			Color.RGBtoHSB(104, 204, 0, hsbvalsDefined);
			float[] hsbvalsUndefined = new float[3];
			Color.RGBtoHSB(204, 204, 204, hsbvalsUndefined);
			
			for(VisualPropertyDependency<?> vpd : vs.getAllVisualPropertyDependencies()){
				if(vpd.getDisplayName().equalsIgnoreCase("Lock node width and height")){
					vpd.setDependency(false);
				}
			}
			
			@SuppressWarnings({ "rawtypes", "unchecked" })
			DiscreteMapping<String, Color> dNodeFillColorMapping = (DiscreteMapping) vmFunctionFactoryD.
					createVisualMappingFunction(DataRailAttributes.NODE_STATUS, String.class,
							BasicVisualLexicon.NODE_FILL_COLOR);
			
			dNodeFillColorMapping.putMapValue(DataRailAttributes.NODE_STATUS_DEFINED, 
							Color.getHSBColor(hsbvalsDefined[0],hsbvalsDefined[1],hsbvalsDefined[2]));
			dNodeFillColorMapping.putMapValue(DataRailAttributes.NODE_STATUS_UNDEFINED, 
							Color.getHSBColor(hsbvalsUndefined[0],hsbvalsUndefined[1],hsbvalsUndefined[2]));
			
			vs.addVisualMappingFunction(dNodeFillColorMapping);
// ----------------------------------------------- else --- -----------------------------------------			
			vs.setDefaultValue(BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE, 
					ArrowShapeVisualProperty.DELTA);
			vs.setDefaultValue(BasicVisualLexicon.NODE_BORDER_PAINT, Color.BLACK);
			vs.setDefaultValue(BasicVisualLexicon.NODE_BORDER_WIDTH, new Double(5));
			vs.setDefaultValue(BasicVisualLexicon.NODE_WIDTH, new Double(150));
			vs.setDefaultValue(BasicVisualLexicon.NODE_HEIGHT, new Double(50));
			vs.setDefaultValue(BasicVisualLexicon.NODE_LABEL_FONT_SIZE, new Integer(15));
			vs.setDefaultValue(BasicVisualLexicon.NODE_FILL_COLOR, 
					Color.getHSBColor(hsbvalsUndefined[0],hsbvalsUndefined[1],hsbvalsUndefined[2]));
			
			vmManager.addVisualStyle(vs);
		}
	}
	
	public void applyVisualStyle(){
		
		@SuppressWarnings({"rawtypes"})
		Iterator it = vmManager.getAllVisualStyles().iterator(); 

			// iterating as long there is a visual style left to check
		while (it.hasNext()) { 
				// set current visual style for testing
			VisualStyle curVS = (VisualStyle) it.next(); 
				// test if visual style is already existing
			if (curVS.getTitle().equalsIgnoreCase(visualStyleName)) {
					// set the current visual style
				vmManager.setCurrentVisualStyle(curVS);
				break; // if it is found, jump out of the loop
			}
		}
		
		storage.getCyApplicationManager().getCurrentNetworkView().updateView();
	}
}
