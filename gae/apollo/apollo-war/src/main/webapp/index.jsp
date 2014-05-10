<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>

<p>Hello World!</p>
<form action="upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" multiple />
    <input type="submit" />
</form>

</body>
</html>