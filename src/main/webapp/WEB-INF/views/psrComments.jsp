<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<!-- header : end-->

<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/bootstrap-table.min-1.12.2.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/bootstrap-3.3.5.min.js" />"></script>
<script src="<c:url value="/resources/js/metro.js" />"></script>

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
	border: 1px solid rgba(0, 0, 0, .125);
	border-radius: .25rem;
}
.caption-copy{
padding-top:8px;padding-bottom: 8px;color: #777;text-padding-top:8px;padding-bottom: 8px;color: #777;text-align: left;
}

.table caption {
   text-align: center;
   font-size:1.0rem;
   color : #1291b9;
   padding-bottom: 10px;   
}

</style>

<!--  Pranali : 22/01/2019 start-->

<script>
	function submitForm() {
		document.forms[0].comments.value=document.forms[0].cmt.value;
		document.forms[0].submit();
	}

	function editComment() {	 
		document.getElementById("cmt").readOnly = "";
		$('#cmt').removeClass('input-disabled');
	  document.getElementById("edit").style.display="none";
	   document.getElementById("save").style.display="";  
		   document.getElementById("clr").style.display=""; 
		   document.forms[0].isEdit.value="edit";
		return false;
	}
	function disableComts()
	{
		if(document.forms[0].comments.value!=null && document.forms[0].comments.value!="")
			{	 
		     $('#cmt').addClass('input-disabled');
			document.forms[0].cmt.value=document.forms[0].comments.value;
			document.getElementById("cmt").readOnly = "true";	
		 
			}
		else{
			if(typeof document.getElementById("save") != "undefined" && document.getElementById("save") !== null ){
			   document.getElementById("save").style.display="";  
			   document.getElementById("clr").style.display=""; 
		}
			   }
			}
	</script>
	<title>Comments</title>
	</head>
	
<body onload="disableComts();"  style="background-color: #e5e5e5;">
<div style="float: top;"><jsp:include page="header.jsp" /></div>
<br><br><br><br><br>

<div id="cont" class="container">
 <div class="row">
  <div class="col-12">
   <div class="card">
    <div class="card-body">
 
      <form:form action="addPsrComments" method="post" id="commentform" commandName="psrComments">

<input type="hidden" id="netId" name="netId" value="${user.netId}"/>
<input type="hidden" id="mth" name="mth" value="${user.loginMth}"/>
<input type="hidden" id="yr" name="yr" value="${user.loginYr}"/>
<input type="hidden" id="ecode" name="ecode" value="${user.emp}"/>
<input type="hidden" id="comments" name="comments" value="${list[16]}"/>
<input type="hidden" id="divid" name="divid" value="${user.divId}"/>
<input type="hidden" id="isEdit" name="isEdit"  />

<c:choose>
 <c:when test="${not empty response}"> 	
   <h5>${response}</h5>
 </c:when>
<c:otherwise>

<br>

<c:set var="count" value="0" scope="page" />
<c:set var="count2" value="0" scope="page" />
<div class="table-responsive">
	<table class="table table-hover table-condensed borderless" width="100%">
	<caption>Parameters To Be Evaluated</caption>
	<thead>
	<tr>
		<th>Parameters</th><th>Weightage %</th><th>Target</th><th>Achievement</th>
		<th>Achievement %</th>
	</tr>
	</thead>
	<tbody>
		<c:forEach items="${paramIdNameWeightageMap}" var="map"> 
			<c:set var="splitVal" value="${fn:split(map.value, '~')}" />
			<c:set var="count2" value="${count2+1}" scope="page"/>
					<tr>
						<th>${map.key}.${splitVal[0]}</th>
						<td>${splitVal[1]}</td>
			<c:choose>
			 <c:when test="${count2 eq paramMapSize}"> <!-- for operational effectivess -->	
			     <td >20</td>
			     <td ></td>
			     <td ></td>
			 </c:when>
			 <c:otherwise>
				<td >${list[count]}</td>
			 </c:otherwise>
			</c:choose>
						
			 <c:if test="${count2 ne paramMapSize}"> <!-- for operational effectivess achv and achv% not to be shown-->	
			 	<td >${list[count+1]}</td>
			 	<td >${list[count+2]}</td>
			 </c:if>
			</tr>
			<c:set var="count" value="${count+3}" scope="page"/>
		</c:forEach>
	</tbody>
	</table>
</div>

<c:if test="${not empty list[16] && (empty list[15] || list[15] eq 0) }">
	<span class="caption-copy" ><b>Comment has been already entered for the month, if you wish to update then click on <i><u>Edit</u></i> below</b> </span><br>
</c:if>

<fmt:parseNumber var = "mgrScore" type = "number" value = "${list[15]}" />

<c:if test="${ (not empty mgrScore && mgrScore ne 0) }">
	<span class="caption-copy" ><b>Comment cannot be updated now as manager has given the feedback</b></span><br>
</c:if>

<br>

<label for="comment">Comment: </label>
<span>(Max 250 Characters)</span>
<br>
	<textarea id="cmt" rows="7" maxlength="250" cols="150" rows="10" name="cmt" class="form-control form-group col-md-6" > </textarea>
<br>

<c:if test="${empty list[15] || list[15] eq 0 }">
	<div align="center">
		 <c:if test="${not empty list[16]}">
			<button id="edit" class="button bg-cyan bg-active-darkBlue fg-white "	onclick="return editComment()">Edit</button>														
		 </c:if>
		 
		<button id="save" type="submit" onclick="submitForm()" style="display: none" class="button bg-cyan bg-active-darkBlue fg-white ">Save</button>
		<button id="clr" class="button bg-cyan bg-active-darkBlue fg-white " style="display: none">Clear</button>
	</div>
</c:if>
 </c:otherwise>
</c:choose>

      </form:form>
     </div>
   </div>
  </div>
 </div>
</div>
<%-- <p>hiiii 1: ${list[1]}</p> --%>
</body>
<!--  Pranali : 22/01/2019 end-->
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
</html>