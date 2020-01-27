package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.MgrEffectivenessParamDAO;
import com.eis.wap.domain.MgrEffectivenessParams;
import com.eis.wap.domain.ParamMaster;
import com.eis.wap.model.PsrPerformanceReview;
import com.eis.wap.model.SessionUser;
import com.eis.wap.service.MgrEffectivenessParamService;
import com.eis.wap.util.CommonUtils;

@Component
public class MgrEffectivenessParamServiceImpl implements MgrEffectivenessParamService {

	@Autowired
	MgrEffectivenessParamDAO mgrDAO;

	public void insertRecord(PsrPerformanceReview review,SessionUser session){

		MgrEffectivenessParams object = new MgrEffectivenessParams();
		if(null != review) {
			object.setParam1(review.getScore1());
			object.setParam2(review.getScore2());
			object.setParam3(review.getScore3());
			object.setParam4(review.getScore4());
			object.setNetid(review.getPsrNetid());
			System.out.println("Netid : "+object.getNetid());
			object.setEcode(review.getPsrEmp());
			System.out.println("Netid : "+object.getEcode());
			object.setManagerId(session.getEmp());
			object.setYr(session.getLoginYr());
			object.setMth(session.getLoginMth());
			object.setDiv(session.getDivId());
			object.setAddedBy(session.getEmp());
			object.setAddDate(CommonUtils.getCurrentDateTime());
			try{
			object=mgrDAO.save(object);
			mgrDAO.saveComment(object.getMgrEffectivenessParamsId(),review.getComment());
			}catch(DataIntegrityViolationException e){
				System.out.println("Exception : "+e.getClass().getName()+": Err Message"+e.getMessage()+
						" rootcause : "+e.getRootCause());
			}
		}
	}

	@Override
	public List getMgr1Scores(String netid, String emp,String divId,String loginMth,String loginYr) {
		List list=new ArrayList<String>();
		List<Object[]> mgr1ScoresList = mgrDAO.getmgr1Scores(emp, netid, loginYr, loginMth, divId);
		if(null != mgr1ScoresList && !mgr1ScoresList.isEmpty()) {
			list=(List) Arrays.asList(mgr1ScoresList.get(0));
		}
		return list;
	}

	public void updateRecordForMgr2(PsrPerformanceReview review,SessionUser session){

		if(null != review) {

			int i=mgrDAO.updateMgr2Scores(review.getScore1(), review.getScore2(), review.getScore3(), review.getScore4(),review.getPsrEmp(),
					review.getPsrNetid(),session.getLoginYr(),session.getLoginMth(),session.getEmp(),review.getComment(),session.getDivId());
			System.out.println("no of rows updated : "+i);
			if(i<1){
				MgrEffectivenessParams object = new MgrEffectivenessParams();		
				object.setParam1(0.0F);
				object.setParam2(0.0F);
				object.setParam3(0.0F);
				object.setParam4(0.0F);
				object.setMgr2Param1(review.getScore1());
				object.setMgr2Param2(review.getScore2());
				object.setMgr2Param3(review.getScore3());
				object.setMgr2Param4(review.getScore4());
				object.setNetid(review.getPsrNetid());
				System.out.println("Netid : "+object.getNetid());
				object.setEcode(review.getPsrEmp());
				System.out.println("Netid : "+object.getEcode());
				object.setMgrId2(session.getEmp());
				object.setYr(session.getLoginYr());
				object.setMth(session.getLoginMth());
				object.setDiv(session.getDivId());
				object.setAddedBy(session.getEmp());
				object.setAddDate(CommonUtils.getCurrentDateTime());
				try{
				object=mgrDAO.save(object);
				}catch(Exception e){
					mgrDAO.updateMgr2ScoresNotCommnt(review.getScore1(), review.getScore2(), review.getScore3(), review.getScore4(),review.getPsrEmp(),
							review.getPsrNetid(),session.getLoginYr(),session.getLoginMth(),session.getEmp(),session.getDivId());
					object.setMgrEffectivenessParamsId(mgrDAO.getId1(review.getPsrEmp(), review.getPsrNetid(), session.getLoginYr(), session.getLoginMth(), session.getDivId()));
				}
				mgrDAO.saveComment2(object.getMgrEffectivenessParamsId(),review.getComment());
			}
		}
	}

	public List getMgrScores(String netid, String emp,String divId,String loginMth,String loginYr,String lvl) {
		List list=new ArrayList<String>();
		List<Object[]> mgr1ScoresList=null;
		if(lvl.equalsIgnoreCase("2"))
			mgr1ScoresList = mgrDAO.getmgr1Scores(emp, netid, loginYr, loginMth, divId);
		else if(lvl.equalsIgnoreCase("4"))
			mgr1ScoresList = mgrDAO.getmgr2Scores(emp, netid, loginYr, loginMth, divId);
		else if(Integer.parseInt(lvl)>=5)
			mgr1ScoresList = mgrDAO.getmgrScores(emp, netid, loginYr, loginMth, divId);

		if(null != mgr1ScoresList && !mgr1ScoresList.isEmpty()) {
			list=(List) Arrays.asList(mgr1ScoresList.get(0));
		}
		return list;
	}

	public String updateMgr1Record(PsrPerformanceReview review,SessionUser session){

		//MgrEffectivenessParams object = new MgrEffectivenessParams();
		if(null != review) {

			int i=mgrDAO.updateMgr1Scores(review.getScore1(), review.getScore2(), review.getScore3(), review.getScore4(),review.getPsrEmp(),
					review.getPsrNetid(),session.getLoginYr(),session.getLoginMth(),session.getEmp(),review.getComment(),session.getDivId());
			if(i>0){
				return "Record Updated Successfully : "+i;
			}else{
				return "No Record Updated";
			}			
		}
		return "SOME PROBLEM DETECTED. NO RECORD UPDATED";
	}
	
	public String updateMgr2Record(PsrPerformanceReview review,SessionUser session){

		if(null != review) {
			int i=mgrDAO.updateMgr2Scores(review.getScore1(), review.getScore2(), review.getScore3(), review.getScore4(),review.getPsrEmp(),
					review.getPsrNetid(),session.getLoginYr(),session.getLoginMth(),session.getEmp(),review.getComment2(),session.getDivId());
			//System.out.println("no of rows updated : "+i);
			if(i>0){
				return "Record Updated Successdully "+i;
			}else{
				return "No Record Updated";
			}			
		}
		return "SOME PROBLEM DETECTED. NO RECORD UPDATED";
	}

	
}
