<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<!-- meta -->
	<meta charset="utf-8">
	<meta name="keywords" content="nektar, trip, organizer, itinerary">
	<meta name="description" content="nektar can help you better organize your itinerary with just one step">
	<link rel="icon" type="image/png" href="pics/favicon.png">

	<!-- title+description -->
	<title>About Nektar</title>

	<!-- css -->
	<link rel="stylesheet" href="css/reset.css" media="all">
	<link rel="stylesheet" href="css/style.css" media="all">
	<link rel="stylesheet" href="css/about.css" media="all">

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

<header>
	<div id = "heading">
		<div id="logo"><a href="/" title="Nektar home"><img src="./pics/paperplane-logo.png"></a></div>	
		<h1 id="title"><a href="/" title="Nektar home">Nektar<a/></h1>
		<ul id="uppermenu">
			<li><a href=""><img class="menuicon" src="pics/new-icon.png" title="add new trip"/></a></li>
			<li><form id="search-wrapper" method="get" action="http://google.com/search" target="blank"><input id="searchform" type="text" results="0" placeholder="Search"/></form></li>
		</ul>
	</div>
</header>

<body>
<div id = "navigation">
	<ul id ="navlist">
		<li><a href="/">Home</a></li>
		<li><a href="">FAQ</a></li>
		<li><a href="">About</a></li>
	</ul>
</div>
<div id="container">
	<div class="about">
		<p>
		Nektar&trade; can help you better organize your piles of Etickets, hotel reservations and maps into a single and sleek itinerary made just for you. Simply drop your Eticket PDFs or forward E-mail confirmations to us, you will get the concise itinerary instantaneously. Click on the links to refer flight status, airport locations and more. 
		</p>
		<img src="pics/demo2.gif" title="How to use Nektar"/>
	</div>
	
	<div class="about">
		<h2>Our Team</h2>
		<p>Team Nektar is ...</p>
		<div class="member">
			<div class="profile" id="bumpei">
				<p class="text"><a href="">Bumpei Sugano</br>Developer</a></p>
			</div>
			<div class="profile" id="takashi">
				<p class="text"><a href="">Takashi Furuya</br>Developer</a></p>
			</div>
			<div class="profile" id="taro">
				<p class="text"><a href="">Taro Inoue</br>Designer</a></p>
			</div>
		</div>
	</div>	
	<div class="about">
		<h2>Creative Philosophy</h2>
		<p>We believe in ...</p>		
	</div>
	<div class="about">
		<h2>Our Products</h2>
		<p>We have ...</p>		
	</div>
</div>

<script>

</script>
</body>
<footer>

</footer>

</html>