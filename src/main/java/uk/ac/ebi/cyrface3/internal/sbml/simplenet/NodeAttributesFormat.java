package uk.ac.ebi.cyrface3.internal.sbml.simplenet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Helper for exporting the node attributes of a {@link Network}, using a format that can be read by cytoscape.
 */
public class NodeAttributesFormat
{
	public static void write (Network net, File out) throws IOException
	{
		PrintWriter writer = new PrintWriter (new FileWriter (out));
		boolean first = true;
		for (String key : net.getNodeAttributeSet())
		{
			if (first)
			{
				first = false;	
			}
			else
			{
				writer.println ();
			}
			
			writer.println(key);
			for (Map.Entry<String, String> entry : net.getNodeAttribute(key).entrySet())
			{
				writer.println (entry.getKey() + " = " + entry.getValue());
			}
		}
		
		writer.close();
	}

	public static void read(Network net, File ids) throws IOException, NetworkException
	{
		BufferedReader reader = new BufferedReader (new FileReader (ids));
		
		String line = null;
		String key = null;
		while ((line = reader.readLine()) != null)
		{
			line = line.trim();
			if (line.isEmpty()) continue;
			
			if (!line.contains("="))
			{
				key = line;
			}
			else
			{
				String fields[] = line.split("\\s*=\\s*");
				if (fields.length > 2)
					throw new NetworkException("Error in attribute file: two times '=' in a line");
				if (key == null)
					throw new NetworkException("No key defined in attribute file");
				net.getNodeAttribute(key).put(fields[0], fields[1]);
			}
		}
		
	}
}
