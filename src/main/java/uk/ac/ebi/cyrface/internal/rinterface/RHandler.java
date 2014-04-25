package uk.ac.ebi.cyrface.internal.rinterface;

import org.cytoscape.service.util.CyServiceRegistrar;

import uk.ac.ebi.cyrface.internal.utils.BioconductorPackagesEnum;

public abstract class RHandler {

	protected CyServiceRegistrar cyServiceRegistrar;
	protected String rInterfaceLibraryName;
	
	public RHandler (CyServiceRegistrar cyServiceRegistrar, String rInterfaceLibraryName) {
		this.cyServiceRegistrar = cyServiceRegistrar;
		this.rInterfaceLibraryName = rInterfaceLibraryName;
	}
	
	
	/**
	 * Abstract method implemented in the R-Java libraries handlers. 
	 * 
	 * Executes the given command without expecting an output.
	 * 
	 * @param command
	 * @throws Exception
	 */
	public abstract void execute(String command) throws Exception;
	
	/**
	 * Checks if it is possible to reach R. Returns True if possible, False otherwise.
	 * 
	 * @return
	 */
	public abstract boolean isConnectionEstablished();

	/**
	 * Takes a Bioconductor package name and installs it. 
	 * 
	 * @param packageName
	 * @throws Exception
	 */
	public abstract void installBioconductorPackage(BioconductorPackagesEnum packageName) throws Exception;
	
	/**
	 * Takes a Bioconductor package name and installs it.
	 * 
	 * @param packageName
	 * @throws Exception
	 */
	public abstract void installBioconductorPackage(String packageName) throws Exception;

	/**
	 * 
	 * Takes an R package name and verifies if the package is installed, returns True if it is, otherwise returns False. 
	 * 
	 * @param packageName
	 * @return
	 * @throws Exception
	 */
	public abstract boolean checkInstalledPackge(BioconductorPackagesEnum packageName) throws Exception;
	
	/**
	 * Takes an R package name and verifies if the package is installed, returns True if it is, otherwise returns False.
	 * 
	 * @param packageName
	 * @return
	 * @throws Exception
	 */
	public abstract boolean checkInstalledPackge(String packageName) throws Exception;
	
	/**
	 * Loads the specified package.
	 * 
	 * @param packageName
	 * @throws Exception
	 */
	public abstract void libraryPackage(BioconductorPackagesEnum packageName) throws Exception;
	
	/**
	 * 
	 * Loads the specified package.
	 * 
	 * @param packageName
	 * @throws Exception
	 */
	public abstract void libraryPackage(String packageName) throws Exception;
	
	/**
	 * Returns the name of the R interface library. 
	 * 
	 * @return
	 */
	public String getrInterfaceLibraryName() {
		return rInterfaceLibraryName;
	}

}
