package uk.ac.ebi.cyrface3.internal.examples.simpleExample;

import uk.ac.ebi.cyrface3.internal.rinterface.RserveHandler;

public class SimpleExample {

	public SimpleExample(){
			startSimpleExample();
	}
	
	private void startSimpleExample(){
		/**
		 * The simple-example-method tries the functionality of the connection to R
		 */
		try{
			RserveHandler handler = new RserveHandler();
			
			double[] values = {1.23,4.23,6.23,2.0,32.0};
			
			handler.storeVariable(values, "list");
			
			double mean = handler.executeReceiveDouble("mean(list)");
			double max = handler.executeReceiveDouble("max(list)");
			double min = handler.executeReceiveDouble("min(list)");
			
			System.out.println("Mean: " + mean);
			System.out.println("Max: " + max);
			System.out.println("Min: " + min);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
