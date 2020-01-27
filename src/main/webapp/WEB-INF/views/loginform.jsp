<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="description" content="Metro, a sleek, intuitive, and powerful framework for faster and easier web development for Windows Metro Style.">
    <meta name="keywords" content="HTML, CSS, JS, JavaScript, framework, metro, front-end, frontend, web development">
    <meta name="author" content="Sergey Pimenov and Metro UI CSS contributors">
    
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel='shortcut icon' type='image/x-icon' href='../favicon.ico' />

    <title>Login form</title>

<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet">
<%-- <script src="<c:url value="/resources/js/jquery-3.1.0.js" />"></script> --%>
<script src="<c:url value="/resources/js/jquery-2.1.4.min.js" />"></script>
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery-ui-1.12.1.js" />"></script>
<link href="<c:url value="/resources/css/jquery-ui-1.12.1.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/checkloginMD5.js" />"></script>

    <style>
        .login-form {
            width: 25rem;
            height: 26.75rem;
            position: fixed;
            top: 40%;
            margin-top: -9.375rem;
            left: 50%;
            margin-left: -12.5rem;
            background-color: #ffffff;
            opacity: 0;
            -webkit-transform: scale(.8);
            transform: scale(.8);
        }
    </style>

    <script>
    var cnt=1;
    
        $(function(){
            var form = $(".login-form");

            form.css({
                opacity: 1,
                "-webkit-transform": "scale(1)",
                "transform": "scale(1)",
                "-webkit-transition": ".5s",
                "transition": ".5s"
            });
        });
        
        
        $(document).ready(function() {
            $('.js-example-basic-single').select2();
        });
        
        function showDialog(){
        	//alert("1 : "+document.forms[0].loginDate.value);
        	if(document.forms[0].userName.value == ""){
        	/* document.getElementById("dialog").style.display="";
        	metroDialog.open('#dialog'); */
        	alert("Please Enter User Name");
        	return false;
        	}
        	else if(document.forms[0].division.value==""){
            	/* document.getElementById("dialog1").style.display="";
            	metroDialog.open('#dialog1'); */
            	alert("Please Select Division");
            	return false;
          	}
        	else if(document.forms[0].password.value==""){
            	/* document.getElementById("dialog2").style.display="";
            	metroDialog.open('#dialog2'); */
            	alert("Please Enter Password");
            	return false;
          	}
        	else if(document.forms[0].loginDate.value==""){
            	/* document.getElementById("dialog2").style.display="";
            	metroDialog.open('#dialog2'); */
            	alert("Please Enter Date");
            	return false;
          	}
        	else{	
        		/* var date=document.forms[0].loginDate.value;
        		document.forms[0].loginMth.value=date.split("-")[0];
        		document.forms[0].loginyr.value=date.split("-")[1]; */
        		//debugger;        		
        		if(cnt==1)
        		{
        		    document.forms[0].password.value=hex_hmac_md5(document.forms[0].password.value, document.forms[0].challenge.value);
        		    //alert(document.forms[0].password.value);
                    cnt++;
                }
        		return true;
        	}
        }
        var date = new Date();
        $(function() {
            $('.date-picker').datepicker( {
            changeMonth: true,
            changeYear: true,
            showButtonPanel: true,
            dateFormat: 'dd-mm-yy',
            maxDate: new Date(),
            /* onClose: function(dateText, inst) { 
                $(this).datepicker('setDate', new Date(inst.selectedYear, inst.selectedMonth, 1));
            } */
            });
        });
        
        function setDefaultDate(){
        	$('.date-picker').datepicker( "setDate", new Date());
        }
        /* function showNotify(typ,mes){		//var mes = 'Alert|Implement independently'; --type|message
            
            $.Notify({
                caption: mes.split("|")[0],
                content: mes.split("|")[1],
                type: typ
            });
        } */
    
        
    </script>
<style>
/* .ui-datepicker-calendar {
    display: none;
} */
</style>
</head>

<body onLoad="setDefaultDate();" class="bg-darkTeal" >
<div class="login-form padding20 block-shadow">
<!-- User Name="UserName" and password="password" -->
<form:form action="loginform.html"  commandName="loginForm">
<input type="hidden" id="loginyr" name="loginyr" />
<input type="hidden" id="loginMth" name="loginMth" />
<input type="hidden" value="$$CHALLENGE" name="challenge">

<c:if test="${not empty error}">
<p><font color="red" size="4%">${error}</font></p>
</c:if>

<h1 class="text-light">Login to E-VALUATE</h1>
            <hr class="thin"/>
            <br />
            
 <div class="input-control text full-size" data-role="input">
                <label for="user_login">User ID:</label> <%-- <FONT color="red"><form:errors	path="userName" /></FONT> --%>
                <input type="text" name="userName" id="userName"> 
                <button class="button helper-button clear"><span class="mif-cross"></span></button>
  </div>
            <br />
            <br /> 
           

 <label for="division">Division:</label>
 <div class="input-control full-size">
 <select class="js-example-basic-single" data-placeholder="Select a Division" style="width: 100%" data-allow-clear="true" data-minimum-results-for-search="Infinity" id="division" name="division">
<option value=""></option>
	<c:forEach items="${divMap}" var="div1">
  		<option value="${div1.key}" ${((loginForm.division!=null) && (loginForm.division == div1.key)) ? 'selected="selected"' : ''} >${div1.value}</option>
 	</c:forEach> 
</select>
</div>
<br />
<br /> 
    	
<div class="input-control password full-size" data-role="input">
                <label for="user_password">User password:</label>  <%-- <form:errors	path="password" /></FONT> --%>
                <input type="password" name="password" id="password"> 
                <button class="button helper-button reveal"><span class="mif-eye"></span></button>
 </div>
            <br />
            <br />
<div class="input-control text full-size" data-role="input">
	<label for="loginDate">Date :</label>
    <input name="loginDate" id="loginDate" class="date-picker" />
</div>            

	<div class="form-actions">
                <button type="submit" class="button primary" onclick="return showDialog()">Login</button>
                <button type="button" class="button primary" onclick="this.form.reset()" >Cancel</button> <!-- showNotify('alert','Alert|Enter Details') -->
            </div>
</form:form>
<!-- <div data-role="dialog" id="dialog" style="width:50px;display:none;" data-close-button="true" data-type="alert" data-width="350" >

    <h3>&nbsp;&nbsp;Alert!</h3>
    <p>
      	&nbsp;&nbsp; Please Enter User Name</p>
</div>
<div data-role="dialog" id="dialog1" style="width:50px;display:none;" data-close-button="true" data-type="alert" data-width="350" >

    <h3>&nbsp;&nbsp;Alert!</h3>
    <p>&nbsp;&nbsp;  Please Select a division</p>
</div>
<div data-role="dialog" id="dialog2" style="width:50px;display:none;" data-close-button="true" data-type="alert" data-width="350" >

    <h3>&nbsp;&nbsp;Alert!</h3>
    <p>&nbsp;&nbsp; Please Enter Password</p>
</div> -->
</div>
</body>
