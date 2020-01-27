<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<!-- <link rel='shortcut icon' type='image/x-icon' href='../favicon.ico' /> -->
<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />" rel="stylesheet"> <!-- used bootstrap-3.3.5.css instead of bootstrap.min2.css, commented .hidden in bootstrap-3.3.5.css which was causing issue in navbar  -->
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">
<!-- header : end-->

<script src="<c:url value="/resources/js/jquery.dataTables.min2.js" />"></script>
<script src="<c:url value="/resources/js/dataTables.bootstrap.js" />"></script>
<%-- <link href="<c:url value="/resources/css/bootstrap.min2.css" />" rel="stylesheet"> --%>
<link href="<c:url value="/resources/css/dataTables.bootstrap.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/bootstrap-table.min-1.12.2.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/bootstrap-3.3.5.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-table-1.12.2.js" />"></script>
<script src="<c:url value="/resources/js/metro.js" />"></script>

<title>Focus Products</title>
<style>
tbody{
font-size:0.7rem;
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
.table-hover tbody tr:hover td, .table-hover tbody tr:hover th {
  background-color: #1ba1e2;
  /* font-weight:bold; */
  /* font-size: 15px; */
  color:#ffffff;
}
.link:hover { color: #FFFFFF; }
</style>
<script>
$(document).ready(function() {
    $('#prod_table').DataTable();
} );


function addProducts(){
	debugger;
	if(confirm("Are You Sure You Want To Add Selected Products To Focus Products List")){
		
		var total = $("[type='checkbox']:checked").length;
		//alert("total : "+total);
		var arr=[];
		if(total>0){
			$("[type='checkbox']:checked").each(function(){
				arr.push("'"+$(this).val()+"'");
			})
		}
		else{
			alert("Please Select Atleast One Product To Add");
			return false;
		}
		document.forms[0].action = "addFocProds.action?prodIdArr="+arr;
		document.forms[0].submit();
		return true;
		
	}else{
		return false
	}
	
}

</script>
</head>
<body style="background-color:#e5e5e5;">
<div style="float: top;"><jsp:include page="header.jsp" /></div>
<form:form id="focusProd" commandName="product" method="POST" >
<br/><br/><br/><br/>
<div class="container">
<!-- <div class="panel panel-primary col-lg-12">
<div class="panel-body col-lg-12">
<div class="row" style="padding-right: 25px;padding-left: 25px;"> -->
<div class="col-12">
<div class="row">
<div class="card">
 <div class="card-body">
 
 <c:choose>
 <c:when test="${not empty response}"> 	
   <h5>${response}</h5>
 </c:when>
<c:otherwise>


<div class="table-responsive">
<table id="prod_table" class="table table-hover table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th>PROD CODE</th>
				<th>PROD NAME</th>
				<th>DIV</th>
				<th>SELECT</th>
				<!-- <th>Cancel</th> -->
			</tr>
		</thead>
<tbody >
				 <c:forEach items="${prodList}" var="prod">
	 			 <tr>
					<td>${prod.prodid}</td>
					<td>${prod.prodName}</td>
					<td>${prod.divId}</td>
					<td><input type="checkbox" id="chk${prod.prodid}" name="divcheck" value="${prod.prodid}" ></td>
				 </tr>	
				</c:forEach>
			   </tbody> 
</table>
</div>
<br><br>
<center>
<div class="form-actions" id="confirm1">
					<button id="confirm" type="button" class="btn btn-primary" onclick="return addProducts();">&nbsp;SUBMIT</button>
				</div></center>
</c:otherwise>
</c:choose>
</div>
</div>
</div>
</div></div>
</form:form>


</body>
</html>