<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel='shortcut icon' type='image/x-icon' href='../favicon.ico' />
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<link href="<c:url value="/resources/css/normalize.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/dataTable.rowReorder.js" />"></script>
<script src="<c:url value="/resources/js/common.js" />"></script>
<title>Listing Admin Users</title>
<script>

function onSubmit1()
{
	debugger;
	var total = $("[type='checkbox']:checked").length;
	//var total2 = $("input[name='usercheck']:checked").length;
	alert("total : "+total);
	var arr=[];
	if(total>0){
		$("[type='checkbox']:checked").each(function(){
			arr.push($(this).val());
		})
		alert(arr);
		var doc = window.opener.document;
		doc.getElementById("arr").value=arr;
		self.close();
	    $(doc.getElementById('dummy')).click();
	    
	}
	else{
		alert("Please Select Atleast One User To Submit");
		return false;
	}
return false;
}
	
</script>
<style>

.hide_column {
    display : none;
}
</style>
</head>
<body > <!--  hideColumn('slab_table',4,'5~6~7~8');-->
<form:form name="adminUsers" id="adminUsers" commandName="adminUsers" method="POST" >
<center>
<fieldset>
<div style="width:80%;" align="center" id='adminuserdiv' >
<table id="slab_table" class="display dataTable striped border hovered cell-hovered  "  width="100%" data-role="datatable"  style="width:100%;"  data-searching="true" >
<thead>
<tr>
<th>SELECT</th>
<th>ECODE</th>
<th>ENAME</th>
<th>ETYPE</th>
</tr>
</thead>

<tbody>
<c:set var="count" value="0" scope="page" />
	  <c:forEach items="${adminUserList}" var="user">
	     <c:set var="count" value="${count + 1}" scope="page"/>
         <tr >
           <td  >
			    <input type="checkbox" id="${user.ecode}" name="usercheck" value="${user.ecode}~${user.ename}~${user.etype}" > 
		   </td> 
           <td  >${user.ecode}</td>
           <td  >${user.ename}</td>
           <td  >${user.etype}</td>
         </tr>
      </c:forEach>
    </tbody>
   </table>
 </div>
</fieldset>
</center>
<center>
<table>
 <tr>
 <td ><button class="button   bg-cyan bg-active-darkBlue fg-white " onclick="return onSubmit1();">
 	  <span class=" mif-plus" style="font-size:10px;"></span>&nbsp;Submit
 	  </button>
 </td>
 </tr>
 </table>
<p><a href="javascript:self.close()">Close the Popup </a></p>
 </center>
</form:form>

</body>
</html>