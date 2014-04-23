package uk.ac.ebi.cyrface.internal.sbml.simplenet;

/**
 * Exception type for exceptions that occur during conversion of a network from one format to another.
 */
public class NetworkException extends Exception
{
	public NetworkException()
	{
		super();
	}
	
	public NetworkException(Throwable cause)
	{
		super (cause);
	}

	public NetworkException(String message)
	{
		super (message);
	}

	public NetworkException(String message, Throwable cause)
	{
		super (message, cause);
	}

}
