<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.apollottb.ticketparser.Trip" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>

<p>Hello World</p>

<c:forEach var="trip" items="${trips}">
	<h3>${trip.airline}</h3>
	<ul>
		<li>${trip.departureTime}</li>
		<li>${trip.departureDate}</li>
		<li>${trip.arrivalTime}</li>
		<li>${trip.arrivalDate}</li>
		<li>${trip.origin}</li>
		<li>${trip.destination}</li>
	</ul>
</c:forEach>

</body>
</html>