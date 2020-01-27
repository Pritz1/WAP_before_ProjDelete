package com.eis.wap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eis.wap.model.LockMaster;

@Service
public interface LockMasterService {
	
	public List<LockMaster> getAllLocklist(String startdate,String endDate,String condn,int div);
	
	public String unlockLockedEntries(String mth,String yr, int divId);
	
	public String isWapProcessed(String mth,String yr, int divId);
	
	public List<String> getLockedYrMth(String finStrtYrMth,String finEndYrMth, int divId);
	
	public List<String> getLockedMthForFinYr(String finStrtYrMth,String finEndYrMth, int divId);
	
}
