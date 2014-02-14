package uk.ac.ebi.cyrface3.internal.sbml.simplenet;

public interface Edge
{

	public abstract Node getDest();

	public abstract Node getSrc();

	public abstract String toString();

	public abstract String getPredictate();

}