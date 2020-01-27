package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.LockMasterDao;
import com.eis.wap.model.LockMaster;
import com.eis.wap.service.LockMasterService;
import com.eis.wap.util.CommonUtils;

@Component
public class LockMasterServiceImpl implements LockMasterService {

	@Autowired
	LockMasterDao lockMasterDAO;

	public List<LockMaster> getAllLocklist(String startdate,String endDate,String condn,int div) {
		List<LockMaster> lockMasterList = new ArrayList<LockMaster>();
		List<com.eis.wap.domain.LockMasterDomain> lockMasterDataList = new ArrayList<com.eis.wap.domain.LockMasterDomain>();
		LockMaster lockMaster = null;
		
		String frmYrmth=	startdate.split("-")[0] + startdate.split("-")[1]; //from yrmth
		//System.out.println(startdate);
		String toYrMth= endDate.split("-")[0] + endDate.split("-")[1]; //to yrmth
		//System.out.println(endDate);
		if(condn.equalsIgnoreCase("all")) {
			lockMasterDataList = lockMasterDAO.getAllLockMasterData(frmYrmth, toYrMth, div);
		}else if(condn.equalsIgnoreCase("locked")) {
			lockMasterDataList = lockMasterDAO.getLockedData(frmYrmth, toYrMth, div);
		}else {
			lockMasterDataList = lockMasterDAO.getUnlockedData(frmYrmth, toYrMth, div);
		}

		if(null != lockMasterDataList) {
			for(com.eis.wap.domain.LockMasterDomain lockMasterData : lockMasterDataList) {
				lockMaster = new LockMaster();
				lockMaster.setLockId(lockMasterData.getLockId());
				lockMaster.setDivId(lockMasterData.getDivId());
				//lockMaster.setDiv_name(lockMasterData.getDivName());
				lockMaster.setMth(lockMasterData.getMth());
				lockMaster.setMonthnm(CommonUtils.getMonth(lockMaster.getMth()));
				lockMaster.setYr(lockMasterData.getYr());
				lockMaster.setYrmth(lockMasterData.getYrmth());
				lockMaster.setIsdataLocked(lockMasterData.getIsDataLocked());
				lockMaster.setLastModBy(lockMasterData.getLastModBy());
				lockMaster.setLastModDt(((lockMasterData.getLastModDt()!=null) ? ((String)lockMasterData.getLastModDt().toString()) : "")); 
				lockMasterList.add(lockMaster);
			}
		}
		return lockMasterList;
	}

	public String unlockLockedEntries(String mth, String yr, int divId) {
		String status = "Failure";
		if(null != mth && null != yr) {
			String yrMth = yr + mth;
			int count = lockMasterDAO.unlockLockedEntries("EIS",yrMth, divId);//CommonUtils.getCurrentDate(),
			if(count > 0) {
				status = "Success";
			}
		}
		return status;
	}
	
	public String isWapProcessed(String mth,String yr,int div){
		return lockMasterDAO.isWapProcessed(mth, yr, div);
	}
	
	public List<String> getLockedYrMth(String finStrtYrMth,String finEndYrMth,int div){
		return lockMasterDAO.getLockedYrMth(finStrtYrMth, finEndYrMth, div);
	}
	
	public List<String> getLockedMthForFinYr(String finStrtYrMth,String finEndYrMth,int div){
		return lockMasterDAO.getLockedMthForFinYr(finStrtYrMth, finEndYrMth, div);
	}
	
		 
}
