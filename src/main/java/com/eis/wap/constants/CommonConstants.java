package com.eis.wap.constants;

import java.util.Arrays;
import java.util.List;

public class CommonConstants {

	public static final int INT_ZERO = 0;
	public static final int INT_ONE = 1;
	public static final int INT_TWO = 2;
	public static final int INT_THREE = 3;
	public static final int INT_FOUR = 4;
	public static final int INT_FIVE = 5;
	public static final int INT_SIX = 6;
	public static final int INT_SEVEN = 7;
	public static final int INT_EIGHT = 8;
	public static final String ERROR_CODE_SSE="0100"; //SSE-> SOFT STOP ERROR
	public static final String ERROR_CODE_HSE="0200"; //HSE-> HARD STOP ERROR
	public static final String SUCCESS_CODE="0300";
	public static final String SUCCESS_LOCK_CODE="0400";
	public static final String ERROR_DESC="WAP is Already Processed for This Month :";
	public static final int MAX_VISIT_IN_DB = 3; //visits till the name is same in summary table
	public static final int SALES_PARAMID=1;
	public static final int POB_PARAMID=2;
	public static final int DOC_REACH_PARAMID=3;
	public static final int PULSE_CHEM_REACH_PARAMID=4;
	public static final int COVERAGE_PARAMID=5;
	public static final int MANAGERIAL_EFF_PARAMID=6;
	public static final String SYSPRM_SVARNAME_OPEFF="OE:Weightage%";
	public static final long A_PLUS_MIN_TARGET=3000000; //this is the minimum sales target that an employee should have in order to get A+ rating.
	public static final int A_PLUS_MIN_ACH_PERC=108; //this is the minimum sales acheivement percent that an employee should achieve in order to get A+ rating.
	public static final double A_PLUS_MIN_SCORE=4.40; //this is the minimum score that an employee should achieve in order to get A+ rating.
	public static final List<Integer> MANDATORY_FIELD_ID_LIST = Arrays.asList(0,1,2,3,4,5,6,7);
	public static final List<String> MANDATORY_FIELD_NAME_LIST = Arrays.asList("Ecode", "EmpName", "Yr", "Mth", "TP COMPLIANCE", "STRATEGY COMPLIANCE", "CONVERSION", "DETAILING");
	public static final int ECODE_LENGHT = 5;
	public static List<String> NO_OF_ZERO_LIST = Arrays.asList("0","00","000","0000");
	public static final String SYSPRM_OPEFF_NAMES="OE:Name%";
	public static final int TOTAL_OP_EFF_SCORE=20;
	public static final int EACH_OP_EFF_SCORE=5;
}
