<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<!-- <link rel='shortcut icon' type='image/x-icon' href='../favicon.ico' /> -->
<title>Login Success</title>

<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">

<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<script src="<c:url value="/resources/js/metro.js" />"></script>
<!-- header : end-->

	
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
<script>
function loadmainMenu(){
	debugger;
	if(document.forms[0].login.value=="true"){
	document.forms[0].action = "mainmenu.html";
	document.forms[0].submit();
	return true;
	}
	return false;
}
</script>	
</head>

<body style="background-color:#e5e5e5;" onload="loadmainMenu();" >
	<%-- <div class="app-bar fixed-top darcula" data-role="appbar">
		<!-- <a class="app-bar-element branding">Dashboard</a>
        <span class="app-bar-divider"></span> -->
		<ul class="app-bar-menu">
			<li><a href="dashboard.html">Dashboard</a></li>
			<!-- <li><a href="">WAP Parameter</a></li> -->
			<li><a onclick="wapClick()">WAP Options</a> <!-- <li><a href=""></a></li>
                    <li class="divider"></li>
                    <li>
                        <a href="" class="dropdown-toggle">Reopen</a>
                        <ul class="d-menu" data-role="dropdown">
                            <li><a href="">Project 1</a></li>
                            <li><a href="">Project 2</a></li>
                            <li><a href="">Project 3</a></li>
                            <li class="divider"></li>
                            <li><a href="">Clear list</a></li>
                        </ul>
                    </li> --></li>
			<li><a onclick="superUser()">Super User</a></li>

			<li><a href="" class="dropdown-toggle">Report</a>
				<ul class="d-menu" data-role="dropdown">
					<li><a href="" class="dropdown-toggle">WAP Report</a>
						<ul class="d-menu" data-role="dropdown">
							<li><a href="">View Report</a></li>
							<li><a href="downloadReport">Download Report</a></li>
							<!-- <li class="divider"></li>
                            <li><a href="">Clear list</a></li> -->
						</ul></li>
					<!-- <li><a href="">QIP - Quarterly Incentive Project</a></li> -->
					<li class="divider"></li>
					<li><a href="">About</a></li>
				</ul></li>

		</ul>
		</ul>

		<div class="app-bar-element place-right">
			<span class="dropdown-toggle"><span class="mif-cog"></span>
				User Options</span>
			<div
				class="app-bar-drop-container padding10 place-right no-margin-top block-shadow fg-dark"
				data-role="dropdown" data-no-close="true" style="width: 220px">
				<!-- <h2 class="text-light">Quick settings</h2> -->
				<h5 class="text-light">&nbsp;User Name : ${user.employeeName}</h5>
				<h5 class="text-light">&nbsp;User ID : ${user.emp}</h5>
				<h5 class="text-light">&nbsp;Designation : ${desig}</h5>
				<h5 class="text-light">&nbsp;Division : ${user.divName}</h5>
				<h5 class="text-light">&nbsp;Head Quarter : ${user.hqName}</h5>
				<ul class="unstyled-list fg-dark">
					<li class="divider"></li>
					<li><a href="" class="fg-white3 fg-hover-yellow">Change	Password</a></li>
					<li class="divider"></li>
					<li><a href="http://localhost:8087/EISWAP/loginform.html" class="fg-white3 fg-hover-yellow">Logout</a></li>
				</ul>
			</div>
		</div>
	</div>

	<div id="wapMenu" class="flex-grid no-responsive-future"
		style="height: 100%; display: none; margin-top: 4%;">
		<div class="row" style="height: 100%">
			<div class="cell size-x200" id="cell-sidebar"
				style="background-color: #ffffff; height: 100%;">
				<ul class="sidebar2">
					<li><a href="parameters" id="param"> <span
							class="mif-list icon" style="font-size: 20px;"></span> <span
							class="title">Parameters for Appraisal</span>

					</a></li>

					<li><a href="slabs"> <span
							class="mif-paragraph-justify icon" style="font-size: 20px;"></span>
							<span class="title">Parameter Slabs</span>

					</a></li>
					<li><a href="wapcalc"> <span class="mif-calculator icon"
							style="font-size: 20px;"></span> <span class="title">Wap
								Calculation Example</span>

					</a></li>
					<li class="active"><a href="showincrement"> <span
							class="mif-plus icon" style="font-size: 20px;"></span> <span
							class="title">Know Your Increment</span>

					</a></li>
					<li><a href="upload"> <span class="mif-file-upload icon"
							style="font-size: 20px;"></span> <span class="title">Upload
								(Manager's Option) </span>
						<!--todo get manager designtions from db  -->

					</a></li>
					<li><a href="showprocessWap"> <span
							class="mif-rocket icon" style="font-size: 20px;"></span> <span
							class="title">Process WAP </span>

					</a></li>
				</ul>
			</div>

		</div>
	</div>

	<div id="superUsrMenu" class="flex-grid no-responsive-future"
		style="height: 100%; display: none; margin-top: 4%;">
		<div class="row" style="height: 100%">
			<div class="cell size-x200" id="cell-sidebar"
				style="background-color: #ffffff; height: 100%">
				<ul class="sidebar2">
					<li class="active"><a href="users" id="users"> <span
							class="mif-users icon" style="font-size: 20px;"></span> <span
							class="title">Users</span>
					</a></li>
					<!-- <li >
                        	<a  href="adminUsers" id="adminUsers">
                            	<span class="mif-users icon" style="font-size: 20px;"></span>
                            	<span class="title">Admin Users</span>
                        	</a>
                        </li> -->
					<li><a href="division"> <span class="mif-flow-tree icon"
							style="font-size: 20px;"></span> <span class="title">Division</span>
					</a></li>
					<!-- <li ><a href="lockHistory">
                            <span class="mif-history icon" style="font-size: 20px;"></span>
                            <span class="title">Lock History</span>
                          
                        </a></li>
                        <li ><a href="deleteWapData">
                            <span class="mif-bin icon" style="font-size: 20px;"></span>
                            <span class="title">Delete Data</span>
                        </a></li> -->
					<li><a href="lockmaster"> <span class="mif-lock icon"
							style="font-size: 20px;"></span> <span class="title">Lock
								Master</span>
					</a></li>

					<li><a href="cloneParam"> <span
							class="mif-flow-parallel icon" style="font-size: 20px;"></span> <span
							class="title">Clone Parameters</span>

					</a></li>

				</ul>
			</div>

		</div>
	</div> --%>
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<br/><br/>
<center>
<form:form id="mainmenu" method="get">
<br/><br/>
<input type="hidden" value="${login}" name="login">
<center>
<div class="container">
<center>
<div class="col-lg-2 col-md-3"></div>
<div class="col-lg-6 col-md-10">
<!-- Card -->
<br/><br/><br/><br/><br/><br/>
<center>
<div class="card" >
<img class="card-img-top img-responsive"  src="<c:url value="/resources/images/palsons.png"/>" alt="Card image cap">
<div class="card-body">
<h4 class="card-title">Palsons Derma Pvt. Ltd.</h4>
</div>
</div></center></div></center>
<!-- Card -->
</div>
</center>
</form:form></center>
<%-- <div style="float: left;"><jsp:include page="sideBar.jsp"/></div> --%>
<br/>
</body>
</html>