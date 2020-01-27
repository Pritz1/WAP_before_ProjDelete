<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Appraisal</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/bootstrap-3.3.5.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-datepicker.min-1.4.1.js" />"></script>
<link href="<c:url value="/resources/css/bootstrap-datepicker3_1.4.1.css" />" rel="stylesheet">

<style>
.caption-copy{
padding-top:8px;padding-bottom: 8px;color: #777;text-padding-top:8px;padding-bottom: 8px;color: #777;text-align: left;
}

.table caption {
   font-size:2.0rem;
   color : #1291b9;
   padding-bottom: 10px;   
}

</style>
<script>
$(document).ready(function(){
    var date_input=$('input[name="date"]'); //our date input has the name "date"
    var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
    var options={
      format: 'dd/mm/yyyy',
      container: container,
      todayHighlight: true,
      autoclose: true,
      orientation: "top",
    };
    date_input.datepicker(options);
  })

function confirm(){

	var today = new Date();
	var dateSel=(document.forms[0].date.value).split("/");
	var selectedDt = new Date(dateSel[2], dateSel[1] - 1, dateSel[0]);
	
	if(selectedDt > today){
		alert("Date Cannot Be greater Than Today!!");
		return false;
	}
	
	document.forms[0].loginMth.value=(document.forms[0].date.value).split("/")[1];
 	document.forms[0].loginYr.value=(document.forms[0].date.value).split("/")[2];
	return true;
}  
  
</script>
</head>
<body >
<br/><br/>

<!-- <div class="bootstrap-iso"> -->
<div class="container col-md-12"> 
 <div class="container-fluid">
  <!-- <div class="row"> -->
   <div class="col-lg-12 col-md-12 col-sm-12">

    <!-- Form code begins -->   
  <form:form name="changeDate" id="changeDate" commandName="changeDate" method="POST" action="confirmChangeDate" autocomplete="off" >
    <%-- <form method="post"> --%>
    
    <input type="hidden" id="loginMth" name="loginMth" />
	<input type="hidden" id="loginYr" name="loginYr" />
    
    <table class="col-lg-12">
    <caption>Select Date : </caption>
    <tr>
    <td width="50%">
      <div class="form-group" > <!-- Date input -->
        <!-- <label class="control-label" for="date">Date</label> -->
        <input class="form-control" id="date" name="date" placeholder="DD/MM/YYY" type="text"/>
        </div>
       </td> 
      <td> 
      <div class="form-group "> <!-- Submit button -->
        <button class="btn btn-primary " name="submit" type="submit" onClick="return confirm();">Submit</button>
      </div>
      </td>
      </tr>
      </table>
      
      <c:if test="${not empty res}">
      <p class="caption-copy">${res}</p>
       <script type="text/javascript">
        window.onunload = setTimeout(refreshParent, 1000);
        function refreshParent() {
            window.opener.location.reload();
            window.close();
        }
        </script>
      </c:if>
      
    <%--  </form> --%></form:form>
     <!-- Form code ends --> 

    </div>
  <!-- </div> -->    
 </div>
 </div>
<!-- </div> -->

</body>
</html>