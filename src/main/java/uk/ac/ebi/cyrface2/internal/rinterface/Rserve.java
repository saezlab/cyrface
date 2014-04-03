package uk.ac.ebi.cyrface2.internal.rinterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Rserve {
	private boolean print_RserveIsInstalled_outputStream = false;
	private boolean print_RserveInstall_outputStream = false;
	
	public Rserve(){
		/**
		 * Constructor for lunching Rserve
		 */
		// Check which OS is running in the background
		String osName = getOS();
		// launch Rserve and check afterwards, if it worked
		if(launchRserve(osName)==true){
			System.out.println("Workflow can be started - Rserve is running");			
		}else{
			System.out.println("Rserve is not running - find out the reason!!");			
		}
	}
	
	private boolean launchRserve(String osName){
		/**
		 * In this method all necessary steps for starting Rserve correctly are implemented
		 */
// ------------------------------------------------  Windows --------------------------------------------
	
		if(osName.toLowerCase().contains("win")){
			
			String rPath= "";
			
			try {
					// check the windows registry for an "R"-entry that proofs that and where R is installed
				Process proc = Runtime.getRuntime().exec("reg query HKLM\\Software\\R-core\\R");
					// save all information from the R-registry-entry
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
					// wait till the process has terminated
				proc.waitFor();
				
				StringBuffer output = new StringBuffer();
				
				String line = "";		
					
				while ((line = reader.readLine())!= null) {
					output.append(line + "\n");
						// get the R-install-path reading the information of the registry-entry
					if(line.contains("InstallPath") & line.contains("REG_SZ")){
							// erase unimportant information from the line and correct the path
						rPath = line.substring(line.indexOf(":\\")-1).trim().replace("\\", "/");
							// extend the path of the R-folder to the location of the R.exe-file
						rPath = "\""+rPath+"/bin/R.exe\"";
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
				// check, if the R.exe is located in the correct folder
			if(rExeExist_win(rPath)==true){
					// check, if Rserve is installed already
				if(rserveIsInstalled_win(rPath)==true){
						// check, if a Rserve-process is running already
					if(isRserveProcessRunning_win()==false){
							// if not - start the Rserve-process
						startRserve_win(rPath);
					}
					return true;
					
				}else{
						// Install Rserve, if Rserve is not installed yet
					installRserve_win(rPath);
					if(isRserveProcessRunning_win()==false){
							// Start the Rserve-process, if the Rserve-process in not running yet
						startRserve_win(rPath);
						return true;
					}
				}
			}else{
				System.out.println("R is not installed or another error occured");
			}
	// ------------------------------------------------  UNIX BASED --------------------------------------------
		}else{
				// check, if R is installed in one of the default installation-folders
			if(rIsInstalled_unix()==true){
					// check, if Rserve is installed
				if(rserveIsInstalled_unix()==true){
						// start Rserve
					startRserve_unix();
					return true;
				}else{
						// if Rserve is not installed yet - install it and run in afterwards
					System.out.println("Rserve is not installed yet!");
					installRserve_unix();
					startRserve_unix();
					return true;
				}
				
			}else{
				System.out.println("R is not installed yet! - Please install it manually");
			}
		}
		return false;
	}
// ------------------------------------ general methods --------------------------------------	
	private String getOS(){
		/**
		 * Return name of OS
		 */
		return System.getProperty("os.name");
	}
// --------------------------------- Windows-depending methods -------------------------------
	
	private boolean rExeExist_win(String rPath){
		/**
		 * This method checks, if the Rserve.exe-file exists in the given path.
		 */
		rPath = rPath.replace("\"", "");
		File rserve = new File(rPath);
		System.out.println("R.exe exists: "+rserve.exists());
		return rserve.exists();	
	}
	
	private boolean rserveIsInstalled_win(String rPath){
		/**
		 * This method checks in windows, if Rserve is installed and returns true, if it is installed -
		 * otherwise it returns false.
		 */
		try {
			
			String rargs = "--no-save --slave";
			Process proc = Runtime.getRuntime().exec(rPath+" -e \"find.package('Rserve')\" "+rargs);
			int exitValue = proc.waitFor();
			
			if(exitValue == 0){
				System.out.println("Rserve is installed: "+true);
				return true;
			}else{
				System.out.println("Rserve is installed: "+false);
				return false;
			}
			
		} catch (Exception e) {
			
		}
		return false;
	}
	
	private void installRserve_win(String rPath){
		/**
		 * This method installes Rserve for windows.
		 */
		try {
			System.out.println("Plugin is installing Rserve now...");
			String rargs = "--no-save --slave";
			Process proc = Runtime.getRuntime().exec(rPath+" -e \"install.packages('Rserve',"
					+ "repos='http://cran.us.r-project.org')\" "+rargs);
			proc.waitFor();
			
			if(print_RserveInstall_outputStream == true){
				StringBuffer output = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	 			String line = "";			
				while ((line = reader.readLine())!= null) {
					output.append(line + "\n");
				}
				System.out.println(output);
			}
		} catch (Exception e) {
			
		}
	}
	
	private void startRserve_win(String installPath){
		/**
		 * This methods starts the Rserve.exe in windows.
		 */
		try {
			String rsrvargs = "--no-save --slave";
			String rargs = "--no-save --slave";
			Process proc = Runtime.getRuntime().exec(installPath+" -e \"library(Rserve)"
					+ ";Rserve(FALSE,args='"+rsrvargs+"')\" "+rargs);
			proc.waitFor();
			System.out.println("Rserve.exe-process started");
		} catch (Exception e) {
//				JOptionPane.showMessageDialog(new Frame(), "Wrong path for r-root-directory selected!");
		}
	
	}
	
	private boolean isRserveProcessRunning_win(){
		/**
		 * This method checks, if the Rserve.exe is running. (for windows)
		 */
		try{
			String line;
			String pidInfo ="";

				// get all tasks from the task-list
			Process p =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

			BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));
			
				// save all task-names in the pidInfo
			while ((line = input.readLine()) != null) {
			    pidInfo+=line; 
			}
				// close the input-stream
			input.close();

				/* test, if the Rserve.exe-task is running already (it is part of the pidInfo-String).
				 * Create it, if it is not running */
			if(pidInfo.contains("Rserve.exe")==false){
				System.out.println("Rserve.exe-process is not running yet");
				return false;
			}else{
				System.out.println("Rserve.exe-process is already running");
				return true;
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
// ------------------------------------- UNIX-depending methods -------------------------------------

	private boolean rIsInstalled_unix(){
		/**
		 * This method checks in an unix-based system, if R is installed.
		 */
		try {
			Process proc = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "R --version"});
			int exitValue;
			exitValue = proc.waitFor();
			
			if(exitValue==0){
				System.out.println("R is installed");
				return true;
			}else{
				System.out.println("R is not installed - please install it manually");
				return false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		System.out.println("look at stack-trace");
		return false;
	}
	
	private boolean rserveIsInstalled_unix(){
		/**
		 * This method checks in an unix-based system, if Rserve is installed.
		 */
		try {
			String rargs = "--no-save --slave";
			String cmd = getRInstallPath_unix();
			
			Process proc = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", 
					"echo 'find.package(\"Rserve\")'|"+cmd+" "+rargs});

			int exitValue = proc.waitFor();
			
			if(print_RserveIsInstalled_outputStream == true){
				StringBuffer output = new StringBuffer();
	 			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	 			String line = "";			
				while ((line = reader.readLine())!= null) {
					output.append(line + "\n");
				}
				System.out.println(output);
			}
			
			if(exitValue==0){
				System.out.println("Rserve is installed");
				return true;
			}else{
				System.out.println("Rserve is not installed yet");
				return false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		System.out.println("look at stack-trace");
		return false;
	}
	
	private void installRserve_unix(){
		/**
		 * This method installes Rserve in an unix-based system.
		 */
		try {
			System.out.println("Please wait until Rserve is installed");
			
			String rargs = "--no-save --slave";
			String cmd = getRInstallPath_unix();
			Process proc = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", 
					"echo 'install.packages(\"Rserve\", repos=\"http://cran.us.r-project.org\")'|"+cmd+" "+rargs});
			
			proc.waitFor();
			
			if(print_RserveInstall_outputStream == true){
				StringBuffer output = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	 			String line = "";			
				while ((line = reader.readLine())!= null) {
					output.append(line + "\n");
				}
				System.out.println(output);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch (InterruptedException e2) {
			e2.printStackTrace();
		}
	}
	
	private String getRInstallPath_unix(){
		/**
		 * This method provides default installation-paths for R on an unix-based sytem.
		 * These paths will be used for checking, if R is installed on the computer.
		 */
		if((new File("/Library/Frameworks/R.framework/Resources/bin/R")).exists()){
			return"/Library/Frameworks/R.framework/Resources/bin/R";
			
		}else if((new File("/usr/local/lib/R/bin/R")).exists()){
			return "/usr/local/lib/R/bin/R";
			
		}else if((new File("/usr/lib/R/bin/R")).exists()){
			return "/usr/lib/R/bin/R";	
			
		}else if((new File("/usr/local/bin/R")).exists()){
			return "/usr/local/bin/R";
			
		}else if((new File("/sw/bin/R")).exists()){
			return "/sw/bin/R";
			
		}else if((new File("/usr/common/bin/R")).exists()){
			return "/usr/common/bin/R";
			
		}else if((new File("/opt/bin/R")).exists()){
			return "/opt/bin/R";
			
		}else if((new File("R")).exists()){
			return "R";
		}else{
			return null;
		}
	}
	
	private void startRserve_unix(){
		/**
		 * This method starts Rserve in an unix-based system.
		 */
		try {
			String rsrvargs = "--no-save --slave";
			String rargs = "--no-save --slave";
			String cmd = getRInstallPath_unix();
			
			if(cmd.equals(null)){
				System.out.println("R install path not found");
			}else{
				Process proc = Runtime.getRuntime().exec(new String[]{"/bin/sh", 
						"-c", "echo 'library(Rserve); Rserve(FALSE, args=\""+ 
								rsrvargs +"\""+")'|"+cmd+" "+rargs});
				
				proc.waitFor();
				System.out.println("Rserve started");
			}		
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e2){
			e2.printStackTrace();
		}
	}
}
