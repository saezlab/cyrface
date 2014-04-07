package uk.ac.ebi.cyrface2.internal.sbml.simplenet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * Helper class for reading and writing {@link Network}s in SIF format.
 */
public class SifFormat
{	
	public Network readFromFile(File f) throws IOException
	{
		Network result = new NetworkImpl();
		
		int lineNo = 0;
		BufferedReader reader = new BufferedReader(new FileReader (f));
		String line;
		while ((line = reader.readLine()) != null)
		{
			lineNo ++;
			String [] fields = line.split("\\s+");
			if (fields.length < 3) throw new IOException("Not valid SIF at line " + lineNo);
			
			NodeImpl src = result.createOrGetNode(fields[0]);
			NodeImpl dest = result.createOrGetNode(fields[2]);
			result.createEdge (src, dest, fields[1]);
		}
		reader.close();
		
		return result;
	}
	
	public void writeToFile (Network net, File out) throws IOException
	{
		FileWriter writer = new FileWriter (out);
		write (net, writer);
		writer.close();
	}
	
	public void write (Network net, Writer writer) throws IOException
	{
		for (Edge e : net.getEdges())
		{
			writer.write(e.getSrc().getId());
			writer.write ('\t');
			writer.write(e.getPredictate());
			writer.write ('\t');
			writer.write(e.getDest().getId());
			writer.write ('\n');
		}
	}
	
}
