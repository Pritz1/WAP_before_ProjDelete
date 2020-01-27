<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="javax.servlet.*,java.text.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
 "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<title>Parameters</title>
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />"
	rel="stylesheet"/>
<link href="<c:url value="/resources/css/metro-responsive.css" />"
	rel="stylesheet"/>
<link href="<c:url value="/resources/css/select2.min.css" />"
	rel="stylesheet"/>
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<link href="<c:url value="/resources/css/normalize.css" />"
	rel="stylesheet">
<script src="<c:url value="/resources/js/d3.v4.min.js" />"></script>
<script src="<c:url value="/resources/js/common.js" />"></script>

<style>
#emp_table tr 
{
line-height : 10px !important;
}

#emp_table tr td 
{
max-height : 20px !important;
font-size : small;
}

</style>
</head>

<body>
<form:form id="reportForm" commandName="report" method="POST" action="">
<fieldset>
	<table>
		<tr>
			<td><div class="input-control">
					<label for="division">Employee Selection : </label> 
					<select	class="js-example-basic-single"	data-placeholder="Select-Report is to be Viewed For All/Selected"
						style="width: 350px" data-allow-clear="true" data-minimum-results-for-search="Infinity" id="viewType"
		name="viewType">
		<option value=""></option>
		<option value="1" >ALL</option>
		<option value="2">SELECTED</option>
	</select>
</div></td>
<td>
	<button class="button  bg-cyan bg-active-darkBlue fg-white " onclick="return onSubmit1();" value="Save">Submit</button>
		</td>
	</tr>
</table>
	
 
<c:set var="count" value="0" scope="page" />
<div>
 <table id="emp_table" class="display dataTable striped border hovered cell-hovered  " width="80%" data-role="datatable" 
		style="width: 100%;" data-searching="true">
	<thead>
		<tr>
		    <th>Select</th>
			<th>EMP NAME</th>
			<th>NETID</th>
			<th>ECODE</th>
			<th>HQ NAME</th>
			<th>LEVEL</th>
		</tr>
	</thead>
 <tbody>
<c:forEach items="${empList}" var="emp">
 <c:set var="count" value="${count + 1}" scope="page"/>
  <tr>
	<td>
	<input type="checkbox" id="${count}" name="usercheck" > 
	</td>							
	<td>${emp.empName}</td>
	<td>${emp.netId}</td>
	<td>${emp.eCode}</td>
	<td>${emp.hqName}</td>
	<td>${emp.level}</td>
 </tr>
</c:forEach>
</tbody>
</table>
</div>
</fieldset>
</form:form>
</body>
	
	<script type="text/javascript">
	$(document).ready(function() {
		$('.js-example-basic-single').select2();
		$("#viewType").select2({
			width : 'resolve'
		});
	});
	
	function onSubmit1() {
		if(document.forms[0].viewType.value=="" || document.forms[0].viewType.value==null);{
			alert("Please Select Value For Employee Selection");
			return false;
		}
		alert(1);
		document.forms[0].action = "showEmployeeList.action?viewType="+document.forms[0].viewType.value;
		document.forms[0].submit();
		return true;
	
	}
	</script>
	</html>