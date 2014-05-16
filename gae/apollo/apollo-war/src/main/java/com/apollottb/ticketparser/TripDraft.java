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
	
	
	public String getAirline()
	{
		if (airlineWord == null)
		{
			return UNKOWN;
		}
		
		String s = "";
		s += airlineWord.airline.name;
		s += " (" + airlineWord.airline.iata + " " + airlineWord.flightNumber + ")";
		return s;
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
