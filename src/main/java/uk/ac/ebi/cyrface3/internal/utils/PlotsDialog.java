//package uk.ac.ebi.cyrface3.internal.utils;
//
//import java.awt.BorderLayout;
//import java.awt.Dialog;
//import java.awt.Dimension;
//import java.awt.Image;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.io.File;
//
//import javax.imageio.ImageIO;
//import javax.swing.JDialog;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JMenu;
//import javax.swing.JMenuBar;
//import javax.swing.JMenuItem;
//import javax.swing.JPanel;
//import javax.swing.JSeparator;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.FilenameUtils;
//
//@SuppressWarnings("serial") 
//
//public class PlotsDialog extends JDialog{
//
//	private File plotFile;
//
//	private JPanel plotPanel;
//
//	private JMenuBar menuBar;
//	private JMenu fileMenu;
//
//	private File midasFile;
//
//	
//	public PlotsDialog(File plotFile){
//		this(plotFile, null);
//	}
//	 
//	public PlotsDialog(File plotFile, File midasFile){
//
//		this.midasFile = midasFile;
//		this.plotFile = plotFile;
//
//		// Configure Menu Bar 
//		menuBar = new JMenuBar();
//
//		fileMenu = new JMenu("File");
//		fileMenu.setMnemonic(KeyEvent.VK_F);
//		menuBar.add(fileMenu);
//
//		JMenuItem savePlotMenu = initFileSavePlotMenu();
//		fileMenu.add(savePlotMenu);
//		
//		JMenuItem saveMIDASMenu = initFileSaveMIDASMenu();
//		fileMenu.add(saveMIDASMenu);
//
//		fileMenu.add(new JSeparator());
//
//		JMenuItem closeMenu = initFileCloseMenu();
//		fileMenu.add(closeMenu);
//
//		if( FilenameUtils.getExtension(plotFile.getName()).toUpperCase().equals("SVG") ){
//			SVGPlots svgPlot = new SVGPlots(plotFile);
//			plotPanel = new JPanel(true);
//			plotPanel.setLayout(new BorderLayout());
//			plotPanel.add(svgPlot.createPlotPanel(), BorderLayout.CENTER);
//		}else{
//			Image plotImage = getPlotAsImage();
//			if( plotImage!=null ){
//				plotPanel = new ImagesPanel(plotImage);
//			}
//		}
//
//		menuBar.revalidate();
//
//		// Configure JFrame
//		this.setJMenuBar(menuBar);
//		this.setTitle("R Plot");
//		this.setMinimumSize(new Dimension(400, 600));
//		this.setPreferredSize(new Dimension(400, 600));
//		this.getContentPane().add(plotPanel);
//		this.setLocationRelativeTo(null); // should center the window
//		this.pack();
//		this.toFront();
//		this.setAlwaysOnTop(true);
//		this.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
//		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		this.setVisible(true);
//	}
//
//	private Image getPlotAsImage(){
//		try{
//			Image plotImage = ImageIO.read(plotFile);
//			return plotImage;
//		}catch(Exception e){
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private JMenuItem initFileSavePlotMenu(){
//		JMenuItem savePlotMenu = new JMenuItem("Save R plot...");
//		savePlotMenu.setMnemonic(KeyEvent.VK_S);
//
//		savePlotMenu.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				try{
//					JFileChooser fc = new JFileChooser();
//					fc.setDialogTitle("Save R Plot");
//					
//					int browserReturn = fc.showSaveDialog(null);
//					String savePath = "";
//							
//					if (browserReturn == JFileChooser.APPROVE_OPTION){
//						savePath = fc.getSelectedFile().getAbsolutePath();
//						if(!(fc.getSelectedFile().getName().toLowerCase().endsWith(".svg"))){
//							savePath = savePath+".png";
//						}
//						File destinationFile = new File(plotFile, savePath);
//						
//						FileUtils.copyFile(plotFile, destinationFile);
//					}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//		});
//		// TODO must be tested
//
//		return savePlotMenu;
//	}
//	
//	private JMenuItem initFileSaveMIDASMenu(){
//		JMenuItem saveMIDASMenu = new JMenuItem("Save MIDAS file...");
//		saveMIDASMenu.setMnemonic(KeyEvent.VK_S);
//
//		saveMIDASMenu.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				
//				try{
//					JFileChooser fc = new JFileChooser();
//					fc.setDialogTitle("Save R Plot");
//					
//					int browserReturn = fc.showSaveDialog(null);
//					String savePath = "";
//							
//					if (browserReturn == JFileChooser.APPROVE_OPTION){
//						savePath = fc.getSelectedFile().getAbsolutePath();
//						if(!(fc.getSelectedFile().getName().toLowerCase().endsWith(".csv"))){
//							savePath = savePath+".csv";
//						}
//						File destinationFile = new File(midasFile, savePath);
//						
//						FileUtils.copyFile(midasFile, destinationFile);
//					}
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				// TODO must be tested
//			}
//		});
//		
//		if(midasFile==null)
//			saveMIDASMenu.setEnabled(false);
//
//		return saveMIDASMenu;
//	}
//	
//
//	private JMenuItem initFileCloseMenu(){
//		JMenuItem closeMenu = new JMenuItem("Close");
//
//		closeMenu.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				PlotsDialog.this.dispose();
//			}
//		});
//
//		return closeMenu;
//	}
//
//}
