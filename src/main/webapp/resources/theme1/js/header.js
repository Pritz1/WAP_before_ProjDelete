/**
 * 19th Feb 2019 : prithvi
 * This JS is created to put all JS functions used in header.jsp
 */
function openChangeDate(url){
        	debugger;
        	//var mywindow=null;
        width = 500;
        height = 500;
        var mywindow = window.open(url+"/showChangeDate.html", "Title",
            "location=0,status=1,scrollbars=1,resizable=1,menubar=0,toolbar=no,width="
                        + width + ",height=" + height);
        mywindow.moveTo(500, 100);
        mywindow.focus();
}

function onClickMenu(lockStat,lvl){
	debugger;
	if(lockStat!='Y' && lvl < 7){
		alert("WAP is not Locked Yet !! ")
		return false;
	}
	return true;
}

/* function wapClick() {
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
}) */