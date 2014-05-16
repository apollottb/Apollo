package com.apollottb.ticketparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AirlineWord extends PdfWord
{
	Airline airline;
	int flightNumber;
	
	public AirlineWord(Airline a)
	{
		super();
		airline = a;
	}
	
	
	@Override
	public void setText(String s)
	{
		super.setText(s);
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(s);
		
		// Get last number.
		while (m.find())
		{
			flightNumber = Integer.parseInt(m.group());
		}
	}
	
	
	@Override
	public String toString()
	{
		return "AirlineWord: \"" + text + "\" " + airline.name + " " + flightNumber;
	}
}
