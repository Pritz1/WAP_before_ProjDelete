<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
	<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
 "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
   
<title>Process E-Valuate</title>

<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<!-- header : end-->

<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />" 	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />" 	rel="stylesheet">
<link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<%-- <link href="<c:url value="/resources/css/normalize.css" />" rel="stylesheet"> --%>
<script src="<c:url value="/resources/js/common.js" />"></script>
<script>

/* $(document).ready(function() {
    $('.js-example-basic-single').select2();
    $("#mth").select2({ width: 'resolve' }); 
}); */

function onSubmitProcWap()
{
	debugger;
	
	//var mthVal=document.forms[0].mth.value;
	var mth=$("#yrMth option:selected").text();
	
	if(document.forms[0].yrMth.value!=null && document.forms[0].yrMth.value!=""){
	if(confirm('Are You Sure You Want To Process E-Valuate For '+mth+'?')){
		on();	
			document.getElementById("par1").innerHTML="Process In Progress.....";
			//setTimeout(myFunction, 5000);
			var mthnm=mth.split('-');
			document.forms[0].monthnm.value=mthnm[0];
			document.forms[0].yr.value=mthnm[1];
			document.forms[0].mth.value=(document.forms[0].yrMth.value).substring(4,6);
			//alert("yr : "+document.forms[0].yr.value);
			//alert("yr : "+document.forms[0].yr.value);
			//var mthval=document.forms[0].monthnm.value;
			//document.forms[0].mth.value=(((mthval).length<2) ? ("0"+mthval) : mthval)
			//alert("document.forms[0].mth.value : "+document.forms[0].mth.value);
			document.forms[0].action="processWap.action";
			document.forms[0].submit();
		    //return true;
			return false;
		}
		return false;
	}else{
		alert("Please Select Month");
		return false;
	}
	return false;
}

function checkForError(){
	debugger;
	if(document.forms[0].errCode.value!="" && document.forms[0].errCode.value!=null){
	if(document.forms[0].errCode.value =="0100" ){
		//alert("errcode : "+ document.forms[0].errCode.value);
		if(confirm(document.forms[0].errorDesc.value + " Do You Still Want To Continue?")){
			debugger;
			on();
			document.getElementById("par1").innerHTML="Process In Progress.....";
			//setTimeout(myFunction, 5000);
			document.forms[0].action="processWapContinue.action";
			document.forms[0].submit();
		    return true;
			//return false;
		}else{
			document.forms[0].getElementById("par1").innerHTML="";
			return false;
		}
	   }else if(document.forms[0].errCode.value =="0300" ){
			if(confirm(document.forms[0].errorDesc.value + " Do You Want to Lock The E-Valuate For the Month?")){
				document.forms[0].isdataLocked.value="Y";
			}else{
				document.forms[0].isdataLocked.value="W";
			}
			on();
			document.getElementById("par1").innerHTML="Process In Progress.....";
			document.forms[0].action="processWapContinue.action";
			document.forms[0].submit();
		    return true;
		   }
	}
	return false;
}
function on() {
	  document.getElementById("loading").style.display = "block";
	}

	function off() {
	  document.getElementById("loading").style.display = "none";
	}
function myFunction(){
	off();
}	

function removeError(){
	if(typeof document.getElementById("par1") != "undefined" && document.getElementById("par1") !== null)
		document.getElementById("par1").style.display="none";
}

</script>

<style>.bg-steel {
    background-color: #d2d4d6 !important;
}
.bg-white {
    background-color: #ffffff !important;
}

.class-disabled{
color : #a39393;
cursor: not-allowed;
}

.class-green{
color : #4b983a;
cursor: not-allowed;
font-weight : bold
}

#overlay {
  position: fixed;
  /* display : none; */
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  z-index: 2;
  cursor: pointer;
}

#loader{
  position: absolute;
  top: 50%;
  left: 50%;
  font-size: 50px;
  color: white;
  transform: translate(-50%,-50%);
  -ms-transform: translate(-50%,-50%);
}


</style>

</head>
<body onload="checkForError();" class="bg-white">
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<br/><br/><br/>
<!-- wap MEnu Ends -->

<!-- hideColumn('paramDiv',4,'0~3~4~5'); -->
<!-- margin-left:20%; -->

<!-- wap MEnu Ends -->
<div  style="margin-top:5%;">
<form:form name="processWapform" id="processWapform" commandName="processWap" method="POST" action="processWap">
<form:hidden path ="monthnm" />
<form:hidden path ="yr" />
<form:hidden path ="mth" />
<input type="hidden" name="errCode" id="errCode" value="${((not empty error) ? error['errCode'] : '')}">
<input type="hidden" name="errorDesc" id="errorDesc" value="${((not empty error) ? error['errDesc'] : '')}">
<input type="hidden" name="isdataLocked" id="isdataLocked" >

<div class="container" >
<div class="panel panel-primary">
<div class="panel-body">
<div id="locktable" >
<h5>Select Month for Which E-Valuate is to be Processed For Whole Field Force:</h5> 


<%--<label for="division">Month : </label>
 <div class="input-control">
 <select class="js-example-basic-single" data-placeholder="Select Month for WAP Processing" style="width:100%" 
         data-allow-clear="true" data-minimum-results-for-search="Infinity" id="mth" name="mth" >
  	<option value=""></option>
  	<c:forEach items="${finYrMonthsMap}" var="monthyr">
  		<option value="${monthyr.key}" ${((pwap.yrMth!=null && pwap.yrMth==monthyr.key) ? 'selected="selected"' : '')}  >${monthyr.value}</option>
 	</c:forEach> 
 </select>
 <button class="button  bg-cyan bg-active-darkBlue fg-white" onclick="return onSubmitProcWap();" type="submit" value="Process" />&nbsp;Process
</div> --%>

<table style="border: none;" width="100%">
<tr>
	<td>
		<select class="col-lg-8 col-md-8 col-sm-8 col-xs-5 form-control" id="yrMth" name="yrMth" onchange="removeError()" >
			<option value="">Select Month for E-Valuate Processing</option>
			<c:forEach items="${finYrMonthsMap}" var="monthyr">
				<c:choose>
				
				<c:when test="${fn:contains(processedYrMthList, monthyr.key)}">
						<option title="already processed" class="class-green" value="${monthyr.key}" ${((pwap.yrMth!=null && pwap.yrMth==monthyr.key) ? 'selected="selected"' : '')}  disabled >${monthyr.value}</option>
					</c:when>
				
					 <c:when test="${logYrMth ge  monthyr.key }"> <!-- (fn:split(monthyr.value, '-'))[1].concat(monthyr.key) -->	
						<option value="${monthyr.key}" ${((pwap.yrMth!=null && pwap.yrMth==monthyr.key) ? 'selected="selected"' : '')}  >${monthyr.value}</option>
					</c:when>
								
					<c:otherwise>
						<option title="greater than logged in mth yr" class="class-disabled" value="${monthyr.key}" ${((pwap.yrMth!=null && pwap.yrMth==monthyr.key) ? 'selected="selected"' : '')} disabled  >${monthyr.value}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
	</td>
	<td ><!-- id="editsubmitbuttons" -->
		<button id="confirm" type="submit"  class="btn btn-primary col-lg-6 col-md-6 col-sm-6 col-xs-6 " onclick="return onSubmitProcWap();">&nbsp;&nbsp;Process&nbsp;&nbsp;</button>
	</td>
</tr>
</table>
<%-- FOR TESTING 
 <p>${processedMthList}<p>
<c:forEach items="${finYrMonthsMap}" var="monthyr">
<p>${monthyr.key}</p><p>${monthyr.value}</p><p>${(fn:split(monthyr.value, '-'))[1]}</p>
<p>${(fn:split(monthyr.value, '-'))[1].concat(monthyr.key)}</p>
<c:if test = "${fn:contains(processedMthList, monthyr.key)}">
         <p>Found test string ${monthyr.key}<p>
      </c:if>
</c:forEach> --%>
<!-- ------------IF ERROR WHILE PROCESSING ------------------------------------------ -->
<c:choose>
<c:when test="${not empty error}">
<p id="par1">${error['errCode']} and Desc : ${error['errDesc']} </p>

</c:when>
<c:otherwise>
<p id="par1" ></p>
</c:otherwise>
 </c:choose>
 </div>

 <c:if test="${not empty pwap.stList}">
<table>
<tr><th>&nbsp;&nbsp;STNAME</th><th>&nbsp;&nbsp;STCODE</th><th>&nbsp;&nbsp;STATE</th><th>&nbsp;&nbsp;ALLOCATION %</th></tr>
<c:forEach items="${pwap.stList}" var="stockist">
<tr>
<td>&nbsp;&nbsp;${stockist.stName}</td>
<td>&nbsp;&nbsp;${stockist.stCode}</td>
<td>${stockist.state}</td>
<td>&nbsp;&nbsp;${stockist.perc}</td>
</tr>
</c:forEach>
</table>
</c:if>
<!-- <table id="editsubmitbuttons">
 <tr>
 <td ><button class="button  bg-cyan bg-active-darkBlue fg-white" onclick="return onEditlockMaster();" value="Edit" /><span class="mif-pencil"></span>&nbsp;Edit</td>
 	<td ><button class="button  bg-cyan bg-active-darkBlue fg-white" onclick="return onSubmitProcWap();" type="submit" value="Process" />&nbsp;Process</td>
 </tr>
 </table>  -->
</div></div></div>


</form:form>
 <div data-role="dialog" id="dialog1" style="width:50px;display:none;" data-close-button="true" data-type="alert" data-width="350" >
    <h3>&nbsp;&nbsp;Alert!</h3>
    <p>
      	&nbsp;&nbsp; E-Valuate is already processed for all months</p>
      	
      	
</div>
</div>
</body>
<div id="loading" style="display:none">
<div id="overlay" >overlay display</div>
<div id="loader"><img class="main-logo" width="50px" height="50px" src="<c:url value="/resources/img/loader.gif"/>" alt="" /></div>
</div>
</html>