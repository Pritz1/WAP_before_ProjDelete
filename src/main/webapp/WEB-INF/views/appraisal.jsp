<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>Appraisal</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">
<!-- header : end-->

<link href="<c:url value="/resources/css/bootstrap-table.min-1.12.2.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/bootstrap-3.3.5.min.js" />"></script>
<script src="<c:url value="/resources/js/metro.js" />"></script>
<style>
.table>tbody>tr>td,
.table>tbody>tr>th {
  border-top: none;
}
.table caption {
   text-align: center;
   font-size:1.0rem;
   color : #1291b9;
   padding-bottom: 10px;   
}
.caption-copy{
padding-top:8px;padding-bottom: 8px;color: #777;text-padding-top:8px;padding-bottom: 8px;color: #777;text-align: left;
}
.caption-copy-head{
padding-top:2px;padding-bottom: 8px;color: #777;text-padding-top:2px;padding-bottom: 5px;color: #777;text-align: middle;font-weight:bold;font-size:1.2rem;
}
.panel-footer {
background-color: #f5f5f5 !important; /* #1291b91f */
}
</style>
<script>
function getAppraisal(){
	var empselVal = $("#empSel").val();
	
	if(empselVal!=null && empselVal!=""){
		$.ajax({
			url : 'getAppraisal',
			cache: false,
			data: 'psrEmp='+empselVal.split("~")[3]+"&psrNetid="+empselVal.split("~")[4]+"&divId="+document.forms[0].hidDivId.value+"&loginMth="+document.forms[0].hidLogMth.value+"&loginYr="+document.forms[0].hidLogYr.value,
			type:'GET', 
			dataType: 'json',
			success : function(obj) 
			{
				//debugger;
				document.forms[0].ajaxRes.value=obj;
				var arr=(document.forms[0].ajaxRes.value).split("~");
				//alert(document.forms[0].ajaxRes.value);
				var j=0;
				var k=1;
				var mgrCnt=0;
				var len=arr.length;
				for(i=0;i<len;i++){
					
					if(mgrCnt==0){
					j++;
					if(j>5){
						j=1;
						k++;
					}
					if(k<=6){
						if(k==6 && j==1){
							document.getElementById('td'+j+k).innerHTML = document.forms[0].totOpEffScore.value;
							i--;
						}else
							document.getElementById('td'+j+k).innerHTML = arr[i];
					}else{
						mgrCnt=1;
						j=1;
						document.getElementById('mgr1'+j).innerHTML = arr[i];
						i++;
						document.getElementById('mgr2'+j).innerHTML = arr[i];
					}
					}else{
						j++;
						if(j<=4){
							document.getElementById('mgr1'+j).innerHTML = arr[i];
							i++;
							document.getElementById('mgr2'+j).innerHTML = arr[i];
						}
					}
				
					if(i==(len-7))
						document.getElementById("comment1").value = arr[i];
					if(i==(len-6))
						document.getElementById("comment2").value = arr[i];
					if(i==(len-5))
						document.getElementById("totScore").innerHTML = arr[i];
					if(i==(len-4))
						document.getElementById("rating").innerHTML = arr[i];
					if(i==(len-3))
						document.getElementById("ytdScore").innerHTML = arr[i];
					if(i==(len-2))
						document.getElementById("ytdRating").innerHTML = arr[i];
					if(i==(len-1))
						document.getElementById("commentPsr").innerHTML = arr[i];
					
				}
			
			},error:function (xhr, ajaxOptions, thrownError){
			    alert(xhr.status);
			    alert(thrownError);
			}
			});
		
		
		var empselTxt = $("#empSel option:selected").text();
		
		document.getElementById('empName').innerHTML = empselTxt.split("/")[0];
		document.getElementById('mgrName').innerHTML = empselVal.split("~")[2];
		document.getElementById('desig').innerHTML = empselVal.split("~")[5];
		document.getElementById('ecode').innerHTML = empselVal.split("~")[0];
		document.getElementById('grade').innerHTML = "--";
		document.getElementById('hname').innerHTML = empselTxt.split("/")[1];
		document.getElementById('doj').innerHTML = empselVal.split("~")[1];
		
		document.getElementById('appraisal-body').style.display="block";
		document.getElementById('appraisal-footer').style.display="block";
		
		
	}else{
			alert("Please Select An Employee");
	}
}

function getAppraisalForLvl1(){

if(document.forms[0].lvl.value!=null && document.forms[0].lvl.value!="" && document.forms[0].lvl.value=='1'){
	$.ajax({
		url : 'getAppraisal',
		cache: false,
		data: 'psrEmp='+document.forms[0].emp.value+"&psrNetid="+document.forms[0].netid.value+"&divId="+document.forms[0].hidDivId.value+"&loginMth="+document.forms[0].hidLogMth.value+"&loginYr="+document.forms[0].hidLogYr.value,
		type:'GET', 
		dataType: 'json',
		success : function(obj) 
		{
			debugger;
			document.forms[0].ajaxRes.value=obj;
			var arr=(document.forms[0].ajaxRes.value).split("~");
			//alert(document.forms[0].ajaxRes.value);
			var j=0;
			var k=1;
			var mgrCnt=0;
			var len=arr.length;
			for(i=0;i<len;i++){
				
				if(mgrCnt==0){
				j++;
				if(j>5){
					j=1;
					k++;
				}
				if(k<=6){
					if(k==6 && j==1){
						document.getElementById('td'+j+k).innerHTML = document.forms[0].totOpEffScore.value;
						i--;
					}else
						document.getElementById('td'+j+k).innerHTML = arr[i];
				}else{
					mgrCnt=1;
					j=1;
					document.getElementById('mgr1'+j).innerHTML = arr[i];
					i++;
					document.getElementById('mgr2'+j).innerHTML = arr[i];
				}
				}else{
					j++;
					if(j<=4){
						document.getElementById('mgr1'+j).innerHTML = arr[i];
						i++;
						document.getElementById('mgr2'+j).innerHTML = arr[i];
					}
				}
				
				if(i==(len-7))
					document.getElementById("comment1").value = arr[i];
				if(i==(len-6))
					document.getElementById("comment2").value = arr[i];
				if(i==(len-5))
					document.getElementById("totScore").innerHTML = arr[i];
				if(i==(len-4))
					document.getElementById("rating").innerHTML = arr[i];
				if(i==(len-3))
					document.getElementById("ytdScore").innerHTML = arr[i];
				if(i==(len-2))
					document.getElementById("ytdRating").innerHTML = arr[i];
				if(i==(len-1))
					document.getElementById("commentPsr").innerHTML = arr[i];
			}
		
		},error:function (xhr, ajaxOptions, thrownError){
		    alert(xhr.status);
		    alert(thrownError);
		}
		});
	
	document.getElementById('mgrName').innerHTML = document.forms[0].mgrName.value;
	document.getElementById('desig').innerHTML = document.forms[0].grade.value;
	document.getElementById('ecode').innerHTML = document.forms[0].cocode.value;
	document.getElementById('grade').innerHTML = "--";
	document.getElementById('doj').innerHTML = document.forms[0].jndate.value;
	
	document.getElementById('appraisal-body').style.display="block";
	document.getElementById('appraisal-footer').style.display="block";
	
}
}

function clearExistingData(){
	//debugger;
	
	document.getElementById('empName').innerHTML = "";
	document.getElementById('mgrName').innerHTML = "";
	document.getElementById('desig').innerHTML = "";
	document.getElementById('ecode').innerHTML = "";
	document.getElementById('grade').innerHTML = "--";
	document.getElementById('hname').innerHTML = "";
	document.getElementById('doj').innerHTML = "";
	
	document.getElementById("comment1").value = "";
	document.getElementById("comment2").value = "";
	document.getElementById("totScore").innerHTML = "";
	document.getElementById("rating").innerHTML = "";
	document.getElementById("ytdScore").innerHTML = "";
	document.getElementById("ytdRating").innerHTML = "";
	
	 $(".group1").empty();
	 $(".group2").empty();
	
	//document.getElementById('appraisal-body').style.display="none";
	//document.getElementById('appraisal-footer').style.display="none";
	
	
}
</script>
</head>
<body onload="getAppraisalForLvl1();">
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<br/><br/><br/>

<form:form id="appraisal"  method="POST" >
<br/><br/><br/><br/>
	<input type="hidden" value="${user.divId}" name="hidDivId">
	<input type="hidden" value="${user.loginMth}" name="hidLogMth">	
	<input type="hidden" value="${user.loginYr}" name="hidLogYr">
	<input type="hidden" value="${noOfParams}" name="noOfParams">
	<input type="hidden" value="${user.level}" name="lvl">
	<input type="hidden"  name="ajaxRes">
	<input type="hidden"  name="totOpEffScore" value="${totOpEffScore}">
	<input type="hidden"  name="eachOpEffScore" value="${eachIpEffScore}">
	
<c:if test="${ not empty user.level && user.level eq '1' }">
	<input type="hidden" value="${emp.coCode}" name="cocode">
	<input type="hidden" value="${emp.joinDate}" name="jndate">	
	<input type="hidden" value="${emp.mgrName}" name="mgrName">
	<input type="hidden" value="${user.emp}" name="emp">
	<input type="hidden" value="${user.netId}" name="netid">
	<input type="hidden" value="${emp.grade}" name="grade">
</c:if>	
	
<center><p class="caption-copy-head">Month : ${mthnm} Year : ${user.loginYr}</p>	 </center>		
<c:choose>
<c:when test="${(error ne null) && (error ne '')}">
<div class="container">
<div class="panel panel-primary">
<div class="panel-body">
<div class="well"><span class="caption-copy">${error}</span></div>
</div></div></div>
</c:when>
<c:otherwise>
<div class="container" >
<div class="panel panel-primary ">
 <c:if test="${ not empty user.level && user.level!='1' }">
<div class="panel-footer" ><!-- <div class="table-responsive"> -->
<!-- <table style="border: none;" class="col-lg-12 col-md-12 col-sm-10 col-xs-4" width="100%">
				<tr>
					<td> -->
					<div class="row">
					<div class="col-lg-10 col-md-10 col-sm-8 col-xs-4">
					<select  class="form-control" id="empSel" name="empSel"  onChange="clearExistingData()">
							<option value="">Select One Emp</option>
							<c:forEach items="${empList}" var="emp">
								<option value="${emp.coCode}~${emp.joinDate}~${emp.mgrName}~${emp.ecode}~${emp.netId}~${emp.grade}">${emp.ename}/${emp.hname}</option>
							</c:forEach>
					</select> </div> <!-- </td>
					<td> -->
					<div class="col-lg-2 col-md-2 col-sm-4 col-xs-10">
					<button id="confirm" type="button" class="btn btn-primary" onclick="return getAppraisal();">&nbsp;SUBMIT</button></div>
<!-- 					</td>
				</tr>
</table>
 --></div>
<!-- </div> --></div>
</c:if>

<div class="panel-body" id="appraisal-body" style="display:none;">
<div class="table-responsive">
<table class="table table-hover table-condensed borderless" width="100%">
<tr>
<th class="col-sm-2">Emp Name</th>
<td class="col-sm-4 caption-copy" id="empName">${(user.level eq '1') ? user.employeeName : ''}</td>
<th class="col-sm-1">Manager</th>
<td class="col-sm-3 caption-copy" id="mgrName"></td>
<th class="col-sm-1">Designation</th>
<td class="col-sm-1 caption-copy" id="desig">${emp.grade}</td> <!-- ${user.lvlDescMap['Level1Desc']} -->
</tr>
</table>
</div>
<div class="table-responsive">
<table class="table table-hover table-condensed borderless" width="100%">
<tr>
<th class="col-sm-2">Emp Code</th>
<td class="col-sm-1 caption-copy" id="ecode"></td>
<th class="col-sm-1">Grade</th>
<td class="col-sm-1 caption-copy" id="grade">--</td>
<th class="col-sm-2 " >HQ NAME</th>
<td class="col-sm-2 caption-copy" id="hname">${(user.level eq '1') ? user.hqName : ''}</td>
<th class="col-sm-1">DOJ</th>
<td class="col-sm-2 caption-copy" id="doj"></td>
</tr>
</table>
</div>
<c:set var="count" value="0" scope="page" />
<div class="table-responsive">
<table class="table table-hover table-condensed borderless" width="100%">
<caption>Parameters To Be Evaluated</caption>
<thead>
<tr>
	<th>Parameters</th><th>Weightage</th><th>Target</th><th>Achievement</th>
	<th>Achievement %</th><th>Marks</th><th>Score</th>
</tr>
</thead>
<tbody>
<c:forEach items="${paramIdNameWeightageMap}" var="map"> 
<c:set var="splitVal" value="${fn:split(map.value, '~')}" />
<c:set var="count" value="${count + 1}" scope="page"/>
		<tr>
			<th>${map.key}.${splitVal[0]}</th>
			<td>${splitVal[1]}</td>
			<td class="group1" id="td1${count}"></td>
			<td class="group1" id="td2${count}"></td>
			<td class="group1" id="td3${count}"></td>
			<td class="group1" id="td4${count}"></td>
			<td class="group1" id="td5${count}"></td>
		</tr>
</c:forEach>
</tbody>
</table>
</div>
<c:set var="count2" value="0" scope="page" />
<div class="table-responsive">
<table class="table table-hover table-condensed borderless" width="100%">
<caption >Operational Effectiveness</caption>
<thead>
<tr>
<th>Operational Effectiveness</th>
<th>&nbsp;</th>
<th>&nbsp;</th>
<th>&nbsp;</th>
<th>&nbsp;</th>
<th>AFM/ABM</th>
<th>${user.lvlDescMap['Level4Desc']}/${user.lvlDescMap['Level5Desc']}/HO</th>
<th></th>
</tr>
</thead>
<tbody>
<c:forEach items="${opEffMap}" var="map"> 
<c:set var="splitVal" value="${fn:split(map.value, '~')}" />
<c:set var="count2" value="${count2 + 1}" scope="page"/>
<tr>
<th>${splitVal[1]}</th>
<td>${splitVal[0]}%</td>
<td>${eachIpEffScore}</td>
<td ></td>
<td ></td>
<td class="group2" id="mgr1${count2}"></td>
<td class="group2" id="mgr2${count2}"></td>
</tr>
</c:forEach>

</tbody>
</table>
</div>

<label for="comment">${user.lvlDescMap['Level1Desc']}'s Comment: </label>
<textarea class="form-control form-group col-md-6" rows="5"	maxlength="250" id="commentPsr" name="commentPsr"	readonly=true></textarea>
<br/>
<label for="comment">${user.lvlDescMap['Level2Desc']}'s Comment: </label>
<textarea class="form-control form-group col-md-6" rows="5"	maxlength="250" id="comment1" name="comment1"	readonly=true></textarea>
<br/>
<label for="comment">${user.lvlDescMap['Level4Desc']}/${user.lvlDescMap['Level5Desc']}/HO's Comment: </label>
<textarea class="form-control form-group col-md-6" rows="5" maxlength="250" id="comment2" name="comment2"	readonly=true></textarea>

</div>
<div class="panel-footer" id="appraisal-footer" style="display:none;">

<table class="table table-condensed borderless" width="50%">
<tr>
<th>Overall Score</th><td id="totScore"></td><th>over all rating</th><td id="rating"></td>
<th>YTD Score</th><td id="ytdScore">4</td><th>YTD Rating</th><td id="ytdRating">A</td>
</tr>
</table>

</div>
</div>
</div>
</c:otherwise></c:choose>
</form:form>
</body>
</html>