package com.apollottb.ticketparser;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

public class Token
{
	public String text;
	public float x1;
	public float y1;
	public float x2;
	public float y2;
	public ArrayList<Float> charsX;
	public ArrayList<Float> charsY;
	public float charSpaceWidth;
	
	private float distParallelStart;
    private float distParallelEnd;
	
	
	public Token(TextRenderInfo renderInfo)
	{
		LineSegment line = renderInfo.getBaseline();
		Vector start = line.getStartPoint();
		Vector end = line.getEndPoint();
		
		text = renderInfo.getText();
		x1 = start.get(Vector.I1);
		y1 = start.get(Vector.I2);
		x2 = end.get(Vector.I1);
		y2 = end.get(Vector.I2);
		charSpaceWidth = renderInfo.getSingleSpaceWidth();
		
		List<TextRenderInfo> charInfos = renderInfo.getCharacterRenderInfos();
		int nChars = charInfos.size();
		charsX = new ArrayList<Float>(nChars);
		charsY = new ArrayList<Float>(nChars);
		for (TextRenderInfo charInfo : charInfos)
		{
			Vector startPoint = charInfo.getBaseline().getStartPoint();
			charsX.add(startPoint.get(Vector.I1));
			charsY.add(startPoint.get(Vector.I2));
		}
		
		Vector orientationVector = end.subtract(start).normalize();
		distParallelStart = orientationVector.dot(start);
		distParallelEnd = orientationVector.dot(end);
	}
	
	
	@Override
	public String toString()
	{
		String s = "";
		s += "[" + text + "]";
		s += "(" + x1 + ", " + y1 + ")";
		s += "--";
		s += "(" + x2 + ", " + y2 + ")";
		return s;
	}
	
	
	public boolean isAtWordBoundaryWith(Token previousToken)
	{
		float dist = distanceFromEndOf(previousToken);
		
		if (dist < -charSpaceWidth || dist > charSpaceWidth/2.0f)
		{
			return true;
		}
		
		return false;
	}
	
	
	public float distanceFromEndOf(Token other)
	{
		return distParallelStart - other.distParallelEnd;
	}
}
