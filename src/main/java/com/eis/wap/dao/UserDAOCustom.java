package com.eis.wap.dao;

import java.util.HashMap;
import java.util.List;

import com.eis.wap.model.Division;

public interface UserDAOCustom {

	public List<Object[]> getAdminUsers(String dbName,String presentEcodes);
	
	public List<Object[]> getLoginDetails(String coCode, String password, String yr, String mth,String fmsDbRef, boolean superpwd);
	
	public Division getDivisionDetails(String divId);
	
	public List<Object[]> getEmployeeList(String year, String month, String hqRef, String level,String fmsDbRef,String dataReqLvl,String emp);
	
	public List<Object[]> getECodeList(List<String> eCodeList,String fmsDbRef);
	
	public List<Object[]> getAllEmpHierarchy(String fmsDbRef,String loginId,String level);
	
	public List<Object[]> getSelEmpHierarchy(String fmsDbRef,String loginId,String level,String selEmp,String yr,String mth);
	
	public List<Object[]> getEmpDetails(String userName, String password,String fmsDbRef,boolean superpwd);
	
	public HashMap<Integer,String> getDivIdDesc();
	
	public HashMap<String, String> getLevelDesc(String fmsDbRef);
	
	public List<Object[]> getReviewEmployeeList(String year, String month, String hqRef, String level, String fmsDbRef,
			String dataReqLvl,String emp,int divId);
	
	public List<Object[]> getEmployeeListForReviewStatus(String year, String month, String hqRef, String level, String fmsDbRef,
			String dataReqLvl,String emp,int divId);
	
	public List<Object[]> getAppraisedEmployees(String hqref,String yr,String mth,String dataReqLvl,int divId,String emp,String fmsDbRef,String level);
	
	public List<Object[]> getAppraisedEmpDtls(String yr,String mth,int divId,String emp,String fmsDbRef,String fmCode);
	
	public List<Object[]> getReviewedEmpList(String year, String month, String hqRef, String level, String fmsDbRef,
			String dataReqLvl,String emp,int divId);
	
	public List<Object[]> getAppraisedEmpDtls2(String yr,String mth,int divId,String emp,String fmsDbRef,String hqref,String level);
}
