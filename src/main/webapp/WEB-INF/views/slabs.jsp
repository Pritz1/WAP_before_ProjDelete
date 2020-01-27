<%@ page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
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
<%-- <script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script> --%>
<%-- <script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<link href="<c:url value="/resources/css/normalize.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/dataTable.rowReorder.js" />"></script>
<script src="<c:url value="/resources/js/common.js" />"></script> 
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>--%>

<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />" rel="stylesheet"> <!-- used bootstrap-3.3.5.css instead of bootstrap.min2.css, commented .hidden in bootstrap-3.3.5.css which was causing issue in navbar  -->
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/common.js" />"></script>
<!-- header : end-->

<script src="<c:url value="/resources/js/jquery.dataTables.min2.js" />"></script>
<script src="<c:url value="/resources/js/dataTables.bootstrap.js" />"></script>
<%-- <link href="<c:url value="/resources/css/bootstrap.min2.css" />" rel="stylesheet"> --%>
<link href="<c:url value="/resources/css/dataTables.bootstrap.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.dataTables.min-1.10.18.js" />"></script>
<script src="<c:url value="/resources/js/dataTable.rowReorder.js" />"></script>

<title>Slabs</title>
<script>

/* $(document).ready(function() {
    $('.js-example-basic-single').select2();
    $("#param_id").select2({ width: 'resolve' }); 
    
    document.getElementById("addSlabBtn").style.display="none";
    
    
}); */

function changetext(){
	//alert(val)
	var text=$("#paramId option:selected").text();
	document.getElementById("slabtext").innerHTML = "<i>"+text+"</i> Parameter is Divided Into Following Slabs :";
}
function onChangeParamId(){
	document.forms[0].action="listSlab.action?param_id="+document.forms[0].param_id.value;
	document.forms[0].submit();
}
function addRow(){
if(addSlabValidate()){
//var table = document.getElementById('slab_table');
//var totRows =table.rows.length;
//alert(totRows)
   var table = $('#slab_table').DataTable();
   var rowNode = table.row.add(['' ,'<input type="text" id="slabNameAdd" name="slabName" style="max-width:115px !important;">',
          '<input type="text" id="rangeMinAdd" name="rangeMin" onkeypress="return allowNumbersAndDecimalOnly(event)" >',
          '<input type="text" id="rangeMaxAdd" name="rangeMax" onkeypress="return allowNumbersAndDecimalOnly(event)">',
          '<input type="text" id="pointsAdd" name="points" onkeypress="return allowNumbersAndDecimalOnly(event)">','','',
          '<a onclick="return submitForm1(\'add\')">Save</a>&nbsp;/&nbsp;<a href="slabs.action"  id="cancelLink">Cancel</a>'
          ]).draw().node();
   $(rowNode).find('td:eq(0)').addClass( 'hide_column' );
   changetext();
}           
return false;	
}
function submitForm1(isAddEdit)
{
//Edited by Shivani for Validations


	var valFlag=false;
	var AllValuesFlag=false;
	var NumericFlag=false;
	var RangeFlag=false;
	var reg = /^(\d*\.)?\d+$/;
	
if(isAddEdit!="")
{
	document.forms[0].addEdit.value=isAddEdit;
}
else
	return false;
	
	if(document.getElementById("slabNameAdd").value==""||document.getElementById("rangeMinAdd").value=="" || 
			document.getElementById("rangeMaxAdd").value==""||document.getElementById("pointsAdd").value=="" ){
		valFlag=true;
		AllValuesFlag=true;
		 alert("Please fill up the required details before saving");
	}
	
	if(!AllValuesFlag)
 if(!reg.test(document.getElementById("rangeMinAdd").value) 
		||!reg.test(document.getElementById("rangeMaxAdd").value)
		||!reg.test(document.getElementById("pointsAdd").value)){
	valFlag=true;
	NumericFlag=true;
	 alert("Please enter numeric values for Min Range, Max Range, Points");
} 
 
 if(!NumericFlag && !AllValuesFlag)
 if((parseInt(document.getElementById("rangeMinAdd").value) >= parseInt(document.getElementById("rangeMaxAdd").value))){
		valFlag=true;
		RangeFlag=true;
		 alert("Min Range should be less than Max Range");
	} 
 
// check if same range values are entered
if(!NumericFlag && !AllValuesFlag && !RangeFlag){
	var table = $('#slab_table').DataTable();
	var data = table.rows().data();
	data.each(function (value, index) {
		 console.log('Data in index: ' + index + ' is: ' + value);
		 if(index>0){
			 if(value[2]==document.getElementById("rangeMinAdd").value && value[3]==document.getElementById("rangeMaxAdd").value){
				 valFlag=true;
				 alert("Range already Exists. Refer Slab name - "+value[1]);
				 //break;
			 }
			 
			 if(value[1]==document.getElementById("slabNameAdd").value && isAddEdit!='edit'){
				 valFlag=true;
				 alert("Slab name already exists");
			 }
			 
		 }
		 
		}); 
}

 
		
if(!valFlag){
	document.forms[0].action="addSlab.action";
	document.forms[0].submit();
	return true;
}	
else
	return false;
}
     
function onSubmitSlabs(){
	/* var table = document.getElementById('slab_table');
	var totRows =table.rows.length-1;
	alert(totRows)
	var sum=0;
	for(var i=1;i<=totRows;i++){
	if((document.getElementById("salesslab"+i).value=="") || (document.getElementById("salesfrom"+i).value=="")
	   || (document.getElementById("salesto"+i).value=="") || (document.getElementById("salespoints"+i).value=="")){
		document.getElementById("dialog1").style.display="";
		metroDialog.open('#dialog1');
		return false
	}
	} */		
}
function editRow(element){
	var table = $('#slab_table').DataTable;
      var $row = $(element).closest("tr").off("mousedown");
      var $tds = $row.find("td");
      $.each($tds, function(i, el) {
    	  
    	  if(i==0){
    		  var txt = $(this).text();
    	        $(this).html("").append("<div class='input-control text'><input type='text'  name='slabId' readonly='readonly' value=\""+txt+"\">");
    	  }   	  
      
    	  if(i==1){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' id='slabNameAdd' name='slabName' onkeypress='return alphaNumericSpaceHyphen(event)' value=\""+txt+"\" style='max-width:115px !important;'>");
    	  }
    	  if(i==2){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' id='rangeMinAdd' name='rangeMin' onkeypress='return allowNumbersAndDecimalOnly(event)' value=\""+txt+"\">");
    	  }
    	  if(i==3){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' id='rangeMaxAdd' name='rangeMax' onkeypress='return allowNumbersAndDecimalOnly(event)' value=\""+txt+"\">");
    	  }
    	  if(i==4){
    		  var txt = $(this).text();
    	        $(this).html("").append("<input type='text' id='pointsAdd' name='points' onkeypress='return allowNumbersAndDecimalOnly(event)'	 value=\""+txt+"\">");
    	  }
    	  if(i==7){
    	        $(this).html("").append("<a onclick='return submitForm1(\"edit\")'>Save</a>&nbsp;/&nbsp;<a href='slabs.action'  id='cancelLink'>Cancel</a>");
    	  }
      });
	    //});
	return false;
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
function addSlabValidate(){
	if (typeof document.forms[0].slabName !== 'undefined' )
		{   //debugger;
			document.getElementById("dialog2").style.display="";
	    	metroDialog.open('#dialog2');
			return false;
		}
	return true;
}
function onSubmitSales(){
	if (typeof document.forms[0].slabName === 'undefined' && typeof document.forms[0].rangeMin === 'undefined' )
	{
	  document.getElementById("dialog3").style.display="";
  		metroDialog.open('#dialog3');
		return false;
	} else{
		if(document.forms[0].slabName.value=="" || document.forms[0].rangeMin.value=="" 
				|| document.forms[0].rangeMax.value=="" || document.forms[0].points.value==""){
			document.getElementById("dialog1").style.display="";
			metroDialog.open('#dialog1');
			return false;
		}
	}
return true;
}
/* function submitForm(){
	document.getElementById("slabform").submit();
	return true;
} */

/*below code is written to get all params on change of dropdown.*/
function getParamsDropdown(obj)
{
    var empLevel = obj.value;
    //alert("empLevel : "+empLevel);
    if(typeof empLevel != 'undefined' &&  empLevel!=""){
    $.ajax({
        type: 'GET',
        url: "getParams",
        cache: false,
        data: 'empLevel='+obj.value,
        success: function(data){
        	var $select = $('#paramId');
        	var temp=$.parseJSON(data);
        	//alert(temp)
        	if(temp!=null){
            $select.find('option').remove(); 
            $select.append('<option value="">Select a Parameter</option>');
       		$.each(temp,function(key, value) 
       				{//alert("hi");
       				if(key == document.forms[0].selParam.value)
	        			$select.append('<option value=' + key + ' selected="selected">' + value + '</option>');
       				else
       					$select.append('<option value=' + key + ' >' + value + '</option>');
       				});
        }else{
        	alert("Data is Not defined For Selected Criteria");
        	$select.append('<option value="">No Params Defined</option>');
        }
        	if(document.forms[0].paramId.value!=""){
    			onSubmit1();
    		}	
        },
        error:function(){
            alert("error");
        }
    });
  document.getElementById("paramdiv").style.display="";
  //alert("document.forms[0].selParam.value : "+document.forms[0].selParam.value);
  //alert("document.forms[0].paramId.value : "+document.forms[0].paramId.value);
	}
    
}

function onSubmit1()
{
	//alert(1)
	debugger;
	var element = document.getElementById("addSlabBtn");
	if (typeof(element) != 'undefined' && element != null )
	{
		document.getElementById("addSlabBtn").style.display="";
	}
	
	
	if(document.forms[0].empLevel.value==""){
		alert("Please Select Designation");
		return false;
	}
	else if(document.forms[0].paramId.value==""){
		alert("Please Select Parameter");
		return false;
	}
	else{//TODO data table doesnt gets cleared if blank is returned from controller,it should show NO REC FOUND but it shows prev rec.
		//alert(2)
		$('#tablediv').show();
		$('#slab_table').DataTable().clear();
		$.ajax({
		url : 'getSlabs',
		cache: false,
		data: 'paramId='+document.forms[0].paramId.value,
		type:'GET',	
		dataType: 'json',
		success : function(obj) 
		{
			if(obj!=""){
				var table = $('#slab_table').DataTable();
				$('#slab_table').DataTable().clear().draw();
				$.each(obj, function(){//slabAddDate now used slabAddDt
					var rowNode;
					if(document.forms[0].lvl.value<7){
						rowNode = table.row.add([this.slabId,this.slabName,this.rangeMin,this.rangeMax,this.points,this.slabAddDate,this.addedBy, 
						                            'NA']).draw().node();
				    }else{
				    	rowNode = table.row.add([this.slabId,this.slabName,this.rangeMin,this.rangeMax,this.points,this.slabAddDate,this.addedBy, 
						                        '<a class="link" onclick="return editRow(this);">Edit</a>&nbsp;/&nbsp;<a onclick="return onClickDelete(this,'+this.paramId+','+this.slabId+',\'delete\');" href="">Delete</a>'
							       		       ]).draw().node();
				    }
				
				$(rowNode).find('td:eq(0)').addClass( 'hide_column' );
				});
				//hideColumn('param_table',1,'5');
			}else{
				//$('#slab_table').DataTable().destroy();
				/* var table = $('#slab_table').dataTable( {
					"oLanguage": {
					"sEmptyTable": "No data found."
					},
					"bDestroy":true
					}); */
				$('#slab_table').DataTable().clear().draw();
				alert("No Data found For Selected Criteria");
	
			}
		
		},error:function (xhr, ajaxOptions, thrownError){
		    alert(xhr.status);
		    alert(thrownError);
		}
		});
	document.getElementById("slabdiv").style.display="";
	changetext();
	 
	}
  return false;
}
function onClickDelete(obj,paramid,slabid,addEdit)
{
	debugger;
	if(confirm("Are You Sure You Want To Delete?"))
	{
		obj.setAttribute("href", "deleteSlab.action?paramId="+paramid+"&slabId="+slabid+"&addEdit="+addEdit+"&empLevel="+document.forms[0].empLevel.value);
	} 
	else{
		return false;
	}
}
function check(){
	if(document.forms[0].addEdit.value!='' && (document.forms[0].addEdit.value=='add' || document.forms[0].addEdit.value=='edit')){
		getParamsDropdown(document.forms[0].empLevel);
		//alert("document.forms[0].paramId.value : "+document.forms[0].paramId.value);
	}
}

</script>
<style>
/* .input-control input{
	 font-size:13px;
	 height:25px;
	 width:150px;
 } */
.hide_column {
    display : none;
}

#slab_table tr {
	line-height: 10px !important;
}

#slab_table td {
	max-height: 20px !important;
	font-size: small;
}

#slab_table td input, td select {
	max-height: 20px !important;
	max-width: 50px !important;
	font-size: small;
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
.table-hover tbody tr:hover td input {
	font-weight: normal;
  color: #000000;
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
.table-hover tbody tr:hover td, .table-hover tbody tr:hover th {
  background-color: #1ba1e2;
  font-weight:bold;
  font-size: 15px;
  color:#ffffff;
}
.card-title {
    margin-bottom: .75rem;
}
.card .card-title {
    position: relative;
    font-weight: 500;
}

</style>
</head>
<body onload="changetext();check();"  class="bg-white">
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<br/><br/><br/>

<div style=" margin-top:2%;">
	<!--  hideColumn('slab_table',4,'5~6~7~8');-->
	<form:form name="slabform" id="slabform" commandName="slabs"
		method="POST">
		<form:hidden path="addEdit" value="${slab.addEdit}" />
		<input type="hidden" value="${user.level}" name="lvl">
		<input type="hidden" value="${slab.paramId}" name="selParam">
		<fieldset>
			
					<div class="container">
	 <div class="col-lg-12 col-md-12 col-sm-10 col-xs-4"> 
<div class="row">
<div class="card">
            <div class="card-body">
            <div class="row">
            <div>
			<div class="col-md-4 col-sm-4">
              <div class="form-group">
					<select class="form-control " id="empLevel" name="empLevel" onchange="getParamsDropdown(this)" width="50%" >
							<option value="" >Select-Designation</option>
						<c:forEach items="${empLvlDescMap}" var="elvldescmap">
  		  					<option value="${elvldescmap.key}"
										${((slab.empLevel!=null) && (slab.empLevel == elvldescmap.key)) ? 'selected="selected"' : ''}>${elvldescmap.value}</option>
 						</c:forEach>
					</select>
			</div></div></div>

		<div  id="paramdiv" style="display: none;">
			<div class="col-md-4 col-sm-4">
              <div class="form-group"> 
						
						<select class="form-control " id="paramId" name="paramId"  >
							
						<c:if test="${(slab.addEdit == 'edit') || (slab.addEdit == 'add') || (slab.addEdit=='delete') }">
								<option value="">Select a Parameter</option>
								<c:forEach items="${paramMap}" var="params">
									<option value="${params.key}"	>${params.value}</option>
								</c:forEach>
							</c:if>
					</select>
			</div></div>

			<div class="col-md-2 col-sm-2">
              <div class="form-group">
						<button id="nextbtn" class="button  bg-cyan bg-active-darkBlue fg-white "
							onclick="return onSubmit1();" value="Save">Next</button>
						
				</div></div></div>
			</div>	
			</div>
			</div>
			</div>
			</div>
			</div>
			<%-- testingg... 
<p>${(slab.addEdit == 'edit') ? "edit" : "no edit"}</p>
<p>${(slab.addEdit == 'add') ? "add" : "no add"}</p>  --%>
<br/>
	
<div id="tablediv" class="container" >
	
    
        <div class="row">
		
         <div class="col-12">
          <div class="card">
            <div class="card-body">
            <h4 class="card-title"><div id="slabtext" ></div>
<br/><!-- <br/> -->
<c:choose>
<c:when test="${(slab.addEdit == 'edit') || (slab.addEdit == 'add') || (slab.addEdit=='delete') }">
<div style="width:75%;" id='slabdiv' >
</c:when>
<c:otherwise>
<div style="width:75%;display:none;" id='slabdiv' >
</c:otherwise>
</c:choose></h4>
<table id="slab_table" class="table table-hover table-striped table-bordered" cellspacing="0" width="100%">
<!-- <table id="example" class="display dataTable" cellspacing="0" width="100%" role="grid" aria-describedby="example_info" style="width: 100%;"> -->
<thead>
<tr>
<th class='hide_column'>Slab ID</th>
<th>Slab Name</th>
<th>Min Range</th>
<th>Max range</th>
<th>Points</th>
<th>Slab Add Date</th>
<th>Added By</th>
<!-- <th>Last Modified</th>
<th>Modified Date</th> -->
<th>Edit/Delete</th>
</tr>
</thead>

<tbody>
	<c:if test="${(slab.addEdit == 'edit') || (slab.addEdit == 'add') || (slab.addEdit == 'delete') }">	
	  <c:forEach items="${slabList}" var="slab">
         <tr >
           <td  class='hide_column'>${slab.slabId}</td>
           <td  >${slab.slabName}</td>
           <td  >${slab.rangeMin}</td>
           <td  >${slab.rangeMax}</td>
           <td  >${slab.points}</td>
		   <td>${slab.slabAddDate}</td>
           <td  >${slab.addedBy}</td>
           <%-- <td  >${slab.lastModDt}</td>
           <td  >${slab.lastModBy}</td> --%>
           <td >
           <a class="link" onclick="return editRow(this);">Edit</a>&nbsp;/&nbsp;
           <%-- <a onclick="return confirm('Are You Sure You Want To Delete?')" href="deleteSlab.action?slab_id=${slab.slabId}&param_id=${slab.paramId}">Delete</a> --%>
           <a onclick="return onClickDelete(this,${slab.paramId},${slab.slabId},'delete');" href="">Delete</a> 
         </td>
         </tr>
      </c:forEach>
      </c:if>
    </tbody>
   </table>
   <br>
   <c:if test="${user.level ge 7}">	
   		<button style="margin-left:10px;" id="addSlabBtn" class="button   bg-cyan bg-active-darkBlue fg-white "	onclick="return addRow();">
		<span class=" mif-plus" style="font-size: 10px;"></span>&nbsp;Add Slab</button>
	</c:if>
	</div>
	</div>
	</div>
	</div>
	</div>
			

</fieldset>

	</form:form>
	<div data-role="dialog" id="dialog1"
		style="width: 50px; display: none;" data-close-button="true"
		data-type="alert" data-width="350">

    <h3>&nbsp;&nbsp;Alert!</h3>
    <p>
      	&nbsp;&nbsp; Please Fill All The Fields</p>
</div>
<div data-role="dialog" id="dialog2" style="width:50px;display:none;" data-close-button="true" data-type="alert" data-width="350" >

    <h3>&nbsp;&nbsp;Alert!</h3>
    <p>&nbsp;&nbsp; Please <strong>Save</strong> the Existing Changes </p>
</div> 
<div data-role="dialog" id="dialog3" style="width:50px;display:none;" data-close-button="true"  data-width="350" >
    <h3>&nbsp;&nbsp;Info!</h3>
    <p>&nbsp;&nbsp; All Changes Are Up To date</p>
</div>
<script type="text/javascript">

$(function() {
    $('#paramId').change(function(){
        
        	$('#tablediv').hide();
        
    });
});
$(function() {
    $('#empLevel').change(function(){
        
        	$('#tablediv').hide();
        
    });
});
$( document ).ready(function() {
	$('#tablediv').hide();
});
$(document).ready(function() {
    $('#slab_table').dataTable();
} );
</script>
</body>
</html>
