package com.apollottb.ticketparser;

import java.util.ArrayList;

public class PdfContent
{
	public int nPages = 0;
	public ArrayList<String> texts;
	public ArrayList<Float> pageWidths;
	public ArrayList<Float> pageHeights;
	public ArrayList< ArrayList<Float> > charsX;
	public ArrayList< ArrayList<Float> > charsY;
	public String allText;
	public ArrayList< Float > allCharsX;
	public ArrayList< Float > allCharsY;
	
	private ArrayList<Integer> pageLastCharIndices;
	
	
	public PdfContent()
	{
		nPages = 0;
		texts = new ArrayList<String>();
		pageWidths = new ArrayList<Float>();
		pageHeights = new ArrayList<Float>();
		charsX = new ArrayList< ArrayList<Float> >();
		charsY = new ArrayList< ArrayList<Float> >();
		allText = "";
		allCharsX = new ArrayList<Float>();
		allCharsY = new ArrayList<Float>();
		
		pageLastCharIndices = new ArrayList<Integer>();
		pageLastCharIndices.add(0);
	}
	
	
	public void add(PdfPageContent pageContent, float width, float height)
	{
		++ nPages;
		texts.add(pageContent.text);
		pageWidths.add(width);
		pageHeights.add(height);
		
		ArrayList<Float> xs = pageContent.charsX;
		ArrayList<Float> ys = new ArrayList<Float>();
		int nChars = xs.size();
		
		for (Float y : pageContent.charsY)
		{
			ys.add(height - y);
		}
		
		charsX.add(xs);
		charsY.add(ys);
		
		allText += pageContent.text + " ";
		allCharsX.addAll(xs);
		allCharsX.add(xs.get(nChars - 1));
		allCharsY.addAll(ys);
		allCharsY.add(ys.get(nChars - 1));
		
		int lastIdx = pageLastCharIndices.get(pageLastCharIndices.size() - 1);
		pageLastCharIndices.add(lastIdx + nChars + 1);
	}
	
	
	public int getPageNumber(int index)
	{
		int page = 0;
		
		for (Integer lastIdx : pageLastCharIndices)
		{
			if (index > lastIdx)
			{
				++ page;
			}
		}
		
		return page;
	}
}
