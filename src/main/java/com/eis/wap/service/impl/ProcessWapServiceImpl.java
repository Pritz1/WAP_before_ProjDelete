package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.constants.CommonConstants;
import com.eis.wap.controllers.WapMenuController;
import com.eis.wap.dao.DClassDocCovDao;
import com.eis.wap.dao.DClassDocCovId;
import com.eis.wap.dao.IncrementDao;
import com.eis.wap.dao.LockMasterDao;
import com.eis.wap.dao.OtherClassDocRchAndCovDao;
import com.eis.wap.dao.OtherClassDocRchCovId;
import com.eis.wap.dao.ParamMasterDAO;
import com.eis.wap.dao.SlabDao;
import com.eis.wap.dao.WapCalDao;
import com.eis.wap.dao.WapFinalScoreAndIncrementDao;
import com.eis.wap.dao.WapFinalScoreIncId;
import com.eis.wap.dao.YTDWapDao;
import com.eis.wap.domain.DClassDocCov;
import com.eis.wap.domain.OtherClassDocRchAndCov;
import com.eis.wap.domain.WapFinalScoreAndIncrementDomain;
import com.eis.wap.domain.YTDWapDomain;
import com.eis.wap.model.CummWapDetails;
import com.eis.wap.model.ProcessWap;
import com.eis.wap.model.SessionUser;
import com.eis.wap.model.Stockist;
import com.eis.wap.model.TotalDocs;
import com.eis.wap.model.WapFinalScoreAndIncrement;
import com.eis.wap.service.ProcessWapService;
import com.eis.wap.util.CommonUtils;

@Component
public class ProcessWapServiceImpl implements ProcessWapService{

	@Autowired
	WapCalDao wapCalDao;

	@Autowired
	ParamMasterDAO paramMasterDao;

	@Autowired
	SlabDao slabDao;

	@Autowired
	IncrementDao incrDao;

	@Autowired 
	WapFinalScoreAndIncrementDao wapFinalDaoObj;

	@Autowired 
	OtherClassDocRchAndCovDao otherClassDocRchCovDao;

	@Autowired
	DClassDocCovDao dclassDocCovDaoObj;
	
	@Autowired 
	YTDWapDao ytdWapDaoObj;
/*	
	@Autowired 
	LockMasterDao lockMstDaoObj;*/
	
	private static final Logger logger = Logger.getLogger(WapMenuController.class);	

	@SuppressWarnings("unchecked")
	public HashMap<String, String> processAndLockWap(ProcessWap processWap,SessionUser user,LinkedHashMap monthsMap) {
		/*Note : Nettrans and Allocation is condidered of the logged in yr and mth  
		 * */
		
		//TODO make a property file to keep which parameters to be inserted in which table
		// TODO to check whether prev financial year also contains unlocked data.
		/*
		 * 1. HSE : Hard Stop Error 
		 * 2. SSE : Soft Stop Error*/
		boolean success=false;
		HashMap<String,String> error=new HashMap<String,String>();
		try{
			
			String loginid=user.getEmp();//"eis";//
			String salesDbRef=user.getSalesDbRef();//"A";//
			String fmsDbRef=user.getFmsDbRef();//"palsons1";// 
			String hqref=user.getHqRef();//"A";//
			String wapDb="wap1"; //get from constant or property file
			int divId=user.getDivId();//5;//get from session
			String finYr=user.getFinStartDt().split("-")[0].substring(2)+user.getFinEndDt().split("-")[0].substring(2);//"1819"; 
			String loginMth=user.getLoginMth();
			String loginYr=user.getLoginYr();
			String finStartYrMth=user.getFinStartDt().split("-")[0]+user.getFinStartDt().split("-")[1];//"201804";

			//System.out.println("--lockunlockedEntries--");
			String yr=processWap.getYr();
			String mth=processWap.getMth();
			String yrmth=processWap.getYrMth();//yr+mth;
			System.out.println("--yrmth-- : "+yrmth);

			//Iterator itr=yrmthlist.iterator();
			//String sql="";
			String errorDesc="";
			int level=1; //TODO get from jsp

			/* 1.check whether primary sales is present in DB for that mth.*/
			if((salesDbRef!=null && !salesDbRef.equalsIgnoreCase("")) && (fmsDbRef!=null && !fmsDbRef.equalsIgnoreCase("")) )	 
			{	 
				processWap.setTotPriSales(wapCalDao.checkPrimarySalesForYrMth(yrmth,salesDbRef));

				System.out.println("pwap.getTotPriSales : "+processWap.getTotPriSales());
				if(processWap.getTotPriSales() > 0)
				{
					//2. check whether all stockists are 100% allocated 
					//TODO allocation to be considered of which yrmth
					List<Stockist> unallocatedStlist = wapCalDao.stockistsNotPresentInAllocationTbl(yrmth,loginYr+loginMth,yr,mth,salesDbRef);
					if(unallocatedStlist!=null && unallocatedStlist.size()>0)
					{
						processWap.setStList(unallocatedStlist);
						errorDesc="WAP Cannot Be Processed as Some Stockists are Not Allocated : "+mth+" Year: "+yr;
						error.put("errCode",CommonConstants.ERROR_CODE_HSE);
						error.put("errDesc", errorDesc);
						return error;
					}else{
						unallocatedStlist=wapCalDao.checkStockistsAllocationNotHundredPerc(yrmth,loginYr+loginMth,salesDbRef);
						if(unallocatedStlist!=null && unallocatedStlist.size()>0)
						{
							processWap.setStList(unallocatedStlist);
							errorDesc="WAP Cannot Be Processed as Stockists Allocation is not 100% : "+mth+" Year: "+yr;
							error.put("errCode", CommonConstants.ERROR_CODE_HSE);
							error.put("errDesc", errorDesc);
							return error;
						}else{
							int finMthNo= (new ArrayList<String>(monthsMap.keySet()).indexOf(yrmth))+1;
							System.out.println("finMthNo : "+finMthNo+"~monthsMap.keySet() : "+monthsMap.keySet()+"~mth : "+mth);
							String currdate=CommonUtils.getCurrentDate(new Date());
							//if only present records are needed then -> join with empmst and put this condn (e.resdate='0000-00-00' or e.resdate>'0000-00-00')
							LinkedHashMap<String,String> docClassVal=wapCalDao.getRecordsFromSysprmInMap(fmsDbRef,"%class%","",0);
							/*Set<String> valueSet=(Set) docClassVal.values();
							Integer visitArr[]=new Integer[valueSet.size()];
							int i=0;
							for(String value : valueSet){
								visitArr[i]=Integer.parseInt((String)value.split(";")[1]);
								i++;
							}
							Arrays.sort(visitArr); //, Collections.reverseOrder()*/
							System.out.println("--docClassVal-- : "+docClassVal);
							if(docClassVal!=null && !docClassVal.isEmpty()){//if classes are defined in sysprm then continue
								String docVisitedClassWiseCovHeaders=getDocVisitedClassWiseCovHeaders(CommonConstants.MAX_VISIT_IN_DB,docClassVal);
								String totDocClassWiseHeaders=getTotDocsHeaders(CommonConstants.MAX_VISIT_IN_DB,docClassVal);
								String docMetClassWiseHeaders=getDocsMetHeaders(CommonConstants.MAX_VISIT_IN_DB,docClassVal);
								// 3. calculate sales Ach% and POB Sales Ach%
								Integer rows=wapCalDao.deleteExistingrecords("WapSalesAchvmt", yrmth,divId);
								if(rows==null){
									/*errorDesc="WAP Cannot Be Processed: Exception Occured While Deleting Records (WapSalesAchvmt): "+mth+" Year: "+yr;
									error.put("errCode", CommonConstants.ERROR_CODE_HSE);
									error.put("errDesc", errorDesc);
									return error;*/
									return CommonUtils.showError("WAP Cannot Be Processed: Exception Occured While Deleting Records (WapSalesAchvmt): "+mth+" Year: "+yr,CommonConstants.ERROR_CODE_HSE,error);
								}
								rows=wapCalDao.deleteExistingrecords("WapOtherParamsAchvmt", yrmth,divId);
								if(rows==null){
									/*errorDesc="WAP Cannot Be Processed: Exception Occured While Deleting Records (WapOtherParamsAchvmt): "+mth+" Year: "+yr;
									error.put("errCode", CommonConstants.ERROR_CODE_HSE);
									error.put("errDesc", errorDesc);
									return error;*/
									return CommonUtils.showError("WAP Cannot Be Processed: Exception Occured While Deleting Records (WapOtherParamsAchvmt): "+mth+" Year: "+yr,CommonConstants.ERROR_CODE_HSE,error);
								}
								wapCalDao.createTempNetidwithMaxjoinDate(loginid,loginMth,loginYr,hqref,fmsDbRef);
								System.out.println("----------------created TempNetidwithMaxjoinDate ----------------");
								if(finMthNo>0){
									wapCalDao.insertIntoWapSalesAchvmtPrimSale(yrmth,salesDbRef,finYr,finMthNo,yr,mth,CommonConstants.SALES_PARAMID,loginid,divId);  
									wapCalDao.insertIntoWapSalesAchvmtPBSale(yrmth,salesDbRef,finYr,finMthNo,fmsDbRef,yr,mth,CommonConstants.POB_PARAMID,loginid,divId);
									//4. calculate REACH (DOCTOR) %
									//rows=wapCalDao.deleteExistingrecords("otherclassdocrchandcov", yrmth,divId);
									//rows=wapCalDao.deleteExistingrecords("dclassdoccov", yrmth,divId);

									//getDocRchAndCovForOtherClassDocs(docClassVal,yr,mth,fmsDbRef,loginid,divId);
									wapCalDao.calDocReachPerc(salesDbRef,fmsDbRef,yr,mth,CommonConstants.DOC_REACH_PARAMID,totDocClassWiseHeaders,loginid,divId,docMetClassWiseHeaders);

									//5. calculate REACH (COVERAGE) % 
									wapCalDao.calDocCoveragePerc(salesDbRef,fmsDbRef,yr,mth,CommonConstants.COVERAGE_PARAMID,docVisitedClassWiseCovHeaders,totDocClassWiseHeaders,loginid,divId); //TODO get specific headers only for VISITEDDOCS

									//6. calculate REACH (PULSE CHEMIST) % 
									wapCalDao.calPulseChemReachPerc(yrmth,salesDbRef,fmsDbRef,yr,mth,CommonConstants.PULSE_CHEM_REACH_PARAMID,docVisitedClassWiseCovHeaders,totDocClassWiseHeaders,loginid,divId,loginYr,loginMth);

									wapCalDao.deleteExistingrecords("wap_final_scoreandincrement", yrmth,divId);

									/*Param wise slab's min range and score in map => Slabs table: starts*/
									List<Object[]> paramWiseSlabsScore=slabDao.getParamWiseSlabsScore();//{paramid={slabsMinRange1=score1,SlabsMinRange2=score2},....}

									HashMap<Integer, TreeMap<Float, Float>> paramWiseSlabsScoreMap=new HashMap<Integer,TreeMap<Float,Float>>();

									TreeMap<Float,Float> innerSlabsMap=new TreeMap<Float,Float>(Collections.reverseOrder());
									if(null != paramWiseSlabsScore && !paramWiseSlabsScore.isEmpty()){
										for(Object[] obj: paramWiseSlabsScore){
											if(paramWiseSlabsScoreMap.containsKey(obj[0])){
												innerSlabsMap=new TreeMap<Float,Float>(Collections.reverseOrder());
												innerSlabsMap=(TreeMap<Float,Float>) paramWiseSlabsScoreMap.get(obj[0]);
												if(null != innerSlabsMap){
													innerSlabsMap.put((Float)obj[1],(Float)obj[3]);
												}else{
													innerSlabsMap=new TreeMap<Float,Float>(Collections.reverseOrder());
													innerSlabsMap.put((Float)obj[1],(Float)obj[3]);
												}
												paramWiseSlabsScoreMap.put((Integer)obj[0],innerSlabsMap);
											}else{
												innerSlabsMap=new TreeMap<Float,Float>(Collections.reverseOrder());
												innerSlabsMap.put((Float)obj[1],(Float)obj[3]);
												paramWiseSlabsScoreMap.put((Integer)obj[0],innerSlabsMap);
											}
										}
									}
									System.out.println("paramWiseSlabsScoreMap : "+paramWiseSlabsScoreMap);
									/*Param wise slab's min range and score in map => Slabs table: starts*/

									/*{paramId1=wightage,paramId2=weightage2...} => param_master table*/
									List<Object[]> paramIdWeightageList=paramMasterDao.getParamIdAndWeightage(level);
									HashMap<Integer,Float> paramIdWeightageMap=new HashMap<Integer,Float>();
									if(null != paramIdWeightageList && !paramIdWeightageList.isEmpty()){
										for(Object[] obj: paramIdWeightageList){
											paramIdWeightageMap.put((Integer)obj[0], (Float)obj[1]);
										}
									}
									/*{paramId1=wightage,paramId2=weightage2...} => param_master table : end*/
									List<Object[]> incrementList=incrDao.getScoreWiseRatingIncr(); 
									//TreeMap<Float,String> scoreRatingMap=new TreeMap<Float,String>(Collections.reverseOrder());/*key=min_score and value=rating*/
									LinkedHashMap <Float,String> scoreRatingMap=new LinkedHashMap<Float, String>();/*key=min_score and value=rating*/
									if(incrementList!=null){
										for(Object[] obj : incrementList){
											scoreRatingMap.put((Float)obj[0],(String) obj[2]); //+"~"+obj[3]
										}
									}
									System.out.println("scoreIncrementMap : "+scoreRatingMap);
									/*7. get the score,rating and increment for each employee */

									LinkedHashMap<String,String> mgrEffWeightagePerParam= wapCalDao.getRecordsFromSysprmInMap(wapDb,CommonConstants.SYSPRM_SVARNAME_OPEFF,"~",1);
									if(null != mgrEffWeightagePerParam && !mgrEffWeightagePerParam.isEmpty())
									{

									}else{
										return CommonUtils.showError("Weightage Is Not Defined For Params under Manager's Effectiveness ",CommonConstants.ERROR_CODE_HSE,error);
									}

									wapCalDao.updateMEParamsAcheivement(mgrEffWeightagePerParam,yr,mth);
									List<Object[]> list= wapCalDao.getScoreAndIncrement(yrmth,salesDbRef,fmsDbRef,yr,mth,CommonConstants.SALES_PARAMID,CommonConstants.POB_PARAMID,
											CommonConstants.DOC_REACH_PARAMID,CommonConstants.COVERAGE_PARAMID,CommonConstants.PULSE_CHEM_REACH_PARAMID,mgrEffWeightagePerParam.size(),loginYr,loginMth,divId,loginid);

									//gets netid,level1,sales.AcheivementPerc,pob.acheivementPerc,docrch.achievementPerc,docCov.AchievementPerc,pulChmRch.achievementPerc
									ArrayList<WapFinalScoreAndIncrement> list2=new ArrayList<WapFinalScoreAndIncrement>(); 
									ArrayList<CummWapDetails> list3=new ArrayList<CummWapDetails>();
									//ArrayList<String> netids=new ArrayList<String>();
									double totalScore=0.0;
									if(null != list && !list.isEmpty()){
										Float weightage=0.0F;
										//TreeMap is used to Get Descending Sorted List Of Values
										TreeMap<Float,Float> salesSlabMap=null; /*key=slab's min range, Value=Score => SLABS Table*/
										TreeMap<Float,Float> pobSlabMap=null;
										TreeMap<Float,Float> docRchSlabMap=null;
										TreeMap<Float,Float> docCovSlabMap=null;
										TreeMap<Float,Float> pulChmRchSlabMap=null;
										ArrayList<Float> salesMinScores=null;
										ArrayList<Float> pobMinScores=null;
										ArrayList<Float> docrchMinScores=null;
										ArrayList<Float> docCovMinScores=null;
										ArrayList<Float> pulChmMinScores=null;

										//TODO display param names from param_master for the errors

										if(paramWiseSlabsScoreMap!=null){
											salesSlabMap=paramWiseSlabsScoreMap.get(CommonConstants.SALES_PARAMID); /*{slabMinRange1=Score1,slabMinRange2=Score2....} for particular param based on paramId*/
											pobSlabMap=paramWiseSlabsScoreMap.get(CommonConstants.POB_PARAMID);
											docRchSlabMap=paramWiseSlabsScoreMap.get(CommonConstants.DOC_REACH_PARAMID);
											docCovSlabMap=paramWiseSlabsScoreMap.get(CommonConstants.COVERAGE_PARAMID);
											pulChmRchSlabMap=paramWiseSlabsScoreMap.get(CommonConstants.PULSE_CHEM_REACH_PARAMID);

											System.out.println("*************salesSlabMap : "+salesSlabMap+"\n pobSlabMap : "+pobSlabMap+
													"\n docRchSlabMap : "+docRchSlabMap+" \n docCovSlabMap : "+docCovSlabMap+"\n pulChmRchSlabMap : "+pulChmRchSlabMap);


											if(null != salesSlabMap)
												salesMinScores=new ArrayList<Float>(salesSlabMap.keySet()); /*descending sorted keyset as list => to set score based on result received after comparisons */
											else return CommonUtils.showError("Slabs Not Defined For Sales Param ",CommonConstants.ERROR_CODE_HSE,error);
											if(null != pobSlabMap)
												pobMinScores=new ArrayList<Float>(pobSlabMap.keySet());
											else return CommonUtils.showError("Slabs Not Defined For POB Param ",CommonConstants.ERROR_CODE_HSE,error);
											if(null != docRchSlabMap)
												docrchMinScores=new ArrayList<Float>(docRchSlabMap.keySet());
											else return CommonUtils.showError("Slabs Not Defined For Doc Reach Param ",CommonConstants.ERROR_CODE_HSE,error);
											if(null != docCovSlabMap)
												docCovMinScores=new ArrayList<Float>(docCovSlabMap.keySet());
											else return CommonUtils.showError("Slabs Not Defined For Doc Cov Param ",CommonConstants.ERROR_CODE_HSE,error);
											if(null != pulChmRchSlabMap)
												pulChmMinScores=new ArrayList<Float>(pulChmRchSlabMap.keySet());
											else return CommonUtils.showError("Slabs Not Defined For Pulse Chem Reach Param ",CommonConstants.ERROR_CODE_HSE,error);
										}

										ArrayList<Float> incrMinScoreList=null;
										if(null != scoreRatingMap){
											incrMinScoreList=new ArrayList<Float>(scoreRatingMap.keySet());    
										}

										WapFinalScoreAndIncrement wapobj=null;
										HashMap<String,Long> annualTargetMap=wapCalDao.getAnnualTargetNetidWise(finYr,salesDbRef); //get annual targets per netid. Key=netid,value=annualTarget 
										if(null == annualTargetMap || annualTargetMap.isEmpty()){
											return CommonUtils.showError("Target Is Not Defined For The Year ",CommonConstants.ERROR_CODE_HSE,error);
										}
										double aPlusMinScore=CommonConstants.A_PLUS_MIN_SCORE; 
										Number num=0;
										for(Object[] object : list){
											
											wapobj=new WapFinalScoreAndIncrement();
											wapobj.setNetid((String)object[0]);
											
											wapobj.setEcode((String)object[1]);
											wapobj.setYr(yr);
											wapobj.setMth(mth);
											wapobj.setAnnualTarget(annualTargetMap.get(wapobj.getNetid()));
											wapobj.setDivId(divId);
											/*Set Achievement Percent As Received*/
											num=(Number)object[2];
											wapobj.setSalesAch(num.doubleValue());
											num=(Number)object[3];
											wapobj.setPobSalesAch(num.doubleValue());
											num=(Number)object[4];
											wapobj.setDocReachAch(num.doubleValue());
											num=(Number)object[5];
											wapobj.setDocCovAch(num.doubleValue());
											num=(Number)object[6];
											wapobj.setPulseChemAch(num.doubleValue());
											num=(Number)object[7];
											wapobj.setMgrEffAch(num!=null ? num.doubleValue() : 0); /*this can be considered points also as each score given by manager is divided by 25 
																									and multiplied by 00 to get some points...and sum of all points ultimately becomes operational eff points*/  
											
											/*Set Achievement Percent As Received : End*/

											/*Set Score as per Achievement Percent i.e. if achievement percent lies within a a specific range a slab of a param then get points against it.*/
											if(wapobj.getSalesAch()>0){
												for(Float minSlab : salesMinScores){
													if(wapobj.getSalesAch()>=minSlab){ //compares salesAch with the highest min score of slab range first,the 2nd highest so on...lowest
														//value in the last,if sales ach is greater than score(min slab range) then that score is set.
														wapobj.setSalesPoints(salesSlabMap.get(minSlab));
														break;
													}
												}
											}
											if(wapobj.getPobSalesAch()>0 && null!=pobMinScores){
												for(Float minSlab : pobMinScores){
													if(wapobj.getPobSalesAch()>=minSlab){
														wapobj.setPobSalesPoints(pobSlabMap.get(minSlab));
														break;
													}
												}
											}	
											if(wapobj.getDocReachAch()>0 && null!=docrchMinScores){
												for(Float minSlab : docrchMinScores){
													if(wapobj.getDocReachAch()>=minSlab){
														wapobj.setDocReachPoints(docRchSlabMap.get(minSlab));
														break;
													}
												}
											}	
											if(wapobj.getDocCovAch()>0 && null!=docCovMinScores){
												for(Float minSlab : docCovMinScores){
													if(wapobj.getDocCovAch()>=minSlab){
														wapobj.setDocCovPoints(docCovSlabMap.get(minSlab));
														break;
													}
												}
											}
											if(wapobj.getPulseChemAch()>0 && null!=pulChmMinScores){
												for(Float minSlab : pulChmMinScores){
													if(wapobj.getPulseChemAch()>=minSlab){
														wapobj.setPulChmRchPoints(pulChmRchSlabMap.get(minSlab));
														break;
													}
												}
											}

											/*Set Score as per Acheivement Percent i.e. if acheivement percent lies within a a specific range a slab of a param then get points against it. : end*/	

											/*Set Score as per score calculated. Formula: if weightage of sale=45%
											 * Score = 4
											 * then sales%=45% of 4*/
											//1. sales
											weightage=(Float)paramIdWeightageMap.get(CommonConstants.SALES_PARAMID);
											wapobj.setSalesScore((wapobj.getSalesPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,wapobj.getSalesPoints()) : 0.0);			

											//2. POB Sales
											weightage=(Float) paramIdWeightageMap.get(CommonConstants.POB_PARAMID);
											wapobj.setPobScore((wapobj.getPobSalesPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,wapobj.getPobSalesPoints()) : 0.0);

											//3.Docreach
											weightage=(Float) paramIdWeightageMap.get(CommonConstants.DOC_REACH_PARAMID);
											wapobj.setDocRchScore((wapobj.getDocReachPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,wapobj.getDocReachPoints()) : 0.0);

											//4.Doc Coverage
											weightage=(Float) paramIdWeightageMap.get(CommonConstants.COVERAGE_PARAMID);
											wapobj.setDocCovScore((wapobj.getDocCovPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,wapobj.getDocCovPoints()) : 0.0);

											//5. Pulse Chem Reach
											weightage=(Float) paramIdWeightageMap.get(CommonConstants.PULSE_CHEM_REACH_PARAMID);
											wapobj.setPulChmScore((wapobj.getPulChmRchPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,wapobj.getPulChmRchPoints()) : 0.0);

											//6. Manager's Effectiveness : does not required points here,as slabs are not there for Manager's Eff and already 25% of each param is calculated and added to get ach
											weightage=(Float) paramIdWeightageMap.get(CommonConstants.MANAGERIAL_EFF_PARAMID);
											wapobj.setMgrEffScore((wapobj.getMgrEffAch() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,wapobj.getMgrEffAch()) : 0.0);
											/*Set Score as per score calculated : end*/

											/*Get Rating : start*/
											totalScore=wapobj.getMgrEffScore()+wapobj.getPulChmScore()+wapobj.getDocCovScore()+wapobj.getDocRchScore()+
													wapobj.getPobScore()+wapobj.getSalesScore();
											System.out.println("totalScore : "+totalScore);
											wapobj.setTotalScore(totalScore);

											if(totalScore>=aPlusMinScore && wapobj.getAnnualTarget()!=null && wapobj.getAnnualTarget()>=CommonConstants.A_PLUS_MIN_TARGET
													&& wapobj.getSalesAch()>CommonConstants.A_PLUS_MIN_ACH_PERC){
												wapobj.setRating("A+");
											}
											else if(totalScore>0 && null!=incrMinScoreList){
												for(Float minIncrScore : incrMinScoreList){
													if(totalScore>=minIncrScore){
														wapobj.setRating(scoreRatingMap.get(minIncrScore));
														break;
													}
												}
											}
											else{
												wapobj.setRating(scoreRatingMap.get(0.0f));
												System.out.println("totalScore is below 0: "+totalScore);
											}
											/*Get Rating : end*/

											list2.add(wapobj);
											//netids.add(wapobj.getNetid());
										}
										
										List<Object[]>  cummWapDtlsList=wapCalDao.getCummWapDetails(finStartYrMth,loginYr+loginMth, yr, mth, loginid, fmsDbRef, divId,"");
										
										list3=setAndGetCummWapMap(cummWapDtlsList,salesMinScores,pobMinScores,docrchMinScores,
												docCovMinScores,pulChmMinScores,paramIdWeightageMap,salesSlabMap,pobSlabMap,docRchSlabMap,
												docCovSlabMap,pulChmRchSlabMap,scoreRatingMap,annualTargetMap,finStartYrMth,yrmth,Integer.parseInt(yr),Integer.parseInt(mth));
										
									}else{
										return CommonUtils.showError("No Data Received From Database ",CommonConstants.ERROR_CODE_HSE,error);
									}
									try{
									saveWapFinalScoreAndIncrementList(list2);
									}catch(Exception e){
										errorDesc="WAP Cannot Be Processed as Unable Save Wap Final Score And Increment List : "+mth+" Year: "+yr;
										error.put("errCode", CommonConstants.ERROR_CODE_HSE);
										error.put("errDesc", errorDesc);
										e.printStackTrace();
									}
									
									try{
										saveYtdWapDtlsList(list3,mth,yr,divId);
										}catch(Exception e){
											errorDesc="WAP Cannot Be Processed as Unable Save YTD WAP Details : "+mth+" Year: "+yr;
											error.put("errCode", CommonConstants.ERROR_CODE_HSE);
											error.put("errDesc", errorDesc);
											e.printStackTrace();
										}
									success=true;
									
								}	
								else{
									//add error
									errorDesc="WAP Cannot Be Processed as Unable To Get finMthNo : "+mth+" Year: "+yr;
									error.put("errCode", CommonConstants.ERROR_CODE_HSE);
									error.put("errDesc", errorDesc);
									return error; 
								}
							}else{
								errorDesc="WAP Cannot Be Processed as Unable To Get Doctor Class From Sysprm : "+mth+" Year: "+yr;
								error.put("errCode", CommonConstants.ERROR_CODE_HSE);
								error.put("errDesc", errorDesc);
								return error; 
							}
						}
					}
				}
				else
				{
					errorDesc="WAP Cannot Be Processed as Sales Is Not Present For Month : "+mth+" Year: "+yr;
					error.put("errCode", CommonConstants.ERROR_CODE_HSE);
					error.put("errDesc", errorDesc);
					return error; 
				}
			}
			else{
				errorDesc="Db Reference is Not Provided - Please Contact System Administrator. Sales : "+salesDbRef + " FMS : "+fmsDbRef;
				error.put("errCode", CommonConstants.ERROR_CODE_HSE);
				error.put("errDesc", errorDesc);
				return error;
			}
		}catch(Exception e){
			if(null!=user)
				logger.error("Custom Error - ProcessWapServiceImpl : User : "+user.getEmp()+" Date Time"+user.getCurrDate(),e);
			else
				logger.error("Custom Error - ProcessWapServiceImpl : "+new java.util.Date(),e);
			//e.printStackTrace();
			error.put("errCode", CommonConstants.ERROR_CODE_HSE);
			error.put("errDesc", e.toString());
		}
		
		if(error!=null && !error.isEmpty())
			return error;
		else if(!success)
			return CommonUtils.showError("WAP PROCESSING IS NOT SUCCESSFUL",CommonConstants.ERROR_CODE_HSE , error);
		else
			return CommonUtils.showError("WAP PROCESSING IS SUCCESSFUL",CommonConstants.SUCCESS_CODE , error);
	}
	//show columns from mgreffectivenessparams like 'param%';
	//this gets all the max doc visits or more than the max visit defined in system.
	//o/p is like -> C_A_CLASS_DOCTORS_VISITED_THRICE+C_B_CLASS_DOCTORS_VISITED_THRICE+C_B_CLASS_DOCTORS_VISITED_TWICE+C_C_CLASS_DOCTORS_VISITED_THRICE+C_C_CLASS_DOCTORS_VISITED_TWICE+C_C_CLASS_DOCTORS_VISITED_ONCE+C_E_CLASS_DOCTORS_VISITED_THRICE+C_E_CLASS_DOCTORS_VISITED_TWICE+C_E_CLASS_DOCTORS_VISITED_ONCE+C_D_CLASS_DOCTORS_VISITED_8nMoreThan8Vst+C_F_CLASS_DOCTORS_VISITED_FOURTH	
	public String getDocVisitedClassWiseCovHeaders(int maxVisitInDb,LinkedHashMap<String,String> docClassVal){
		String docClass="";
		//ArrayList<String> headers=new ArrayList<String>();
		StringBuffer headers=new StringBuffer();
		//String visitsClass="";
		String noOfvisits="";
		int flag=0;
		if(docClassVal!=null){
			for(Map.Entry<String, String> entry : docClassVal.entrySet()){
				docClass=entry.getKey();
				if(docClass!=null){
					docClass=docClass.substring(docClass.length()-1);
				}
				/*if(docClass!=null && docClass.equalsIgnoreCase("A") || docClass.equalsIgnoreCase("B") 
						|| docClass.equalsIgnoreCase("C")){*/
				flag++;
				noOfvisits=((String)entry.getValue()).split(";")[1];
				for(int i=maxVisitInDb;i>=Integer.parseInt(noOfvisits);i--){ //as of now all classes has thrice visits but D & F has 4nleassThan8Vst,8nMoreThan8vst and fourth visit respectively 
					if(flag==1)
						headers.append("C_"+docClass+"_CLASS_DOCTORS_VISITED_"+getNoInWords(i));
					else
						headers.append("+C_"+docClass+"_CLASS_DOCTORS_VISITED_"+getNoInWords(i));	
				}
				//}
			}
			headers.append("+C_D_CLASS_DOCTORS_VISITED_8nMoreThan8Vst+C_F_CLASS_DOCTORS_VISITED_"+getNoInWords(4));
		}
		System.out.println("getdocVisitedClassWiseHeaders : "+headers.toString());
		return headers.toString();
	}

	public String getNoInWords(int i){
		String value="";

		switch(i){
		case 1: value= "ONCE";
		break;
		case 2: value= "TWICE";
		break;
		case 3: value= "THRICE";
		break;
		case 4: value= "FOURTH";
		break;
		default:value="";
		break;
		}
		return value;
	}
	public String getTotDocsHeaders(int maxVisitInDb,LinkedHashMap<String,String> docClassVal){
		StringBuffer headers=new StringBuffer();
		String docClass="";
		int flag=0;
		if(docClassVal!=null){
			for(Map.Entry<String, String> entry : docClassVal.entrySet()){
				docClass=entry.getKey();
				if(docClass!=null){
					docClass=docClass.substring(docClass.length()-1);
				}
				/*if(docClass!=null && docClass.equalsIgnoreCase("A") || docClass.equalsIgnoreCase("B") 
						|| docClass.equalsIgnoreCase("C")){*/
				flag++;
				if(flag==1)
					headers.append("C_TOTAL_"+docClass+"_CLASS_DOCTORS");
				else
					headers.append("+C_TOTAL_"+docClass+"_CLASS_DOCTORS");	
			}
			//}
		}
		return headers.toString();
	}
	public String getDocsMetHeaders(int maxVisitInDb,LinkedHashMap<String,String> docClassVal){
		StringBuffer headers=new StringBuffer();
		String docClass="";
		int flag=0;
		if(docClassVal!=null){
			for(Map.Entry<String, String> entry : docClassVal.entrySet()){
				docClass=entry.getKey();
				if(docClass!=null){
					docClass=docClass.substring(docClass.length()-1);
				}
				/*if(docClass!=null && docClass.equalsIgnoreCase("A") || docClass.equalsIgnoreCase("B") 
						|| docClass.equalsIgnoreCase("C")){*/
				flag++;
				if(flag==1)
					headers.append("C_TOTAL_"+docClass+"_CLASS_DOCTORS_MET");
				else
					headers.append("+C_TOTAL_"+docClass+"_CLASS_DOCTORS_MET");	
			}
			//}
		}
		return headers.toString();
	}

	public void saveWapFinalScoreAndIncrementList(List<WapFinalScoreAndIncrement> list) {
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
			domainIdObj.setDivId(obj.getDivId());
			domainObj.setWapFinalScoreIncId(domainIdObj);
			domainObj.setSalesScore(obj.getSalesScore());
			domainObj.setPobSalesScore(obj.getPobScore());
			domainObj.setDocCoverageScore(obj.getDocCovScore());
			domainObj.setDocReachScore(obj.getDocRchScore());
			domainObj.setPulseChemReachScore(obj.getPulChmScore());
			domainObj.setOtherScore(obj.getMgrEffScore());
			domainObj.setRating(obj.getRating());
			domainObj.setFinalScore(obj.getTotalScore());
			domainObj.setSalesPts(obj.getSalesPoints());
			domainObj.setPobPts(obj.getPobSalesPoints());
			domainObj.setDocRchPts(obj.getDocReachPoints());
			domainObj.setPulseChmRchPts(obj.getPulChmRchPoints());
			domainObj.setDocCovPts(obj.getDocCovPoints());
			domainObj.setOtherPts(obj.getMgrEffAch());
			domainList.add(domainObj);
		}
		//saving all the users to DB
		wapFinalDaoObj.save(domainList);
	}
	public void getDocRchAndCovForOtherClassDocs(LinkedHashMap<String,String> docClassVal,String yr,String mth,String fmsDbRef,String loginId,int divId){
		//get list of netids
		//1. get no. of docs that are completely covered as per no. of visit netid wise

		ArrayList<OtherClassDocRchAndCov> finalObjList=new ArrayList<OtherClassDocRchAndCov>(); 
		List<Object[]> list=wapCalDao.getDocReachForOtherClassDocs(mth,yr,"'E','F'",fmsDbRef);
		HashMap<String,int[]> docRchMap=getMapReady(list); //netid=[E class count,F class count]
		list.clear();
		Integer temp=0;
		list=wapCalDao.getTotOtherClassDocs("'D','E','F'",fmsDbRef,yr,mth,loginId);
		Number num=null;
		HashMap<String,TotalDocs> totDocMap=new HashMap<String,TotalDocs>();
		for(Object[] obj : list){
			TotalDocs drObj=new TotalDocs();
			if(totDocMap.containsKey((String)obj[0])){
				drObj=totDocMap.get((String)obj[0]);
				num=0;
				if(drObj!=null){//set only class and count as netid and ecode will already be present
					if(((String)obj[2]).equalsIgnoreCase("E")){
						num=(Number)obj[3];
						drObj.setTotCntE(num.intValue());
					}
					else if(((String)obj[2]).equalsIgnoreCase("F")){
						num=(Number)obj[3];
						drObj.setTotCntF(num.intValue());
					}else if(((String)obj[2]).equalsIgnoreCase("D")){
						num=(Number)obj[3];
						drObj.setTotCntD(num.intValue());
					}
				}else{
					drObj=new TotalDocs();
					drObj.setNetid((String)obj[0]);
					drObj.setEcode((String)obj[1]);
					num=(Number)obj[3];
					if(((String)obj[2]).equalsIgnoreCase("E")){
						drObj.setTotCntE(num.intValue());
					}
					else if(((String)obj[2]).equalsIgnoreCase("F")){
						drObj.setTotCntF(num.intValue());
					}
					else if(((String)obj[2]).equalsIgnoreCase("D")){
						num=(Number)obj[3];
						drObj.setTotCntD(num.intValue());
					}
				}	
				totDocMap.put((String)obj[0],drObj);
			}else{
				drObj.setNetid((String)obj[0]);
				drObj.setEcode((String)obj[1]);
				if(((String)obj[2]).equalsIgnoreCase("E")){
					num=(Number)obj[3];
					drObj.setTotCntE(num.intValue());
				}
				else if(((String)obj[2]).equalsIgnoreCase("F")){
					num=(Number)obj[3];
					drObj.setTotCntF(num.intValue());
				}else if(((String)obj[2]).equalsIgnoreCase("D")){
					num=(Number)obj[3];
					drObj.setTotCntD(num.intValue());
				}
				totDocMap.put((String)obj[0],drObj);
			}	
		}
		list.clear();

		/* Adding D class Doc Coverage List in DClassDocCov table */
		list=wapCalDao.getDocCovForOtherClassDocs("D",(String)docClassVal.get("CLASSD").split(";")[1],yr,mth,fmsDbRef);
		ArrayList<DClassDocCov> dclassCovList=new ArrayList<DClassDocCov>();
		TotalDocs drObj=null;
		for(Object[] obj : list){
			drObj=new TotalDocs();
			DClassDocCov dclassCovObj=new DClassDocCov();
			DClassDocCovId dclassCovIdObj = new DClassDocCovId();

			//select netid,emp,class,count(cntcd)
			dclassCovIdObj.setNetid((String)obj[0]);
			dclassCovIdObj.setEcode((String)obj[1]);
			dclassCovIdObj.setMth(mth);
			dclassCovIdObj.setYr(yr);
			dclassCovObj.setDclassDocCovId(dclassCovIdObj);
			drObj=totDocMap.get((String)obj[0]);
			if(null != drObj){
				dclassCovObj.setTotDocs(drObj.getTotCntD()!=null ? drObj.getTotCntD() : 0);
			}else{
				dclassCovObj.setTotDocs(0);
			}
			num=0;
			num=(Number)obj[3];
			dclassCovObj.setCovDocs(num.intValue());
			dclassCovObj.setDivId(divId);
			dclassCovList.add(dclassCovObj);    		
		}
		dclassDocCovDaoObj.save(dclassCovList);
		list.clear();
		/* Adding D class Doc Coverage List in DClassDocCov table : end */

		/* Adding E & F class Doc Coverage and Reach List in OtherClassDocRchAndCov table : start*/
		list=wapCalDao.getDocCovForOtherClassDocs("E",(String)docClassVal.get("CLASSE").split(";")[1],yr,mth,fmsDbRef);
		HashMap<String,Integer> docCovMapE=getMapReady2(list); //netid=[E class Tot docs,F class Tot docs]
		list.clear();

		list=wapCalDao.getDocCovForOtherClassDocs("F",(String)docClassVal.get("CLASSF").split(";")[1],yr,mth,fmsDbRef);
		HashMap<String,Integer> docCovMapF=getMapReady2(list); //netid=[E class Tot docs,F class Tot docs]
		list.clear();

		ArrayList<String> netidList=wapCalDao.getAllNetids(loginId);
		int arr[]=new int[2];
		Iterator itr=netidList.iterator();
		String netid="";
		while(itr.hasNext()){//iterate with all currently present netids in the system
			netid=(String)itr.next();
			drObj=totDocMap.get(netid);
			if(drObj!=null){
				for(int i=0;i<=1;i++){
					OtherClassDocRchAndCov obj=new OtherClassDocRchAndCov();
					OtherClassDocRchCovId idobj=new OtherClassDocRchCovId();
					idobj.setNetid(drObj.getNetid());
					idobj.setEcode(drObj.getEcode());
					idobj.setMth(mth);
					idobj.setYr(yr);
					obj.setDivId(divId);
					arr=docRchMap.get(netid);

					if(i==0){
						idobj.setDocClass("E");
						obj.setTotDocs(drObj.getTotCntE()!=null ? drObj.getTotCntE() : 0 );
						temp=docCovMapE.get(netid);
						obj.setCovDocs(temp!=null ? temp : 0);
					}else if(i==1){
						idobj.setDocClass("F");
						obj.setTotDocs(drObj.getTotCntF()!=null ? drObj.getTotCntF() : 0);
						temp=docCovMapF.get(netid);
						obj.setCovDocs(temp!=null ? temp : 0);
					}	

					if(arr!=null){
						obj.setRchdDocs(arr[i]); //arr[0]= E class rchd doc count,arr[1] = F class rchd doc count
					}
					obj.setOtherClassDocRchCovId(idobj);
					finalObjList.add(obj);
				}
			}
		}
		otherClassDocRchCovDao.save(finalObjList);

		/* Adding E & F class Doc Coverage and Reach List in OtherClassDocRchAndCov table : end*/
	}	

	public HashMap<String,int[]> getMapReady(List<Object[]> list){
		int[] arr=null;
		Number num=null;

		HashMap<String,int[]> map=new HashMap<String,int[]>();
		for(Object[] obj:list){ //sequence -> netid,emp,class,count(cntcd)
			arr=new int[2];
			if(map.containsKey((String)obj[0])){ //check whether netid is already present in map or not
				arr=map.get((String)obj[0]);
				if(null != arr){
					num=(Number)obj[3];
					if(((String)obj[2]).equalsIgnoreCase("E"))
						arr[0]=num.intValue();
					else
						arr[1]=num.intValue();
				}else{
					num=(Number)obj[3];
					arr=new int[2];
					if(((String)obj[2]).equalsIgnoreCase("E"))
						arr[0]=num.intValue();
					else
						arr[1]=num.intValue();
				}
				map.put((String)obj[0],arr);
			} else{
				num=(Number)obj[3];
				if(((String)obj[2]).equalsIgnoreCase("E")) //if class=E then put doc count in 1st position of array else put in 2nd.
					arr[0]=num.intValue();
				else
					arr[1]=num.intValue();
				map.put((String)obj[0],arr);
			}

		}
		return map;
	}
	public HashMap<String,Integer> getMapReady2(List<Object[]> list){
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		Number num=0;
		for(Object[] obj:list){
			num=(Number)obj[3];
			map.put((String)obj[0],num.intValue());
		}
		return map;
	}
	
	
	public ArrayList<CummWapDetails> setAndGetCummWapMap(List<Object[]> list,ArrayList<Float> slabsMinScores,ArrayList<Float> pobMinScores,
			ArrayList<Float> docrchMinScores,ArrayList<Float> docCovMinScores,ArrayList<Float> pulChmMinScores,
			HashMap<Integer,Float> paramIdWeightageMap,TreeMap<Float,Float> salesSlabMap,
			TreeMap<Float,Float> pobSlabMap,TreeMap<Float,Float> docRchSlabMap,TreeMap<Float,Float> 
	docCovSlabMap,TreeMap<Float,Float> pulChmRchSlabMap,LinkedHashMap<Float,String> scoreIncrementMap,HashMap<String,Long> annualtargetMap,
	String frmYrMth,String toYrMth,int toYr,int toMth){
		
		//Map<String, CummWapDetails> cummWapDtlsMap=new HashMap<String, CummWapDetails>();//setAndGetCummWapMap(list);
		ArrayList<CummWapDetails> cummWapDtlsList=new ArrayList<CummWapDetails>();
		CummWapDetails cumWapObj=null;
		Number num=null;
		Double perc=0.0;
		Float weightage=0.0F;
		double totalScore=0.0;
		double aPlusMinScore=CommonConstants.A_PLUS_MIN_SCORE;
		//double otherperc=25.0;  
		int mthWrkd=0;
		ArrayList<Float> incrMinScoreList=null;
		if(null != scoreIncrementMap){
			incrMinScoreList=new ArrayList<Float>(scoreIncrementMap.keySet());    
		}
		String jnDate="0000-00-00";
		
		int frmYr=Integer.parseInt(frmYrMth.substring(0, 4));
		int frmMth=Integer.parseInt(frmYrMth.substring(4));
		
		int maxToDay=CommonUtils.getMaxDayOfMonth(toYr,toMth,1);
		int noOfMths=CommonUtils.getmonthsBetweenDates(frmYr,frmMth,1,toYr,toMth,maxToDay) + 1;
		

		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				cumWapObj=new CummWapDetails();
				cumWapObj.setNetid((String)object[0]);
				cumWapObj.setEcode((String)object[1]);
				num=(Number) object[2];
				cumWapObj.setSalesTarget(num!=null ? num.intValue() : 0);
				num=(Number) object[3];
				cumWapObj.setSalesAch(num!=null ? num.intValue() : 0);
				perc=(cumWapObj.getSalesAch()/cumWapObj.getSalesTarget())*100;
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				cumWapObj.setSalesAchPerc(perc);

				num=(Number) object[4];
				cumWapObj.setPbTarget(num!=null ? num.intValue() : 0);
				num=(Number) object[5];
				cumWapObj.setPbAch(num!=null ? num.intValue() : 0);
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				perc=(cumWapObj.getPbAch()/cumWapObj.getPbTarget())*100;
				cumWapObj.setPbAchPerc(perc);

				num=(Number) object[6];
				cumWapObj.setDocRchTarget(num!=null ? num.doubleValue() : 0);
				num=(Number) object[7];
				cumWapObj.setDocRchAch(num!=null ? num.doubleValue() : 0);
				perc=(cumWapObj.getDocRchAch()/cumWapObj.getDocRchTarget())*100;
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				cumWapObj.setDocRchAchPerc(perc);

				num=(Number) object[8];
				cumWapObj.setDocCovTarget(num!=null ? num.doubleValue() : 0);
				num=(Number) object[9];
				cumWapObj.setDocCovAch(num!=null ? num.doubleValue() : 0);
				perc=(cumWapObj.getDocCovAch()/cumWapObj.getDocCovTarget())*100;
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				cumWapObj.setDocCovAchPerc(perc);

				num=(Number) object[10];
				cumWapObj.setPulChmTarget(num!=null ? num.doubleValue() : 0);
				num=(Number) object[11];
				cumWapObj.setPulChmAch(num!=null ? num.doubleValue() : 0);
				perc=(cumWapObj.getPulChmAch()/cumWapObj.getPulChmTarget())*100;
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				cumWapObj.setPulChmAchPerc(perc);

				num=(Number) object[12];
				cumWapObj.setOther1(num!=null ? num.doubleValue() : 0);
				num=(Number) object[13];
				cumWapObj.setOther2(num!=null ? num.doubleValue() : 0);
				num=(Number) object[14];
				cumWapObj.setOther3(num!=null ? num.doubleValue() : 0);
				num=(Number) object[15];
				cumWapObj.setOther4(num!=null ? num.doubleValue() : 0);
				System.out.println("cumWapObj : "+cumWapObj.getEcode());
				//printing no. of months worked since selected from yrmth or DOJ till to yrmth: start
				jnDate=(String)(object[16].toString());
				String jnYr="0000";String jnMth="00";int jnDay=0;
				try{
				if(jnDate!=null && !jnDate.equalsIgnoreCase("")){
					jnYr=jnDate.split("-")[0];
					jnMth=jnDate.split("-")[1];
					jnDay=Integer.parseInt(jnDate.split("-")[2]);
				}
				
				if(Integer.parseInt(jnYr+jnMth) < Integer.parseInt(frmYrMth)){ //if joining yr mth is less than from yr mth then print no of months between frm yr mth and to yr mth selected.
					mthWrkd=noOfMths;
				}
				else if(Integer.parseInt(jnYr+jnMth) == Integer.parseInt(frmYrMth)){ /*if joining yr mth and frm yr mth are equal then consider day of joining.
													if day of joining <= 15 then consider whole month, if joining day is after 15th of the month then don't consider 
													the joining month in total months worked*/
					if(jnDay<=15){
						mthWrkd=noOfMths;
						
					}else{
						mthWrkd=noOfMths-1;
						
					}
				}else if(Integer.parseInt(jnYr+jnMth) > Integer.parseInt(toYrMth)){ //if joining yr mth is greater than to yr mth then print 0.
					mthWrkd=0;
				}
				else if(Integer.parseInt(jnYr+jnMth) > Integer.parseInt(frmYrMth)){ /* if join yr mth is greater than from yr mth but its less 
																					   than to yrmth (as checked in above condn) then calculate the months worked till toyrmth*/
					int temp1=CommonUtils.getmonthsBetweenDates(Integer.parseInt(jnYr),Integer.parseInt(jnMth),1,
							   toYr,toMth,maxToDay);
					if(jnDay<=15)
						mthWrkd=(temp1+1);
					else
						mthWrkd=(temp1);
					
				}
				}catch(Exception e){
					e.printStackTrace();
					logger.error("Custom Error - ReportServiceImpl : no. of months worked : ",e);
				}
				//printing no. of months worked since selected from yrmth or DOJ till to yrmth: end
				
				perc=((cumWapObj.getOther1()!=0 ? cumWapObj.getOther1()/mthWrkd : 0))+
						(cumWapObj.getOther2()!=0 ? cumWapObj.getOther2()/mthWrkd : 0)+
						(cumWapObj.getOther3()!=0 ? cumWapObj.getOther3()/mthWrkd : 0)+
						(cumWapObj.getOther4()!=0 ? cumWapObj.getOther4()/mthWrkd : 0);//cumWapObj.getOther1()+cumWapObj.getOther2()+cumWapObj.getOther3()+cumWapObj.getOther4();
				if(perc!=null && perc!=0){
					perc = (double) Math.round(perc * 100);
					perc = perc/100;
				}
				cumWapObj.setOtherAch(perc);
				//cumWapObj.setOtherPoints(cumWapObj.getOtherAch()); 

				cumWapObj.setAnnualTarget(annualtargetMap.get(cumWapObj.getNetid()));

				/*Set Points as per Acheivement Percent i.e. if acheivement percent lies within a a specific range a slab of a param then get points against it.*/
				if(cumWapObj.getSalesAchPerc()>0){
					for(Float minSlab : slabsMinScores){
						if(cumWapObj.getSalesAchPerc()>=minSlab){ //compares salesAch with the highest min slab range first,the 2nd highest so on...lowest
							//value in the last,if sales ach is greater than min slab range then that point is set.
							cumWapObj.setSalesPoints(salesSlabMap.get(minSlab));
							break;
						}
					}
				}
				if(cumWapObj.getPbAchPerc()>0 && null!=pobMinScores){
					for(Float minSlab : pobMinScores){
						if(cumWapObj.getPbAchPerc()>=minSlab){
							cumWapObj.setPbPoints(pobSlabMap.get(minSlab));
							break;
						}
					}
				}	
				if(cumWapObj.getDocRchAchPerc()>0 && null!=docrchMinScores){
					for(Float minSlab : docrchMinScores){
						if(cumWapObj.getDocRchAchPerc()>=minSlab){
							cumWapObj.setDocRchPoints(docRchSlabMap.get(minSlab));
							break;
						}
					}
				}	
				if(cumWapObj.getDocCovAchPerc()>0 && null!=docCovMinScores){
					for(Float minSlab : docCovMinScores){
						if(cumWapObj.getDocCovAchPerc()>=minSlab){
							cumWapObj.setDocCovPoints(docCovSlabMap.get(minSlab));
							break;
						}
					}
				}
				if(cumWapObj.getPulChmAchPerc()>0 && null!=pulChmMinScores){
					for(Float minSlab : pulChmMinScores){
						if(cumWapObj.getPulChmAchPerc()>=minSlab){
							cumWapObj.setPulChmPoints(pulChmRchSlabMap.get(minSlab));
							break;
						}
					}

					//as there are no slabs defined for others,hence points is : 25% of total 		
					/*perc=(cumWapObj.getOtherAch() * (otherperc/100)); //TODO doubt -> why getting 25% again when already saved in DB 25% of the score.
					cumWapObj.setOtherPoints(perc);
					System.out.println("points acheived MGR : "+perc);*/
					//points and ach are same here as %ach is taken which is already multiplied by 0.25,so, now only 10% of the total needs to be calculated.
					
					/*perc=cumWapObj.getOther1()+cumWapObj.getOther2()+cumWapObj.getOther3()+cumWapObj.getOther4();
					if(perc!=null && perc!=0){
						perc = (double) Math.round(perc * 100);
						perc = perc/100;
					}*/
					
					cumWapObj.setOtherPoints(cumWapObj.getOtherAch());
					/*Set Points as per Acheivement Percent i.e. if acheivement percent lies within a a specific range a slab of a param then get points against it. : end*/
					/*Set Score as per points acheived. Formula: if weightage of sale=45%
					 * Score = 4
					 * then sales%=45% of 4*/
					//1. sales
					weightage=(Float)paramIdWeightageMap.get(CommonConstants.SALES_PARAMID);
					cumWapObj.setSalesScore((cumWapObj.getSalesPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getSalesPoints()) : 0.0);			

					//2. POB Sales
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.POB_PARAMID);
					cumWapObj.setPbScore((cumWapObj.getPbPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getPbPoints()) : 0.0);

					//3.Docreach
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.DOC_REACH_PARAMID);
					cumWapObj.setDocRchScore((cumWapObj.getDocRchPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getDocRchPoints()) : 0.0);

					//4.Doc Coverage
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.COVERAGE_PARAMID);
					cumWapObj.setDocCovScore((cumWapObj.getDocCovPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getDocCovPoints()) : 0.0);

					//5. Pulse Chem Reach
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.PULSE_CHEM_REACH_PARAMID);
					cumWapObj.setPulChmScore((cumWapObj.getPulChmPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getPulChmPoints()) : 0.0);

					//6. Manager's Effectiveness : does not required points here,as slabs are not there for Manager's Eff
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.MANAGERIAL_EFF_PARAMID);
					cumWapObj.setOtherScore((cumWapObj.getOtherPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getOtherPoints()) : 0.0);
					System.out.println("MGR score : "+cumWapObj.getOtherScore());
					/*Set Score as per points acheived : end*/		
				}
				/*Get Rating : start*/
				totalScore=0.0;
				totalScore=cumWapObj.getOtherScore()+cumWapObj.getPulChmScore()+cumWapObj.getDocCovScore()+cumWapObj.getDocRchScore()+
						cumWapObj.getPbScore()+cumWapObj.getSalesScore();
				System.out.println("totalScore : "+totalScore);
				cumWapObj.setTotalScore(totalScore);

				if(totalScore>=aPlusMinScore && cumWapObj.getAnnualTarget()!=null && 
						cumWapObj.getAnnualTarget()>=CommonConstants.A_PLUS_MIN_TARGET
						&& cumWapObj.getSalesAch()>=CommonConstants.A_PLUS_MIN_ACH_PERC){
					cumWapObj.setRating("A+");
				}
				else if(totalScore>0 && null!=incrMinScoreList){
					for(Float minIncrScore : incrMinScoreList){
						if(totalScore>=minIncrScore){
							cumWapObj.setRating(scoreIncrementMap.get(minIncrScore));
							break;
						}
					}
				}
				else{
					cumWapObj.setRating(scoreIncrementMap.get(0.0f));
					System.out.println("totalScore is below 0: "+totalScore);
				}
				/*Get Rating : end*/
				//cummWapDtlsMap.put((String)object[1],cumWapObj);
				cummWapDtlsList.add(cumWapObj);
			}

		}
		return cummWapDtlsList;
	}
	
	
	public void saveYtdWapDtlsList(List<CummWapDetails> list,String wapMth,String wapYr,int divId) {
		YTDWapDomain domainObj = null;
		WapFinalScoreIncId domainIdObj=null;
		List<YTDWapDomain> domainList = new ArrayList<YTDWapDomain>();

		for(CummWapDetails obj : list) {
			domainIdObj = new WapFinalScoreIncId(); 
			domainObj = new YTDWapDomain();

			domainIdObj.setNetid(obj.getNetid());
			domainIdObj.setEcode(obj.getEcode());
			domainIdObj.setMth(wapMth);
			domainIdObj.setYr(wapYr);
			domainIdObj.setDivId(divId);
			domainObj.setWapFinalScoreIncId(domainIdObj);
			domainObj.setYsalesScore(obj.getSalesScore());
			domainObj.setYpobSalesScore(obj.getPbScore());
			domainObj.setYdocCoverageScore(obj.getDocCovScore());
			domainObj.setYdocReachScore(obj.getDocRchScore());
			domainObj.setYpulseChemReachScore(obj.getPulChmScore());
			domainObj.setYotherScore(obj.getOtherScore());
			domainObj.setYrating(obj.getRating());
			domainObj.setYfinalScore(obj.getTotalScore());
			domainObj.setYsalesPts(obj.getSalesPoints());
			domainObj.setYpobPts(obj.getPbPoints());
			domainObj.setYdocRchPts(obj.getDocRchPoints());
			domainObj.setYpulseChmRchPts(obj.getPulChmPoints());
			domainObj.setYdocCovPts(obj.getDocCovPoints());
			domainObj.setYotherPts(obj.getOtherPoints());
		System.out.println("obj.getNetid() : "+obj.getNetid()+"-"+domainObj.getYotherPts());
			domainList.add(domainObj);
		}
		//saving all the users to DB
		ytdWapDaoObj.save(domainList);
	}

	
}

