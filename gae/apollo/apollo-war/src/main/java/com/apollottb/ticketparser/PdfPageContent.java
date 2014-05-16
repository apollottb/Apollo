package com.apollottb.ticketparser;

import java.util.ArrayList;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class PdfPageContent implements RenderListener
{
	public ArrayList<Token> tokens;
	public String text;
	public ArrayList<Float> charsX;
	public ArrayList<Float> charsY;
	public ArrayList<Float> charsX1; // Top left.
	public ArrayList<Float> charsY1;
	public ArrayList<Float> charsX2; // Bottom right.
	public ArrayList<Float> charsY2;
	public float marginLeftX;
	public float marginRightX;
	public float marginTopY;
	public float marginBottomY;
	
	private Rectangle2D.Float textRectangle = null;
	
	
	public PdfPageContent()
	{
		tokens = new ArrayList<Token>();
		text = "";
		charsX = new ArrayList<Float>();
		charsY = new ArrayList<Float>();
		charsX1 = new ArrayList<Float>();
		charsY1 = new ArrayList<Float>();
		charsX2 = new ArrayList<Float>();
		charsY2 = new ArrayList<Float>();
	}
	
	
	@Override
	public void beginTextBlock()
	{
		// Empty.
	}

	
	@Override
	public void endTextBlock()
	{
		// Empty.
	}

	
	@Override
	public void renderImage(ImageRenderInfo renderInfo)
	{
		// Empty.
	}

	
	@Override
	public void renderText(TextRenderInfo renderInfo)
	{
		Token token = new Token(renderInfo);
		tokens.add(token);
		
		if (textRectangle == null)
		{
			textRectangle = renderInfo.getDescentLine().getBoundingRectange();
		}
		else
		{
			textRectangle.add(renderInfo.getDescentLine().getBoundingRectange());
		}
		textRectangle.add(renderInfo.getAscentLine().getBoundingRectange());
	}
	
	
	public void organize()
	{
		StringBuffer sb = new StringBuffer();
		Token lastToken = null;

		for (Token token : tokens)
		{
			// Only insert a blank space if the trailing character of the previous string wasn't a space,
			// and the leading character of the current string isn't a space.
			if (lastToken != null &&
				token.isAtWordBoundaryWith(lastToken) &&
				!startsWithSpace(token.text) &&
				!endsWithSpace(lastToken.text))
			{
				sb.append(' ');
				
				int lastIdx = lastToken.text.length() - 1;
				charsX.add(lastToken.charsX.get(lastIdx));
				charsY.add(lastToken.charsY.get(lastIdx));
				charsX1.add(lastToken.topRightX);
				charsY1.add(lastToken.topRightY);
				charsX2.add(token.bottomLeftX);
				charsY2.add(token.bottomLeftY);
			}
			
			sb.append(token.text);
			charsX.addAll(token.charsX);
			charsY.addAll(token.charsY);
			charsX1.addAll(token.charsTopLeftX);
			charsY1.addAll(token.charsTopLeftY);
			charsX2.addAll(token.charsBottomRightX);
			charsY2.addAll(token.charsBottomRightY);
			
			lastToken = token;
		}
		
		text = sb.toString();
		
		marginLeftX = textRectangle.x;
		marginRightX = marginLeftX + textRectangle.width;
		marginBottomY = textRectangle.y;
		marginTopY = marginBottomY + textRectangle.height;
	}
	
	
	private boolean startsWithSpace(String str)
	{
		if (str.length() == 0) return false;
		return str.charAt(0) == ' ';
	}
	
	
	private boolean endsWithSpace(String str)
	{
		if (str.length() == 0) return false;
		return str.charAt(str.length()-1) == ' ';
	}
}
