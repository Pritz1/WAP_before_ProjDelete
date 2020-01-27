<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<div style="float: top;"><jsp:include page="header.jsp"/></div>
<head>
 <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel='shortcut icon' type='image/x-icon' href='../favicon.ico' />
<%-- <script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script> --%>
<script src="<c:url value="/resources/js/jquery.dataTables.js" />"></script>
<link href="<c:url value="/resources/css/metro.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-icons.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/metro-responsive.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/metro.js" />"></script>
<script src="<c:url value="/resources/js/select2.min.js" />"></script>
<link href="<c:url value="/resources/css/normalize.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/sum().js" />"></script>
<script src="<c:url value="/resources/js/common.js" />"></script>
<link href="<c:url value="/resources/css/datatables-metro.css" />" rel="stylesheet">
<title>E-VALUATE Calculation Example</title>
<script>
function wapCal(val){
//var totalParams=document.forms[0].totParams.value;
var score=0.00;
var achievement=0.00;
var totalSlabs=0;
var wap=0.00;
var avgScore=0.00,totScore=0.00;



/* if(val==0){
for( var i=1;i<=totalParams;i++)
{
totalSlabs=(document.getElementsByName("param"+i).length)/2; //gets total no. of slabs in one parameter
achievement=parseFloat(document.getElementById("achieve"+i).value);
score=getScoreSlabWise(totalSlabs,i,achievement,score);

if(score!=0)
{
	getAndSetWapVal(wap,score,i);
}
else
{
	document.getElementById("wap"+i).value=score;
}
document.getElementById("score"+i).value=score;
}	
}//closes if score==0 i.e for onload
else{
	achievement=parseFloat(document.getElementById("achieve"+val).value);
	totalSlabs=(document.getElementsByName("param"+val).length)/2; //gets total no. of slabs in one parameter
	score=getScoreSlabWise(totalSlabs,val,achievement,score);
	if(score!=0)
	{
		getAndSetWapVal(wap,score,val);
	}
	else
	{
		document.getElementById("wap"+val).value=score;
	}
	document.getElementById("score"+val).value=score;
}//closes else of if(score==0) i.e for edit buttons of particular row
for(var i=1;i<=totalParams;i++){
	totScore=totScore+parseFloat(document.getElementById("score"+i).value);
	wap=wap+parseFloat(document.getElementById("wap"+i).value);
	//achievement=achievement+parseFloat(document.getElementById("achieve"+i).value);
}
avgScore=totScore/totalParams;
document.getElementById("scoreTot").value=avgScore;
document.getElementById("wapTot").value=wap;
getRating(); */
//document.getElementById("achieveTot").value=(achievement/100);
return false;
}

//below function gets the score by finding the slab where acheivement value fits.
//i=gets the row no.
function getScoreSlabWise(totalSlabs,i,achievement,score){
	var min=0.00;
	var max=0.00;
for(var j=0;j<totalSlabs;j++)
{
  min=parseFloat(document.getElementById("param"+(i)+"min"+(j+1)).value);
  max=parseFloat(document.getElementById("param"+(i)+"max"+(j+1)).value);
  if(achievement>=min && achievement<=max)
  {
   score = parseFloat(document.getElementById("param"+(i)+"val"+(j+1)).value);
   break;
  }
}
	return score;
}

function getAndSetWapVal(wap,score,i){
	wap=score*(parseFloat(document.getElementById("weight"+i).value)/100);
	document.getElementById("wap"+i).value=wap;
}

function getRating(){
debugger;
var incrCount=parseInt(document.getElementById("totIncrement").value,10);
var wap=parseFloat(document.getElementById("wapTot").value);
if(wap>=parseFloat(document.getElementById("incr1min").value)){
	document.getElementById("rating").value=document.getElementById("incr1val").value;
}else{
for(var i=2;i<=incrCount;i++){
if(wap>=parseFloat(document.getElementById("incr"+i+"min").value) && wap<=parseFloat(document.getElementById("incr"+i+"max").value)){
	document.getElementById("rating").value=document.getElementById("incr"+i+"val").value;
	break;
}
}
}
return false;
}

function sample(){
	
	var table = $('#example_table').DataTable();
	
	$('#example_table').on( 'click', 'td', function () {
		 var tr = $(this).closest("tr");
		    var rowindex = tr.index();

		     alert(rowindex);
	} );
}

function populateRandomVals(){
	var table = document.getElementById('wapDemo_table');
	var totRows =table.rows.length-1;
	//alert(totRows)
	for(var i=1;i<=totRows;i++){
		document.getElementById("achieve"+i).value="95";
	}
	
}
function getScore(){
	alert(1)
	document.forms[0].action="getWapScore.action?param_id="+document.forms[0].param_id.value+"&achieve="+document.forms[0].achieve.value;
	alert(document.forms[0].action)
	document.forms[0].submit();
}
function editRow(element){alert(2)
	//alert("salesslab"+val)
	var table = $('#wapDemo_table').DataTable;

	  //  $("#param_table").on('mousedown.edit', "a.link", function(e) {
	     
	      var $row = $(element).closest("tr").off("mousedown");
	      var $tds = $row.find("td");
	      $.each($tds, function(i, el) {
	    	  
	    	  if(i==0){
	    		  var txt = $(this).text();
	    	        $(this).html("").append("<input type='text'  name='param_id' readonly='readonly' value=\""+txt+"\">");
	    	  }   	  
	      
	    	 /*  if(i==1){
	    		  var txt = $(this).text();
	    	        $(this).html("").append("<input type='text' name='param_name' onkeypress='return alphaNumericSpaceHyphen(event)' value=\""+txt+"\">");
	    	  } */
	    	  if(i==2){
	    		  var txt = $(this).text();
	    	        $(this).html("").append("<input type='text' name='weightage' onkeypress='return allowNumbersAndDecimalOnly(event)' value=\""+txt+"\">");
	    	  }
	    	  if(i==3){
	    		  var txt = $(this).text();
	    	        $(this).html("").append("<input type='text' name='achieve' onkeypress='return allowNumbersAndDecimalOnly(event)' value=\""+txt+"\">");
	    	  }
	    	  if(i==4){
	    		  var txt = $(this).text();
	    	        $(this).html("").append("<input type='text' name='score' onkeypress='return allowNumbersAndDecimalOnly(event)'	disabled=true value=\""+txt+"\">");
	    	  }
	    	  if(i==5){
	    		  var txt = $(this).text();
	    	        $(this).html("").append("<input type='text' name='wap' onkeypress='return allowNumbersAndDecimalOnly(event)'  disabled=true  value=\""+txt+"\">");
	    	  }
	      });
	    //});
		return false;
}


 $(document).ready(function() {
	 var rowno=0;
	 var table,paramid_celldata,achieve_celldata;
$("#wapDemo_table").on('mousedown.edit', "a.editlink", function(e) {

    $(this).removeClass().addClass("savelink").text("Save");
    var $row = $(this).closest("tr").off("mousedown");
    var $tds = $row.find("td").not(':first').not(':last');
    rowno=$(this).closest('td').parent()[0].sectionRowIndex;
    //alert("rowno : "+rowno)
    $.each($tds, function(i, el) {
    	if(i === 2){
      var txt = $(this).text();
      $(this).html("").append("<input type='text' onkeypress='return allowNumbersAndDecimalOnly(event)' value=\""+txt+"\">");
      return false;
    	}
    });
  });

  $("#wapDemo_table").on('mousedown', "input", function(e) {
    e.stopPropagation();
  });

  $("#wapDemo_table").on('mousedown.save', "a.savelink", function(e) {
    
    $(this).removeClass().addClass("editlink").text("Edit");
    var $row = $(this).closest("tr");
    var $tds = $row.find("td").not(':first').not(':last');
    
    $.each($tds, function(i, el) {
      var txt = $(this).find("input").val()
      if(typeof txt !== 'undefined'){
      var table2 = $('#wapDemo_table').DataTable();
       	table2.cell( rowno , 3 ).data( txt ).draw();
       	return false;
      }
    });
    
    var table1 = $('#wapDemo_table').DataTable({"bDestroy": true});
   
    paramid_celldata=table1.cell( rowno , 0 ).data(); //get paramid i.e 0th column
    achieve_celldata=table1.cell( rowno , 3 ).data(); //get achievement i.e. 3rd cloumn(starting from 0)
    
    $.ajax({
        url : 'getWapScore.html?param_id='+paramid_celldata+'&achieve='+achieve_celldata,
        success : function(data) {
        	var table = $('#wapDemo_table').DataTable();
        	table.cell( rowno , 4 ).data( data ).draw(); // populate data in score column i.e. 4th cell of that row 
        	calWap(rowno,data);
        }
    });
    
  });
  //calWap(-1);
}); 

 function calWap(val,score){
	 debugger;
	 var table1 = document.getElementById('wapDemo_table');
		var totRows =table1.rows.length-1;
		
		var table = $('#wapDemo_table').DataTable();
		
		var score,weight,wap=0.0;
		if(val==-1){
    		for(var i=0;i<totRows-1;i++){
			score=table.cell( i , 4 ).data();
    		weight=table.cell( i , 2 ).data();
    		wap=score*(weight/100);
    		table.cell( i , 5 ).data( wap ).draw();
    	}
		}else if(val>=0){
			//score=table.cell( val , 4 ).data();
	    	weight=table.cell( val , 2 ).data();
	    	wap=score*(weight/100);
	    	table.cell( val , 5 ).data( wap ).draw();
		}	
    	document.forms[0].achieveTot.value=table.column(3).data().sum();
    	document.forms[0].scoreTot.value=table.column(4).data().sum();
    	document.forms[0].wapTot.value=table.column(5).data().sum();
    	 $.ajax({
    		// type:post
    	        url : 'getRating.html?wap='+document.forms[0].wapTot.value,
    	        success : function(data) {
    	        	document.forms[0].rating.value=data;
    	        },error:function (xhr, ajaxOptions, thrownError){
    	            alert(xhr.status);
    	            alert(thrownError);
    	        } 
    	    });
    	
    	return false;
 }

/* function crunchifyAjax() {
    $.ajax({
        url : 'getWapScore.html',
        success : function(data) {
            $('#result').html(data);
        }
    });
} */

/* function onload(){alert(555)
	var table = $('#wapDemo_table').DataTable();
	table.cell( 1, 4 ).data( 'Updated' ).draw();
} */

</script>
<style>
input{
	 width:50px;
 }

</style>
</head>
<body onload=""> <!--wapCal(0); populateRandomVals();onload(); -->
<form:form id="wapCalform" commandName="wapCal" method="POST" action="calculateWap" >

<fieldset>
<legend><h5>This is How Your Score For Appraisal is Calculated:</h5></legend>
<div id="calc" style="float:left; width:50%">
<div id="wap">
<table><tr>
                <td width="5%"><label for="rating">Rating : </label></td> 
                <td width="50%"><input type="text" name="rating" id="rating" readonly="readonly" style="width:10%;height:25px;"></td> 
</tr></table>

<table id="wapDemo_table" class="dataTable striped border hovered cell-hovered " data-role="datatable" data-searching="true">
                <thead>
                <tr >
                <th>Param ID</th>
                    <th>Parameter</th>
                    <th>Weightage</th>
                    <th>Achievement (%)</th>
                     <th>Score</th>
                    <th>WAP</th>
                    <th>try</th> 
                    
                </tr>
                </thead>

        <tbody>
        <%-- <c:set var="count" value="0" scope="page" /> --%>
        <c:forEach items="${paramList}" var="parameter">
        <%-- <c:set var="count" value="${count + 1}" scope="page"/> --%>
         <tr >
           <td  width="60" align="center">${parameter.param_id}</td>
           <td  >${parameter.param_name}</td>
           <td  >${parameter.weightage}</td>
           <td  >${parameter.achieve}</td>
           <td  >${parameter.score}</td>
           <td></td>
           <td >
           <a class="editlink" >Edit</a>&nbsp;<!-- onclick="return editRow(this);" -->
         </tr>
      </c:forEach>

                  <tfoot>
                <tr>
                    <td>Total</td>
                    <td></td>
                    <td>100%</td>
                    <td><input type="text"  id="achieveTot"  name="achieveTot" disabled="disabled" readonly="readonly"></td>
                    <td><input type="text"  id="scoreTot"  name="scoreTot" disabled="disabled" readonly="readonly" ></td>
                    <td><input type="text"  id="wapTot"  name="wapTot" disabled="disabled" readonly="readonly" ></td>
					<td></td>
                </tr>
                </tfoot> 
                 
                </tbody>
</table>
<div id="result"></div> 
</div>
<table><tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr></table>
<div id="example" >
 <div class="accordion" data-role="accordion">
                        <!-- <h5>.accordion with icon</h5> -->
                            <div class="frame active">
                                <div class="heading">Page Info<span class="mif-home icon"></span></div>
                                <div class="content">
                                    
                                    <ul class="simple-list">
                            <li>This option lets you to calculate E-VALUATAE score by changing acheivement</li>
                            <li>You need to change achievement field of any parameter and click on edit
                            	to get the changed score and rating</li>
                            <li>You can sort the columns by clicking on header.</li>
                            <li>To know how score is decided you can check <em>Parameter</em> and <em>Slab</em> options
                            	to get slab wise score per parameter.</li>
                           
                        </ul>
                                </div>
                            </div>
                            
                            <div class="frame ">
                                <div class="heading">E-VALUATE Calculation<span class="mif-cog icon"></span></div>
                                <div class="content cell">
                                <ul class="simple-list">
                                
                                <li>Parameter : Forms the basis of evaluation of an employee (e.g if SALES then how much sale you are bringing to the company)  </li>
                                <li>Achievement : It is calculated parameter wise in Field Management System as per your DCR entry.</li> 
                                <li>Score : is fetched as per the slabs decided by the company (check SLAB menu for more details).</li>
                                <li>Slabs : it has max value and min value to form a range to get the score</li>
                                <li>E-VALUATE : is calculated based on the weightage of each parameter. (Weightage is set through PARAMETER menu)</li>
                                <li>Formula for E-VALUATE=score*(weightage/100)</li>
								<li>Rating : Based on E-VALUATE calculated rating is decided.</li>
								<li>To know more about rating and increment amount please refer <strong>Know Your Increment</strong> menu</li>	                                			  	
								</ul>
                                </div>
                            </div>
                        
                        </div>
                    </div>
</div>
</fieldset>
</form:form>
</body>
</html>