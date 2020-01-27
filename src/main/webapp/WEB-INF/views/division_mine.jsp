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

/* wap menu script starts : shivani */
function wapClick() {
	document.getElementById("superUsrMenu").style.display = "none";
  var x = document.getElementById("wapMenu");
  if (x.style.display === "none") {
      x.style.display = "block";
  }else{
  	x.style.display = "none";
  }
}
function superUser() {
	document.getElementById("wapMenu").style.display = "none";
  if (document.getElementById("superUsrMenu").style.display === "none") {
  	document.getElementById("superUsrMenu").style.display = "";
  }else{
  	document.getElementById("superUsrMenu").style.display = "none";
  }
}

function menuCall(){
	//alert("1");
document.getElementById("param1").style.display = "";
	 
	 
}
  function pushMessage(t){
      var mes = 'Info|Implement independently';
      $.Notify({
          caption: mes.split("|")[0],
          content: mes.split("|")[1],
          type: t
      });
  }

  $(function(){
      $('.sidebar').on('click', 'li', function(){
          if (!$(this).hasClass('active')) {
              $('.sidebar li').removeClass('active');
              $(this).addClass('active');
          }
      })
  })
  
  /* wap menu script ends : shivani */

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
function onDelete(){
	debugger;
	var arr=[];
	var total = $("[type='checkbox']:checked").length;
	if(total>0){
		if(confirm("Are You Sure You Want to Delete?")){
			$("[type='checkbox']:checked").each(function(){
				arr.push($(this).val());
			})
			alert(arr);
			document.forms[0].action="deleteDiv.action?idList="+arr;
			document.forms[0].submit();
			return true;
		}else{
			return false;
		}
	}else{
		alert("Please Select a Division To Delete");
		return false;
	}
}
function addRow(){
	debugger;
	if(addDivValidate()){
	//var table = document.getElementById('slab_table');
	//var totRows =table.rows.length;
	//alert(totRows)
	   var table = $('#div_table').DataTable();
	   var rowNode = table.row.add(['' ,
	          '',
	          '<input type="text" id="divNameAdd" name="divName" >',
	          '<input type="text" id="fmsDbRefAdd" name="fmsDbRef" >',
	          '<input type="text" id="salesDbRefAdd" name="salesDbRef" >',
	          '<a onclick="return onSave(\'add\')">Save</a>&nbsp;/&nbsp;<a href="division.action"  id="cancelLink">Cancel</a>'
	          ]).draw().node();
		}           
	return false;	
	}	
	
function onSave(addEdit)
{
	//edited by shivani for validations
	
	var valFlag=false;
	var AllValuesFlag=false;
	
	
	
if(addEdit!="")
{
	document.forms[0].addEdit.value=addEdit;
}
else return false;
//start : shivani
if(document.getElementById("divNameAdd").value=="" 
	|| document.getElementById("fmsDbRefAdd").value==""
	|| document.getElementById("salesDbRefAdd").value==""){

valFlag=true;
AllValuesFlag=true;
alert("Please fill up the required details before saving");
}


if(!valFlag){
	document.forms[0].action="addUpdateDivision.action";
	document.forms[0].submit();
	return true;
}else{
	return false;
}
//end : shivani
}
	
function editRow(element){
 if(addDivValidate()){
	var table = $('#div_table').DataTable;
    var $row = $(element).closest("tr").off("mousedown");
    var $tds = $row.find("td");
    $.each($tds, function(i, el) {
    
       if(i==1){
    		var txt = $(this).text();
    	    $(this).html("").append("<input type='text' name='divId' value=\""+txt+"\" readonly='true' >");
    	}
    	if(i==2){
  		  var txt = $(this).text();
  	        $(this).html("").append("<input type='text' name='divName' onkeypress='return alphaNumericSpaceHyphen(event)' value=\""+txt+"\">");
  	  }
  	  if(i==3){
  		  var txt = $(this).text();
  	        $(this).html("").append("<input type='text' name='fmsDbRef' onkeypress='return alphaNumericSpaceHyphen(event)' value=\""+txt+"\">");
  	  }
  	  if(i==4){
  		  var txt = $(this).text();
  	        $(this).html("").append("<input type='text' name='salesDbRef' onkeypress='return alphaNumericSpaceHyphen(event)' value=\""+txt+"\">");
  	  }
  	  if(i==5){
  	        $(this).html("").append("<a onclick='return onSave(\"edit\")'>Save</a>&nbsp;/&nbsp;<a href='division.action'  id='cancelLink'>Cancel</a>");
  	  }
    });
 }
return false;
}	
function addDivValidate(){ 
	debugger;
	if (typeof document.forms[0].divName !== 'undefined' && typeof document.forms[0].fmsDbRef !== 'undefined' ) //if no textbox is present on screen then only add a new row
		{   //debugger;
			document.getElementById("dialog1").style.display="";
	    	metroDialog.open('#dialog1');
			return false;
		}
	return true;
}

</script>



<style>

.hide_column {
    display : none;
}

#div_table tr{
    line-height: 10px !important;
}

#div_table td{
max-height:20px !important;
font-size: small;
}
#div_table td input,td select{
max-height:20px !important;
font-size: small;
}
.bg-steel {
    background-color: #d2d4d6 !important;
}

</style>
</head>
<body class="bg-white"> <!--  hideColumn('slab_table',4,'5~6~7~8');-->

<div style="float: top;"><jsp:include page="header.jsp"/></div>
<br/><br/>
<div style="float: left;"><jsp:include page="sideBar.jsp"/></div>
<br/>
<!-- wap MEnu Ends -->
<div style=" margin-top:5%;">

<form:form name="division" id="division" commandName="division" method="POST" >
<form:hidden path ="addEdit" value="${division.addEdit}"/>
<center>
<fieldset>
<div style="width:80%;" align="center" id='adminuserdiv' >
<table id="div_table" class="display dataTable striped border hovered cell-hovered  "  width="100%" data-role="datatable"  style="width:100%;"  data-searching="true" >
<thead>
<tr>
<th>SELECT</th>
<th>DIVISION ID</th>
<th>DIVISION NAME</th>
<th>REFERENCE FOR FM DB</th>
<th>REFERENCE FOR SALES DB</th>
<th>EDIT</th>
</tr>
</thead>

<tbody>
<c:set var="count" value="0" scope="page" />
	  <c:forEach items="${divList}" var="division">
	     <c:set var="count" value="${count + 1}" scope="page"/>
         <tr >
           <td  >
			    <input type="checkbox" id="chk${count}" name="divcheck" value="${division.divId}" > 
		   </td> 
           <td  >${division.divId}</td>
           <td  >${division.divName}</td>
           <td  >${division.fmsDbRef}</td>
           <td  >${division.salesDbRef}</td>
           <td  ><a class="link" onclick="return editRow(this);">Edit</a></td>
         </tr>
      </c:forEach>
    </tbody>
   </table>
 </div>
</fieldset>
</center>

<table style="margin-left: 11%;">
 <tr>
 <td ><button class="button   bg-cyan bg-active-darkBlue fg-white " onclick="return addRow();">
 	  <span class=" mif-plus" style="font-size:10px;"></span>&nbsp;Add
 	  </button>
 </td>
 <td ><button class="button   bg-cyan bg-active-darkBlue fg-white " onclick="return onDelete();">
 	  <span class=" mif-minus" style="font-size:10px;"></span>&nbsp;Delete
 	  </button>
 </td>
 </tr>
 </table>

</form:form>
<div data-role="dialog" id="dialog1"
	style="width: 50px; display: none;" data-close-button="true"
	data-type="alert" data-width="350">
	<h3>&nbsp;&nbsp;Alert!</h3>
	<p>
		&nbsp;&nbsp; Please <strong>Save</strong> the Existing Changes
	</p>
</div>
</div>
</body>
</html>