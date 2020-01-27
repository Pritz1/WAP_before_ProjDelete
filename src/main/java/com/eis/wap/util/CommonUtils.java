package com.eis.wap.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.joda.time.LocalDate;
import org.joda.time.Months;
import java.text.DateFormatSymbols;
import org.joda.time.Years;

public class CommonUtils {

	public static String getMonth(String mth){
		System.out.println("month : "+mth);
		HashMap<String, String> monthMap=new HashMap<String, String>();
		monthMap.put("01","Jan");monthMap.put("02","Feb");monthMap.put("03","Mar");monthMap.put("04","Apr");
		monthMap.put("05","May");monthMap.put("06","Jun");monthMap.put("07","Jul");monthMap.put("08","Aug");
		monthMap.put("09","Sep");monthMap.put("10","Oct");monthMap.put("11","Nov");monthMap.put("12","Dec");
		String mthName="";
		if(mth!=null){
			mthName=monthMap.get(mth);
		}
		return mthName;
	}

	public static Date getCurrentDateTime() {
		DateFormat df=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		Date currdt=new Date();
		try {
			currdt=df.parse(df.format(currdt).toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("--currdt--"+currdt);
		return currdt;
	}
	public static String getCurrentDateTimeString() {
		DateFormat df=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		Date currdt=new Date();
		String newdate="";
		try {
			newdate=df.format(currdt).toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("--currdt--"+currdt);
		return newdate;
	}
	public static Date getCurrentDate() {
		DateFormat df=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		Date currdt=new Date();
		Date newdate=null;
		try {
			newdate=df.parse(df.format(currdt).toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("--currdt--"+currdt);
		return newdate;
	}

	public static String getCurrentDate(Date date){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String dateStr = "";
		try{
			dateStr = dateFormat.format(date).toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Formatted Date " + dateStr);
		return dateStr;
	}
	public static String getCurrentDateTime(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String dateStr = "";
		try{
			dateStr = dateFormat.format(new Date()).toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Formatted Date " + dateStr);
		return dateStr;

	}

	/*below code takes start date as an input and gives whole financial year starting from the month of finstartdate*/	
	public static LinkedHashMap<String,String> months(String finStartdate,String loginYr){
		//final String finStartdate="2018-04-01";//loginYr+"-"+finStartDateSys.split("-")[1]+"-01"; 
		DateFormat inputdateFormat= new SimpleDateFormat("yyyy-MM-dd");
		DateFormat outputdateFormat= new SimpleDateFormat("MMM-yyyy");
		Date inputDate=null;
		try{
			inputDate=(Date)inputdateFormat.parse(finStartdate);
		}catch(Exception  e)
		{
			e.printStackTrace();
		}
		String mth="";
		final Calendar cal=Calendar.getInstance();//as of now
		cal.setTime(inputDate);//set to input
		final int month=cal.get(Calendar.MONTH)+1;//gives index 0 - 11, Jan - Dec
		LinkedHashMap<String,String> finYrMonthsMap=new LinkedHashMap<String,String>();
		do{
			inputDate=cal.getTime();//give date
			mth=(cal.get(Calendar.MONTH)+1)+"";
			finYrMonthsMap.put(cal.get(cal.YEAR)+(mth.length()<2?"0"+mth:mth),outputdateFormat.format(inputDate));//date to string as per above format
			cal.add(Calendar.MONTH,1);//adds month one-by-one
		} while(month!=(cal.get(Calendar.MONTH)+1));//unitl next year of given input month
		
		return finYrMonthsMap;
	}
	
	public static HashMap<String, String> showError(String errorDesc,String errCode,HashMap<String,String> errorMap){
		errorMap.put("errCode", errCode);
		errorMap.put("errDesc", errorDesc);
		return errorMap;
	}
	
	public static Double getScorePerc(Double weightage,Double score){
		if(score>0 && weightage>0)
			return ((weightage/100)*score);
		else 
			return 0.0;
	}
 
 public static int[] parseDate(String date)
	{
		int retArr[] = new int[3];

		retArr[0]= Integer.parseInt(date.split("-")[0]);
		retArr[1]= Integer.parseInt(date.split("-")[1]);
		retArr[2]= Integer.parseInt(date.split("-")[2]);
		return retArr;

	}// parseDate closes
	

	
	
	public static int getFinStrtYr(int strtMth,int logMth,int logYr){
		if(logMth < strtMth){ //login date=012017 -> (logmth)01 <= (endMth)0
			return (logYr-1); //2017
		}else{ //login date=042017 -> 04>03
			return logYr; //2016
		}
	}
	
	public static String fillMonthNumber(String mth)
    {
        String retString=mth;
        if (retString.length()==1)
            retString = "0"+retString;

        return retString;

    } // fillMonthNumber closes
	
	public static String getFinancialYr(String finStrtMth,String logMth,String logYr){
		//String finStrtMth = "04";
		//String logMth = "01";
		//String logYr = "2019";
		
        int endMth = Integer.parseInt(finStrtMth)-1;
        int strtYr = getFinStrtYr(Integer.parseInt(finStrtMth),Integer.parseInt(logMth),Integer.parseInt(logYr));
        int endYr=0;
        
        if(endMth==0)
        {
        	endMth=12;
        	endYr=strtYr;
        }else{
        	endYr=strtYr+1;
        }
        System.out.println("Financial Year is :"+finStrtMth+strtYr+" to "+endMth+""+endYr);
	
        return strtYr+finStrtMth+"-"+endYr+""+((endMth+"").length()<2 ? "0"+endMth : endMth);
	}
	
	/*public static String getFinEndYrMth(String finStrtMth,String logMth,String logYr){
		//String finStrtMth = "04";
		//String logMth = "01";
		//String logYr = "2019";
		
        int endMth = Integer.parseInt(finStrtMth)-1;
        int strtYr = getFinStrtYr(Integer.parseInt(finStrtMth),Integer.parseInt(logMth),Integer.parseInt(logYr));
        int endYr=0;
        
        if(endMth==0)
        {
        	endMth=12;
        	endYr=strtYr;
        }else{
        	endYr=strtYr+1;
        }
        System.out.println("Financial Year is :"+finStrtMth+strtYr+" to "+endMth+""+endYr);
        return endYr+""+((endMth+"").length()<2 ? "0"+endMth : endMth);
	}*/
	
	public static String getFinEndDt(String finStrtDt,String logMth,String logYr){
		String finStrtMth = finStrtDt.split("-")[1];
		int strtYr = Integer.parseInt(finStrtDt.split("-")[0]);
		//String logMth = "01";
		//String logYr = "2019";
        int endMth = Integer.parseInt(finStrtMth)-1;
        int endYr=0;
        
        if(endMth==0)
        {
        	endMth=12;
        	endYr=strtYr;
        }else{
        	endYr=strtYr+1;
        }
        System.out.println("Financial Year is :"+finStrtMth+strtYr+" to "+endMth+""+endYr);
        return getLastDateOfMth(endYr+"-"+((endMth+"").length()<2 ? "0"+endMth : endMth)+"-01");
	}
	
	public static String getLastDateOfMth(String date)
	{
		//String retString = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try{
		Date convertedDate = dateFormat.parse(date);
		c.setTime(convertedDate);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		}catch(Exception e){
			e.printStackTrace();
		}
		int mth=c.get(c.MONTH)+1;
		return (c.get(c.YEAR)+"-"+((mth+"").length()<2? "0"+mth : mth )+"-"+c.get(c.DATE));
		//return retString;
	} // rollDateForward closes
	
	public static String getFinStrtDt(String sysfinStrtDt,String logMth,String logYr){
		String finStrtMth = sysfinStrtDt.split("-")[1];
        int strtYr = getFinStrtYr(Integer.parseInt(finStrtMth),Integer.parseInt(logMth),Integer.parseInt(logYr));
        
        System.out.println("Financial Start is :"+finStrtMth+strtYr);
	
        return strtYr+"-"+finStrtMth+"-01";
	}
	
	public static int  getmonthsBetweenDates(int frmYr,int frmMth,int frmDay,int toYr,int toMth,int toDay){
		LocalDate startDate = new LocalDate(frmYr, frmMth, frmDay); 
		LocalDate endDate = new LocalDate(toYr, toMth, toDay);
		//int monthsBetween = Months.monthsBetween(startDate, endDate).getMonths(); 
		return Months.monthsBetween(startDate, endDate).getMonths();
	}
	public static int getMaxDayOfMonth(int yr,int mth,int day){
		LocalDate endDate = new LocalDate(yr, mth, day);
		return endDate.dayOfMonth().getMaximumValue();
	}
	public static String getMonthName(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}
}
