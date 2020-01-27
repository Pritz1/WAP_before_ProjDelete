package com.eis.wap.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.eis.wap.model.SessionUser;

@Service
public interface LoginService {
  
	public SessionUser login(String userName, String password, String date, Integer divId, boolean superpwd);
	
	public HashMap<Integer,String> getDivIdDesc();
	
	public String getLockStatus(String mthYr, int divId); 
}
