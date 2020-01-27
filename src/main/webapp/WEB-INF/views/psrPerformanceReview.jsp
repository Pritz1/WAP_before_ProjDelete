<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<!-- header : end-->

<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-3.3.5.min.js" />"></script>

<style>

/* The container1 */
table {
	border-spacing: 0;
	width: 70%;
	border: 1px solid #ddd;
}

th, td {
	text-align: left;
	/* padding: 16px; */
}

thead, tr:nth-child(even) {
	background-color: #f9f9f9
}

.container1 {
	display: block;
	position: relative;
	padding-left: 35px;
	margin-bottom: 12px;
	cursor: pointer;
	font-size: 22px;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

/* Hide the browser's default radio button */
.container1 input {
	position: absolute;
	opacity: 0;
	cursor: pointer;
}

/* Create a custom radio button */
.checkmark {
	position: absolute;
	top: 0;
	left: 0;
	height: 20px;
	width: 20px;
	background-color: #c3c3c3;
	border-radius: 50%;
}

/* On mouse-over, add a grey background color */
.container1:hover input ~ .checkmark {
	background-color: #ccc;
}

/* When the radio button is checked, add a blue background */
.container1 input:checked ~ .checkmark {
	background-color: #2196F3;
}

/* Create the indicator (the dot/circle - hidden when not checked) */
.checkmark:after {
	content: "";
	position: absolute;
	display: none;
}

/* Show the indicator (dot/circle) when checked */
.container1 input:checked ~ .checkmark:after {
	display: block;
}

/* Style the indicator (dot/circle) */
.container1 .checkmark:after {
	top: 7px;
	left: 7px;
	width: 6px;
	height: 6px;
	border-radius: 50%;
	background: white;
}
.form-actions {
    margin: 0;
    background-color: transparent;
    text-align: center;
}

.caption-copy{
padding-top:8px;padding-bottom: 8px;color: #777;text-padding-top:8px;padding-bottom: 8px;color: #777;text-align: left;
}

.radios:disabled ~ .checkmark {
  color: silver;
  cursor: not-allowed;
}

.radios:not([disabled]):hover:after {
  background: lightblue;
}
</style>

<script type="text/javascript">

function countChar(val) {
	var len = val.value.length;
	if (len > 250) {
		val.value = val.value.substring(0, 250);
	} else {
		$('#charNum').text(250 - len);
	}
};

function valuesToModal() {

	if (document.forms[0].empSel.value == "") {
		alert("Please Select Employee to be Reviewed");
		return false;
	}

	if (!$("input[name=score1]:checked").val()) {
		alert('Please Give Score For First Parameter');
		return false;
	}
	if (!$("input[name=score2]:checked").val()) {
		alert('Please Give Score For Second Parameter');
		return false;
	}
	if (!$("input[name=score3]:checked").val()) {
		alert('Please Give Score For Third Parameter');
		return false;
	}
	if (!$("input[name=score4]:checked").val()) {
		alert('Please Give Score For Fourth Parameter');
		return false;
	}
	if(document.forms[0].update.value!="update"){
		if (document.forms[0].comment.value == null || document.forms[0].comment.value == "") { //when not update only comment field is displayed whether it be any manager
			alert('Please Write Some Comments');
			return false;
		}	
	}else{
	if(document.forms[0].lvl.value=='2'){
		if (document.forms[0].comment.value == null
				|| document.forms[0].comment.value == "") {
			alert('Please Write Some Comments');
			return false;
		}	
	}else if(document.forms[0].lvl.value > '2'){
		if (document.forms[0].comment2.value == null || document.forms[0].comment2.value == "") {
			alert('Please Write Some Comments');
			return false;
		}	
	}
}

	document.getElementById('modTd1').innerHTML = $('input[name=score1]:checked').val();
	document.getElementById('modTd2').innerHTML = $('input[name=score2]:checked').val();
	document.getElementById('modTd3').innerHTML = $('input[name=score3]:checked').val();
	document.getElementById('modTd4').innerHTML = $('input[name=score4]:checked').val();
	var empselVal = $("#empSel").val();
	var empselTxt = $("#empSel option:selected").text();
	document.getElementById('coCode').innerHTML = "<b>ID : </b>"
			+ empselVal.split("~")[1];
	document.getElementById('empName').innerHTML = "<b>Name : </b>"
			+ empselTxt.split("/")[0];
	document.getElementById('hname').innerHTML = "<b>HQ : </b>"
			+ empselTxt.split("/")[1];
	if(document.forms[0].update.value=="update" && document.forms[0].lvl.value > '2'){
		document.getElementById('modComment').innerHTML = document.forms[0].comment2.value;
	}else
	document.getElementById('modComment').innerHTML = document.forms[0].comment.value;
	document.forms[0].psrNetid.value = empselVal.split("~")[2];
	document.forms[0].psrEmp.value = empselVal.split("~")[0];
	//alert("document.forms[0].psrNetid.value : "+document.forms[0].psrNetid.value);
	//alert(document.getElementById('modTd4').innerHTML);
	document.getElementById('confirmMod').click()
}

function confirmReview() {
	//alert(2);
	if(document.forms[0].update.value=="update"){
		document.forms[0].action = "reviewUpdated.action";
		document.forms[0].submit();
		return true;
	}else{
		document.forms[0].action = "reviewConfirmed.action";
		document.forms[0].submit();
		return true;
	}
}

function onChangeEmpSel(){
	debugger;
	//alert(document.forms[0].update.value);
	if(document.forms[0].update.value=="update"){
		if(document.forms[0].empSel.value==""){
			$("input[type=radio]").attr('checked', false);
			document.getElementById("update").style.display="none";
		}else{
		document.getElementById("update").style.display="block";
		getLvlWiseMgrScores(document.forms[0].lvl.value);
		$("input[type=radio]").attr('disabled', true);
	if(document.forms[0].lvl.value>=5){	
		 document.getElementById("capMgr12").style.display="block";
		 document.getElementById("capMgr11").style.display="none";
		 document.getElementById("cmt2").style.display="block";
		 document.getElementById("confirm1").style.display="none";
		 document.getElementById("confirm2").style.display="none";
		 document.getElementById("charsLft").style.display="none";
		 $('#comment').addClass('input-disabled');
		 document.getElementById("comment").readOnly = "true";
		 $('#comment2').addClass('input-disabled');
		 document.getElementById("comment2").readOnly = "true";
		 
		}else if(document.forms[0].lvl.value=="2"){
			
			document.getElementById("capMgr12").style.display="block";
			 document.getElementById("capMgr11").style.display="none";
			 document.getElementById("cmt2").style.display="none";
			 document.getElementById("confirm1").style.display="none";
			 document.getElementById("charsLft").style.display="block";
			 document.getElementById("edit").style.display="block";
			 $('#comment').addClass('input-disabled');
			 document.getElementById("comment").readOnly = "true";
			 
		}else if(document.forms[0].lvl.value=="4"){
			
			 document.getElementById("cmt1").style.display="none";
			 document.getElementById("cmt2").style.display="block";
			 $('#comment2').addClass('input-disabled');
			 document.getElementById("comment2").readOnly = "true";
			 document.getElementById("edit").style.display="block";
			 document.getElementById("confirm2").style.display="none";
			 var fmCode = (document.forms[0].empSel.value).split("~")[3];
			 if(fmCode!=null && fmCode!=""){
					document.getElementById("fmScores").style.display="block";
				}else{
					document.getElementById("fmScores").style.display="none";
				}
		}
	  }
	}else{
		var fmCode = (document.forms[0].empSel.value).split("~")[3];
		
		if(fmCode!=null && fmCode!=""){
			document.getElementById("fmScores").style.display="block";
		}else{
			if(document.forms[0].lvl.value>2)
			alert("AFM Has Not Reviewed This PSR Yet");
			document.getElementById("fmScores").style.display="none";
		}	
	}
}

function getMgr1Scores()
{
	debugger;
var empselVal = $("#empSel").val();
document.forms[0].ajaxRes.value="";
$.ajax({
url : 'getMgr1Scores',
cache: false,
data: 'psrEmp='+empselVal.split("~")[0]+"&psrNetid="+empselVal.split("~")[2]+"&divId="+document.forms[0].hidDivId.value+"&loginMth="+document.forms[0].hidLogMth.value+"&loginYr="+document.forms[0].hidLogYr.value,
type:'GET', 
dataType: 'json',
success : function(obj) 
{
	document.forms[0].ajaxRes.value=obj;
	if(document.forms[0].update.value!="update"){
	 document.forms[0].score1.value=document.getElementById('panTd1').innerHTML = (document.forms[0].ajaxRes.value).split("~")[0];
	 document.forms[0].score2.value=document.getElementById('panTd2').innerHTML = (document.forms[0].ajaxRes.value).split("~")[1];
	 document.forms[0].score3.value=document.getElementById('panTd3').innerHTML = (document.forms[0].ajaxRes.value).split("~")[2];
	 document.forms[0].score4.value=document.getElementById('panTd4').innerHTML = (document.forms[0].ajaxRes.value).split("~")[3];
	}else{
		document.getElementById('panTd1').innerHTML = (document.forms[0].ajaxRes.value).split("~")[0];
		document.getElementById('panTd2').innerHTML = (document.forms[0].ajaxRes.value).split("~")[1];
		document.getElementById('panTd3').innerHTML = (document.forms[0].ajaxRes.value).split("~")[2];
		document.getElementById('panTd4').innerHTML = (document.forms[0].ajaxRes.value).split("~")[3];
	}
	document.forms[0].mgr1Cmt.value=(document.forms[0].ajaxRes.value).split("~")[4];
},error:function (xhr, ajaxOptions, thrownError){
    alert(xhr.status);
    alert(thrownError);
}
});
}

function getPsrAch(obj){
	
var empselVal = $("#empSel").val();
//alert(empselVal);

if(empselVal!=null && empselVal!=""){

var size=document.forms[0].paramMapSize.value;
//alert(size)
document.forms[0].ajaxRes.value="";
$.ajax({
url : 'getPsrAch',
cache: false,
data: 'psrEmp='+empselVal.split("~")[0]+"&psrNetid="+empselVal.split("~")[2]+"&divId="+document.forms[0].hidDivId.value+"&loginMth="+document.forms[0].hidLogMth.value+"&loginYr="+document.forms[0].hidLogYr.value,
type:'GET', 
dataType: 'json',
success : function(obj) 
{
	var j=0;
	document.forms[0].ajaxRes.value=obj;
	//alert(document.forms[0].ajaxRes.value)
	for(var i=0;i<size;i++){
		if(i!=size-1){
			document.getElementById('td0'+i).innerHTML = (document.forms[0].ajaxRes.value).split("~")[j]; //in case of Operational Effectiveness this TD contains 5
			document.getElementById('td1'+i).innerHTML = (document.forms[0].ajaxRes.value).split("~")[j+1];
			document.getElementById('td2'+i).innerHTML = (document.forms[0].ajaxRes.value).split("~")[j+2];//in case of Operational Effectiveness this TD contains NA
		}else{
			document.getElementById('td1'+i).innerHTML = (document.forms[0].ajaxRes.value).split("~")[j];
			var tmp=(document.forms[0].ajaxRes.value).split("~")[j+1];
			if(tmp!=null && tmp!="null")
				document.forms[0].cmt.value=tmp;
			else
				document.forms[0].cmt.value="";
		}
		j=j+3;
	}
	if(document.forms[0].update.value!="update" && (document.forms[0].cmt.value == "" || document.forms[0].cmt.value == null))
		{
			alert("PSR has Not Given Comments Yet. If You Submit Your Review, PSR will Not Get Chance to Fill Comments.");
		}
},error:function (xhr, ajaxOptions, thrownError){
    alert(xhr.status);
    alert(thrownError);
}
});
}else{
	alert("Please Select an Employee");
}
} 	

function clearData(){
var size=document.forms[0].paramMapSize.value;
	
	for(var i=0;i<size;i++){
		if(i!=size-1){
			document.getElementById('td0'+i).innerHTML = ""; //in case of Operational Effectiveness this TD contains 5
			document.getElementById('td2'+i).innerHTML = ""; //in case of Operational Effectiveness this TD contains NA
		} 
		document.getElementById('td1'+i).innerHTML = "";
	}
}

function onLoad1(){
	debugger;
	 var element = document.getElementById("updt");
	 var element2 = document.getElementsByClassName("well");
	if ((typeof(element2)=='undefined' || element2==null || element2.length==0) && typeof(element) != 'undefined' && element != null && document.forms[0].update.value=="update")
	{
			document.getElementById("update").style.display="none";
	}
}

function getLvlWiseMgrScores(lvl)
{
	debugger;
var empselVal = $("#empSel").val();
document.forms[0].ajaxRes.value="";
if(empselVal!=""){
$.ajax({
url : 'getMgrScores',
cache: false,
data: 'psrEmp='+empselVal.split("~")[0]+"&psrNetid="+empselVal.split("~")[2]+"&divId="+document.forms[0].hidDivId.value+"&loginMth="+document.forms[0].hidLogMth.value+"&loginYr="+document.forms[0].hidLogYr.value+"&lvl="+document.forms[0].lvl.value,
type:'GET', 
dataType: 'json',
success : function(obj) 
{
	document.forms[0].ajaxRes.value=obj;
	var temp=(document.forms[0].ajaxRes.value).split("~");
	document.forms[0].score1.value = parseInt(temp[0]); //beacuse data is in string...receiving data as 3.0.
	document.forms[0].score2.value = parseInt(temp[1]);
	document.forms[0].score3.value = parseInt(temp[2]);
	document.forms[0].score4.value = parseInt(temp[3]);
	if(lvl==2){
		document.forms[0].comment.value = temp[4];
	}else if(lvl==4){
		document.forms[0].comment2.value = temp[4];
	}else{
		document.forms[0].comment.value = temp[4];
		document.forms[0].comment2.value = temp[5];
	}
	document.forms[0].score1.disabled=true;
	//document.forms[0].mgr2.value = temp[6];
	if(document.forms[0].emp.value == temp[6]){
		document.getElementById("edit").style.display="block";
	}
	
},error:function (xhr, ajaxOptions, thrownError){
    alert(xhr.status);
    alert(thrownError);
}
});
}
}

function onEdit(){
	$("input[type=radio]").attr('disabled', false);
	document.getElementById("edit").style.display="none";
	if(document.forms[0].lvl.value > 2){
		$('#comment2').removeClass('input-disabled');
		document.getElementById("comment2").readOnly = "";
		document.getElementById("confirm2").style.display="block";
	}else if(document.forms[0].lvl.value == 2){
		$('#comment').removeClass('input-disabled');
		document.getElementById("comment").readOnly = "";
		document.getElementById("confirm1").style.display="block";
	}
}

</script>

</head>
<body onload="onLoad1();">
<div style="float: top;"><jsp:include page="header.jsp" /></div>
<c:choose>
 <c:when test="${(error ne null) && (error ne '')}">
 <br/><br/><br/><br/>
  <div class="container">
   <div class="panel panel-primary">
    <div class="panel-body">
     <div class="well"><span class="caption-copy">${error}</span></div>
    </div>
   </div>
  </div>
 </c:when>
 <c:when test="${(response ne null) && (response ne '')}">
 <br/><br/><br/><br/>
  <div class="container">
   <div class="panel panel-primary">
    <div class="panel-body">
     <div class="well"><span class="caption-copy">${response}</span></div>
    </div>
   </div>
  </div>
 </c:when>
<c:otherwise>

 <form:form id="reviewform" commandName="psrPerformanceReview"	method="POST" action="reviewConfirmed">
	
			<br />
	<%-- <div style="float: left;"><jsp:include page="sideBar.jsp" /></div>		
	 --%>
			
	<form:hidden path="psrNetid" />
	<form:hidden path="psrEmp" />
	<input type="hidden" value="${user.divId}" name="hidDivId">
	<input type="hidden" value="${user.loginMth}" name="hidLogMth">	
	<input type="hidden" value="${user.loginYr}" name="hidLogYr">		
	<input type="hidden"  name="ajaxRes">	
	<input type="hidden" value="${paramMapSize}" name="paramMapSize">
	<input type="hidden" value="${update}" name="update" id="updt">
	<input type="hidden" value="${user.level}" name="lvl">
	<input type="hidden" value="${user.emp}" name="emp">
	<input type="hidden" name="mgr2">
	<br/><br/><br/>			
	<div class="container">
	<div class="panel panel-primary">
	<div class="panel-body">
	<div  style="overflow-x: auto;">
			<!-- <div class="col-lg-12"> -->
		<!-- List Of PSRs under Manager -->	
	<c:choose>
	<c:when test="${(psrPerformanceReview.empList ne null) && (not empty psrPerformanceReview.empList)}">
		<div class="row" style="padding-right: 25px;padding-left: 25px;">
			<table style="border: none;" class="col-lg-12" width="100%">
				<caption id="title101">Section I: Select Your Team Member To Be reviewed</caption>
				<tr>
				  <td>
					<select class="form-control " id="empSel" name="empSel" width="100%"  onChange="onChangeEmpSel();clearData()"><!-- onClick="onChangeEmpSel()"	 -->
						<option value="">Select One Emp</option>
						<c:forEach items="${psrPerformanceReview.empList}" var="emp">
						<option value="${emp.eCode}~${emp.coCode}~${emp.netId}~${emp.fmCode}">${emp.empName}/${emp.hqName}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
	 </div>
	 <!-- List Of PSRs under Manager : end -->
	<br/>	
	
	
	<div id="update">
	<!-- AFM's Review : Starts -->
	<div class="row" style="padding-right: 25px;padding-left: 25px;" >	
	 <div class="panel-group " id="fmScores" style="display:none;" >
	  <div class="panel panel-default">
	    <div class="panel-heading">
	      <h5 class="panel-title">
	        <a data-toggle="collapse" href="#collapse1">Want To Check What AFM has Reviewed?</a>
	      </h5>
	    </div>
	     <div id="collapse1" class="panel-collapse collapse">
	      <div class="panel-body"><a onclick='getMgr1Scores(this)'>Click To Get AFM's Review</a>
			<table  class="table table-sm-12">
				<thead>
					<tr>
						<th>Parameters</th>
						<th>TP Compliance</th>
						<th>Strategy Compliance</th>
						<th>Detailing</th>
						<th>DR. Conversion</th>
					</tr>
				</thead>
				
				<tbody>
					<tr>
						<td>Rating</td>
						<td id="panTd1"></td>
						<td id="panTd2"></td>
						<td id="panTd3"></td>
						<td id="panTd4"></td>
					</tr>
				</tbody>
			</table>
			   <div class="row" style="padding-right: 25px;padding-left: 25px;" id="mgr1CmtDiv">	
			   <span class="caption-copy">Section III: ${user.lvlDescMap['Level2Desc']}'s Comment</span>
				<br/>
				<label for="mgr1Comment">Comment: </label>
				<textarea class="form-control form-group col-md-6 input-disabled" rows="3" maxlength="250" id="mgr1Cmt" name="mgr1Cmt" onkeyup="countChar(this)" readOnly="true"></textarea>
				<br />
			</div>
		  </div>  
	    </div>
	  </div>
	 </div>
	</div>
	<!-- AFM's Review : End -->
	
	<!-- RM's Review : Starts -->
	
	<div class="row" style="padding-right: 25px;padding-left: 25px;" >	
	 <div class="panel-group " id="fmScores" style="display:none;" >
	  <div class="panel panel-default">
	    <div class="panel-heading">
	      <h5 class="panel-title">
	        <a data-toggle="collapse" href="#collapse1">Want To Check What RM has Reviewed?</a>
	      </h5>
	    </div>
	
	<div id="collapse1" class="panel-collapse collapse">
	      <div class="panel-body"><a onclick='getMgr1Scores(this)'>Click To Get RM's Review</a>
	      
			<table  class="table table-sm-12">
				<caption><span id="coCode"></span> <span id="empName"></span> <span id="hname"></span></caption>
				<thead>
					<tr>
						<th>Parameters</th>
						<th>TP Compliance</th>
						<th>Strategy Compliance</th>
						<th>Detailing</th>
						<th>DR. Conversion</th>
					</tr>
				</thead>
				
				<tbody>
					<tr>
						<td>Rating</td>
						<td id="panTd1"></td>
						<td id="panTd2"></td>
						<td id="panTd3"></td>
						<td id="panTd4"></td>
					</tr>
				</tbody>
			</table>
			   
		  </div>  
	      <div class="panel-footer"><span id="totScore"></span></div>
	    </div>
	  </div>
	 </div>
	</div>
	<!-- RM's Review : End -->
	
<!-- PSR's Achievement and Comment : Starts -->	
	<div class="row" style="padding-right: 25px;padding-left: 25px;" >	
	 <div class="panel-group " id="psrAch" >
	  <div class="panel panel-default">
	    <div class="panel-heading">
	      <h5 class="panel-title">
	        <a data-toggle="collapse" href="#collapse2">PSR's Achievement and Comment</a>
	      </h5>
	    </div>
	    
	    <div id="collapse2" class="panel-collapse collapse">
	     <div class="panel-body"><a onclick='getPsrAch(this)'>Click To Get PSR's Achievement and Comment</a>
	      <c:set var="count" value="0" scope="page" />
		   <div class="table-responsive">
			 <table class="table table-hover table-condensed borderless" width="100%">
			  <caption>Parameters To Be Evaluated</caption>
				 <thead>
					<tr>
						<th>Parameters</th><th>Weightage</th><th>Target</th><th>Achievement</th>
						<th>Achievement %</th>
					</tr>
				 </thead>
				 <tbody>
					<c:forEach items="${paramIdNameWeightageMap}" var="map"> 
						<c:set var="splitVal" value="${fn:split(map.value, '~')}" />
						
								<tr>
									<th>${map.key}.${splitVal[0]}</th>
									<td>${splitVal[1]}</td>
									
									<c:choose>
									 <c:when test="${count eq paramMapSize-1}"> 	
									    <td class="group1" id="td0${count}">20</td>
									    <td class="group1" id="td1${count}"></td>
									    <td class="group1" id="td2${count}">NA</td>
									 </c:when>
			 						<c:otherwise>
										<td class="group1" id="td0${count}"></td>
										<td class="group1" id="td1${count}"></td>
										<td class="group1" id="td2${count}"></td>
			 						</c:otherwise>
								  </c:choose>
								</tr>
								<c:set var="count" value="${count+1}" scope="page"/>
					</c:forEach>
				 </tbody>
				</table>
			</div>
	
			<br>
			
			<label for="comment">Comment: </label>
			<br>
				<textarea id="cmt" rows="4"  cols="150" rows="10" name="cmt" readonly="readonly" class="form-control form-group col-md-6 input-disabled" > </textarea>
			<br>
	    </div>
	  </div>
	 </div>
	</div>

<!-- PSR's Achievement and Comment : Starts -->
	<div class="row" style="padding-right: 25px;padding-left: 25px;">			
				
				<!-- </div> -->
				<!-- <div class="table-responsive col-lg-11"> -->
				<table cellspacing="0" class="table col-lg-12" width="100%">
					<caption id="title102">Section II: Operational Effectiveness<br/><br/>
					NOTE : Poor=1,Average=2,Good=3,Excellent=4,Outstanding=5</caption>
					<thead>
						<tr>
							<!-- First column header is not rotated -->
							<th width="20%">&nbsp;</th>
							<!-- Following headers are rotated -->
							<th width="16%"><div><span>Poor</span></div></th>
							<th width="16%"><div><span>Average</span></div></th>
							<th width="16%"><div><span>Good</span></div></th>
							<th width="16%"><div><span>Excellent</span></div></th>
							<th width="16%"><div><span>Outstanding</span></div></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th><label>TP Compliance</label></th>
							<td title="poor"><label class="container1"><input class="radios" type="radio" name="score1" value="1"> <span class="checkmark"></span></label></td>
							<td title="Average"><label class="container1"> <input class="radios" type="radio" name="score1" value="2"><span class="checkmark"></span></label></td>
							<td title="Good"><label class="container1"> <input class="radios" type="radio" name="score1" value="3"><span class="checkmark"></span></label></td>
							<td title="Excellent"><label class="container1"> <input class="radios" type="radio" name="score1" value="4"><span class="checkmark"></span></label></td>
							<td title="Outstanding"><label class="container1"> <input class="radios" type="radio" name="score1" value="5"><span class="checkmark"></span></label></td>
						</tr>
						<tr>
							<th><label>Strategy Compliance</label></th>
							<td title="poor"><label class="container1"><input class="radios" type="radio" name="score2" value="1"><span class="checkmark"></span></label></td>
							<td title="Average"><label class="container1"><input class="radios" type="radio" name="score2" value="2"><span class="checkmark"></span></label></td>
							<td title="Good"><label class="container1"> <input class="radios" type="radio" name="score2" value="3"><span class="checkmark"></span></label></td>
							<td title="Excellent"><label class="container1"> <input	class="radios" type="radio" name="score2" value="4"><span class="checkmark"></span></label></td>
							<td title="Outstanding"><label class="container1"><input class="radios" type="radio" name="score2" value="5"><span	class="checkmark"></span></label></td>
						</tr>
						<tr>
							<th><label>Detailing</label></th>
							<td title="poor"><label class="container1"> <input class="radios"	type="radio" name="score3" value="1"><span class="checkmark"></span></label></td>
							<td title="Average"><label class="container1"><input class="radios" type="radio" name="score3" value="2"><span class="checkmark"></span></label></td>
							<td title="Good"><label class="container1"> <input class="radios" type="radio" name="score3" value="3"><span class="checkmark"></span></label></td>
							<td title="Excellent"><label class="container1"><input class="radios" type="radio" name="score3" value="4"><span class="checkmark"></span></label></td>
							<td title="Outstanding"><label class="container1"> <input class="radios" type="radio" name="score3" value="5"> <span class="checkmark"></span></label></td>
						</tr>
						<tr>
							<th><label>DR. Conversion</label></th>
							<td title="poor"><label class="container1"> <input class="radios" type="radio" name="score4" value="1"> <span class="checkmark"></span></label></td>
							<td title="Average"><label class="container1"> <input class="radios" type="radio" name="score4" value="2"> <span class="checkmark"></span></label></td>
							<td title="Good"><label class="container1"> <input class="radios" type="radio" name="score4" value="3"> <span class="checkmark"></span></label></td>
							<td title="Excellent"><label class="container1"> <input class="radios" type="radio" name="score4" value="4"> <span class="checkmark"></span></label></td>
							<td title="Outstanding"><label class="container1"> <input class="radios" type="radio" name="score4" value="5"> <span class="checkmark"></span></label></td>
						</tr>
					</tbody>
				</table>
				</div>
	<!-- </div> --><br/>
	<!-- <div class="col-lg-12"> -->
	<div class="row" style="padding-right: 25px;padding-left: 25px;" id="cmt1">	
	<span id="capMgr11" class="caption-copy">Section III: Leave a Comment</span>
	<span style="display : none;" id="capMgr12" class="caption-copy">Section III: ${user.lvlDescMap['Level2Desc']}'s Comment</span>
	<br/>
				<label for="comment">Comment: </label><span>(Max 250 Characters)</span>
				<textarea class="form-control form-group col-md-6" rows="5" maxlength="250" id="comment" name="comment" onkeyup="countChar(this)"></textarea>
				<div id="charsLft">
					Characters Left : <span id="charNum"></span>
				</div>
				<br />
				<div class="form-actions" id="confirm1">
					<button id="confirm" type="button" class="btn btn-primary" onclick="return valuesToModal();">&nbsp;CONFIRM</button>
				</div>
			</div>
			
		<!-- To show higher manager's comment on update -->	
			<div class="row" style="padding-right: 25px;padding-left: 25px;display : none;" id="cmt2" >	
			  <span class="caption-copy">Section IV: Upper Level Manager's Comment</span>
				<br/><br/>
				<label for="comment2">Comment: </label>
					<textarea class="form-control form-group col-md-6" rows="5" maxlength="250" id="comment2" name="comment2" ></textarea>
				<div class="form-actions" id="confirm2" >
					<button  type="button" class="btn btn-primary" onclick="return valuesToModal();">&nbsp;CONFIRM</button></td>
				</div>
			</div>
		<!-- To show higher manager's comment on update : end -->
  <c:choose>
 		<c:when test="${(user.lockStatus ne null) && (user.lockStatus ne 'Y')}">
 			<center><button id="edit" type="button"	 class="btn btn-primary" onclick="onEdit();" style="display: none;">&nbsp;EDIT</button></center>
 		</c:when>
 		<c:otherwise>
 			<center><button id="edit" type="button"	 class="btn btn-primary disabled" onclick="onEdit();" style="display: none;">&nbsp;EDIT</button></center>
 		</c:otherwise>
 </c:choose>
 	
	
	<button id="confirmMod" type="button"	class="btn btn-primary" data-toggle="modal"	data-target="#myModal" style="display: none;"></button>
		<!-- Modal -->
			<div id="myModal" class="modal fade" role="dialog">
				<div class="modal-dialog">
	
					<!-- Modal content-->
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">&times;</button>
							<h4 class="modal-title">Please Check And Confirm The Below Ratings :</h4>
						</div>
						<div class="modal-body">
							<table  class="table table-sm">
								<caption>
									<span id="coCode"></span> <span id="empName"></span><span id="hname"></span>
								</caption>
								<thead>
									<tr>
										<th>Parameters</th>
										<th>TP Compliance</th>
										<th>Strategy Compliance</th>
										<th>Detailing</th>
										<th>DR. Conversion</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>Rating</td>
										<td id="modTd1"></td>
										<td id="modTd2"></td>
										<td id="modTd3"></td>
										<td id="modTd4"></td>
									</tr>
								</tbody>
							</table>
	
							<span>Comment:</span>
							<p id="modComment"></p>
	
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
							<button type="button" class="btn btn-default" onclick="return confirmReview();">Confirm</button>
						</div>
					</div>
				</div>
			</div>
		<!-- modal : end -->
			</div></div>
		</c:when>
	<c:otherwise>
	<br/><br/><br/>
	<div class="well"><span class="caption-copy">NO DATA FOUND</span></div>	
	</c:otherwise>
	 </c:choose>
	 </div>
	 </div>
	</div>
	</div>
  </form:form>
 </c:otherwise>
</c:choose>

</body>
</html>
