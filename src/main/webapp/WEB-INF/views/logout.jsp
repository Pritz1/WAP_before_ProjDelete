<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/bootstrap.min2.css" />" rel="stylesheet">
<title>Logout</title>
<style>
.card-body {
    -ms-flex: 1 1 auto;
    flex: 1 1 auto;
    padding: 1.25rem;
}
.card {
    position: relative;
    display: -ms-flexbox;
    display: flex;
    -ms-flex-direction: column;
    flex-direction: column;
    min-width: 0;
    word-wrap: break-word;
    background-color: #fff;
    background-clip: border-box;
    border: 1px solid rgba(0,0,0,.125);
    border-radius: .25rem;
}

.card .card-title {
position: relative;
font-weight: 500;
}

.card-title {
margin-bottom: .75rem;
}
h4 {
line-height: 22px;
font-size: 18px;
}
h1, h2, h3, h4, h5, h6 {
color: #455a64;
font-family: "Montserrat", sans-serif;
font-weight: 400;
}
p {
margin-top: 0;
margin-bottom: 1rem;
}
* {
outline: none;
}
*, ::after, ::before {
box-sizing: border-box;
}
user agent stylesheet
p {
display: block;
margin-block-start: 1em;
margin-block-end: 1em;
margin-inline-start: 0px;
margin-inline-end: 0px;
}
a{
font-weight : bold;
}

</style>
<script>
function redirectLogin(){
	document.forms[0].action="loginform.html";
	document.forms[0].submit();
	//setTimeout(function(){ callLogin(); }, 3000);
} 
</script>

</head>
<body style="background-color:#e5e5e5;">
<br><br>
<center>
<form:form id="logoutform" method="get">
<center>
<div class="container">
<center>
<div class="col-lg-2 col-md-3"></div>
<div class="col-lg-6 col-md-10">
<!-- Card -->
<center>
<div class="card col-lg-12 col-md-8 col-sm-10 col-xs-4" >
<img class="card-img-top img-responsive" src="<c:url value="/resources/images/animated-check.gif"/>" alt="Card image cap">
<div class="card-body">
<h4 class="card-title">Logout</h4>
<p class="card-text">You Have Successfully Logged Out!!</p>
Please Click Here To <a onclick="redirectLogin();" class="Link">Login</a> Again.
</div>
</div></center></div></center>
<!-- Card -->
</div>
</center>
</form:form></center>
</body>
</html>