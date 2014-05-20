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
	<title>Nektar: Trip Organizer</title>

	<!-- css -->
	<link rel="stylesheet" href="css/reset.css" media="all">
	<link rel="stylesheet" href="css/style.css" media="all">

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
		<li><a href="about.jsp">About</a></li>
	</ul>
</div>
<div id="container">
	<div id="description">
		<img src="pics/demo2.gif" title="How to use Nektar"/>
	</div>
	
	<div align="center" id="mainmenu">
		<div class="fileUpload menubutton">
			<form id="uploadform" action="upload" method="post" enctype="multipart/form-data">
				<span id="span1">Upload Eticket PDFs</span>
				<p><input class="upload" type="file" name="file" value="Select PDFs" multiple id="myfile1" onChange="javascript:showSrc(this);" size="20"><a href="#" id="myframe1"></a></input><p>
				<input id="sendbutton" type="submit" value="Send"/>	
			</form>		
		</div>
		<div class="menubutton">
			<form action="">Forward Email Confirmations</form>
		</div>
	</div>

	<!--
	<div align="center" class="mainmenu">
	<form action="/upload" class="menubutton upload dropzone" method="post" enctype="multipart/form-data"></form>
	<form href="" class="menubutton forward">Forward email confirmations</form>
	<p>To Better Organize Your Itinerary</p>
	</div>
	<!--
	<dl class="dlist">
		<div class="listnav">
			<dt><a href="">date</a></dt>
			<dd><a href="">destination</a></dd>
		</div>
		<div class="listcontent">
			<dt><a href="itinerary.html">MM.DD.YYYY</a></dt>
			<dd><a href="itinerary.html">NYCNY</a></dd>
		</div>
		<div class="listcontent">
			<dt><a href="">MM.DD.YYYY</a></dt>
			<dd><a href="">NYCNY</a></dd>
		</div>
		<div class="listcontent">
			<dt><a href="">MM.DD.YYYY</a></dt>
			<dd><a href="">NYCNY</a></dd>
		</div>
		<div class="listcontent">
			<dt><a href="">MM.DD.YYYY</a></dt>
			<dd><a href="">NYCNY</a></dd>
		</div>
		<div class="listcontent">
			<dt><a href="">MM.DD.YYYY</a></dt>
			<dd><a href="">NYCNY</a></dd>
		</div>
	</dl>
	-->
</div>

<script>

	function addFileFild(){
	  ufile = ufile+1;
	  myfile = myfile+1;
	  myframe = myframe+1;
	  $("#addFileFild").before('<p><input type="file" name="ufile'+ufile+'" id="myfile' +myfile+'" onChange="javascript:showSrc();" size="20"><a href="#" id="myframe' +myframe+'"></a></p>');
	return 0;
	}
	
	var spanChange = $("<input />");
	function showSrc(obj) {
	  //obj.nextSibling.href = obj.value;
	  // this is for Chrome and IE8+ excluding C:/fakepath/...
	  var filename = obj.value.replace(/^.*\\/, '')
	  //Write filename to page.
	  spanChange.val(filename).change();
	};

	$(function() {
		spanChange.change(function(e) {
			var $val = $(this).val(),
				$newVal = $('input[type=file]').val().replace("C:\\fakepath\\", "");
			$("#span1").text($newVal);
		});
	});

</script>
</body>
<footer>

</footer>

</html>