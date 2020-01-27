package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.WapFinalScoreAndIncrementDao;
import com.eis.wap.dao.WapFinalScoreIncId;
import com.eis.wap.domain.WapFinalScoreAndIncrementDomain;
import com.eis.wap.model.WapFinalScoreAndIncrement;
import com.eis.wap.service.WapFinalScoreAndIncrementService;

@Component
public class WapFinalScoreAndIncrementServiceImpl implements WapFinalScoreAndIncrementService {

	@Autowired
	WapFinalScoreAndIncrementDao daoObj;

	public void addUsers(List<WapFinalScoreAndIncrement> list) {
		WapFinalScoreAndIncrementDomain domainObj = null;
		WapFinalScoreIncId domainIdObj=null;
		List<WapFinalScoreAndIncrementDomain> domainList = new ArrayList<WapFinalScoreAndIncrementDomain>();
		
			for(WapFinalScoreAndIncrement obj : list) {
				domainIdObj = new WapFinalScoreIncId(); 
				domainObj = new WapFinalScoreAndIncrementDomain();
				
				domainIdObj.setNetid(obj.getNetid());
				domainIdObj.setEcode(obj.getEcode());
				domainIdObj.setMth(obj.getMth());
				domainIdObj.setYr(obj.getYr());
				domainObj.setWapFinalScoreIncId(domainIdObj);
				domainObj.setSalesScore(obj.getSalesScore());
				domainObj.setPobSalesScore(obj.getPobScore());
				domainObj.setDocCoverageScore(obj.getDocCovScore());
				domainObj.setDocReachScore(obj.getDocRchScore());
				domainObj.setPulseChemReachScore(obj.getPulChmScore());
				domainList.add(domainObj);
		}
		//saving all the users to DB
		daoObj.save(domainList);
	}

	@Override
	public Map<String,WapFinalScoreAndIncrementDomain> getScoreAndRating(String yr,String mth,int divId) {
		Map<String,WapFinalScoreAndIncrementDomain> map=new HashMap<String,WapFinalScoreAndIncrementDomain>(); 
		List<Object[]> list=daoObj.getScoreAndRating(yr, mth,divId);
		WapFinalScoreAndIncrementDomain obj=null;
		WapFinalScoreIncId idObj=null;
		 if(null != list && !list.isEmpty()){
			 for(Object[] object : list){
				 idObj=new WapFinalScoreIncId();
				 obj=new WapFinalScoreAndIncrementDomain();
						
				 idObj.setEcode((String)object[1]);
				 idObj.setNetid((String)object[0]);
				 obj.setWapFinalScoreIncId(idObj);
				 obj.setSalesScore((Double)object[2]);
				 obj.setPobSalesScore((Double)object[3]);
				 obj.setDocReachScore((Double)object[4]);
				 obj.setPulseChemReachScore((Double)object[5]);
				 obj.setDocCoverageScore((Double)object[6]);
				 obj.setOtherScore((Double)object[7]);
				 obj.setRating((String)object[8]);
				 obj.setFinalScore((Double)object[9]);
				 map.put((String)object[1], obj);
			 }
			 
		 }
		 return map;
	}


}
