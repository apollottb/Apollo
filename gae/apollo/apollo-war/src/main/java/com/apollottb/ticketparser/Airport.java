package com.apollottb.ticketparser;

import java.util.TimeZone;

public class Airport
{
	public String name = null;
	public String city = null;
	public String country = null;
	public String iata = null;			// 3-letter IATA code.
	public String icao = null;			// 4-letter ICAO code.
	public float latitude;				// Degrees. Negative is South, positive is North.
	public float longitude;				// Degrees. Negative is West, positive is East.
	public float altitude;				// Feet.
	public TimeZone timeZone = null;	//
	
	
	public Airport()
	{
		// Empty.
	}
	
	
	public void setName(String s) {name = getValue(s);}
	public void setCity(String s) {city = getValue(s);}
	public void setCountry(String s) {country = getValue(s);}
	
	
	private String getValue(String s)
	{
		if (s.equals(""))
		{
			return null;
		}
		return s;
	}
	
	
	public void setIata(String s)
	{
		if (s.length() == 3)
		{
			iata = getValue(s);
		}
	}
	
	
	public void setIcao(String s)
	{
		if (s.length() == 4)
		{
			icao = getValue(s);
		}
	}
	
	
	public void setLatitude(String s)
	{
		latitude = Float.valueOf(s);
	}
	
	
	public void setLongitude(String s)
	{
		latitude = Float.valueOf(s);
	}
	
	
	public void setAltitude(String s)
	{
		latitude = Float.valueOf(s);
	}
	
	
	public void setTimeZone(String s)
	{
		// Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 5.5.
		float time = Float.valueOf(s);
		int h = Math.abs((int) time);
		int m = (int) ((Math.abs(time) - h) * 60.0f);
		
		String sign = (time > 0.0f) ? "+" : "-";
		String hours = ((h < 10) ? "0": "") + h;
		String minutes = ((m < 10) ? "0" : "") + m;
		timeZone = TimeZone.getTimeZone("GMT" + sign + hours + minutes);
	}
}
