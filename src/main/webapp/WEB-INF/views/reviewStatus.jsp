<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<div style="float: top;"><jsp:include page="header.jsp" /></div>
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

<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/bootstrap-table.min-1.12.2.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/bootstrap-3.3.5.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-table-1.12.2.js" />"></script>

<style>
tbody{
font-size:0.6rem;
}
thead{
font-size:0.8rem;
}

.table>tbody>tr>td,
.table>tbody>tr>th {
  border-top: none;
}
.table-hover tbody tr:hover td, .table-hover tbody tr:hover th {
  /* background-color: #eff5ff; */
  background-color: #1ba1e2;
  /* font-weight:bold; */
  color:#ffffff;
}
.green {
    color: #0bad0b;
    font-weight: bold;
}
</style>
</head>
<body>
<form:form id="reviewStatus" commandName="reviewStatus" method="POST" >
<br/><br/><br/><br/>
<div class="container">
<div class="panel panel-primary col-lg-12">
<div class="panel-body col-lg-12">
<div class="row" style="padding-right: 25px;padding-left: 25px;">
<table cellspacing="0" data-field="EMP" data-toggle="table" class="table col-lg-12 table-striped table-hover borderless" data-search="true"  width="100%">
				<thead>
					<tr>
					    <th width="10%" data-sortable="true" ><div><span>EMP <br> CODE</span></div></th>
					    <th width="15%" data-sortable="true"><div><span>PSR</span></div></th>
						<th width="15%" data-sortable="true"><div><span>HQ NAME</span></div></th>
						<th width="15%" data-sortable="true"><div><span>PSR <br> COMMENTS</span></div></th>	
						<th width="15%" data-sortable="true"><div><span>MANAGER <br> REVIEWAL</span></div></th>
						<th width="10%" data-sortable="true"><div><span>UPPER LEVEL <br> REVIEWAL</span></div></th>
						<th width="15%" data-sortable="true"><div><span>DATE</span></div></th>
						
					</tr>
				</thead>
						<tbody >
				<c:forEach items="${reviewStatus.empList}" var="emp">
	 			 <tr>
					<td>${emp.psrEmp}</td>
					<td>${emp.psrName}</td>
					<td>${emp.hname}</td>
					
		<c:choose>
<c:when test="${emp.psrCmts!=null and not empty emp.psrCmts}">
<td class="green">COMPLETED</td>
</c:when>
<c:otherwise>
<td>PENDING</td>
</c:otherwise>
 </c:choose>
 			
					<c:choose>
<c:when test="${emp.mgr1Emp!=null and not empty emp.mgr1Emp and emp.mgr1Emp ne '0'}">
<td class="green">COMPLETED</td>
</c:when>
<c:otherwise>
<td>PENDING</td>
</c:otherwise>
 </c:choose>
 					<c:choose>
<c:when test="${emp.mgr2Emp!=null and not empty emp.mgr2Emp and emp.mgr2Emp ne '0' }">
<td class="green">COMPLETED</td>
</c:when>
<c:otherwise>
<td>PENDING</td>
</c:otherwise>
 </c:choose>
					<td>${emp.reviewDate}</td>
	 			</tr>
			  </c:forEach>
 
  </tbody> 
</table>
</div>
</div>
</div>
</div>
</form:form>
</body>
</html>