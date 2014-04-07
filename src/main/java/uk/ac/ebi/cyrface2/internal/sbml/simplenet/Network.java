package uk.ac.ebi.cyrface2.internal.sbml.simplenet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Network
{

	public abstract void createEdge(Node src, Node dest, String predicate);

	public abstract NodeImpl createOrGetNode(String name);

	public abstract Collection<? extends Node> getNodes();

	public abstract Collection<? extends Edge> getEdges();

	public abstract Set<String> getNodeAttributeSet();

	/**
	 * Returns map with node id as keys and attribute for that node for given key as values.
	 */
	public abstract Map<String, String> getNodeAttribute(String key);
	
	/**
	 * return all edges that run between src and dest
	 */
	public List<Edge> getEdges(Node src, Node dest);

	/**
	 * Returns true if node with given id already exists
	 */
	public abstract boolean exists(String id);


}