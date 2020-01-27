<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
 "http://www.w3.org/TR/html4/loose.dtd">
<html>
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
   
<title>Upload</title>

<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<link href="<c:url value="/resources/css/normalize.css" />" rel="stylesheet">

<style>

.bg-white {
    background-color: #ffffff !important;
}

</style>
<script>
function submit1(){
	document.forms[0].action="fileUpload.action";
	document.forms[0].submit();
	return true;
}
</script>
</head>

<body class="bg-white">
<div style=" margin-top:5%;">

<form:form id="uploadExcel" method="post" enctype="multipart/form-data"  commandName="uploadExcel" action="fileUpload"  >  
<c:out value="${sessionScope.LoginForm.userName}"/>
<fieldset>
<div class="cell">
 <label>Select File To Upload : </label>
<div class="input-control file" data-role="input">
    <input type="file" name="file">
    <button class="button"><span class="mif-folder"></span></button>
    
</div>
<button class="button   bg-cyan bg-active-darkBlue fg-white " type="submit" onclick="return submit1();" ><span class=" mif-upload "></span>&nbsp;Upload</button>
</div>
<!-- <table >
 <tr>
 <td ><button class="button   bg-cyan bg-active-darkBlue fg-white " type="submit" ><span class=" mif-upload "></span>&nbsp;Upload</button></td> onclick="return onSubmitOpload();"
 </tr>
 </table>  -->
</fieldset>
</form:form>
</div>
</body>
</html>