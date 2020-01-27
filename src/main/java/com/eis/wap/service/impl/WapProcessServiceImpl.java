package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.constants.CommonConstants;
import com.eis.wap.dao.WapCalDao;
import com.eis.wap.model.WapCalculation;
import com.eis.wap.service.WapProcessService;

@Component
public class WapProcessServiceImpl implements WapProcessService{

	@Autowired
	WapCalDao wapCalDAO;

	public Float getScore(Integer paramId, Float achieve) {
		Float score = wapCalDAO.getScore(achieve, paramId);
		return score;
	}

	@Override
	public List<WapCalculation> getParams() {

		List<WapCalculation> wapCalList = null;
		List<Object[]> slabDataList = null;
		List<Integer> paramIdList = null;

		WapCalculation wapCal = null;

		List<Object[]> paramDataList = wapCalDAO.getParams();

		if(null != paramDataList) {
			paramIdList = new ArrayList<Integer>();
			for(Object[] paramData: paramDataList) {
				paramIdList.add((Integer) paramData[0]);
			}

			if(null != paramIdList) {
				slabDataList = wapCalDAO.getMinRangeAndScoreByParamId(paramIdList);
			}

			if(null != paramDataList && null != slabDataList) {
				wapCal = new WapCalculation();
				wapCalList = new ArrayList<WapCalculation>();
				for(Object[] slabData: slabDataList) {
					Integer sParamId = (Integer) slabData[0];
					wapCal.setAchieve((Float) slabData[1]);
					wapCal.setScore((Float) slabData[2]);
					for(Object[] paramData: paramDataList) {
						Integer paramId = (Integer)paramData[0];
						if(sParamId.equals(paramId)) {
							wapCal.setParam_id(paramId);
							wapCal.setParam_name((String) paramData[1]);
							wapCal.setWeightage((Float) paramData[2]);
							break;
						}
					}
					wapCalList.add(wapCal);
				}
			}
		}

		return wapCalList;
	}

	public LinkedHashMap<String, String> getOpEffNameWeightageInMap(){ 
		List<Object[]> list = (List<Object[]>) wapCalDAO.getOpEffNameWeightage();
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		if(null != list && !list.isEmpty()){
			String key1="";
			String key0="";
			String value="";
			for(Object[] object : list){
				key1=((String)object[0]).split("~")[1];
				if(map.containsKey(key1)){
					key0=((String)object[0]).split("~")[0];
					if(key0.equalsIgnoreCase(CommonConstants.SYSPRM_OPEFF_NAMES.replaceFirst("%", ""))){
						value=map.get(key1)+"~"+(String)object[1];
					}else if(key0.equalsIgnoreCase(CommonConstants.SYSPRM_SVARNAME_OPEFF.replaceFirst("%", ""))){
						value=(String)object[1]+"~"+map.get(key1);
					}
					map.put(key1,value);
				}else{
					map.put(key1,(String)object[1]);
				}
				
			}
		}
		
		System.out.println("map : "+map);
		return map;
	}

	@Override
	public List<Object[]> getWapDetailsOfEmp(String yr, String mth,String netid, String ecode, String div) {
		List list2=new ArrayList<String>();
		List<Object[]> list = (List<Object[]>) wapCalDAO.getWapDetailsOfEmp(yr, mth,netid,ecode,div);
		if(null != list && !list.isEmpty()) {
			list2=(List) Arrays.asList(list.get(0));
		}
		return list2;
	}
	
	public  List<Object[]> getInitWapDetailsOfEmp(String yr,String mth,String netid,String ecode,int div){
		
		List list2=new ArrayList<String>();
		List<Object[]> list = (List<Object[]>) wapCalDAO.getWapDetailsOfEmp2(yr, mth,netid,ecode,div);
		
		if(null != list && !list.isEmpty()) {
			list2=(List) Arrays.asList(list.get(0));
		}
		
		return list2;
	}

	public int addFinalFocusProdForMth(String yr, String mth, String fmsDbRef,	String hqRef, int divId){
		return wapCalDAO.insertIntoFocProd(yr,mth,divId,fmsDbRef,hqRef);
	}

	@Override
	public int getPsrNotReviewedCnt(String yr, String mth, int divId,String fmsDbRef,String loginid) {
		return wapCalDAO.getPsrNotReviewedCnt(yr,mth,divId,fmsDbRef,loginid);
	}
	
	public List<Object[]> getCummWapDetailsOfEmp(String frmyrmth, String toyrmth,String netid, String ecode, String div) {
		//List list2=new ArrayList<String>();
		List<Object[]> list = (List<Object[]>) wapCalDAO.getCummWapDetailsOfEmp(frmyrmth, toyrmth,netid,ecode,div);
		/*if(null != list && !list.isEmpty()) {
			list2=(List) Arrays.asList(list.get(0));
		}*/
		return list;
	}
	
public  List<Object[]> getInitWapDetailsForPsrComnts(String yr,String mth,String netid,String ecode,int div){
		
		List list2=new ArrayList<String>();
		List<Object[]> list = (List<Object[]>) wapCalDAO.getWapDetailsOfEmp3(yr, mth,netid,ecode,div);
		
		if(null != list && !list.isEmpty()) {
			list2=(List) Arrays.asList(list.get(0));
		}
		
		return list2;
	}
}
