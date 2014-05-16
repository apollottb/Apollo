package com.apollottb.ticketparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;

import au.com.bytecode.opencsv.CSVReader;

public class AirlinesFileParser
{
	public ArrayList<Airline> airlines;
	
	
	public AirlinesFileParser(InputStream stream) throws IOException
	{
		airlines = new ArrayList<Airline>();
		
		CSVReader reader = new CSVReader(new InputStreamReader(stream, Charset.forName("ISO-8859-1")));
		parseFile(reader);
	}
	
	
	private void parseFile(CSVReader reader) throws IOException
	{
		LinkedList<Airline> lines = new LinkedList<Airline>();
		String[] nextLine;
		
		while ((nextLine = reader.readNext()) != null)
		{
			// Skip inactive airlines.
			if (nextLine[7].equals("N"))
			{
				continue;
			}
			
			Airline airline = new Airline();
			
			airline.setName(nextLine[1]);
			airline.setAlias(nextLine[2]);
			airline.setIata(nextLine[3]);
			airline.setIcao(nextLine[4]);
			airline.setCallsign(nextLine[5]);
			airline.setCountry(nextLine[6]);
			
			lines.add(airline);
		}
		reader.close();
		
		airlines.addAll(lines);
	}
}
