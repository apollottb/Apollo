package com.apollottb.ticketparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;

import au.com.bytecode.opencsv.CSVReader;

public class AirportsFileParser
{
	public ArrayList<Airport> airports;
	
	
	public AirportsFileParser(InputStream stream) throws IOException
	{
		airports = new ArrayList<Airport>();
		
		CSVReader reader = new CSVReader(new InputStreamReader(stream, Charset.forName("ISO-8859-1")));
		parseFile(reader);
	}
	
	
	private void parseFile(CSVReader reader) throws IOException
	{
		LinkedList<Airport> ports = new LinkedList<Airport>();
		String[] nextLine;
		
		while ((nextLine = reader.readNext()) != null)
		{
			Airport airport = new Airport();
			
			airport.setName(nextLine[1]);
			airport.setCity(nextLine[2]);
			airport.setCountry(nextLine[3]);
			airport.setIata(nextLine[4]);
			airport.setIcao(nextLine[5]);
			airport.setLatitude(nextLine[6]);
			airport.setLongitude(nextLine[7]);
			airport.setAltitude(nextLine[8]);
			airport.setTimeZone(nextLine[9]);
			
			ports.add(airport);
		}
		reader.close();
		
		airports.addAll(ports);
	}
}
