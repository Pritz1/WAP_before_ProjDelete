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
/* .table>tbody>tr>td,
.table>tbody>tr>th {
  border-top: none;
} */
.table caption {
   text-align: center;
   font-size:1.0rem;
   color : #1291b9;
   padding-bottom: 10px;   
}
.caption-copy{
padding-top:8px;padding-bottom: 8px;color: #777;text-padding-top:8px;padding-bottom: 8px;color: #777;text-align: left;
}
.caption-copy2{
padding-top:1px;padding-bottom: 2px;color: #777;text-padding-top:2px;padding-bottom: 2px;color: #777;text-align: center;
}
.caption-copy-head{
padding-top:2px;padding-bottom: 8px;color: #777;text-padding-top:2px;padding-bottom: 5px;color: #777;text-align: middle;font-weight:bold;font-size:1.2rem;
}
.panel-footer {
background-color: #f5f5f5 !important; /* #1291b91f */
}

.bg-steel {
    background-color: #d2d4d6 !important;
}
.bg-white {
    background-color: #ffffff !important;
}
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
.table-hover tbody tr:hover td input {
	font-weight: normal;
  color: #000000;
}

</style>
<script>
function resetValues(){
	
	document.getElementById('content').style.display="none";
	
	var emp=(document.forms[0].empSel.value).split("~");
	var empselTxt = $("#empSel option:selected").text();
	
	document.getElementById('ecode').innerHTML = emp[0];
	document.getElementById('ename').innerHTML = empselTxt.split("/")[0];
	document.getElementById('netid').innerHTML = emp[3];
	document.getElementById('hname').innerHTML = empselTxt.split("/")[1];
	document.getElementById('jndate').innerHTML = emp[1];
	document.getElementById('grade').innerHTML = emp[4];
}

function getAppraisal(){
	debugger;
	var empselVal = $("#empSel").val();
	
	if(empselVal!=null && empselVal!=""){
		//alert('psrEmp='+empselVal.split("~")[2]+"&psrNetid="+empselVal.split("~")[3]+"&divId="+document.forms[0].hidDivId.value+"&loginMth="+document.forms[0].hidLogMth.value+"&loginYr="+document.forms[0].hidLogYr.value+"&finStrt="+document.forms[0].hidFinStrt.value+"&finEnd="+document.forms[0].hidFinEnd.value);
		$.ajax({
			url : 'getCummAppraisal',
			cache: false,
			data: 'psrEmp='+empselVal.split("~")[2]+"&psrNetid="+empselVal.split("~")[3]+"&divId="+document.forms[0].hidDivId.value+"&loginMth="+document.forms[0].hidLogMth.value+"&loginYr="+document.forms[0].hidLogYr.value+"&finStrt="+document.forms[0].hidFinStrt.value+"&finEnd="+document.forms[0].hidFinEnd.value,
			type:'GET', 
			dataType: 'json',
			success : function(obj) 
			{
				debugger;
				document.forms[0].ajaxRes.value=obj;
				//alert(document.forms[0].ajaxRes.value);
				//yr~mth~!!paramId1~tgt!achv~ach%~score~!!paramId2~tgt!achv~ach%...!@!yr~mth~!!paramId1~tgt!achv~ach%~score~!!paramId2~tgt!achv~ach%...
				
				var arr=(document.forms[0].ajaxRes.value).split("!@!"); //!@! divides data of different months
				//alert(arr);
				var outer=null;
				var innerArr=null;
				var innerMost=null;
				for(var i=0;i<arr.length-1;i++){ //arr.length-1 as last array is always blank
					//alert("2. "+arr[i]);
					outer=arr[i].split("~@@~");
					//alert("outer : "+outer)
					innerArr=outer[0].split("~!!~");//arr[i].split("~!!"); //~!! divides parameters
					//alert("innerArr : "+innerArr);
					for(var j=1;j<innerArr.length;j++){
						//alert("3. "+innerArr[j]);
						innerMost=innerArr[j].split("~");//~ divides data
						
						document.getElementById("tp"+innerMost[0]+"m"+(i+1)).innerHTML = innerMost[1];
						document.getElementById("ap"+innerMost[0]+"m"+(i+1)).innerHTML = innerMost[2];
						document.getElementById("pp"+innerMost[0]+"m"+(i+1)).innerHTML = innerMost[3];
						document.getElementById("sp"+innerMost[0]+"m"+(i+1)).innerHTML = innerMost[4];
						
					}
					document.getElementById("oem"+(i+1)).innerHTML =outer[1];  
					document.getElementById("score"+(i+1)).innerHTML =outer[2];
					document.getElementById("rate"+(i+1)).innerHTML =outer[3];	
				}
				
			},error:function (xhr, ajaxOptions, thrownError){
			    alert(xhr.status);
			    alert(thrownError);
			}
			});
		document.getElementById('content').style.display="";
}
}

function getAppraisalForLvl1(){
	//alert(document.forms[0].lvl.value);
	if(document.forms[0].lvl.value!=null && document.forms[0].lvl.value!="" && document.forms[0].lvl.value=='1'){
		//alert(1);
		document.getElementById('ecode').innerHTML = document.forms[0].emp.value;
		document.getElementById('ename').innerHTML = document.forms[0].ename.value;
		document.getElementById('netid').innerHTML = document.forms[0].netid.value;
		document.getElementById('hname').innerHTML = document.forms[0].hqName.value;
		document.getElementById('jndate').innerHTML = document.forms[0].doj.value;
		document.getElementById('grade').innerHTML = document.forms[0].grade.value;
		
		$.ajax({
			url : 'getCummAppraisal',
			cache: false,
			data: 'psrEmp='+document.forms[0].emp.value+"&psrNetid="+document.forms[0].netid.value+"&divId="+document.forms[0].hidDivId.value+"&loginMth="+document.forms[0].hidLogMth.value+"&loginYr="+document.forms[0].hidLogYr.value+"&finStrt="+document.forms[0].hidFinStrt.value+"&finEnd="+document.forms[0].hidFinEnd.value,
			type:'GET', 
			dataType: 'json',
			success : function(obj) 
			{
				debugger;
				document.forms[0].ajaxRes.value=obj;
				//alert(document.forms[0].ajaxRes.value);
				//yr~mth~!!paramId1~tgt!achv~ach%~score~!!paramId2~tgt!achv~ach%...!@!yr~mth~!!paramId1~tgt!achv~ach%~score~!!paramId2~tgt!achv~ach%...
				
				var arr=(document.forms[0].ajaxRes.value).split("!@!"); //!@! divides data of different months
				var outer=null;
				var innerArr=null;
				var innerMost=null;
				for(var i=0;i<arr.length-1;i++){ //arr.length-1 as last array is always blank
					//alert("2. "+arr[i]);
					outer=arr[i].split("~@@~");
					innerArr=outer[0].split("~!!~");//arr[i].split("~!!"); //~!! divides parameters
					
					for(var j=1;j<innerArr.length;j++){
						//alert("3. "+innerArr[j]);
						innerMost=innerArr[j].split("~");//~ divides data
						
						document.getElementById("tp"+innerMost[0]+"m"+(i+1)).innerHTML = innerMost[1];
						document.getElementById("ap"+innerMost[0]+"m"+(i+1)).innerHTML = innerMost[2];
						document.getElementById("pp"+innerMost[0]+"m"+(i+1)).innerHTML = innerMost[3];
						document.getElementById("sp"+innerMost[0]+"m"+(i+1)).innerHTML = innerMost[4];
						
					}
					document.getElementById("oem"+(i+1)).innerHTML =outer[1];  
					document.getElementById("score"+(i+1)).innerHTML =outer[2];
					document.getElementById("rate"+(i+1)).innerHTML =outer[3];	
				}
				document.getElementById('content').style.display="";
				
			},error:function (xhr, ajaxOptions, thrownError){
			    alert(xhr.status);
			    alert(thrownError);
			}
			});
		
	}
}

</script>

</head>
<body onload="getAppraisalForLvl1();" style="background-color:#e5e5e5;">
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<br/><br/><br/>

<form:form id="ytdAppraisal"  method="POST" >
<input type="hidden"  name="ajaxRes">
	<input type="hidden" value="${user.divId}" name="hidDivId">
	<input type="hidden" value="${user.loginMth}" name="hidLogMth">	
	<input type="hidden" value="${user.loginYr}" name="hidLogYr">
	<input type="hidden" value="${user.finStartDt}" name="hidFinStrt">
	<input type="hidden" value="${user.finEndDt}" name="hidFinEnd">
	<input type="hidden" value="${user.level}" name="lvl">
	
	
<c:if test="${ not empty user.level && user.level eq '1' }">
	<input type="hidden" value="${user.cocode}" name="cocode">
	<%-- <input type="hidden" value="${user.joinDate}" name="jndate"> --%>	
	<input type="hidden" value="${user.emp}" name="emp">
	<input type="hidden" value="${user.netId}" name="netid">
	<input type="hidden" value="${user.employeeName}" name="ename">
	<input type="hidden" value="${user.hqName}" name="hqName">
	<input type="hidden" value="${user.desig}" name="grade">
	<input type="hidden" value="${user.doj}" name="doj">
</c:if>	
	
<br/><br/>	
<center><p class="caption-copy-head">Month : ${user.loginMth} Year : ${user.loginYr} Div: ${user.divName}</p>	 </center>		
<c:choose>
<c:when test="${(error ne null) && (error ne '')}">
<div class="container">
<div class="panel panel-primary">
<div class="panel-body">
<div class="well"><span class="caption-copy">${error}</span></div>
</div></div></div>
</c:when>
<c:otherwise>

<c:if test="${ not empty user.level && user.level!='1' }">
<div class="container">
<div class="row">
<div class="card">
<div class="card-body">
<table style="border: none;" class="col-lg-12 col-md-10 col-sm-7 col-xs-5" width="100%">
				<tr>
					<td>
					<select class="col-sm-5 col-md-10 col-lg-10 form-control" id="empSel" name="empSel"  onChange="resetValues()"> <!-- onChange="clearExistingData()" -->
							<option value="">Select One Emp</option>
							<c:forEach items="${empList}" var="emp">
								<option value="${emp.coCode}~${emp.joinDate}~${emp.ecode}~${emp.netId}~${emp.grade}">${emp.ename}/${emp.hname}</option>
							</c:forEach>
					</select></td>
					<td>
					<button id="confirm" type="button" class="btn btn-primary" onclick="return getAppraisal();">&nbsp;SUBMIT</button>
					</td>
				</tr>
</table>

</div></div></div></div>
<br>
</c:if>

<div id="content" style="display:none;">
<center>
<div class="row" style="padding-left:10%;">

 <div class="col-lg-10 col-md-10 col-sm-12 col-xs-12" style="background-color: #ffffff;padding:1rem;flex:1 1 auto;"> 
<!-- EMP DETAILS -->
<div style="float: left;" class="col-lg-6 col-md-6 col-sm-6">
<!-- <div class="panel panel-primary "> -->
<%--<div style="background-color:#c6b3ff;">
 <p class="caption-copy2"><center>Emp Details</center></p>
<div class="container" >
<div class="row " >
<div class="col-sm-3 col-md-2 col-lg-2 " ><b>EMP ID:</b></div>
<div class="col-sm-1 col-md-2 col-lg-2"><p id="ecode"></p></div>
<div class="col-sm-2 col-md-1 col-lg-2" ><b>NAME:</b></div>
<div class="col-sm-5 col-md-6 col-lg-6"><p id="ename"></p></div>
</div>
</div>
<div class="container" >
<div class="row">
<div class="col-sm-3 col-md-2 col-lg-2" ><b>HQ ID:</b></div>
<div class="col-sm-1 col-md-2 col-lg-2"><p id="netid"></p></div>
<div class="col-sm-2 col-md-1 col-lg-2" ><b>HQ :</b></div>
<div class="col-sm-5 col-md-6 col-lg-6"><p id="hname"></p></div>
</div></div>
<div class="container" >
<div class="row ">
<div class="col-sm-2 col-md-2 col-lg-2" ><b>DOJ:</b></div>
<div class="col-sm-3 col-md-2 col-lg-2"><p id="jndate"></p></div>
<div class="col-sm-2 col-md-1 col-lg-2" ><b>DESIG:</b></div>
<div class="col-sm-4 col-md-5 col-lg-6"><p id="grade"></p></div>
</div>
</div> 
</div>--%>

<div>&nbsp;</div>
<div style="background-color:#c6b3ff;">

<table style="border-top: none;" class="table table-sm-12 table-condensed">
<caption >Emp Details</caption>
<tr>
</tr>	

		<tr>
			<th>EMP ID:</th>
			<td id="ecode"></td>
			<th>NAME:</th>
			<td id="ename"></td>
		</tr>
		<tr>
			<th>HQ ID:</th>
			<td id="netid"></td>
			<th>HQ :</th>
			<td id="hname"></td>
		</tr>
		<tr>
			<th>DOJ:</th>
			<td id="jndate"></td>
			<th>DESIG:</th>
			<td id="grade"></td>
		</tr>


</table>
</div>



<!-- EMP DETAILS : END-->


<!-- WEIGHTAGE -->
<div class="col-lg-6"> <!-- style="float: left;" -->
<!-- <div>&nbsp;</div> -->
<div style="background-color:pink;">

<table  class="table table-sm-12 table-xs-12 table-condensed">
<caption >Weightage</caption>
<thead>
		<tr>
			<th>Parameters</th>
			<th>Weightage</th>
		</tr>
</thead>	
<tbody>
<c:forEach items="${paramIdNameWeightageMap}" var="map"> 
<c:set var="splitVal" value="${fn:split(map.value, '~')}" />
		<tr>
			<td>${map.key}.${splitVal[0]}</td>
			<td>${splitVal[1]}</td>
		</tr>
</c:forEach>
</tbody>
</table>
</div>
</div>
<!-- WEIGHTAGE : END -->

<!-- Rating -->
<div class="col-lg-6" > <!-- style="float: left;" -->
<!-- <div>&nbsp;</div> -->
<c:set var="count" value="0" scope="page" />
<div  style="background-color:lavender;">
<fieldset>
<table  class="table table-sm-12 table-md-12 table-condensed">
<caption >Rating</caption>
<thead><tr>
<td colspan="2">Score</td>
<td>Rating</td>
</tr></thead>
<c:forEach items="${rateList}" var="list"> 
<c:set var="count" value="${count + 1}" scope="page"/>
	<tr>
<c:choose>
	<c:when test="${ list[0] eq 0.00 }">
		<td></td>
		<td>&lt;= ${list[1]}</td>
	</c:when>
	<c:when test="${ count eq rateListSize }">
		<td>&gt;= ${list[0]}</td>
		<td></td>
	</c:when>
	<c:otherwise>
		<td>&gt;= ${list[0]}</td>
		<td>&lt;= ${list[1]}</td>
	</c:otherwise>
	</c:choose>
	<td>${list[2]}</td>
</tr>
</c:forEach>
</table>

<div class="col-md-9" >annual target >= ${aplusMinTgt} lac + score >= ${aplusMinScr} + Sales Ach% >= ${aplusMinAchPer}</div>
<div class="col-md-3" ><b>A+</b></div>
</fieldset>			
</div>
<!-- rating end -->
<!-- </div> -->
</div>
</div>

<!-- Right Side ACH % and POINTS-->
<div style="padding-left:5%;" class="col-lg-6 col-md-6 col-sm-6">
<!-- <div class="panel panel-primary "> -->
<div>&nbsp;</div>
<c:set var="len" value="0" scope="page" />
<div style="background-color:#b3ffd9;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
<c:forEach items="${paramIdNameWeightageMap}" var="map"> 
<c:set var="splitVal" value="${fn:split(map.value, '~')}" />
	<c:if test="${map.key!='6' }">	
	<c:set var="len" value="${fn:length(slabsMap[map.key])}" scope="page"/>	
	
			<table  class="table table-bordered table-sm-12 table-md-12 table-condensed ">
			<tr>
			<td class="caption-copy">${map.key}. ${splitVal[0]}</td>
			<c:forEach begin="1" end="${len}" varStatus="i">
			<td>&gt;=</td>
			</c:forEach>
			</tr>
			<tr>
			<td>Ach %</td>
			<c:forEach items="${slabsMap[map.key]}" var="slabMap">
<%-- 			<c:if test="${slabMap.key!='0.0' }"> --%>
			<td>${slabMap.key}</td>
<%-- 			</c:if> --%>
			</c:forEach>
			</tr>
			<tr>
			<td>Points</td>
			<c:forEach items="${slabsMap[map.key]}" var="slabMap">
<%-- 			<c:if test="${slabMap.value!='0.0' }"> --%>
			<td>${slabMap.value}</td>
<%-- 			</c:if> --%>
			</c:forEach>
			</tr>
			</table></c:if>
</c:forEach>
</div>

</div>
<!-- Right Side ACH % and POINTS : ENDS-->
</div></div></center>

<!-- </div> -->
<!-- YTD ACHV,ACH%,SCORE : STARTS -->
<br>
<div class="container">
	<div class="col-12">
<div class="row">
<div class="card">
<div class="card-body">
<c:set var="len" value="${fn:length(paramIdNameWeightageMap)}" scope="page"/>	
<table  class="table table-sm-12 table-condensed table-bordered">
<caption >YTD REPORT</caption>
	<thead>
	<tr>
		<th>Parameters</th>
		
		<c:forEach items="${finYrMonthsMap}" var="mthMap">
			<th>${mthMap.value}</th>	
		</c:forEach>
<!-- 		<th>Apr</th>
		<th>May</th>
		<th>Jun</th>
		<th>Jul</th>
		<th>Aug</th>
		<th>Sep</th>
		<th>Oct</th>
		<th>Nov</th>
		<th>Dec</th>
		<th>Jan</th>
		<th>Feb</th>
		<th>Mar</th> -->
		</tr>
</thead>
<c:forEach items="${paramIdNameWeightageMap}" var="map"> 
<c:set var="splitVal" value="${fn:split(map.value, '~')}" />
<c:choose>
<c:when test="${map.key < len }"> <!-- below rows not to be printed for Operational effectiveness and len is size of paramIdNameWeightageMap-->
		<tr >
	<td>${splitVal[0]} Tgt</td> <!-- target for param month wise (tpm) -->
		<td id="tp${map.key}m1"></td>
		<td id="tp${map.key}m2"></td>
		<td id="tp${map.key}m3"></td>
		<td id="tp${map.key}m4"></td>
		<td id="tp${map.key}m5"></td>
		<td id="tp${map.key}m6"></td>
		<td id="tp${map.key}m7"></td>
		<td id="tp${map.key}m8"></td>
		<td id="tp${map.key}m9"></td>
		<td id="tp${map.key}m10"></td>
		<td id="tp${map.key}m11"></td>
		<td id="tp${map.key}m12"></td>
		</tr>
		<tr>
		<td>${splitVal[0]} Ach</td>
		<td id="ap${map.key}m1"></td>
		<td id="ap${map.key}m2"></td>
		<td id="ap${map.key}m3"></td>
		<td id="ap${map.key}m4"></td>
		<td id="ap${map.key}m5"></td>
		<td id="ap${map.key}m6"></td>
		<td id="ap${map.key}m7"></td>
		<td id="ap${map.key}m8"></td>
		<td id="ap${map.key}m9"></td>
		<td id="ap${map.key}m10"></td>
		<td id="ap${map.key}m11"></td>
		<td id="ap${map.key}m12"></td>
		</tr>
		<tr>
		<td>${splitVal[0]} Ach%</td>
		<td id="pp${map.key}m1"></td>
		<td id="pp${map.key}m2"></td>
		<td id="pp${map.key}m3"></td>
		<td id="pp${map.key}m4"></td>
		<td id="pp${map.key}m5"></td>
		<td id="pp${map.key}m6"></td>
		<td id="pp${map.key}m7"></td>
		<td id="pp${map.key}m8"></td>
		<td id="pp${map.key}m9"></td>
		<td id="pp${map.key}m10"></td>
		<td id="pp${map.key}m11"></td>
		<td id="pp${map.key}m12"></td>
		</tr>
		<thead>
		<tr>
		<td>${splitVal[0]} Score</td>
		<td id="sp${map.key}m1"></td>
		<td id="sp${map.key}m2"></td>
		<td id="sp${map.key}m3"></td>
		<td id="sp${map.key}m4"></td>
		<td id="sp${map.key}m5"></td>
		<td id="sp${map.key}m6"></td>
		<td id="sp${map.key}m7"></td>
		<td id="sp${map.key}m8"></td>
		<td id="sp${map.key}m9"></td>
		<td id="sp${map.key}m10"></td>
		<td id="sp${map.key}m11"></td>
		<td id="sp${map.key}m12"></td>
		</tr></thead>
</c:when>

<c:otherwise>
		<tr>
		<td>${splitVal[0]} Score</td>
			<c:forEach begin="1" end="12" varStatus="i">
				<td id="oem${i.index}"></td> 	<!-- operation effectiveness month1 -->
			</c:forEach>
	</tr>	
</c:otherwise>

</c:choose>
</c:forEach>
<tr style="background-color: #52677a">
<td></td> 
<c:forEach begin="1" end="12" varStatus="i">
				<td></td> 	
			</c:forEach></tr>
<tr style="background-color:#b3ffd9;">
<th >E-VALUATE Score</th>
<c:forEach begin="1" end="12" varStatus="i">
				<th id="score${i.index}"></th> 	<!-- rating monthwise -->
			</c:forEach>
</tr>

<tr style="background-color: pink">
<th>Rating</th>	
<c:forEach begin="1" end="12" varStatus="i">
				<th id="rate${i.index}"></th> 	<!-- rating monthwise -->
			</c:forEach>
</tr>
</table>
<%-- <p>******************${len}***********************</p> --%>
<!-- YTD ACHV,ACH%,SCORE : ENDS -->
</div></div></div></div></div>
</div>
</c:otherwise></c:choose>
</form:form>
</body>
</html>