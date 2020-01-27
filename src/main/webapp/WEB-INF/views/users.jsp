<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
 "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
   
<title>Users</title>
<%-- 
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<link href="<c:url value="/resources/css/normalize.css" />" rel="stylesheet"> --%>

<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />" rel="stylesheet"> <!-- used bootstrap-3.3.5.css instead of bootstrap.min2.css, commented .hidden in bootstrap-3.3.5.css which was causing issue in navbar  -->
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<!-- header : end-->

<script src="<c:url value="/resources/js/jquery.dataTables.min2.js" />"></script>
<script src="<c:url value="/resources/js/dataTables.bootstrap.js" />"></script>
<%-- <link href="<c:url value="/resources/css/bootstrap.min2.css" />" rel="stylesheet"> --%>
<link href="<c:url value="/resources/css/dataTables.bootstrap.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<script>

/* $(document).ready(function() {alert(1)
    $('.js-example-basic-single').select2();
    $("#status1").select2({ width: 'resolve' }); 
   
}); */
//if checkbox is unchecked for a month then all prev month's checkboxes should also be unchecked
function check_uncheck(val){
	if (!document.getElementById('check'+val).checked) 
	  {
		for(var i=val;i>=1;i--){
		document.getElementById("check"+i).checked = false;
		}
	  } 
	return false;
}

function onSubmitLockMaster(){
	debugger;
	var allcheckflag=0;
	totChkbox=document.getElementsByName("lockcheck").length;
	for(var i=1;i<=totChkbox;i++){
		if(!document.getElementById('check'+i).checked){
			allcheckflag=1;
			break;
		}	
	}
	if(allcheckflag===0){
		document.getElementById("dialog1").style.display="";
		metroDialog.open('#dialog1');
		return false;
	}else{
		return true;
	}
}
var mywindow=null;
function open1(){
	/* $("#divdeps").dialog({
	    autoOpen: false,
	    show: 'slide',
	    resizable: false,
	    position: 'center',
	    stack: true,
	    height: 'auto',
	    width: 'auto',
	    modal: true
	});
	
	$('#divdeps').dialog('open');
	
	window.open("http://localhost:8086/EISWAP/slabs.html",null,	"height=200,width=400,status=yes,toolbar=no,menubar=no,location=no");
	 */
	
        width = 400;
        height = 200;
        mywindow = window.open("http://localhost:8086/EISWAP/slabs.html", "Title",
            "location=0,status=1,scrollbars=1,resizable=1,menubar=0,toolbar=no,width="
                        + width + ",height=" + height);
        mywindow.moveTo(0, 0);
        mywindow.focus();
}
function parent_disable() {
	if(mywindow && !mywindow.closed)
		mywindow.focus();
	}
	
var newwindow;	
function addRow(){
	 debugger;
	 
	 
	 
	 if (typeof document.forms[0].ename !== 'undefined' )
		{   //debugger; 
		
			alert("Please Submit Already selected Values !");
			return false;
		}
	 else{ 
	 var arr=[];
	 var values = [];
	 $("input[name='hidEcode']").each(function() {
	     values.push("'"+$(this).val()+"'");
	 });
var url='http://localhost:8087/EISWAP/adminUsers.html?presentEcodes='+values;

var newwindow=window.open(url,'name','height=450,width=500,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=No,status=yes');

if (window.focus) {newwindow.focus()}

	 }

	return false; 
	
	
	
	
	}	

function a1(){
	debugger;
	//alert("arrLen: "+document.getElementById("arrLength").value);
	var valFlag=false;
	 var table = $('#user_table').DataTable();
	// alert("table length : "+$('#user_table').DataTable().data().count());
	var data = table.rows().data();
	var counter=0;
	 data.each(function (value, index) {
		 counter++;
		 console.log('Data in index: ' + index + ' is: ' + value);

	 }); 
	 for(var i=counter;i>=0;i--){
		 /* if(value[3].indexOf('select')>=0 || value[4].indexOf('select')>=0){ */
			 if(i<document.getElementById("arrLength").value)
		 if(document.getElementById("role"+i).value=="" || document.getElementById("status"+i).value==""){
			 valFlag=true;
		 }
	// }
	 }
	// alert("check role: "+document.getElementById("role").value);
	 if(!valFlag){
	document.forms[0].addEdit.value='add';
	document.forms[0].action="addUsers.action";
	document.forms[0].submit();
	return true;
	 } else{
		 alert("Please fill up the required details before saving");
		 return false;
}	
		/* 	var valFlag=false;
	var table = $('#user_table').DataTable();
	var data = table.rows().data();
	 data.each(function (value, index) {
	     if(value==null || value==""){
	    	 valFlag=true;
	    	 break;
	     }
	 });
	 if(!valFlag){
		 document.forms[0].addEdit.value='add';
			document.forms[0].action="addUsers.action";
			document.forms[0].submit();
			return true;
	 } else{
		 alert("Please fill up the required details");
		 return false;
	 } */
}
function showArr(){
	debugger;
	var arr=[];
	arr=(document.forms[0].arr.value).split(",");
	document.getElementById("arrLength").value=arr.length;
	var newusers=[2];
	
	
	/* Edited by Shivani */
	$('#user_table').DataTable().page.len(5000000000).draw();
	var table = $('#user_table').DataTable();
	
 	var data = table.rows().data();
 
	 data.each(function (value, index) {
	     document.getElementById("EditRow"+value[0]).style.display="none";
	 }) 
	/* Edited by Shivani */
	
	
	
	
	for(var i=0;i<arr.length;i++)
	{ //<input type="text" id="role" name="users[0].role" > ,<input type="text" id="userStatus" name="users[0].userStatus" >',
		newusers=arr[i].split('~');
var rowNode = table.row.add([
                             '<input type="text" id="ecode" name="users['+i+'].ecode" value='+newusers[0]+'>' ,
                               '<input type="text" id="ename" name="users['+i+'].ename" value='+newusers[1]+'>',
					          ' <select id="role'+i+'" class="js-example-basic-single" data-placeholder="Select User Privilege" style="width:90%;"'+ 
					           ' data-allow-clear="true" data-minimum-results-for-search="Infinity" id="role1" name="users['+i+'].role" >'+  
					            ' <option value=""></option>'+
					    		' <option value="1" >All</option>'+
					    		' <option value="2" >Reports only</option>'+
					    		' <option value="3" >WAP Process,Reports,Upload</option>'+
					    		' <option value="4" >Upload,Reports</option>'+
					    		' </select>',
'<select id="status'+i+'" class="js-example-basic-single" data-placeholder="Select Status" style="width:90%;"'+ 
        'data-allow-clear="true" data-minimum-results-for-search="Infinity" id="userStatus" name="users['+i+'].userStatus" >'+
  		'<option value=""></option>'+
  		'<option value="1" >ACTIVE</option>'+
  		'<option value="0" >INACTIVE</option>'+
		'</select>',
        '',
        '<a onclick="return editRow(this);">Cancel</a>',
        '<input type="hidden"  name="hidEcode" value='+newusers[0]+' >'
	     ]).page.len(10).draw().node();
	     
/* page.len(10) -- Edited by Shivani */ 
	}
	return false;
}

function editRow(element){
	debugger;
	var table = $('#user_table').DataTable;
    var $row = $(element).closest("tr").off("mousedown");
    var $tds = $row.find("td");
    $.each($tds, function(i, el) 
    {
    	
    	  if(i==0)
    	  {
    		  var txt = $(this).text();
    	        $(this).html("").append("<div class='input-control text'><input type='text' id='ecode' name='users[0].ecode'  readonly='readonly' value=\""+txt+"\">");
    	  }   	  
    	  if(i==2)
    	  {
    		  var txt = $(this).text();
    	        $(this).html("").append('<select class="js-example-basic-single" data-placeholder="Select User Privilege" style="width:90%;"'+ 
		           ' data-allow-clear="true" data-minimum-results-for-search="Infinity" id="role" name="users[0].role"  value="'+txt+'">'+  
		            ' <option value=""></option>'+
		    		' <option value="1" '+((txt=="1")   ? 'selected="selected"' : '')+'>All</option>'+
		    		' <option value="2" '+((txt=="2") ? 'selected="selected"' : '')+'>Reports only</option>'+
		    		' <option value="3" '+((txt=="3") ? 'selected="selected"' : '')+'>WAP Process,Reports,Upload</option>'+
		    		' <option value="4" '+((txt=="4") ? 'selected="selected"' : '')+'>Upload,Reports</option>'+
		    		' </select>');
    	  }
    	  if(i==3)
    	  {
    		  var txt = $(this).text();
    	        $(this).html("").append('<select class="js-example-basic-single" data-placeholder="Select Status" style="width:90%;"'+ 
    	        'data-allow-clear="true" data-minimum-results-for-search="Infinity" id="userStatus" name="users[0].userStatus" value="'+txt+'" >'+
    	  		'<option value=""></option>'+
    	  		'<option value="1" '+((txt=="1") ? 'selected="selected"' : '')+'>ACTIVE</option>'+
    	  		'<option value="0" '+((txt=="0") ? 'selected="selected"' : '')+'>INACTIVE</option>'+
    			'</select>');
    	  }
    	  if(i==5)
    	  {
    		  var txt = $(this).text();
    		  $(this).html("").append("<a onclick='return submitForm1(\"edit\")'>Save</a>&nbsp;/&nbsp;<a href='parameters.action'  id='cancelLink'>Cancel</a>");
    	  }
      });
	return false;
}
function submitForm1(isAddEdit)
{
document.forms[0].action="editUser.action";
document.forms[0].submit();
return true;
}

</script>

<style>

.bg-steel {
    background-color: #d2d4d6 !important;
}
.bg-white {
    background-color: #e5e5e5 !important;
}

 .hide_column {
    display : none;
}
#user_table tr{
    line-height: 10px !important;
}

#user_table td{
max-height:20px !important;
font-size: small;
}
#user_table td input,td select{
max-height:20px !important;
font-size: small;
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
.table-hover tbody tr:hover td select option {
	font-weight: normal;
  color: #000000;
}
.table-hover tbody tr:hover td select {
	font-weight: normal;
  color: #000000;
}
.card-title {
    margin-bottom: .75rem;
}
.card .card-title {
    position: relative;
    font-weight: 500;
}
.table-hover tbody tr:hover td, .table-hover tbody tr:hover th {
  
  background-color: #1ba1e2;
  font-weight:bold;
  font-size: 15px;
  color:#ffffff;
}


</style>

</head>
<body class="bg-white">
<div style="float: top;"><jsp:include page="header.jsp" /></div>


<div style=" margin-top:6%;">
<form:form name="userform" id="userform" commandName="userform" method="POST"  action="addUsers1">
<input type="hidden" id="addEdit" name="addEdit" />
<input type="hidden" id="arr" name="arr" />
<input type="hidden" id="arrLength" name="arrLength" />
<button value="Dummy"  id="dummy" onclick="return showArr();" style="display:none;">Dummy</button>
<fieldset>
<br/><br/>

<div id="cont" class="container">
	
    
        <div class="row">
		
         <div class="col-12">
          <div class="card">
            <div class="card-body">
            <h4 class="card-title">Tool Administrators</h4><br>
            <div class="table-responsive">
<table id="user_table" class="table table-hover table-striped table-bordered" cellspacing="0" width="100%">
            <!-- <table id="user_table" class="display dataTable striped border hovered cell-hovered  " cellspacing="0" width="100%" data-role="datatable" style="width:100%;"  data-searching="true" > -->
             <!-- <table id="example" class="display dataTable" cellspacing="0" width="100%" role="grid" aria-describedby="example_info" style="width: 100%;"> -->
                <thead>
                <tr>
               
                    <th>Login ID</th>
                    <th>Name</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Added By</th>
                    <th>Edit</th>
                     <th style="display:none"></th>
                </tr>
                </thead>
                <tbody>
<c:choose>
<c:when test="${(userform.addEdit == 'add')}" >
     <c:forEach items="${users}" var="user" varStatus="status">
		<tr>
		    
			<td>${user.ecode}</td>
			<td>${user.ename}</td>
			<td>${user.role}</td>
			<td>${user.userStatus}</td>
			<td>${user.addedBy}</td>
			
			<!-- Edited by Shivani -->
			<td ><a id="EditRow${user.ecode}" onclick="return editRow(this);">Edit</a></td>
			<!-- Edited by Shivani -->
			<td><input type="hidden" value="${user.ecode}" name="hidEcode"></td>
			
		</tr>
	</c:forEach>
	</c:when>
<c:otherwise>
     <c:forEach items="${userform.users}" var="user" varStatus="status">
		<tr>
		    
			<td>${user.ecode}</td>
			<td>${user.ename}</td>
			<td>${user.role}</td>
			<td>${user.userStatus}</td>
			<td>${user.addedBy}</td>
			
			<!-- Edited by Shivani -->
			<td ><a id="EditRow${user.ecode}" onclick="return editRow(this);">Edit</a></td>
			<!-- Edited by Shivani -->
			<td style="display:none"><input type="hidden" value="${user.ecode}" name="hidEcode"></td>
		</tr>
	</c:forEach>
</c:otherwise>
</c:choose>
   </tbody>
  </table></div>
  <br><table id="addUpdate">
 <tr>
 <td ><button id="adduserbtn" style="margin-left:10px;" class="button   bg-cyan bg-active-darkBlue fg-white" onclick="return addRow();">
 <span class=" mif-user-plus "></span>&nbsp;Add User</button></td>
 
 <td><button class="button  bg-cyan bg-active-darkBlue fg-white "  type="submit" value="Update"  onclick="return a1();"/>
<span class=" mif-cloud-upload "></span>&nbsp;Save</td>
 
 <!-- return onSubmitLockMaster(); -->
 </tr>
 <!--  onFocus="parent_disable();" -->
 </table> </div></div></div></div></div>
 </div>
  <br><br>              
<!-- //<div id="divdeps" style="display:none" title="test"><p>Testing...</p></div> -->               
 <!-- <a href="javascript:if (newwindow) newwindow.close()">Close</a> the popup. -->               

</fieldset>
 
</form:form>
 <div data-role="dialog" id="dialog1" style="width:50px;display:none;" data-close-button="true" data-type="alert" data-width="350" >

    <h3>&nbsp;&nbsp;Alert!</h3>
    <p>
      	&nbsp;&nbsp; WAP is already processed for all months</p>
</div>
</div>
</body>
	<script type="text/javascript">
	$(document).ready(function() {
	    $('#user_table').dataTable();
	    	    
	} );
		
	</script>
</html>
