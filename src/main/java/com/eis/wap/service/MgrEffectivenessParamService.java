package com.eis.wap.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.eis.wap.model.PsrPerformanceReview;
import com.eis.wap.model.SessionUser;

@Service
public interface MgrEffectivenessParamService {

 public void insertRecord(PsrPerformanceReview review,SessionUser session);
 
 public List getMgr1Scores(String netid,String emp,String divId,String loginMth,String loginYr);
 
 public void updateRecordForMgr2(PsrPerformanceReview review,SessionUser session);
 
 public List getMgrScores(String netid,String emp,String divId,String loginMth,String loginYr,String lvl);
 
 public String updateMgr1Record(PsrPerformanceReview review,SessionUser session);

 public String updateMgr2Record(PsrPerformanceReview review,SessionUser session);
 
}
