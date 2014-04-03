package uk.ac.ebi.cyrface2.internal.rinterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class StartServeTask extends AbstractTask {

	private TaskMonitor taskMonitor;
	
	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		this.taskMonitor = taskMonitor;
		
		taskMonitor.setTitle("Starting Rserve...");
		
		String osName = System.getProperty("os.name").toLowerCase();
		taskMonitor.setStatusMessage(osName);
		taskMonitor.setProgress(0.1);
		
		if(osName.indexOf("win") >= 0){			
			launchRserveWindows();

		} else if (osName.indexOf("mac") >= 0) {
			launchRserveUnix();

		} else if (osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0 || osName.indexOf("aix") > 0 ) {
			launchRserveUnix();
			
		} else {
			throw new Exception ("Operating system not supported: " + osName);
		}
		
		taskMonitor.setStatusMessage("Cyrface ready!");
		taskMonitor.setProgress(1.0);
	}

	private void launchRserveWindows () throws Exception {		
		String rPath = "";

		// check the windows registry for an "R"-entry that proofs that and where R is installed
		Process proc = Runtime.getRuntime().exec("reg query HKLM\\Software\\R-core\\R");

		// save all information from the R-registry-entry
		BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		// wait till the process has terminated
		proc.waitFor();

		StringBuffer output = new StringBuffer();

		String line = "";		

		while ((line = reader.readLine()) != null) {
			output.append(line + "\n");

			// get the R-install-path reading the information of the registry-entry
			if(line.contains("InstallPath") && line.contains("REG_SZ")) {

				// erase unimportant information from the line and correct the path
				rPath = line.substring(line.indexOf(":\\")-1).trim().replace("\\", "/");

				// extend the path of the R-folder to the location of the R.exe-file
				rPath = "\""+rPath+"/bin/R.exe\"";
			}
		}

		// Check if R is installed
		if (!rExeExist_win(rPath)) throw new Exception("R is not installed");

		// Check is Rserve is installed
		if (!rserveIsInstalled_win(rPath)) installRserve_win(rPath);

		// Check if Rserve is running
		if (!isRserveProcessRunning_win()) startRserve_win(rPath);

	}

	private void launchRserveUnix () throws Exception {

		// Check if R is installed
		if (!rIsInstalled_unix()) throw new Exception("R is not installed");
		
		taskMonitor.setStatusMessage("R installed, good!");
		taskMonitor.setProgress(0.3);

		// Check is Rserve is installed
		if (!rserveIsInstalled_unix()) installRserve_unix();
		
		taskMonitor.setStatusMessage("RServe installed, good!");
		taskMonitor.setProgress(0.6);

		// Check is Rserve is running
		startRserve_unix();
		
		taskMonitor.setStatusMessage("RServe installed and running, perfect!");
		taskMonitor.setProgress(0.9);
	}

	/**
	 * This method checks, if the Rserve.exe-file exists in the given path.
	 */
	private boolean rExeExist_win (String rPath) throws Exception {
		File rserve = new File(rPath.replace("\"", ""));		
		return rserve.exists();	
	}

	/**
	 * This method checks in windows if Rserve is installed returning true, otherwise returns false.
	 */
	private boolean rserveIsInstalled_win(String rPath) throws Exception {

		String rargs = "--no-save --slave";
		Process proc = Runtime.getRuntime().exec(rPath + " -e \"find.package('Rserve')\" " + rargs);

		if (proc.waitFor() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method installes Rserve for windows.
	 */
	private void installRserve_win(String rPath) throws Exception {		
		String rargs = "--no-save --slave";
		Process proc = Runtime.getRuntime().exec(rPath+" -e \"install.packages('Rserve'," + "repos='http://cran.us.r-project.org')\" " + rargs);
		proc.waitFor();			
	}

	/**
	 * This methods starts the Rserve.exe in windows.
	 */
	private void startRserve_win (String installPath) throws Exception {
		String rsrvargs = "--no-save --slave";
		String rargs = "--no-save --slave";

		Process proc = Runtime.getRuntime().exec(installPath+" -e \"library(Rserve)" + ";Rserve(FALSE,args='" + rsrvargs + "')\" " + rargs);
		proc.waitFor();
	}


	/**
	 * This method checks, if the Rserve.exe is running. (for windows)
	 */
	private boolean isRserveProcessRunning_win() throws Exception {
		String line;
		String pidInfo ="";

		// Get all tasks from the task-list
		Process p =Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");

		BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));

		while ((line = input.readLine()) != null) {
			pidInfo+=line; 
		}

		input.close();

		/* test, if the Rserve.exe-task is running already (it is part of the pidInfo-String).
		 * Create it if it is not running */
		if (pidInfo.contains("Rserve.exe")){
			return true;
		} else {
			return false;
		}
	}


	/**
	 * This method checks in an unix-based system, if R is installed.
	 */
	private boolean rIsInstalled_unix() throws Exception {
		Process proc = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", "R --version"});

		if (proc.waitFor() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method checks in an unix-based system if Rserve is installed.
	 */
	private boolean rserveIsInstalled_unix() throws Exception {
		String rargs = "--no-save --slave";
		String cmd = getRInstallPath_unix();

		Process proc = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "echo 'find.package(\"Rserve\")'|" + cmd + " " + rargs});

		if (proc.waitFor() == 0)
			return true;
		else
			return false;
	}


	/**
	 * This method installs Rserve in an unix-based system.
	 */
	private boolean installRserve_unix() throws Exception {
		String rargs = "--no-save --slave";
		String cmd = getRInstallPath_unix();
		Process proc = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "echo 'install.packages(\"Rserve\", repos=\"http://cran.us.r-project.org\")'|" + cmd + " " + rargs});

		if (proc.waitFor() == 0)
			return true;
		else
			return false;
	}

	/**
	 * This method provides default installation-paths for R on an unix-based sytem.
	 * These paths will be used for checking, if R is installed on the computer.
	 */
	private String getRInstallPath_unix(){
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

	/**
	 * This method starts Rserve in an unix-based system.
	 */
	private void startRserve_unix() throws Exception {
		String rsrvargs = "--no-save --slave";
		String rargs = "--no-save --slave";
		String cmd = getRInstallPath_unix();

		if (cmd == null) throw new Exception("R install path not found");

		Process proc = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "echo 'library(Rserve); Rserve(FALSE, args=\"" + rsrvargs + "\"" + ")'|" + cmd + " " + rargs});

		proc.waitFor();
	}

}
