package com.apollottb.ticketparser;

// Stores links between PdfWords and also ways to display them on the web page.
public class TripDraft
{
	public AirlineWord airlineWord;
	public PdfWord departureTime;
	public PdfWord departureDate;
	public PdfWord arrivalTime;
	public PdfWord arrivalDate;
	public AirportWord origin;
	public AirportWord destination;
	
	private static final String UNKOWN = "Unkown";
	
	
	public TripDraft()
	{
		// Empty.
	}
	
	
	public Trip getTrip()
	{
		Trip t = new Trip();
		
		t.setAirline(getAirline());
		t.setDepartureTime(getDepartureTime());
		t.setDepartureDate(getDepartureDate());
		t.setArrivalTime(getArrivalTime());
		t.setArrivalDate(getArrivalDate());
		t.setOrigin(getOrigin());
		t.setDestination(getDestination());
		
		return t;
	}
	
	
	private String getAirline()
	{
		if (airlineWord == null)
		{
			return UNKOWN;
		}
		
		String name = getText(airlineWord.airline.name);
		String iata = getText(airlineWord.airline.iata);
		String flightNumber = getText(airlineWord.flightNumber + "");
		
		return name + " (" + iata + " " + flightNumber + ")";
	}
	
	
	private static <T> String getText(T s)
	{
		return (s == null) ? UNKOWN : s.toString();
	}
	
	
	public String getDepartureTime() {return getText(departureTime);}
	public String getDepartureDate() {return getText(departureDate);}
	public String getArrivalTime() {return getText(arrivalTime);}
	public String getArrivalDate() {return getText(arrivalDate);}
	public String getOrigin() {return getAirportText(origin);}
	public String getDestination() {return getAirportText(destination);}	
	
	
	private static String getAirportText(AirportWord word)
	{
		if (word == null)
		{
			return UNKOWN;
		}
		
		Airport airport = word.airport;
		return airport.name + " (" + airport.iata + ")";
	}
	
	
	private static String getText(PdfWord word)
	{
		if (word == null)
		{
			return UNKOWN;
		}
		return word.text;
	}
	
	
	@Override
	public String toString()
	{
		String output = "";
		output += getAirline() + ": ";
		output += getOrigin() + " (" + getDepartureDate() + " " + getDepartureTime() + ")";
		output += " --> ";
		output += getDestination() + " (" + getArrivalDate() + " " + getArrivalTime() + ")";
		return output;
	}
}
