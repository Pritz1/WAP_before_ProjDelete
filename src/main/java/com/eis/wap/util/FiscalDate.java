package com.eis.wap.util;

import java.util.Calendar;
import java.util.Date;

public class FiscalDate {

    private static final int    FIRST_FISCAL_MONTH  = Calendar.MARCH;

    private Calendar            calendarDate;

    public FiscalDate(Calendar calendarDate) {
        this.calendarDate = calendarDate;
    }

    public FiscalDate(Date date) {
        this.calendarDate = Calendar.getInstance();
        this.calendarDate.setTime(date);
    }

    public int getFiscalMonth() {
        int month = calendarDate.get(Calendar.MONTH);
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

    public int getCalendarMonth() {
        return calendarDate.get(Calendar.MONTH);
    }

    public int getCalendarYear() {
        return calendarDate.get(Calendar.YEAR);
    }

    public static void main(String[] args) {
        //displayFinancialDate(Calendar.getInstance());
        displayFinancialDate(setDate(2018, 4, 1));
        //displayFinancialDate(setDate(2012, 6, 25));
    }

    private static Calendar setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    private static void displayFinancialDate(Calendar calendar) {
        FiscalDate fiscalDate = new FiscalDate(calendar);
        int year = fiscalDate.getFiscalYear();
        System.out.println("Current Date : " + calendar.getTime().toString());
        System.out.println("Fiscal Years : " + year + "-" + (year + 1));
        System.out.println("Fiscal Month : " + fiscalDate.getFiscalMonth());
        System.out.println(" ");
    }
    /*o/ps:
     * Current Date : Wed Sep 04 14:23:17 EDT 2013
Fiscal Years : 2013-2014
Fiscal Month : 6

Current Date : Fri Feb 01 00:00:00 EST 2013
Fiscal Years : 2012-2013
Fiscal Month : 11

Current Date : Wed Jul 25 00:00:00 EDT 2012
Fiscal Years : 2012-2013
Fiscal Month : 4
     * */
}