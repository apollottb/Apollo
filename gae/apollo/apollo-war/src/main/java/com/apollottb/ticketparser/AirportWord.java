package com.apollottb.ticketparser;

public class AirportWord extends PdfWord
{
	Airport airport;
	
	public AirportWord(Airport a)
	{
		super();
		airport = a;
	}
	
	
	@Override
	public void setText(String s)
	{
		super.setText(s);
	}
	
	
	@Override
	public String toString()
	{
		return "AirportWord: \"" + text + "\" " + airport.name;
	}
}
