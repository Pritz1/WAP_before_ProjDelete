/**
 * This JS is created to add all common functions to be used throughout the application
 */

/* Below script will hide columns from datatable.
 * tableid : table id given to the data table
 * noOfCols : no. of columns that needs to be hidden 
 * colno : col nos starting from zero needs to be specified and if columns are more than one then no. should be seperated by ~*/
function hideColumn(tableid,noOfCols,colno)
{
	var table = $('#'+tableid).DataTable();
	var colArr=[];
	noOfCols=parseInt(noOfCols,10);
	if(noOfCols>1 && noOfCols>colno.indexOf("~") > -1)
		{
			colArr=colno.split('~');
			for(var i=0;i<noOfCols;i++){
				table.column( colArr[i] ).visible( false );	
			}
		}
	else{
		table.column(colno).visible( false );
	}
}
/*This function is used to restrict all characters accept for numbers and decimals*/
function allowNumbersAndDecimalOnly(evt)
{
   var charCode = (evt.which) ? evt.which : evt.keyCode;
   if (charCode != 46 && charCode > 31 
     && (charCode < 48 || charCode > 57))
      return false;

   return true;
}
/*This function allows:
 * alpha numerics
 * space,hyphen,underscore*/
function alphaNumericSpaceHyphen(e){
if (e.charCode != 0) {
    var regex = /^[a-zA-Z0-9\-_\s]+$/;
    var key = String.fromCharCode(!e.charCode ? e.which : e.charCode);
    if (!regex.test(key)) {
      e.preventDefault();
      return false;
    }
  }
}
function onlyAlphabets(evt,obj) {
    var e = window.event || evt;
    var charCode = e.which || e.keyCode;
    if ((charCode <= 65 && charCode >= 90) || (charCode <= 97 && charCode >= 122)) {
        if (window.event) //IE
            window.event.returnValue = false;
        else //Firefox
            e.preventDefault();
    }
    if(charCode >= 97 && charCode <= 122)
    {
    	obj.value = String.fromCharCode(charCode).toUpperCase();
    }
 return true;
}
