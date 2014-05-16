package com.apollottb.ticketparser;

public class PdfWord
{
	public String text;
	public float x;		// Center. (TODO not center yet so make it so)
	public float y;
	public float y2;	// Margin and page-compensated.
	
	public float topLeftX;
	public float topLeftY;
	public float width;
	public float height;
	public int page;
	
	public static enum Type
	{
		LOCATION, DATE, TIME, NONE
	}
	
	public Type type = Type.NONE;
	public Airport airport = null;
	
	
	public PdfWord()
	{
		// Empty.
	}
	
	public void setBoundingBox(float x1, float y1, float x2, float y2)
	{
		topLeftX = x1;
		topLeftY = y1;
		width = x2 - x1;
		height = y2 - y1;
		x = topLeftX + width / 2;
		y = topLeftY + height / 2;
	}
	
	public void set(Airport a)
	{
		type = Type.LOCATION;
		airport = a;
	}
}
