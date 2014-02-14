//package uk.ac.ebi.cyrface3.internal.utils;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//import javax.swing.JOptionPane;
//
//import cytoscape.Cytoscape; 
//import cytoscape.task.Task;
//import cytoscape.task.ui.JTaskConfig;
//import cytoscape.task.util.TaskManager;
//
//public class MessageHandler {
//
//	public static void executeTask(Task task, boolean autoDispose){
//		executeTask(task, autoDispose, true, false);
//	}
//	
//	public static void executeTask(Task task, boolean autoDispose, boolean displayCancelButton, boolean displayTimeRemaining){
//		JTaskConfig jTaskConfig = new JTaskConfig();
//		jTaskConfig.setOwner(Cytoscape.getDesktop());
//		
//		jTaskConfig.displayCloseButton(true);
//		jTaskConfig.displayCancelButton(displayCancelButton);
//
//		jTaskConfig.displayStatus(true);
//		jTaskConfig.setAutoDispose(autoDispose);
//		
//		jTaskConfig.displayTimeRemaining(displayTimeRemaining);
//		
//		TaskManager.executeTask(task, jTaskConfig);
//	}
//	
//	public static void errorMessage(String title, Exception exception){
//		JOptionPane.showMessageDialog(Cytoscape.getDesktop(), getStackTrace(exception), title, JOptionPane.ERROR_MESSAGE);
//	}
//	
//	public static void errorMessage(String title, String message){
//		JOptionPane.showMessageDialog(Cytoscape.getDesktop(), message, title, JOptionPane.ERROR_MESSAGE);
//	}
//	
//	public static void warningMessage(String title, String message){
//		JOptionPane.showMessageDialog(Cytoscape.getDesktop(), message, title, JOptionPane.WARNING_MESSAGE);
//	}
//	
//	public static void informationMessage(String title, String message){
//		JOptionPane.showMessageDialog(Cytoscape.getDesktop(), message, title, JOptionPane.INFORMATION_MESSAGE);
//	}
//
//	
//	public static String getStackTrace(Exception e){
//		StringWriter sw = new StringWriter();
//		e.printStackTrace(new PrintWriter(sw));
//		return sw.toString();
//	}
//	
//}
