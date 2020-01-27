package com.eis.wap.dao;

import java.util.ArrayList;

import com.eis.wap.model.Product;

public interface FocusProdDaoCustom {
	
	public ArrayList<Object[]> getFocProdFromProdMst(String d1d2,String fmsDb);
	
	public int removeAllFocProd(String hqRef,String fmsDbRef);
	
	public int removeSelFocProducts(String d1d2, String fmsDbRef,String prodidArr);
	
	public ArrayList<Object[]> getAllProds(String d1d2,String fmsDb);
	
	public int addFocProds(String hqRef, String fmsDbRef,String prodidArr);
	
	public ArrayList<Object[]> getCurrLockedFocProd(String loginYr,String loginMth,int divId,String fmsDbRef);
	
	public ArrayList<Object[]> getFocProducts(String frmYrMth,String toYrMth,int divId,String fmsDbRef);
	
}
