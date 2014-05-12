package com.apollottb.ticketparser;

public class Trip
{
	private String airline;
	private String departureTime;
	private String departureDate;
	private String arrivalTime;
	private String arrivalDate;
	private String origin;
	private String destination;
	
	
	public Trip()
	{
		// Empty.
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
	
	
	public String getAirline() {return airline;}
	public String getDepartureTime() {return departureTime;}
	public String getDepartureDate() {return departureDate;}
	public String getArrivalTime() {return arrivalTime;}
	public String getArrivalDate() {return arrivalDate;}
	public String getOrigin() {return origin;}
	public String getDestination() {return destination;}
	
	public void setAirline(String s) {airline = s;}
	public void setDepartureTime(String s) {departureTime = s;}
	public void setDepartureDate(String s) {departureDate = s;}
	public void setArrivalTime(String s) {arrivalTime = s;}
	public void setArrivalDate(String s) {arrivalDate = s;}
	public void setOrigin(String s) {origin = s;}
	public void setDestination(String s) {destination = s;}
}
