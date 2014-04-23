package uk.ac.ebi.cyrface.internal.sbml.simplenet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetworkImpl implements Network
{
	private Map<String, NodeImpl> nodes = new HashMap<String, NodeImpl>();
	private List<EdgeImpl> edges = new ArrayList<EdgeImpl>();

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Network#createEdge(uk.ac.ebi.sysbiomed.NodeImpl, uk.ac.ebi.sysbiomed.NodeImpl, java.lang.String)
	 */
	@Override
	public void createEdge (Node src, Node dest, String predicate)
	{
		EdgeImpl e = new EdgeImpl (src, dest, predicate);
		edges.add(e);
		dest.getIncoming().add(e);
		src.getOutgoing().add(e);		
	}
	
	/**
	 * return all edges that run between src and dest
	 */
	public List<Edge> getEdges(Node src, Node dest)
	{
		List<Edge> result = new ArrayList<Edge>();
		for (Edge e : edges)
		{
			if (e.getSrc().equals(src) &&
					e.getDest().equals(dest))
			{
				result.add(e);
			}
		}
		return result;
	}
	
	@Override
	public boolean exists(String id)
	{
		return nodes.containsKey(id);
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Network#createOrGetNode(java.lang.String)
	 */
	@Override
	public NodeImpl createOrGetNode (String name)
	{
		if (nodes.containsKey(name))
		{
			return nodes.get(name);
		}
		else
		{
			NodeImpl n = new NodeImpl(name);
			nodes.put (name, n);
			return n;
		}
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Network#getNodes()
	 */
	@Override
	public Collection<NodeImpl> getNodes()
	{
		return nodes.values();
	}

	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Network#getEdges()
	 */
	@Override
	public Collection<EdgeImpl> getEdges()
	{
		return edges;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Network#getNodeAttributeSet()
	 */
	@Override
	public Set<String> getNodeAttributeSet()
	{
		Set<String> result = new HashSet<String>();
		for (Node n : nodes.values())
		{
			result.addAll (n.getAttributeSet());
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ebi.sysbiomed.Network#getNodeAttribute(java.lang.String)
	 */
	@Override
	public Map<String, String> getNodeAttribute (String key)
	{
		Map<String, String> result = new HashMap<String, String>();
		for (Node n : nodes.values())
		{
			if (n.hasAttribute(key))
			{
				result.put (n.getId(), n.getAttribute(key));
			}
		}
		return result;
	}
	

}