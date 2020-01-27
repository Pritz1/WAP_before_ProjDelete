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
<link href="<c:url value="/resources/css/metro-icons.css" />" 	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />" 	rel="stylesheet">
<link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<link href="<c:url value="/resources/css/normalize.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/d3.v4.min.js" />"></script>
<script src="<c:url value="/resources/js/common.js" />"></script>


<style>
#chart { /* NEW */
	height: 360px; /* NEW */
	position: relative; /* NEW */
	width: 360px; /* NEW */
} /* NEW */
.tooltip { /* NEW */
	background: #eee; /* NEW */
	box-shadow: 0 0 5px #999999; /* NEW */
	color: #333; /* NEW */
	display: none; /* NEW */
	font-size: 12px; /* NEW */
	left: 130px; /* NEW */
	padding: 10px; /* NEW */
	position: absolute; /* NEW */
	text-align: center; /* NEW */
	top: 95px; /* NEW */
	width: 80px; /* NEW */
	z-index: 10; /* NEW */
} /* NEW */
.legend {
	font-size: 12px;
}

rect {
	stroke-width: 2;
}
/*  .input-control input, .input-control textarea, .input-control select{
	 width: 83% !important;
    height: 78% !important;
 } */
</style>
</head>
<body onload="hideColumn('param_table',1,'1~6');"><!--  -->
<!-- hideColumn('paramDiv',4,'0~3~4~5'); -->
<form:form id="subParamform" commandName="subParam" method="POST"	action="addUpdateParam">
<table id="param_table" class="dataTable striped border hovered cell-hovered"	width="100%" style="width: 100%;" data-role="datatable" data-searching="true">
		<thead>
			<tr>
				<th>ID</th>
				<th>Parameters</th>
				<th>Weightage (%)</th>
				<th>Add Date</th>
				<th>Added By</th>
				<th>IsActive</th>
				<th>Edit/Delete</th>
				<th>Cancel</th>
			</tr>
		</thead>
		<tbody id="paramtablebody">
		
		<tbody>
		<c:forEach items="${paramList}" var="param">
         <tr >
           <td  >${param.param_id}</td>
           <td  >${param.param_name}</td>
           <td  >${param.weightage}</td>
           <td  >${param.add_date}</td>
           <td  >${param.addedBy}</td>
           <td  >${param.isActive}</td>
           <td  ><a class=link onclick=return editParam(this);>Edit</a>&nbsp;/&nbsp;<a onclick=return confirm(Are You Sure You Want To Delete?);
                 href=deleteParam.action?param_id=${parameter.param_id}>Delete</a></td>
           <td  ><a href="parameters.action">Cancel</a></td>
       </tr>
      </c:forEach>    
		
<!-- getting data through ajax in jquery function -->
	    </tbody>
</table>
</form:form>
</html>