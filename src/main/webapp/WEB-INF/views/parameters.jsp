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


<%-- <link rel='shortcut icon' type='image/x-icon' href='../favicon.ico' />
<script src="<c:url value="/resources/js/jquery-3.1.0.js" />"></script>
 <script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.dataTables.min-1.10.18.js" />"></script>
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/select2.min.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<link href="<c:url value="/resources/css/normalize.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/dataTable.rowReorder.js" />"></script>
<script src="<c:url value="/resources/js/common.js" />"></script> --%>
<%-- <link href="<c:url value="/resources/css/bootstrap.min2.css" />" rel="stylesheet"> --%>
<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<!-- header : end-->
<script src="<c:url value="/resources/js/jquery.dataTables.min2.js" />"></script>
<script src="<c:url value="/resources/js/dataTables.bootstrap.js" />"></script>
<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />" rel="stylesheet"> <!-- used bootstrap-3.3.5.css instead of bootstrap.min2.css, commented .hidden in bootstrap-3.3.5.css which was causing issue in navbar  -->
<link href="<c:url value="/resources/css/dataTables.bootstrap.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/jquery.dataTables.min-1.10.18.js" />"></script>
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/select2.min.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<script src="<c:url value="/resources/js/dataTable.rowReorder.js" />"></script>
<script src="<c:url value="/resources/js/common.js" />"></script>
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<style>

 .hide_column {
    display : none;
}
.setHeight{
 line-height: 10px !important;
}

#param_table tr{
    line-height: 10px !important;
}

#param_table td{
max-height:20px !important;
font-size: small;
}
#param_table td input,td select{
max-height:20px !important;
max-width:75px !important;
font-size: small;
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
  font-weight:bold;
  font-size: 15px;
  color:#ffffff;
}
.link:hover { color: #FFFFFF; }


</style>

</head>
<body style="background-color:#e5e5e5;" onload="hideColumn('param_table',1,'5');" >
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<br/><br/><br/><br/>
<%-- <div style="float: left;"><jsp:include page="sideBar.jsp"/></div>
 --%>

<!-- hideColumn('paramDiv',4,'0~3~4~5'); -->
<!-- margin-left:20%; -->

<div >
<form:form id="paramform" commandName="parameter" method="POST"	action="addUpdateParam">
<form:hidden path ="addEdit" value="${param.addEdit}"/>
<input type="hidden" value="${user.level}" name="lvl">
<fieldset>
<div class="container">
	<div class="col-lg-12 col-md-10 col-sm-6 col-xs-4">
<div class="row">
<div class="card">
            <div class="card-body">
					
					<select class="form-control " id="param_dropdown" name="param_dropdown" onchange="getParams(this)" width="50%" >
							<option value="">Select-Designation</option>
						<c:forEach items="${empLvlDescMap}" var="elvldescmap">
  		  					<option value="${elvldescmap.key}" ${((param.empLevel!=null) && (param.empLevel == elvldescmap.key)) ? 'selected="selected"' : ''} >${elvldescmap.value}</option>
 						</c:forEach>
					</select>
			</div>
			</div>
			</div>
			</div>
			</div>
			<br><br>
<%-- <div class="input-control">
<label for="division">Designation : </label>
 <select id="param_dropdown" class="js-example-basic-single" data-placeholder="Select Level For Which Param Is To Be Added" style="width: 350px" 
         data-allow-clear="true" data-minimum-results-for-search="Infinity" id="empLevel" name="empLevel" onchange="getParams(this)">
  	<c:forEach items="${empLvlDescMap}" var="elvldescmap">
  		<option value=""></option>
  		<option value="${elvldescmap.key}" ${((param.empLevel!=null) && (param.empLevel == elvldescmap.key)) ? 'selected="selected"' : ''} >${elvldescmap.value}</option>
 	</c:forEach> 
 </select>
</div> --%>
<%-- testing.... 
<p>${(param.addEdit == 'edit') ? "edit" : "no edit"}</p>
<p>${(isDelete == 'delete') ? "hiiii" : "nooooo"}</p> --%>

<c:choose>
<c:when test="${(param.addEdit == 'edit') || (param.addEdit == 'add') }">
	<div class="paramDiv" id="paramDiv">
</c:when>
<c:when test="${(isDelete ne null) && (isDelete eq 'delete')}">
	<div class="paramDiv" id="paramDiv">
</c:when>
<c:otherwise>
	<div class="paramDiv" id="paramDiv" style="display:none;">
</c:otherwise>
</c:choose>

<div id="paramtablediv" class="container">
	
    
        <div class="row">
		
         <div class="col-12">
          <div class="card">
            <div class="card-body">
            <div class="table-responsive">
<table id="param_table" class="table table-hover table-striped table-bordered" cellspacing="0" width="100%">
		<thead>
			<tr>
				<th class="hide_column">ID</th>
				<th>Parameters</th>
				<th>Weightage (%)</th>
				<th>Add Date</th>
				<th>Added By</th>
				<th>IsActive</th>
				<th>Edit</th>
				<!-- <th>Cancel</th> -->
			</tr>
		</thead>
		<tbody id="paramtablebody">
<!-- getting data through ajax in jquery function -->
	<%-- <c:if test = "${(param.AddEdit == 'edit')}"> --%>
		<c:forEach items="${paramList}" var="params">
         <tr >
           <td class="hide_column">${params.param_id}</td>
           <td  >${params.param_name}</td>
           <td  >${params.weightage}</td>
           <td  >${params.add_date}</td>
           <td  >${params.addedBy}</td>
           <td  >${params.isActive}</td>
           <td >
           <c:choose>
<c:when test="${user.level<7 }">
	NA
</c:when>
<c:otherwise>
<a class=link onclick='return editParam(this);'>Edit</a>&nbsp;&nbsp;
</c:otherwise>
</c:choose>
           
           <%-- <a onclick="return onClickDelete(this,${params.param_id});" href="">Delete</a> --%>
           </td>
         </tr>
      </c:forEach>
   <%--  </c:if> --%>
  </tbody> 
</table></div>
<!-- <div style="float:left;">
<table>
<tr>
<td><button class="button  bg-cyan bg-active-darkBlue fg-white " onclick="return onSubmitParameters();" type="submit" value="Update" />
<span class=" mif-cloud-upload "></span>&nbsp;Save</td>
<td> <button id="addParamBtn"  class="button  bg-cyan bg-active-darkBlue fg-white " onclick="return addRow();"><span class="mif-plus" style="font-size: 10px;"></span>
&nbsp;Add Parameter</button></td>
</tr>
</table>
</div> -->
</div></div></div></div>
</div>

<br />

</div>
</fieldset>

</form:form>
</div>
<div data-role="dialog" id="dialog" style="width: 50px; display: none;"
data-close-button="true" data-type="alert" data-width="350">
	<h3>&nbsp;&nbsp;Alert!</h3>
	<p>&nbsp;&nbsp; Total Weightage Must Be 100%</p>
</div>
<div data-role="dialog" id="dialog1"
	style="width: 50px; display: none;" data-close-button="true"
	data-type="alert" data-width="350">
	<h3>&nbsp;&nbsp;Alert!</h3>
	<p>&nbsp;&nbsp; Parameter name or Weightage Cannot Be Empty</p>
</div>
<div data-role="dialog" id="dialog2"
	style="width: 50px; display: none;" data-close-button="true"
	data-type="alert" data-width="350">
	<h3>&nbsp;&nbsp;Alert!</h3>
	<p>
		&nbsp;&nbsp; Please <strong>Save</strong> the Existing Changes
	</p>
</div>
<div data-role="dialog" id="dialog3"
	style="width: 50px; display: none;" data-close-button="true"
		data-width="350">
		<h3>&nbsp;&nbsp;Info!</h3>
		<p>&nbsp;&nbsp; All Changes Are Up To date</p>
	</div>
	
</body>
<script type="text/javascript">

$(function() {
    $('#param_dropdown').change(function(){
        if($('#param_dropdown').val() == '1') {
        	$('#paramtablediv').show();
        } else if($('#param_dropdown').val() == '2') {
            $('#paramtablediv').show();
        } else {
            $('#paramtablediv').hide();
        } 
    });
});
$(document).ready(function() {
    $('#param_table').DataTable();
} );


/* 
$(document).ready(function() {
    $('.js-example-basic-single').select2();
    $("#param_id").select2({ width: 'resolve' }); 
    
}); */ 
    	/* document.getElementById("addParamBtn").style.display="none"; */
  


function addParamValidate(){
	debugger;
	if (typeof document.forms[0].param_name !== 'undefined' )
		{   //debugger;
			document.getElementById("dialog2").style.display="";
	    	metroDialog.open('#dialog2');
			return false;
		}
	return true;
}

function onloadParameters(){
	document.forms[0].action="/parameters.action";
}
function editParam(element){
	if(addParamValidate()){
		debugger;
    var table = $('#param_table').DataTable;

  //  $("#param_table").on('mousedown.edit', "a.link", function(e) {
     
      var $row = $(element).closest("tr").off("mousedown");
      var $tds = $row.find("td");
      $.each($tds, function(i, el) {
    	  
    	  if(i==0){
    		  var txt = $(this).text();
    	        $(this).html("").append("<div class='input-control text'><input type='text'  name='param_id' readonly='readonly' value=\""+txt+"\" >");
    	  }  	  
    	  if(i==1){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' id='param_nameAdd' name='param_name' onkeypress='return alphaNumericSpaceHyphen(event)' value=\""+txt+"\" style='max-width:115px !important;'>");
    	  }
    	  if(i==2){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' id='weightageAdd' name='weightage' onkeypress='return allowNumbersAndDecimalOnly(event)' value=\""+txt+"\">");
    	  }
    	  if(i==5){
    	        $(this).html("").append("<a onclick=\"submitForm('edit')\">Save</a>&nbsp;/&nbsp;<a href='parameters.action'  id='cancelLink'>Cancel</a>"); //onclick='return cancelParam(this)'
    	  }
      });

    //});
}
	return false;
}

function cancelParam(element){
		debugger;
    var table = $('#param_table').DataTable;

  //  $("#param_table").on('mousedown.edit', "a.link", function(e) {
     
      var $row = $(element).closest("tr").off("mousedown");
      var $tds = $row.find("td");
      $.each($tds, function(i, el) {
    	  
    	  if(i==0){
    		  var txt = document.forms[0].param_id.value;
    	        $(this).html("").append(txt);
    	  }  	  
    	  if(i==1){
    		  var txt = document.forms[0].param_name.value;
    	        $(this).html("").append(txt);
    	  }
    	  if(i==2){
    		  var txt = document.forms[0].weightage.value;
    		  $(this).html("").append(txt);
    	  }
    	  if(i==5){
    	        $(this).html("").append("<a class=link onclick='return editParam(this);'>Edit</a>&nbsp;&nbsp;"); //onclick=\"submitForm('edit');
    	  }
      });

    //});
	return false;
}

function addRow(){
	debugger;
if(addParamValidate()){
	//var table = document.getElementById('param_table');
	//var totRows =table.rows.length;
	//$('#param_table').DataTable();
  var table = $('#param_table').DataTable();
 // var $row = table.addClass("setHeight");
  var rowNode = table.row.add(['','<input type="text"  id="param_nameAdd" name="param_name" onkeypress="return alphaNumericSpaceHyphen(event)" style="max-width:150px !important;">',
                               '<input type="text" id="weightageAdd" name="weightage" onkeypress="return allowNumbersAndDecimalOnly(event)" >','','','',
                               '<a onclick=submitForm("add")>Save</a>&nbsp;/&nbsp;<a  href="parameters.action"  id="cancelLink">Cancel</a>' //onclick="onClickCancel()" 
                               ]).draw().node();
  $(rowNode).addClass("setHeight");
  $(rowNode).find('td:eq(0)').addClass( 'hide_column' );
}              
  // table.row.add([first_td_html_of_tr,second_td-html_of_tr,third_td_html_of_tr,...nth td_html_of_tr]).draw();
 return false;
}

function totWeightage() {
	var table = document.getElementById('param_table');
	var totRows =table.rows.length-1;
	//alert(totRows)
	var sum=0;
	for(var i=1;i<=totRows;i++){
	sum=sum+parseFloat(document.getElementById("weight"+i).value);	
	}
	if(sum!=100){
		document.getElementById("dialog").style.display="";
    	metroDialog.open('#dialog');
		return false
	}else{
	return true;
	}
}

function onSubmitParameters(){
  if (typeof document.forms[0].param_name === 'undefined' && typeof document.forms[0].weightage === 'undefined' )
	{
	  document.getElementById("dialog3").style.display="";
  		metroDialog.open('#dialog3');
		return false;
	}else{
		if(document.forms[0].param_name.value=="" || document.forms[0].weightage.value=="" ){
		document.getElementById("dialog1").style.display="";
		metroDialog.open('#dialog1');
		return false;
	}
return true;
}
}
function submitForm(isAddEdit){
debugger;

var valFlag=false;
var AllValuesFlag=false;
var NumericFlag=false;
var reg = /^(\d*\.)?\d+$/;
	if(isAddEdit=="edit")
	{
		document.forms[0].addEdit.value=isAddEdit;
	}
	else if(isAddEdit=="add")
	{
		document.forms[0].addEdit.value=isAddEdit;
	}
	else return false;
	
	if(document.getElementById("param_nameAdd").value=="" || document.getElementById("weightageAdd").value=="" ){
		valFlag=true;
		AllValuesFlag=true;
		alert("Please fill up the required details before saving");
	}
	
	if(!AllValuesFlag)
		 if(!reg.test(document.getElementById("weightageAdd").value)){
			valFlag=true;
			NumericFlag=true;
			 alert("Please enter numeric values for Weightage");
		} 
	
	if(!valFlag){
	document.forms[0].action="addUpdateParam.action";
	document.forms[0].submit();
	return true;
	}
	else
		return false;
	
}
function deleteParam(){
	var result = confirm("Want to delete?");
	if (result) {
	    return true;
	}
}

/*below code is written to get all params on change of dropdown.*/
function getParams(obj)
{
	debugger;
	/* document.getElementById("addParamBtn").style.display=""; */
$('#param_table').DataTable().clear();
$.ajax({
url : 'getparameters',
cache: false,
data: 'empLevel='+obj.value,
type:'GET',	
dataType: 'json',
success : function(obj) 
{
var table = $('#param_table').DataTable();
$('#param_table').DataTable().state.clear();
//table.clear();
if(obj!=null){
$.each(obj, function(){//"return confirm(Are You Sure You Want To Delete?); href='deleteParam.action?param_id=${parameter.param_id}'
	var rowNode;
	if(document.forms[0].lvl.value<7){
		rowNode = table.row.add([this.param_id,this.param_name,this.weightage,this.add_date,this.addedBy,this.isActive,
		                             "NA"]).draw().node();
    }else{
    	rowNode = table.row.add([this.param_id,this.param_name,this.weightage,this.add_date,this.addedBy,this.isActive,
	                             "<a class=link onclick='return editParam(this);'>Edit</a>&nbsp;&nbsp;"
	                             //'<a href="parameters.action">Cancel</a>'
	                             ]).draw().node();
    }
		

$(rowNode).find('td:eq(0)').addClass( 'hide_column' );
});
}else{
	$('#param_table').DataTable().clear().draw();
	alert("No Data found For Selected Criteria");
}
hideColumn('param_table',1,'5');
},error:function (xhr, ajaxOptions, thrownError){
    alert(xhr.status);
    alert(thrownError);
}
});
//alert(document.getElementById("paramDiv").style.display); 
document.getElementById("paramDiv").style.display="";
}
function onClickDelete(obj,paramid)
{
	debugger;
	if(confirm("Are You Sure You Want To Delete?"))
	{
		obj.setAttribute("href", "deleteParam.action?param_id="+paramid+"&empLevel="+document.forms[0].empLevel.value);
	} 
	else{
		return false;
	}
}
/* function onClickCancel(){
	debugger;
	/* $(".cancelLink").on("click", function(){ */
		//  $('#param_table').DataTable().state.clear();
		/* }); */
/*}
$(document).ready(function() {
    $('#example').DataTable( {
        "paging":   true,
        "ordering": true,
        "info":     false,
        stateSave: true
    } );
} ); */
</script>

</html>