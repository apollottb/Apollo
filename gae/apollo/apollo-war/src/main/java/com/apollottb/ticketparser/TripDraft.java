package com.apollottb.ticketparser;

public class TripDraft
{
	public PdfWord airline;
	public PdfWord departureTime;
	public PdfWord departureDate;
	public PdfWord arrivalTime;
	public PdfWord arrivalDate;
	public PdfWord origin;
	public PdfWord destination;
	
	
	public TripDraft()
	{
		// Empty.
	}
	
	
	public String getAirline() {return getText(airline);}
	public String getDepartureTime() {return getText(departureTime);}
	public String getDepartureDate() {return getText(departureDate);}
	public String getArrivalTime() {return getText(arrivalTime);}
	public String getArrivalDate() {return getText(arrivalDate);}
	public String getOrigin() {return getText(origin);}
	public String getDestination() {return getText(destination);}
	
	
	private String getText(PdfWord word)
	{
		if (word == null)
		{
			return "null";
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
