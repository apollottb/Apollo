package com.apollottb.ticketparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

public class TicketParser
{
	public static List<Trip> getTrips(InputStream pdfStream, InputStream airportsFileStream) throws IOException
	{
		PdfAnalysis analysis = analyze(pdfStream, airportsFileStream);
		return analysis.getTrips();
	}
	
	
	public static PdfAnalysis getTripsDebug(InputStream pdfStream, InputStream airportsFileStream) throws IOException
	{
		PdfAnalysis analysis = analyze(pdfStream, airportsFileStream);
		return analysis;
	}
	
	
	public static PdfAnalysis analyze(InputStream pdfStream, InputStream airportsFileStream) throws IOException
	{
		PdfReader reader = new PdfReader(pdfStream);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		PdfContent content = new PdfContent();
		int nPages = reader.getNumberOfPages();
		PdfAnalysis analysis = new PdfAnalysis(airportsFileStream);
		
		for (int i = 1; i <= nPages; ++i)
		{
			PdfPageContent pageContent = new PdfPageContent();
			parser.processContent(i, pageContent);
			pageContent.organize();
			content.add(pageContent, reader.getPageSize(i).getWidth(), reader.getPageSize(i).getHeight());
		}
		
		analysis.analyze(content);
		return analysis;
	}
}
