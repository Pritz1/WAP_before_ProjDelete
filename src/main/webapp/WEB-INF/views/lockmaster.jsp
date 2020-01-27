<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
   
<title>Lock Master</title>

<!-- header -->
<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
<link href="<c:url value="/resources/css/metro-icons.css" />"	rel="stylesheet">

<!-- header : end-->

<link href="<c:url value="/resources/css/bootstrap-3.3.5.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />"	rel="stylesheet">
<link href="<c:url value="/resources/css/bootstrap-table.min-1.12.2.css" />"	rel="stylesheet">
<script src="<c:url value="/resources/js/bootstrap-3.3.5.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap-table-1.12.2.js" />"></script>
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script>

//if checkbox is unchecked for a month then all prev month's checkboxes should also be unchecked
function check_uncheck(val,mth,yr){
	var totChkbox=document.getElementsByName("lockcheck").length;
	//for uncheck
	if (!document.getElementById('check'+val).checked) 
	  {
		if(confirm('Are You Sure You Want To Unlock Entry For this Month ?')){
		for(var i=val;i<=totChkbox;i++){
			if(document.getElementById("check"+i).checked){
				document.getElementById("check"+i).checked = false;
			}
		 }
		document.forms[0].action="unlockEntries.action?mth="+mth+"&yr="+yr;
	    document.forms[0].submit();
	    }else{
	    	return false;
	    }	
	  		
	  }else{ //for check
		  /* for(var i=val;i>=1;i--){
			  if(!document.getElementById("check"+i).checked)
				document.getElementById("check"+i).checked = true;
				}
	  document.forms[0].action="lockEntries.action?mth="+mth+"&yr="+yr;
	  alert(document.forms[0].action)
	  document.forms[0].submit(); */
	  alert("You Cannot Lock An Entry. Only Unlock is allowed");
	  }
		
}

function onSubmitLockMaster(){
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

function onEditlockMaster(){
	$.Notify({
	    caption: 'Info',
	    content: 'if you unlock a month all next months will be unlocked.'
	});
	var totChkbox=document.getElementsByName("lockcheck").length;
	for(var i=1;i<=totChkbox;i++){
	document.getElementById("check"+i).disabled = false;
		}
	document.getElementById("spanDisableChk").style.display="none";
	document.getElementById("spanEnableChk").style.display="";
	
	return false;
}

function onLoadlockMst(){
	//alert(document.forms[0].mode.value);
	if(document.forms[0].lvl.value<7){
	 document.getElementById("btn1").style.display="none";
	 document.getElementById("btn2").style.display="none";
	 document.getElementById("blank1").style.display="block";
	 document.getElementById("blank2").style.display="block";
	}
	if(document.forms[0].mode.value=="unlock"){
		document.forms[0].action="lockmaster.action";
	    document.forms[0].submit();
	}
}

</script>
<style>
/* The container */
.container1 {
 
  /* display: block; */
  position: relative;
  padding-left: 30px;
  margin-bottom: 10px;
  cursor: pointer;
  font-size: 20px;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  
  text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
}



/* Hide the browser's default checkbox */
.container1 input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
  
  text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
}

/* Create a custom checkbox */
.checkmark {
  position: absolute;
  top: 0;
  left: 0;
  height: 0.938em;
  width: 0.938em;
  border-radius: .10em;
  background-color: #eee;
  
  text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
}

/* On mouse-over, add a grey background color */
.container1:hover input ~ .checkmark   {
  background-color: #ccc;
  
  text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
  
  text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
}

/* When the checkbox is checked, add a blue background */
.container1 input:checked ~ .checkmark {
  background-color: #2196F3;
  
  text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
}

/* Create the checkmark/indicator (hidden when not checked) */
.checkmark:after {
  content: "";
  position: absolute;
  display: none;
  
  text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
}

/* Show the checkmark when checked */
.container1 input:checked ~ .checkmark:after {
  display: block;
  text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
}

/* Style the checkmark/indicator */
.container1 .checkmark:after {
  left: 7px;
  top: 2px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 3px 3px 0;
  
  -webkit-transform: rotate(45deg);
  -ms-transform: rotate(45deg);
  transform: rotate(45deg);
  
  text-align: center; /* center checkbox horizontally */
    vertical-align: middle; /* center checkbox vertically */
}
label input[type="checkbox"]:disabled + span {
background-color: #ccc;
  opacity: .7;
  cursor: not-allowed;
 
}

.table th,td {
   text-align: center;   
}

.table > tbody > tr > td {
     vertical-align: middle !important;
}
.caption-copy{
padding-top:8px;padding-bottom: 8px;color: #777;text-padding-top:8px;padding-bottom: 8px;color: #777;text-align: left;
}


</style>

</head>
<body onload="onLoadlockMst();">
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<div style="margin-top:6%;">
<form:form name="lockmasterform" id="lockmasterform" commandName="lockmaster" method="POST" action="lockmaster">
<input type="hidden" value="${user.level}" name="lvl">
<input type="hidden" value="${mode}" name="mode">
<div class="container" >
<div class="panel panel-primary ">
<div class="panel-body col-lg-12">
<div id="locktable">
<div class="panel-footer" >
Following Table Shows Months For Which WAP is Processed As 
<label class="input-control checkbox"><input type="checkbox" checked disabled="disabled"><span class="check"></span></label>
For Current Financial Year
</div>
  <!-- <table id="lockmst_table" class="display dataTable striped border hovered cell-hovered  " cellspacing="0" width="100%" data-role="datatable" style="width:100%;"  data-searching="true" > -->
             <!-- <table id="example" class="display dataTable" cellspacing="0" width="100%" role="grid" aria-describedby="example_info" style="width: 100%;"> -->
          
             <div class="table-responsive">
             <table cellspacing="0"  data-toggle="table" class="table col-lg-10 table-striped table-hover " data-search="true"  width="100%">
             
                <thead>
                <tr>
                <th width="10%" data-sortable="true" ><div><span>Lock ID</span></div></th>
                <th width="10%" data-sortable="true" ><div><span>Div ID</span></div></th>
                <th width="10%" data-sortable="true" ><div><span>Div Name</span></div></th>
                <th width="5%" data-sortable="true" ><div><span>Month</span></div></th>
                <th width="5%" data-sortable="true" ><div><span>Year</span></div></th>
                <th width="10%" data-sortable="true" ><div><span>Data Locked</span></div></th>
                <th width="10%" data-sortable="true" ><div><span>Last Updated By</span></div></th>
                <th width="10%" data-sortable="true" ><div><span>Last Update Date</span></div></th>
                
                </tr>
                </thead>

           <tbody>
            <c:set var="count" value="0" scope="page" />
			<c:forEach items="${locklist}" var="lock" >
			<c:set var="count" value="${count + 1}" scope="page"/>
			<tr>
			<td >${lock.lockId}</td>
			<td>${lock.divId}</td>
			<td>${lock.div_name}</td>
			<td>${lock.monthnm}</td>
			<td>${lock.yr}</td>
			<td>
			<label class="container1">
  <input  type="checkbox"  id="check${count}" name="lockcheck" value="${lock.isdataLocked}" ${lock.isdataLocked == 'Y' ? 'checked' : ''} disabled="disabled" onclick="check_uncheck(${count},${lock.mth},${lock.yr});">
  <span class="checkmark" title="To unlock,click on edit first!" id="spanDisableChk"></span>
  <span class="checkmark" title="Unlocks further months Too,if any" id="spanEnableChk" style="display:none"></span>
</label>
			</td>
			<td>${lock.lastModBy}</td>
			<td>${lock.lastModDt}</td></tr>
			<%-- <td><c:if ${lock.isdataLocked == 'Y'}> 
  				<c:set value="mif-cloud-upload " var="cssClass"></c:set>
				</c:if>
				<div class="${cssClass}">
   <span class=" mif-cloud-upload "></span> 
</div></td> --%>
			</c:forEach>
           </tbody>
          </table></div>
 </div></div>
<br/>
<center>

<table id="editsubmitbuttons">
 <tr>
 <td id="btn1"><button type="button" class="btn btn-primary btn-md" style="margin-right: 10px;" onclick="return onEditlockMaster();">Edit</button></td>
 <!-- <td id="btn2"><button type="button" class="btn btn-primary btn-md" onclick="return onSubmitLockMaster();" type="submit" >Save</button></td> -->
 	
 <td style="diplay: none;" id="blank1">&nbsp</td>
 <td style="diplay: none;" id="blank2">&nbsp</td>
 </tr>
 </table></center>
 <br/><br/>
 </div>
 </div> 
</form:form>
<span class="tooltiptext" style="visibility:hidden">Tooltip text</span>
<div data-role="dialog" id="dialog1" style="width:50px;display:none;" data-close-button="true" data-type="alert" data-width="350" >

    <h3>&nbsp;&nbsp;Alert!</h3>
    <p>
     &nbsp;&nbsp; WAP is already processed for all months
    </p>
</div>
</div> 

</body>
</html>