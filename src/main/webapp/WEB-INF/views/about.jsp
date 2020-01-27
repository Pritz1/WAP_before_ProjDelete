<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />" rel="stylesheet"> <!-- used bootstrap-3.3.5.css instead of bootstrap.min2.css, commented .hidden in bootstrap-3.3.5.css which was causing issue in navbar  -->
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<!-- header : end-->
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">

<title>About</title>
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

</style>


</head>
<body style="background-color:#e5e5e5;">
<div style="float: top;"><jsp:include page="header.jsp" /></div>
<br><br>
<center>
<form:form id="aboutform" method="get">
<br/><br/>
<center>
<div class="container">
<center>
<div class="col-lg-2 col-md-3"></div>
<div class="col-lg-6 col-md-10">
<!-- Card -->
<center>
<div class="card" >
<img class="card-img-top img-responsive"  src="<c:url value="/resources/images/palsons.png"/>" alt="Card image cap">
<div class="card-body">
<h4 class="card-title">Palsons Derma Pvt. Ltd.</h4>
<p class="card-text">This product is developed to calculate incentives depending on the performance of the field force.</p>
</div>
</div></center></div></center>
<!-- Card -->
</div>
</center>
</form:form></center>
</body>



<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
</html>