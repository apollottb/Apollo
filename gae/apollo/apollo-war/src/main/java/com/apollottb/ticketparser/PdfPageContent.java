package com.apollottb.ticketparser;

import java.util.ArrayList;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class PdfPageContent implements RenderListener
{
	public ArrayList<Token> tokens;
	public String text;
	public ArrayList<Float> charsX;
	public ArrayList<Float> charsY;
	public float pageWidth;
	public float pageHeight;
	
	public PdfPageContent()
	{
		tokens = new ArrayList<Token>();
		text = "";
		charsX = new ArrayList<Float>();
		charsY = new ArrayList<Float>();
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
				charsX.add(lastToken.x2);
				charsY.add(lastToken.y2);
			}
			
			sb.append(token.text);
			charsX.addAll(token.charsX);
			charsY.addAll(token.charsY);
			
			lastToken = token;
		}
		text = sb.toString();
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
