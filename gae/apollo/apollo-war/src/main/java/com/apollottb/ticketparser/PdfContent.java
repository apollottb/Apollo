package com.apollottb.ticketparser;

import java.util.ArrayList;

public class PdfContent
{
	public int nPages = 0;
	public ArrayList<Float> pageWidths;
	public ArrayList<Float> pageHeights;
	public ArrayList<Float> marginLeftX;
	public ArrayList<Float> marginRightX;
	public ArrayList<Float> marginTopY;
	public ArrayList<Float> marginBottomY;
	public String text;
	public ArrayList<Float> charsX;		// Left (baseline start).
	public ArrayList<Float> charsY;
	public ArrayList<Float> charsX1;	// Top left (ascentline start).
	public ArrayList<Float> charsY1;
	public ArrayList<Float> charsX2;	// Bottom right (descentline end).
	public ArrayList<Float> charsY2;
	
	private ArrayList<Integer> pageLastCharIndices;
	
	
	public PdfContent()
	{
		nPages = 0;
		pageWidths = new ArrayList<Float>();
		pageHeights = new ArrayList<Float>();
		marginLeftX = new ArrayList<Float>();
		marginRightX = new ArrayList<Float>();
		marginTopY = new ArrayList<Float>();
		marginBottomY = new ArrayList<Float>();
		text = "";
		charsX = new ArrayList<Float>();
		charsY = new ArrayList<Float>();
		charsX1 = new ArrayList<Float>();
		charsY1 = new ArrayList<Float>();
		charsX2 = new ArrayList<Float>();
		charsY2 = new ArrayList<Float>();
		
		pageLastCharIndices = new ArrayList<Integer>();
		pageLastCharIndices.add(0);
	}
	
	
	public void add(PdfPageContent pageContent, float width, float height)
	{
		++ nPages;
		//texts.add(pageContent.text);
		pageWidths.add(width);
		pageHeights.add(height);
		
		int nChars = pageContent.text.length();
		ArrayList<Float> xs = pageContent.charsX;
		ArrayList<Float> ys = new ArrayList<Float>(nChars);
		ArrayList<Float> xs1 = pageContent.charsX1;
		ArrayList<Float> ys1 = new ArrayList<Float>(nChars);
		ArrayList<Float> xs2 = pageContent.charsX2;
		ArrayList<Float> ys2 = new ArrayList<Float>(nChars);
		
		for (Float y : pageContent.charsY) {ys.add(height - y);}
		for (Float y : pageContent.charsY1) {ys1.add(height - y);}
		for (Float y : pageContent.charsY2) {ys2.add(height - y);}
		
		text += pageContent.text + " ";
		charsX.addAll(xs);
		charsX.add(xs.get(nChars - 1));
		charsY.addAll(ys);
		charsY.add(ys.get(nChars - 1));
		charsX1.addAll(xs1);
		charsX1.add(xs1.get(nChars - 1));
		charsY1.addAll(ys1);
		charsY1.add(ys1.get(nChars - 1));
		charsX2.addAll(xs2);
		charsX2.add(xs2.get(nChars - 1));
		charsY2.addAll(ys2);
		charsY2.add(ys2.get(nChars - 1));
		
		int lastIdx = pageLastCharIndices.get(pageLastCharIndices.size() - 1);
		pageLastCharIndices.add(lastIdx + nChars + 1);
		
		
		marginLeftX.add(pageContent.marginLeftX);
		marginRightX.add(pageContent.marginRightX);
		marginTopY.add(height - pageContent.marginTopY);
		marginBottomY.add(height - pageContent.marginBottomY);
	}
	
	
	// Starts at 1.
	public int getPageNumber(int index)
	{
		int page = 0;
		
		for (Integer lastIdx : pageLastCharIndices)
		{
			if (index >= lastIdx)
			{
				++ page;
			}
		}
		
		return page;
	}
}
