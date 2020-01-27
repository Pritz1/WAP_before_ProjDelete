package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.FocusProdDao;
import com.eis.wap.model.Product;
import com.eis.wap.service.FocusProductService;

@Component
public class FocusProductServiceImpl implements FocusProductService{

	@Autowired
	FocusProdDao focProdDao;
	
	public ArrayList<Product> getFocProducts(String d1d2, String fmsDbRef){
		
		ArrayList<Object[]> list = (ArrayList<Object[]>) focProdDao.getFocProdFromProdMst(d1d2,fmsDbRef);
		ArrayList<Product> prodList = new ArrayList<Product>();
		if(null != list && !list.isEmpty()){

			for(Object[] object : list){

				Product prodObj=new Product();
				prodObj.setProdid((String)object[0]);
				prodObj.setProdName((String)object[1]);
				prodObj.setDivId((String)object[2]);
				prodList.add(prodObj);	
			}
		}
		return prodList;
	}

	@Override
	public int removeAllFocProducts(String hqRef, String fmsDbRef) {
		
		return focProdDao.removeAllFocProd(hqRef,fmsDbRef);
	}

	@Override
	public int removeSelFocProducts(String hqRef, String fmsDbRef,String selProdArr) {
		return focProdDao.removeSelFocProducts(hqRef,fmsDbRef,selProdArr);
	}
	
	public ArrayList<Product> getAllProds(String hqRef,String fmsDbRef){
		ArrayList<Object[]> list = (ArrayList<Object[]>) focProdDao.getAllProds(hqRef,fmsDbRef);
		ArrayList<Product> prodList = new ArrayList<Product>();
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				Product prodObj=new Product();
				prodObj.setProdid((String)object[0]);
				prodObj.setProdName((String)object[1]);
				prodObj.setDivId((String)object[2]);
				prodList.add(prodObj);	
			}
		}
		return prodList;
	}

	@Override
	public int addFocProds(String hqRef, String fmsDbRef, String selProdArr) {
		return focProdDao.addFocProds(hqRef,fmsDbRef,selProdArr);
	}

	@Override
	public ArrayList<Product> getLockedFocProducts(String loginYr,String loginMth, int divId, String fmsDbRef) {
		ArrayList<Object[]> list =focProdDao.getCurrLockedFocProd(loginYr,loginMth,divId,fmsDbRef);
		ArrayList<Product> prodList = new ArrayList<Product>();
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				Product prodObj=new Product();
				prodObj.setProdid((String)object[0]);
				prodObj.setProdName((String)object[1]);
				prodObj.setDivId((String)object[2]);
				prodList.add(prodObj);	
			}
		}
		return prodList;
	}
	
	
	
}
