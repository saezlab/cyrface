package uk.ac.ebi.cyrface3.internal.sbml.simplenet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NodeImpl implements Node
{
	private String id;
	private double x = 0;
	private double y = 0;
	
	private List<EdgeImpl> outgoing = new ArrayList<EdgeImpl>();
	private List<EdgeImpl> incoming = new ArrayList<EdgeImpl>();
	
	private Map<String, String> attributes = new HashMap<String, String>();
	
	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#getOutgoing()
	 */
	@Override
	public List<EdgeImpl> getOutgoing() { return outgoing; }
	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#getIncoming()
	 */
	@Override
	public List<EdgeImpl> getIncoming() { return incoming; }

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#setPos(double, double)
	 */
	@Override
	public void setPos (double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#getOutgoingNodes()
	 */
	@Override
	public Iterable<? extends Node> getOutgoingNodes() 
	{
		List<Node> nodes = new ArrayList<Node>();
		for (Edge e : getOutgoing())
		{
			nodes.add(e.getDest());
		}
		return nodes;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#getIncomingNodes()
	 */
	@Override
	public Iterable<? extends Node> getIncomingNodes()
	{
		List<Node> nodes = new ArrayList<Node>();
		for (Edge e : getIncoming())
		{
			nodes.add(e.getSrc());
		}
		return nodes;
	}

	public NodeImpl (String id)
	{
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#getId()
	 */
	@Override
	public String getId()
	{
		return id;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#getX()
	 */
	@Override
	public double getX()
	{
		return x;
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#getY()
	 */
	@Override
	public double getY()
	{
		return y;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#setAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public void setAttribute (String key, String value)
	{
		attributes.put (key, value);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute (String key)
	{
		return attributes.get(key);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#getAttributeSet()
	 */
	@Override
	public Set<String> getAttributeSet()
	{
		return attributes.keySet();
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Node#hasAttribute(java.lang.String)
	 */
	@Override
	public boolean hasAttribute(String key)
	{
		return attributes.containsKey(key);
	}
}