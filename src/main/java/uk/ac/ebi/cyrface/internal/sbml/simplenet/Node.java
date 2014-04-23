package uk.ac.ebi.cyrface.internal.sbml.simplenet;

import java.util.List;
import java.util.Set;

public interface Node
{

	public abstract List<EdgeImpl> getOutgoing();

	public abstract List<EdgeImpl> getIncoming();

	public abstract void setPos(double x, double y);

	public abstract Iterable<? extends Node> getOutgoingNodes();

	public abstract Iterable<? extends Node> getIncomingNodes();

	public abstract String getId();

	public abstract double getX();

	public abstract double getY();

	public abstract void setAttribute(String key, String value);

	public abstract String getAttribute(String key);

	public abstract Set<String> getAttributeSet();

	public abstract boolean hasAttribute(String key);

}