package com.eis.wap.util;

import java.util.Calendar;
import java.util.Date;

public class FinancialDate {

	private static int FIRST_FISCAL_MONTH  = 0;
	
	private static Calendar calendarDate;
	
	public static int getFiscalMonth() {
		int month = Calendar.getInstance().MONTH  + 1;
		int result = ((month - FIRST_FISCAL_MONTH - 1) % 12) + 1;
		if (result < 0) {
			result += 12;
		}
		return result;
	}

	public int getFiscalYear() {
		int month = calendarDate.get(Calendar.MONTH);
		int year = calendarDate.get(Calendar.YEAR);
		return (month >= FIRST_FISCAL_MONTH) ? year : year - 1;
	}

	public static void main(String args[]) {
		String finDate = "2018-04-01";
		
		
		Integer yr = Integer.parseInt(finDate.substring(0, 4));
		Integer mth =Integer.parseInt(finDate.substring(5, 7)) - 1;
		System.out.println(mth);
		calendarDate = Calendar.getInstance();
		calendarDate.set(yr, mth, 1);
		FIRST_FISCAL_MONTH = calendarDate.get(Calendar.MONTH) + 1;
				
		System.out.println(FIRST_FISCAL_MONTH +""+ calendarDate.getTime());
		System.out.println(getFiscalMonth());
		
			
	}

}