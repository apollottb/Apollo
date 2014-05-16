package com.apollottb.ticketparser;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;

public class Token
{
	public String text;
	public float bottomLeftX;
	public float bottomLeftY;
	public float topRightX;
	public float topRightY;
	public ArrayList<Float> charsX;
	public ArrayList<Float> charsY;
	public ArrayList<Float> charsTopLeftX;
	public ArrayList<Float> charsTopLeftY;
	public ArrayList<Float> charsBottomRightX;
	public ArrayList<Float> charsBottomRightY;
	public float charSpaceWidth;
	
	private float distParallelStart;
    private float distParallelEnd;
	
	
	public Token(TextRenderInfo renderInfo)
	{
		text = renderInfo.getText();
		charSpaceWidth = renderInfo.getSingleSpaceWidth();
		
		bottomLeftX = renderInfo.getDescentLine().getStartPoint().get(Vector.I1);
		bottomLeftY = renderInfo.getDescentLine().getStartPoint().get(Vector.I2);
		topRightX = renderInfo.getAscentLine().getEndPoint().get(Vector.I1);
		topRightY = renderInfo.getAscentLine().getEndPoint().get(Vector.I2);
		
		List<TextRenderInfo> charInfos = renderInfo.getCharacterRenderInfos();
		int nChars = charInfos.size();
		charsX = new ArrayList<Float>(nChars);
		charsY = new ArrayList<Float>(nChars);
		charsTopLeftX = new ArrayList<Float>(nChars);
		charsTopLeftY = new ArrayList<Float>(nChars);
		charsBottomRightX = new ArrayList<Float>(nChars);
		charsBottomRightY = new ArrayList<Float>(nChars);
		for (TextRenderInfo charInfo : charInfos)
		{
			Vector baselineStart = charInfo.getBaseline().getStartPoint();
			Vector ascentlineStart = charInfo.getAscentLine().getStartPoint();
			Vector descentlineEnd = charInfo.getDescentLine().getEndPoint();
			charsX.add(baselineStart.get(Vector.I1));
			charsY.add(baselineStart.get(Vector.I2));
			charsTopLeftX.add(ascentlineStart.get(Vector.I1));
			charsTopLeftY.add(ascentlineStart.get(Vector.I2));
			charsBottomRightX.add(descentlineEnd.get(Vector.I1));
			charsBottomRightY.add(descentlineEnd.get(Vector.I2));
		}
		
		Vector start = renderInfo.getBaseline().getStartPoint();
		Vector end = renderInfo.getBaseline().getEndPoint();
		Vector orientationVector = end.subtract(start).normalize();
		distParallelStart = orientationVector.dot(start);
		distParallelEnd = orientationVector.dot(end);
	}
	
	
	@Override
	public String toString()
	{
		int lastIdx = text.length() - 1;
		String s = "";
		s += "[" + text + "]";
		s += "(" + charsX.get(0) + ", " + charsY.get(0) + ")";
		s += "--";
		s += "(" + charsX.get(lastIdx) + ", " + charsY.get(lastIdx) + ")";
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
