//package uk.ac.ebi.cyrface3.internal.utils;
//
//import java.io.File;
//
//import org.apache.batik.swing.JSVGCanvas;
//
//public class SVGPlots {
//
//	private File plotFile;
//	
//	public SVGPlots(File file){
//		this.plotFile = file;
//	}
//	
//	public JSVGCanvas createPlotPanel(){
//		
//		try {
//			JSVGCanvas canvas = new JSVGCanvas();
//			
//			canvas.setDoubleBufferedRendering(true);
//			
//			canvas.setSize(600, 400);			
//			canvas.setURI(plotFile.toURI().toURL().toString());
//			
//			return canvas;
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//}
