package com.eis.wap.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.eis.wap.model.Stockist;
import com.eis.wap.model.WapFinalScoreAndIncrement;

public interface WapCalDaoCustom {

	public Long checkPrimarySalesForYrMth(String yrmth,String salesDbRef);
	
	public List<Stockist> stockistsNotPresentInAllocationTbl(String yrmth,String logYrMth,String yr,String mth,String salesDbRef);
	
	public List<Stockist> checkStockistsAllocationNotHundredPerc(String yrmth,String logYrMth,String salesDbRef);
	
	public LinkedHashMap<String, String> getRecordsFromSysprmInMap(String dbref,String svarname,String delimiter,int arrReq);
	
	public void createTempNetidwithMaxjoinDate(String loginid,String mth,String yr,String hqref,String fmsDbRef);
	
	public Integer deleteExistingrecords(String tblname,String yrmth,int divid);
	
	public void insertIntoWapSalesAchvmtPrimSale(String yrmth,String salesDbRef,String finYr,int finMthNo,String yr,String mth,
			int primSalesAchParamId,String loginId,int divId);
	
	public void insertIntoWapSalesAchvmtPBSale(String yrmth,String salesDbRef,String finYr,int finMthNo,String fmsDbRef,String yr,
			String mth,int poSalesAchParamId,String loginId,int DivId);
	
	public void calDocReachPerc(String salesDbRef,String fmsDbRef,String yr,String mth,int docReachAchParamId,String totDocClassWiseHeaders,String loginid,int divId,String docMetClassWiseHeaders);
	
	public void calPulseChemReachPerc(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int pulseChemAchParamId,
			String docVisitedClassWiseHeaders,String totDocClassWiseHeaders,String loginid,int divId,String logYr,String logMth);
	
	public void getTotalPulseChem(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int pulseChemAchParamId,
			String docVisitedClassWiseHeaders,String totDocClassWiseHeaders,String loginid,String logYr,String logMth);
	
	public void updateTotalPulseChem(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int pulseChemAchParamId,String loginid,int divId);
	//public void calPulseChemAchvmntPerc(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int pulseChemAchParamId);
	
	public void calDocCoveragePerc(String salesDbRef,String fmsDbRef,String yr,String mth,int docCovAchParamId,
			String docVisitedClassWiseHeaders,String totDocClassWiseHeaders,String loginid,int divId);
	
	public int updateLockMasterTolockMonth(String yrmth,String loginid,Date currdate,int divId);
	
	public List<Object[]> getScoreAndIncrement(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int salesParamId,int pobParamId,
			int docRchParamId,int docCovParamId,int pulChmRchParamId,int noOfMEParams,String loginYr,String loginMth,int divId,String loginid);
	
	public void updateMEParamsAcheivement(HashMap<String,String> meParamsWeightageMap,String yr,String mth);
	
	public HashMap<String,Long> getAnnualTargetNetidWise(String finYr,String salesDbRef);
	
	public List<Object[]> getWapDetails(String loginId,String mth, String yr,String fmsDbRef,int divId,String toYr,String toMth,String selEmp);
	
	public List<Object[]> getCummWapDetails(String frmYrMth,String toYrMth,String yr,String mth,String loginid,String fmsDbRef,int divId,String selEmp);
	
	public List<Object[]> getDocCovForOtherClassDocs(String docCls,String vstReqd,String yr,String mth,String fmsdbref);
	
	public List<Object[]> getDocReachForOtherClassDocs(String mth,String yr,String cls,String fmsDbRef);
	
	public List<Object[]> getTotOtherClassDocs(String docCls,String fmsDbRef,String yr,String mth,String loginid);
	
	public ArrayList<String> getAllNetids(String loginid);
	
	public int insertIntoFocProd(String yr, String mth, int divId, String fmsDbRef, String hqRef);
	
	public int getPsrNotReviewedCnt(String yr, String mth, int divId,String fmsDbRef,String loginid);
	
	public void createTempNetidwithMaxjoinDateLvlWise(String loginid,String mth,String yr,String hqref,String fmsDbRef,String lvl);
	
	public void dropTable(String loginid);
}
