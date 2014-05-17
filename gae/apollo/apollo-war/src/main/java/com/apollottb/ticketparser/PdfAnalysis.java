package com.apollottb.ticketparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfAnalysis
{
	public PdfContent pdfContent;
	public ArrayList<TripDraft> trips;
	
	public ArrayList<AirlineWord> airlineWords;
	public ArrayList<PdfWord> dateWords;
	public ArrayList<PdfWord> timeWords;
	public ArrayList<AirportWord> airportWords;
	
	public static final int INTERPAGE_MARGIN = 30;
	
	private String regexDate;
	private String regexTime;
	private static ArrayList<Float> adjustedHeights;
	private ArrayList<Airport> airports;
	private ArrayList<Airline> airlines;
	
	
	public PdfAnalysis(InputStream airportsFileStream, InputStream airlinesFileStream)
	{
		trips = new ArrayList<TripDraft>();
		try
		{
			AirportsFileParser airportsFileParser = new AirportsFileParser(airportsFileStream);
			AirlinesFileParser airlinesFileParser = new AirlinesFileParser(airlinesFileStream);
			airports = airportsFileParser.airports;
			airlines = airlinesFileParser.airlines;
		}
		catch (IOException e)
		{
			System.out.println("Failed to open airports.dat.");
			e.printStackTrace();
		}
		
		
		regexDate = "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December)( |-|/)(3[01]|[12][0-9]|0?[1-9])";
		regexDate = "(" + regexDate + ")|(" + "(1[012]|0?[1-9])(-|/)(3[01]|[12][0-9]|0?[1-9])" + ") ";
		regexTime = "(0?[0-9]|1[0-2]):[0-5][0-9] ?(PM|AM|A|P)";
		regexTime = "(" + regexTime + ")|(" + "(0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9]" + ")";
	}
	
	
	public void analyze(PdfContent content)
	{
		pdfContent = content;
		adjustedHeights = new ArrayList<Float>();
		adjustedHeights.add(0.0f);
		for (int i = 0; i < content.nPages; ++i)
		{
			float h = adjustedHeights.get(adjustedHeights.size() - 1);
			adjustedHeights.add(h + content.pageHeights.get(i) - INTERPAGE_MARGIN);
		}
		
		airlineWords = search(content, airlines);
		dateWords = findMatches(content, regexDate);
		timeWords = findMatches(content, regexTime);
		airportWords = search(content, airports);
		trips = createTrips(airlineWords, dateWords, timeWords, airportWords);
	}
	
	
	private static <T extends PdfWord, S extends Searchable<T> > ArrayList<T> search(PdfContent content, ArrayList<S> searchables)
	{
		ArrayList<T> words = new ArrayList<T>();
		for (S searchable : searchables)
		{
			Pattern pattern = searchable.getPattern();
			if (pattern == null) continue;
			
			Matcher m = pattern.matcher(content.text);
			
			while (m.find())
			{
				T word = null;
				int idxFirst = m.start();
				int idxLast = m.end() - 1;
				
				// Skip matches with any letters or digit in the front or back (note: commas are OK).
				if (idxFirst > 0)
				{
					if (Character.isLetterOrDigit(content.text.charAt(idxFirst - 1)) ||
						Character.isLetterOrDigit(content.text.charAt(idxLast + 1)))
					{
						continue;
					}
				}
				
				word = searchable.createPdfWord();
				word.setText(m.group());
				word.setBoundingBox(content.charsX1.get(idxFirst), content.charsY1.get(idxFirst),
									content.charsX2.get(idxLast), content.charsY2.get(idxLast));
				word.page = content.getPageNumber(idxFirst) - 1;
				word.y2 = adjustedHeights.get(content.getPageNumber(idxFirst) - 1) + word.y;
				
				words.add(word);
			}
		}
		return words;
	}
	
	
	private ArrayList<PdfWord> findMatches(PdfContent content, String regex)
	{
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content.text);
		ArrayList<PdfWord> matchedWords = new ArrayList<PdfWord>();
		
		while (m.find())
		{
			PdfWord word = null;
			int idx = m.start();
			
			word = new PdfWord();
			word.text = m.group();
			word.x = content.charsX.get(idx);
			word.y = content.charsY.get(idx);
			word.y2 = adjustedHeights.get(content.getPageNumber(idx) - 1) + word.y;
		    
		    matchedWords.add(word);
		}
		
		return matchedWords;
	}
	
	
	private static ArrayList<TripDraft> createTrips(List<AirlineWord> airlineWords, List<PdfWord> dateWords, List<PdfWord> timeWords, List<AirportWord> airportWords)
	{
		ArrayList<TripDraft> trips = new ArrayList<TripDraft>();
		
		for (AirlineWord airlineWord : airlineWords)
		{
			TripDraft trip = new TripDraft();
			List<PdfWord> correspondingTimes = findCorrespondingTimes(airlineWord, timeWords);
			
			trip.airlineWord = airlineWord;
			if (correspondingTimes != null)
			{
				trip.departureTime = correspondingTimes.get(0);
				trip.arrivalTime = correspondingTimes.get(1);
			}
			else
			{
				trip.departureTime = null;
				trip.arrivalTime = null;
			}
			trip.departureDate = findCorrespondingDate(trip.departureTime, dateWords);
			trip.arrivalDate = findCorrespondingDate(trip.arrivalTime, dateWords);
			trip.origin = findCorrespondingAirport(trip.departureTime, airportWords);
			trip.destination = findCorrespondingAirport(trip.arrivalTime, airportWords);
			
			trips.add(trip);
		}
		
		
		return trips;
	}
	
	
	// Return PdfWord with minimum y difference (greater than 0)
	private static List<PdfWord> findCorrespondingTimes(PdfWord flight, List<PdfWord> timeWords)
	{
		if (flight == null || timeWords == null) return null;
		float minDeltaY1 = Float.MAX_VALUE;
		float minDeltaY2 = Float.MAX_VALUE;
		PdfWord departure = null;
		PdfWord arrival = null;
		
		// Find two words (times) with minimum y difference with flight
		for (PdfWord time : timeWords)
		{
			float deltaY = time.y2 - flight.y2;
			if (deltaY < -25.0f) continue;
			
			// Time closer to flight is departure (since departure is listed first).
			if (minDeltaY1 > deltaY)
			{
				minDeltaY1 = deltaY;
				departure = time;
				continue;
			}
			
			if (minDeltaY2 > deltaY)
			{
				minDeltaY2 = deltaY;
				arrival = time;
			}
		}
		
		return Arrays.asList(departure, arrival);
	}
	
	
	private static PdfWord findCorrespondingDate(PdfWord time, List<PdfWord> dateWords)
	{
		if (time == null || dateWords == null) return null;
		float minDelta = Float.MAX_VALUE;
		float minDeltaY = Float.MAX_VALUE;
		PdfWord correspondingDate1 = null;
		PdfWord correspondingDate2 = null;
		
		for (PdfWord date : dateWords)
		{
			float deltaX = time.x - date.x;
			float deltaY = time.y2 - date.y2;
			float delta = deltaX * deltaX + deltaY * deltaY;
			
			if (minDelta > delta)
			{
				minDelta = delta;
				correspondingDate1 = date;
			}
			
			if (minDeltaY > deltaY && deltaY > 0)
			{
				minDeltaY = deltaY;
				correspondingDate2 = date;
			}
		}
		
		if (minDelta < 70*70) return correspondingDate1;
		return correspondingDate2;
	}
	
	
	private static AirportWord findCorrespondingAirport(PdfWord time, List<AirportWord> airports)
	{
		if (time == null) return null;
		float minDelta = Float.MAX_VALUE;
		AirportWord correspondingAirport = null;
		
		for (AirportWord airport : airports)
		{
			float deltaX = time.x - airport.x;
			float deltaY = time.y2 - airport.y2;
			float delta = deltaX * deltaX + deltaY * deltaY;
			
			if (minDelta > delta)
			{
				minDelta = delta;
				correspondingAirport = airport;
			}
		}
		
		return correspondingAirport;
	}
	
	
	// Convert TripDraft to Trip.
	public List<Trip> getTrips()
	{
		ArrayList<Trip> finalTrips = new ArrayList<Trip>();
		
		for (TripDraft trip : trips)
		{
			Trip t = trip.getTrip();
			finalTrips.add(t);
		}
		
		return finalTrips;
	}
	
	
	public void dispDebugInfo()
	{
		for (TripDraft trip : trips)
		{
			System.out.println(trip);
		}
	}
}
