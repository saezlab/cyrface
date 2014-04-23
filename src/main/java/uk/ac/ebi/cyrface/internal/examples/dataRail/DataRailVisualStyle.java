package uk.ac.ebi.cyrface.internal.examples.dataRail;

import java.awt.Color;

import org.cytoscape.view.presentation.property.ArrowShapeVisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualPropertyDependency;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

import uk.ac.ebi.cyrface.internal.CyActivator;

public class DataRailVisualStyle {
	
	private static final String visualStyleName = "DataRailVisualStyle";
	
	private CyActivator activator;
	
	private VisualStyleFactory vsFactory;
	private VisualMappingManager vmManager;
	
	private VisualMappingFunctionFactory vmFunctionFactoryD;
	private VisualMappingFunctionFactory vmFunctionFactoryP;
	
	
	public DataRailVisualStyle (CyActivator activator) {
		this.activator = activator;
		
		vmManager = activator.cyAppAdapter.getVisualMappingManager(); 
		vsFactory = activator.cyAppAdapter.getVisualStyleFactory();
		
		vmFunctionFactoryD = activator.cyAppAdapter.getVisualMappingFunctionDiscreteFactory(); 
		vmFunctionFactoryP = activator.cyAppAdapter.getVisualMappingFunctionPassthroughFactory();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public VisualStyle createDataRailVisualStyle () {

		VisualStyle vs = getVisualStyle(visualStyleName);
		
		if (vs == null) {
			vs = vsFactory.createVisualStyle(visualStyleName);

			PassthroughMapping pNodeLabels = (PassthroughMapping) vmFunctionFactoryP.createVisualMappingFunction("label", String.class, BasicVisualLexicon.NODE_LABEL);
			vs.addVisualMappingFunction(pNodeLabels);
			
			DiscreteMapping dNodeShapeMapping = (DiscreteMapping) vmFunctionFactoryD.createVisualMappingFunction("type", String.class, BasicVisualLexicon.NODE_SHAPE);
			dNodeShapeMapping.putMapValue("Object", NodeShapeVisualProperty.RECTANGLE);
			dNodeShapeMapping.putMapValue("Function", NodeShapeVisualProperty.HEXAGON);
			vs.addVisualMappingFunction(dNodeShapeMapping);
			
			
			DiscreteMapping dNodeFillColorMapping = (DiscreteMapping) vmFunctionFactoryD.createVisualMappingFunction(DataRailAttributes.NODE_STATUS, String.class, BasicVisualLexicon.NODE_FILL_COLOR);
			dNodeFillColorMapping.putMapValue(DataRailAttributes.NODE_STATUS_DEFINED, new Color(0x73b360));
			dNodeFillColorMapping.putMapValue(DataRailAttributes.NODE_STATUS_UNDEFINED, new Color(0xe4e2e2));
			vs.addVisualMappingFunction(dNodeFillColorMapping);
			
			
			for (VisualPropertyDependency vpd : vs.getAllVisualPropertyDependencies()) {
				if (vpd.getDisplayName().equalsIgnoreCase("Lock node width and height")) {
					vpd.setDependency(false); break;
				}
			}
			
			
			vs.setDefaultValue(BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.DELTA);
			vs.setDefaultValue(BasicVisualLexicon.NODE_BORDER_PAINT, Color.BLACK);
			vs.setDefaultValue(BasicVisualLexicon.NODE_BORDER_WIDTH, new Double(5));
			vs.setDefaultValue(BasicVisualLexicon.NODE_WIDTH, new Double(150));
			vs.setDefaultValue(BasicVisualLexicon.NODE_HEIGHT, new Double(50));
			vs.setDefaultValue(BasicVisualLexicon.NODE_LABEL_FONT_SIZE, new Integer(15));
			vs.setDefaultValue(BasicVisualLexicon.NODE_FILL_COLOR, new Color(0x786f6f));
			
			
			vmManager.addVisualStyle(vs);
		}
		
		return vs;
	}
	
	public void applyVisualStyle () {
		VisualStyle vs = createDataRailVisualStyle();
		vmManager.setCurrentVisualStyle(vs);
		activator.cyApplicationManager.getCurrentNetworkView().updateView();
	}	
	
	public VisualStyle getVisualStyle (String name) {
		for (VisualStyle style : vmManager.getAllVisualStyles())
			if (style.getTitle().equals(name))
				return style;
		
		return null;
	}
}
