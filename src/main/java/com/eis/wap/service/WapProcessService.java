package com.eis.wap.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eis.wap.model.WapCalculation;

@Service
public interface WapProcessService {
 
	public Float getScore(Integer paramId, Float achieve);
	
	public List<WapCalculation> getParams();
	
	public LinkedHashMap<String, String> getOpEffNameWeightageInMap();
	
	public  List<Object[]> getWapDetailsOfEmp(String yr,String mth,String netid,String ecode,String div);
	
	public  List<Object[]> getInitWapDetailsOfEmp(String yr,String mth,String netid,String ecode,int div); // this is without wap final score and increment
	
	public int addFinalFocusProdForMth(String yr, String mth, String fmsDbRef,	String hqRef, int divId);
	
	public int getPsrNotReviewedCnt(String yr, String mth, int divId,String fmsDbRef,String loginid);
	
	public List<Object[]> getCummWapDetailsOfEmp(String frmyrmth, String toyrmth,String netid, String ecode, String div);
	
	public  List<Object[]> getInitWapDetailsForPsrComnts(String yr,String mth,String netid,String ecode,int div);
}
