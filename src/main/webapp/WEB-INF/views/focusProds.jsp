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

<%-- <link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />"	rel="stylesheet"> --%>
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
.caption-copy{
padding-top:10px;padding-bottom: 8px;color: #777;text-padding-top:8px;padding-bottom: 8px;color: #777;
text-align: center;font-weight:bold;
}
</style>
<script>
function removeAll(){
		//alert(2);
		if(confirm("Are You Sure You Want To Remove All The Products From Focus Products List?")){
			document.forms[0].action = "removeAllFocProd.action";
			document.forms[0].submit();
			return true;
		}else{
			return false
		}
}

function removeSelected(){
	debugger;
	if(confirm("Are You Sure You Want To Remove Selected Products From Focus Products List?")){
		
		var total = $("[type='checkbox']:checked").length;
		//var total2 = $("input[name='usercheck']:checked").length;
		//alert("total : "+total);
		var arr=[];
		if(total>0){
			$("[type='checkbox']:checked").each(function(){
				arr.push("'"+$(this).val()+"'");
			})
		}
		else{
			alert("Please Select Atleast One Product To Remove From Focus List");
			return false;
		}
		document.forms[0].action = "removeSelFocProd.action?prodIdArr="+arr;
		document.forms[0].submit();
		return true;
		
	}else{
		return false
	}
}

function addProducts(){
	
	if(confirm("Are You Sure You Want To Add Products To Focus Prod List?")){
		document.forms[0].action = "showAllProds.action";
		document.forms[0].submit();
		return true;
	}else{
		return false
	}	
	
}

</script>
</head>
<body style="background-color:#e5e5e5;">

<form:form id="focusProd" commandName="product" method="POST" >
<div style="float: top;"><jsp:include page="header.jsp" /></div>
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
 
	<input type="hidden" value="${user.hqRef}" name="hqRef">
	<input type="hidden" value="${user.level}" name="lvl">
 <c:choose>
 <c:when test="${user.lockStatus ne 'Y' && user.level ge '7'}">
 <div class="btn-group btn-group-md">
    <button type="button" class="btn btn-info" onclick="addProducts();">ADD</button>
    <button type="button" class="btn btn-info" onclick="removeSelected();">REMOVE</button>
    <button type="button" class="btn btn-info" onclick="removeAll();">REMOVE ALL</button>
  </div>
  </c:when>
  <c:otherwise>
  <p class="caption-copy">Focus Products Are Locked For Mth : ${user.loginMth} and Yr : ${user.loginYr} </p>
  </c:otherwise>
  </c:choose>
<table cellspacing="0" data-field="EMP" data-toggle="table" class="table col-lg-12 table-striped table-hover borderless" data-search="true"  width="100%">
				<thead>
					<tr>
					    <th width="10%" data-sortable="true" ><div><span>PROD CODE</span></div></th>
					    <th width="15%" data-sortable="true"><div><span>PROD NAME</span></div></th>
						<th width="15%" data-sortable="true"><div><span>DIV</span></div></th>
						<th width="15%" data-sortable="true"><div><span>SELECT</span></div></th>
					</tr>
				</thead>
			    <tbody >
				 <c:forEach items="${prodList}" var="prod">
	 			 <tr>
					<td>${prod.prodid}</td>
					<td>${prod.prodName}</td>
					<td>${prod.divId}</td>
					<c:choose>
 					  <c:when test="${user.lockStatus ne 'Y'}"> 
						<td><input type="checkbox" id="chk${prod.prodid}" name="divcheck" value="${prod.prodid}" ></td>
					  </c:when>
					<c:otherwise>
						<td></td>
					</c:otherwise>
					</c:choose>
				 </tr>	
				</c:forEach>
			   </tbody> 
</table>
</c:otherwise>
</c:choose>
</div>
</div>
</div>
</div></div>
</form:form>


</body>
</html>