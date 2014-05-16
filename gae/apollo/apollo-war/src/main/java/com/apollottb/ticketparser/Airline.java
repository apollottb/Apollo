package com.apollottb.ticketparser;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


public class Airline implements Searchable<AirlineWord>
{
	public String name = null;
	public String alias = null;
	public String iata = null;			// 2-letter IATA code.
	public String icao = null;			// 3-letter ICAO code.
	public String callsign = null;
	public String country = null;
	
	
	public Airline()
	{
		// Empty.
	}
	
	
	public void setName(String s)
	{
		name = getValue(s);
	}
	
	
	public void setAlias(String s)
	{
		if (!s.equals("N"))
		{
			alias = getValue(s);
		}
	}
	
	
	public void setIata(String s)
	{
		if (s.length() == 2 && StringUtils.isAlpha(s))
		{
			iata = getValue(s);
		}
	}
	
	
	public void setIcao(String s)
	{
		if (s.length() == 3 && StringUtils.isAlpha(s))
		{
			icao = getValue(s);
		}
	}
	
	
	public void setCallsign(String s) {callsign = getValue(s);}
	public void setCountry(String s) {country = getValue(s);}
	
	
	private String getValue(String s)
	{
		if (s.equals(""))
		{
			return null;
		}
		return s;
	}
	
	
	@Override
	public Pattern getPattern()
	{
		String regex = "";
		
		regex += "(" + name + ")";
		
		if (alias != null)
		{
			regex += "|(" + alias + ")";
		}
		if (iata != null)
		{
			regex += "|(" + iata + ")";
		}
		if (icao != null)
		{
			regex += "|(" + icao + ")";
		}
		if (callsign != null)
		{
			regex += "|(" + callsign + ")";
		}
		
		regex = "(" + regex + ")" + "( Flight:?)? ?[0-9]{1,4}便?";
		
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}
	
	
	@Override
	public String toString()
	{
		String s = "";
		s += "Airline Name: " + name + ", ";
		s += "Alias: " + alias + ", ";
		s += "IATA: " + iata + ", ";
		s += "ICAO: " + icao + ", ";
		s += "Callsign: " + callsign + ", ";
		s += "Country: " + country;
		return s;
	}


	@Override
	public AirlineWord createPdfWord()
	{
		return new AirlineWord(this);
	}
}
