package com.eis.wap.service;


import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.eis.wap.model.Product;


@Service
public interface FocusProductService {
	
	public ArrayList<Product> getFocProducts(String d1d2, String fmsDbRef);
	
	public int removeAllFocProducts(String d1d2, String fmsDbRef);

	public int removeSelFocProducts(String hqRef, String fmsDbRef, String selProdArr);
	
	public ArrayList<Product> getAllProds(String hqRef, String fmsDbRef);
	
	public int addFocProds(String hqRef, String fmsDbRef, String selProdArr);
	
	public ArrayList<Product> getLockedFocProducts(String loginYr, String loginMth,int divId, String fmsDbRef);
	
}
