package uk.ac.ebi.cyrface2.internal.utils;

import java.io.File;

import org.apache.batik.swing.JSVGCanvas;

public class SVGPlots {

	private File plotFile;
	
	public SVGPlots(File file) {
		this.plotFile = file;
	}
	
	public JSVGCanvas createPlotPanel() throws Exception {
		JSVGCanvas canvas = new JSVGCanvas();
		
		canvas.setDoubleBufferedRendering(true);
		
		canvas.setSize(600, 400);			
		canvas.setURI(plotFile.toURI().toURL().toString());
		
		return canvas;
	}
	
}
