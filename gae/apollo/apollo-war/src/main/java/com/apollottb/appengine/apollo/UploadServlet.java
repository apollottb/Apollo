package com.apollottb.appengine.apollo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Exception;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.apollottb.ticketparser.TicketParser;
import com.apollottb.ticketparser.Trip;

public class UploadServlet extends HttpServlet
{
	private static final Logger log =
		Logger.getLogger(UploadServlet.class.getName());
	
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws IOException
	{
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, this is UploadServlet GET request. \n\n");
		Properties p = System.getProperties();
		p.list(resp.getWriter());
	}
	
	
	// Source code from:
	// http://stackoverflow.com/questions/2422468
	// https://developers.google.com/appengine/kb/java?csw=1#fileforms
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		ServletContext context = getServletContext();
		InputStream airportsStream = context.getResourceAsStream("/WEB-INF/airports.dat");
		InputStream airlinesStream = context.getResourceAsStream("/WEB-INF/airlines.dat");
		String fileRawText = "";
		List<Trip> trips = null;
		
		try
		{
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext())
			{
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();
				String fileName = item.getName();
				
				log.warning("Got an uploaded file: " + fileName);
				
				trips = TicketParser.getTrips(stream,
											  airportsStream,
											  airlinesStream);
			}
		}
		catch (Exception e)
		{
			throw new ServletException("Cannot parse multipart request.", e);
		}
		
		RequestDispatcher dispatcher = 
								context.getRequestDispatcher("/summary.jsp");
		req.setAttribute("trips", trips);
		dispatcher.forward(req, resp);
	}
}
