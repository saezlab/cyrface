package uk.ac.ebi.cyrface2.internal.sbml.simplenet;

public class EdgeImpl implements Edge
{
	private String predicate;

	private Node src;
	private Node dest;

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Edge#getDest()
	 */
	@Override
	public Node getDest()
	{
		return dest;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Edge#getSrc()
	 */
	@Override
	public Node getSrc()
	{
		return src;
	}

	public EdgeImpl (Node src, Node dest, String predicate)
	{
		this.src = src;
		this.dest = dest;
		this.predicate = predicate;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Edge#toString()
	 */
	@Override
	public String toString()
	{
		return  "Edge: " + src.getId() + " " + predicate + " " + dest.getId();
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Edge#getPredictate()
	 */
	@Override
	public String getPredictate()
	{
		return predicate;
	}
}