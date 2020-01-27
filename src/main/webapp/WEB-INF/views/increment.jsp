
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel='shortcut icon' type='image/x-icon' href='../favicon.ico' />
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
<script src="<c:url value="/resources/js/common.js" />"></script>
<title>Know Your Increment</title>

<script>
function editIncrement(element){
	if(preValidate(3,document.forms[0].incr_desc,document.forms[0].max_score,document.forms[0].min_score)){
		
    var table = $('#incr_table').DataTable;

  //  $("#param_table").on('mousedown.edit', "a.link", function(e) {
     
      var $row = $(element).closest("tr").off("mousedown");
      var $tds = $row.find("td");
      $.each($tds, function(i, el) {
    	  
    	  if(i==0){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text'  name='incr_id' readonly='readonly' value=\""+txt+"\">");
    	  }   	  
      
    	  if(i==1){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' style='width:150px' name='incr_desc' onkeypress='return alphaNumericSpaceHyphen(event)' value=\""+txt+"\">");
    	  }
    	  if(i==2){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' name='min_score' onkeypress='return allowNumbersAndDecimalOnly(event)'  value=\""+txt+"\">");
    	  }
    	  if(i==3){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' name='max_score' onkeypress='return allowNumbersAndDecimalOnly(event)' value=\""+txt+"\">");
    	  }
    	  if(i==4){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' name='rating' onkeypress='return onlyAlphabets(event,this)' maxlength='2'  value=\""+txt+"\">");
    	  }
    	  if(i==5){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' name='incr_amount' onkeypress='return allowNumbersAndDecimalOnly(event)' value=\""+txt+"\">");
    	  }
    	  if(i==6){
    	        $(this).html("").append("<a onclick='return submitForm();'>Save</a>&nbsp;/&nbsp;<a href='showincrement.action'>Cancel</a>");
    	  }
      });

    //});
}
	return false;
}
function submitForm(){
	document.getElementById("incrementform").submit();
	return true;	
}
function deleteRow(val){
	//alert(val)
	//document.getElementById("slab_table").deleteRow(val);
	var row = document.getElementById('row1');
	row.parentElement.removeChild(row);
	return false;
	/* var row = document.getElementById('row1');
	row.parentElement.removeChild(row); */
}
function addRow(){
if(preValidate(3,document.forms[0].incr_desc,document.forms[0].max_score,document.forms[0].min_score)){
$('#incr_table').DataTable();
var table = $('#incr_table').DataTable();
var rowNode= table.row.add(['','<input type="text"  id="incr_descAdd" name="incr_desc" style="width:150px" >',
           '<input type="text" id="min_scoreAdd" name="min_score" onkeypress="return allowNumbersAndDecimalOnly(event)" >',
           '<input type="text"  id="max_scoreAdd" name="max_score" onkeypress="return allowNumbersAndDecimalOnly(event)">',
           '<input type="text" id="ratingAdd" name="rating" onkeypress="return onlyAlphabets(event,this)" maxlength="2"   >',
           '<input type="text"  id="incr_amountAdd" name="incr_amount"  onkeypress="return allowNumbersAndDecimalOnly(event)">',
          
           '<a onclick="return onSubmitIncrement()">Save</a>&nbsp;/&nbsp;<a href="showincrement.action"  id="cancelLink">Cancel</a>'
           ]).draw().node();
	} 
// table.row.add([first_td_html_of_tr,second_td-html_of_tr,third_td_html_of_tr,...nth td_html_of_tr]).draw();
return false;	
}

function onSubmitIncrement(){
	
	var valFlag=false;
	var AllValuesFlag=false;
	var NumericFlag=false;
	var RangeFlag=false;
	var reg = /^(\d*\.)?\d+$/;
	
	if(document.getElementById("incr_descAdd").value=="" 
			|| document.getElementById("min_scoreAdd").value==""
			|| document.getElementById("max_scoreAdd").value==""
			|| document.getElementById("ratingAdd").value==""
			|| document.getElementById("incr_amountAdd").value==""){
		
		valFlag=true;
		AllValuesFlag=true;
		alert("Please fill up the required details before saving");
	}
	
	if(!AllValuesFlag)
		 if(!reg.test(document.getElementById("min_scoreAdd").value) 
				||!reg.test(document.getElementById("max_scoreAdd").value)
				||!reg.test(document.getElementById("incr_amountAdd").value)){
			valFlag=true;
			NumericFlag=true;
			 alert("Please enter numeric values for Min Score, Max Score, Amount");
		} 
	
	 if(!NumericFlag && !AllValuesFlag)
		 if(parseFloat(document.getElementById("min_scoreAdd")).value!=null && parseFloat(document.getElementById("max_scoreAdd")).value!=null && (parseFloat(document.getElementById("min_scoreAdd")).value >= parseFloat(document.getElementById("max_scoreAdd")).value)){
				valFlag=true;
				RangeFlag=true;
				 alert("Min Score should be less than Max Score");
			} 
	
	if(!valFlag){
		
		// call action
		alert("Call Save Action");
		document.forms[0].action="addUpdateincrement.action";
		document.forms[0].submit();
		return true;
	}else{
		return false;
	}
	
	/* if (typeof document.forms[0].incr_desc === 'undefined' && typeof document.forms[0].min_score === 'undefined' )
	{
	  document.getElementById("dialog3").style.display="";
  		metroDialog.open('#dialog3');
		return false;
	} else{
		if(document.forms[0].incr_desc.value=="" || document.forms[0].min_score.value=="" 
			&& document.forms[0].max_score.value=="" || document.forms[0].rating.value=="" || document.forms[0].incr_amount.value==""){
			document.getElementById("dialog1").style.display="";
			metroDialog.open('#dialog1');
			return false;
		}
	} */
	
}

function hideForPSR(){
	alert("level: "+document.getElementById('level').value);
	if(document.getElementById("level").value=="1" || document.getElementById("level").value=="2")
		{
			hideColumn('incr_table',1,'5~6');
			document.getElementById("addUpdate").style.display="none";
		}
	
}

function preValidate(no,field1,field2,field3){
	
if(no==1)
{
	if(typeof field1!=='undefined')
		{
		document.getElementById("dialog2").style.display="";
    	metroDialog.open('#dialog2');
		return false;
		}
}else if(no==2){
	if(typeof field1!=='undefined' && typeof field2!=='undefined'){
		document.getElementById("dialog2").style.display="";
    	metroDialog.open('#dialog2');
    	return false;	
	}
}else if(no==3){
	if(typeof field1!=='undefined' && typeof field2!=='undefined' && typeof field3!=='undefined'){
		document.getElementById("dialog2").style.display="";
    	metroDialog.open('#dialog2');
    	return false;
	}
}
	return true;
}
</script>
<style>
input{
	 width:50px;
 }
 
.bg-steel {
    background-color: #d2d4d6 !important;
}
.bg-white {
    background-color: #e5e5e5 !important;
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
.card-title {
    margin-bottom: .75rem;
}
.card .card-title {
    position: relative;
    font-weight: 500;
}
/*input, select, textarea { 
color: red; 
}*/

.table-hover tbody tr:hover td, .table-hover tbody tr:hover th {
  
  background-color: #1ba1e2;
  /* font-weight:bold;
  font-size: 15px; */
  color:#ffffff;
}

</style>
</head>
<body class="bg-white">
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<div style=" margin-top:5%;">
	<form:form id="incrementform" commandName="increment" method="POST" action="addUpdateincrement">
<!--  this page will just get data min Increment table and show on the screen 
get level from session													  -->
<input type="hidden" name="level" id="level" value="4">

<fieldset>

	<br>
	<div class="container" >
	
    
        <div class="row">
		
         <div class="col-12">
          <div class="card">
            <div class="card-body">
            <h4 class="card-title">Following Table Shows The Increment Based On The
		Calculated E-Valuate :</h4><br>
<table id="incr_table" class="table table-hover table-striped table-bordered" cellspacing="0" width="100%">
	
<!-- <table id="example" class="display dataTable" cellspacing="0" width="100%" role="grid" aria-describedby="example_info" style="width: 100%;"> -->
<thead>
	<tr>
		<th>Increment ID</th>
		<th>Description</th>
		<th>Min (Score)</th>
		<th>Max (Score)</th>
		<th>Rating</th>
		<th>Amount (Rs.)</th>
		<th>Edit/Delete</th>
	</tr>
</thead>

<tbody>
	<c:forEach items="${incrList}" var="incr">
<tr>
	<%-- <td width="60" align="center">${incr.param_id}</td> --%>
<td >${incr.incr_id}</td>
<td>${incr.incr_desc}</td>
<td>${incr.min_score}</td>
<td>${incr.max_score}</td>
<td>${incr.rating}</td>
<td>${incr.incr_amount}</td>
<td><a class="link" onclick="return editIncrement(this);">Edit</a>&nbsp;/&nbsp;
<a onclick="return confirm('Are You Sure You Want To Delete?')" href="deleteincrement.action?incr_id=${incr.incr_id}">Delete</a>
</tr>
</c:forEach>
			</tbody>
		</table><br>
		<table id="addUpdate" >
<tr>
<!-- <td ><button class="button  bg-cyan bg-active-darkBlue fg-white " onclick="return onSubmitIncrement();" type="submit" value="Save" /><span class=" mif-cloud-upload "></span>&nbsp;Save</td> -->
<td><button style="margin-left: 10px;" class="button   bg-cyan bg-active-darkBlue fg-white " onclick="return addRow();"><span class=" mif-plus" style="font-size: 10px;"></span>&nbsp;Add Entry</button></td>
</tr>
</table>
	</div>
</div></div></div></div><br><br>
</fieldset>


<div data-role="dialog" id="dialog1"
	style="width: 50px; display: none;" data-close-button="true"
	data-type="alert" data-width="350">

	<h3>&nbsp;&nbsp;Alert!</h3>
	<p>&nbsp;&nbsp; Please Fill All The Fields</p>
</div>
<div data-role="dialog" id="dialog2" style="width:50px;display:none;" data-close-button="true" data-type="alert" data-width="350" >

    <h3>&nbsp;&nbsp;Alert!</h3>
    <p>&nbsp;&nbsp; Please <strong>Save</strong> the Existing Changes </p>
</div> 
<div data-role="dialog" id="dialog3" style="width:50px;display:none;" data-close-button="true"  data-width="350" >
    <h3>&nbsp;&nbsp;Info!</h3>
    <p>&nbsp;&nbsp; All Changes Are Up To date</p>
</div>
</form:form>
</div>
</body>
<script type="text/javascript">
	
	$(document).ready(function() {
	    $('#incr_table').dataTable();
	} );
	</script>
</html>
