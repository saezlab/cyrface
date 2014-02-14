package uk.ac.ebi.cyrface3.internal.utils;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import uk.ac.ebi.cyrface3.internal.rinterface.RHandler;

public class ExceptionHandler {
	
	public static Exception handleRserveException(RHandler plugin, REXP rexp, Exception rserveException){
		
		// Check if Rserve connection was established before begin used
		if( !plugin.isConnectionEstablished() )
			return connectionNotEstablishException(rserveException);
		
		// Check type of Rserve exception was caught
		if( rserveException instanceof RserveException){
			return rServeExceptionCaught(rserveException);
		
		}else if( rserveException instanceof REngineException){
			return rServeREngineExceptionCaught(rserveException, rexp);
			
		}else if( rserveException instanceof REXPMismatchException){
			return rServeREXPMismatchExceptionCaught(rserveException);
		
		}else{
			return new Exception("General Java Exception", rserveException);
		}
	}
	
	private static Exception rServeExceptionCaught(Exception rserveException){
		Exception cyrfaceException = new Exception("Rserve exception", rserveException);
		
		return cyrfaceException;
	}
	
	private static Exception rServeREngineExceptionCaught(Exception rserveException, REXP rexp){
		Exception cyrfaceException = new Exception("Rserve exception: " + rserveException.getMessage());
		cyrfaceException.setStackTrace(rserveException.getStackTrace());
		
		return cyrfaceException;
	}
	
	private static Exception rServeREXPMismatchExceptionCaught(Exception rserveException){
		Exception cyrfaceException = new Exception("Rserve mismacth exception", rserveException);
		
		return cyrfaceException;
	}
	
	private static Exception connectionNotEstablishException(Exception rserveException){
		Exception cyrfaceException = new Exception("Connection not initialized. Check if Rserve was called in R (i.e. type Rserve(args=\"--vanilla\")), or if establishConnection() method was called.");
		cyrfaceException.setStackTrace(rserveException.getStackTrace());
		return cyrfaceException;
	}
}
