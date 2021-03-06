<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.apollottb.ticketparser.Trip" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>

	<!-- meta -->
	<meta charset="utf-8">
	<meta name="keywords" content="">
	<meta name="description" content="">
	<link rel="icon" type="image/png" href="pics/favicon.png">

	<!-- title+description -->
	<title>Your Itinerary | Nektar</title>

	<!-- css -->
	<link rel="stylesheet" href="/css/reset.css" media="all">
	<link rel="stylesheet" href="/css/style.css" media="all">

	<!-- fonts -->
	<link href='//fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>

	<!-- js+jquery -->
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
		
	<!-- ga -->
	<script>
		  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

		  ga('create', 'UA-50927827-1', 'apollo-13.appspot.com');
		  ga('require', 'displayfeatures');
		  ga('send', 'pageview');

	</script>

</head>

<body>
<!--
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
-->
<header>
	<div id = "heading">
		<div id="logo">
			<a href="/"><img src="./pics/paperplane-logo.png"></a>
		</div>	
		<h1 id="title"><a href="/">Nektar</a> <span>> trip.depDate to trip.arrDate</span></h1>
		<ul id="uppermenu">
			<li><a href="" value="Print Div" onclick="PrintElem("#itinerary-container")"><img class="menuicon" src="pics/print-icon.png" title="print itinerary"/></a></li>
			<li><a href="mailto:"><img class="menuicon" src="pics/mail-icon.png" title="email itinerary"/></a></li>
			<li><a href=""><img class="menuicon" src="pics/new-icon.png" title="add new trip"/></a></li>
			<li><form id="search-wrapper" method="get" action="http://google.com/search" target="blank"><input id="searchform" type="text" results="0" placeholder="Search"/></form></li>
		</ul>
	</div>
</header>

<body>

<div id = "navigation">
	<ul id ="navlist">
		<li><a href="/">Home</a></li>
		<li><a href="">Add Eticket PDFs</a></li>
		<li><a href="">Add Email Confirmations</a></li>
		<li><a href="">FAQ</a></li>
		<li><a href="">About</a></li>
	</ul>
</div>

<div id = "container">
<!--
	<div class="decl"><h3>Your Itinerary to:</h3>
	<h2>${trips[0].origin} <img class="arrow" src="pics/arrow1.png"/> Kuala Lumpur, Malaysia</h2></div>
-->
	<div class="itinerary-container">
		<c:forEach var="trip" items="${trips}">	
			<div class="wrap">
				<div class="datebar"><h2 class="date"><span>${trip.departureDate}</span></h2></div>
				<div class="logistics">
					<dl class="loglist">
						<dt>${trip.departureTime}</dt>
						<dd><a href="http://maps.google.com/?q=${trip.origin}" target="blank">${trip.origin}</a></br><span>Terminal X</span></dd>
						<dt> </dt>
						<dd><span><a href="http://www.google.com/search?q=${trip.airline}" target="blank"><img class="carrierlogo" src="http://upload.wikimedia.org/wikipedia/en/thumb/e/e0/United_Airlines_Logo.svg/470px-United_Airlines_Logo.svg.png"/></br></a>${trip.airline}</span></dd>
						<dt>${trip.arrivalTime}</dt>
						<dd><a href="http://maps.google.com/?q=${trip.destination}" target="blank">${trip.destination}</a></br><span>(A) Terminal Y <img class="transfer" src="pics/arrow3.png"/> (D) Terminal Z</span></dd>			
					</dl>
				</div>
			</div>
		</c:forEach>	
	</div>
</div>

<script type="text/javascript">

    function PrintElem(elem)
    {
        Popup($(elem).html());
    }

    function Popup(data) 
    {
        var mywindow = window.open('', '#itinerary-container', 'height=auto,width=650');
        mywindow.document.write('<html><head><title>Your Itinerary</title>');
        mywindow.document.write('</head><body >');
        mywindow.document.write(data);
        mywindow.document.write('</body></html>');

        mywindow.print();
        mywindow.close();

        return true;
    }

</script>
</body>
<footer>

</footer>
</html>