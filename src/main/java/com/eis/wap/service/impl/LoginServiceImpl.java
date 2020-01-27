/**
 * 
 */
package com.eis.wap.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.LockMasterDao;
import com.eis.wap.dao.UserDAO;
import com.eis.wap.model.Division;
import com.eis.wap.model.SessionUser;
import com.eis.wap.service.LoginService;
import com.eis.wap.util.CommonUtils;

@Component
public class LoginServiceImpl implements LoginService {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	LockMasterDao lockDAO;

	public SessionUser login(String userName, String password, String date, Integer divId, boolean superpwd) {
		// validate user credentials against credentials stored in DB for given user name and password
		SessionUser user = null;
		String yr = date.substring(6, 10);
		String mth = date.substring(3, 5);
        
		// get division details from div_master table
		System.out.println("divId : "+divId);
		Division division = userDAO.getDivisionDetails(divId+"");//findOne(divId);

		if(null != division) {
			user=new SessionUser();
			List<Object[]> empData = userDAO.getEmpDetails(userName, password,division.getFmsDbRef(),superpwd); /*for authentication employee code is required for user 
																										with level 7 and above while other's cocode is checked.*/
			if(null != empData && !empData.isEmpty()){
				if(empData.size()>1){
					user.setError("More Than One Employee With Same ID!!");
				}else{
					for(Object[] emp : empData){
						if(emp[2]!=null && !emp[2].equals("")){
							if(Integer.parseInt((String)emp[2])<6){ //if level is less than 6 then get details from nettrans
								List<Object[]> userDataList = userDAO.getLoginDetails((String) empData.get(0)[0], password, yr, mth,division.getFmsDbRef(),superpwd);
								 if(null != userDataList && !userDataList.isEmpty()) {
									user = new SessionUser();
									user.setLoginMth(mth);
							        user.setLoginYr(yr);
									for(Object[] userData : userDataList) {
									  if(userData[9]!=null && division.getHqRef()!=null){
										if((Integer.parseInt((String)emp[2])<=2 && (((String)userData[9]).contains(("("+division.getHqRef()+")")))) || //PSR's HQNAME must have (A) or (B)
												(Integer.parseInt((String)emp[2])==2 && (((String)userData[9]).contains(("(AB)")))) ||  //FM's HQ Name may contain (AB) 
												(Integer.parseInt((String)emp[2]) > 2)){ //users like RM and above it can be of both Palsons1 and Palsons2 
										user.setEmp((String) userData[0]);
										user.setEmployeeName((String) userData[1]);
										user.setLevel(userData[8].toString());
										user.setNetId((String) userData[3]);
										user.setAfm((String) userData[4]);
										user.setState((String) userData[5]);
										user.setRm((String) userData[6]);
										user.setZm((String) userData[7]);
										user.setFmsDbRef(division.getFmsDbRef());
										user.setSalesDbRef(division.getSalesDbRef());
										user.setHqRef(division.getHqRef());
										user.setDivId(division.getDivId());
										user.setDivName(division.getDivName());
										user.setFinStartDt(CommonUtils.getFinStrtDt(userDAO.getFinancialStartDate("FinStartDate"),user.getLoginMth(),user.getLoginYr()));
										user.setFinEndDt(CommonUtils.getFinEndDt(user.getFinStartDt(), user.getLoginMth(), user.getLoginYr()));
										user.setError("");
										user.setAccGrp((String) emp[3]);
										user.setHqName((String) userData[9]);
										user.setLvlDescMap(userDAO.getLevelDesc(division.getFmsDbRef()));
										user.setCocode((String) userData[10]);
										user.setDoj((String) userData[11].toString());
										}else{
											user.setError("Conflict in Division!!");
										}
									}else{
									  user.setError("HQ Info is Not Available!!");
								  }
								}
							}else{
								user.setError("Could Not Get Employee Info From Database!!");
							}
							}else{
								user = new SessionUser();
								user.setLoginMth(mth);
						        user.setLoginYr(yr);
								user.setEmp((String) emp[0]);
								user.setEmployeeName((String) emp[1]);
								user.setLevel((String) emp[2]);
								user.setNetId("NA");
								user.setAfm("NA");
								user.setState("NA");
								user.setRm("NA");
								user.setZm("NA");
								user.setDivId(divId);
								user.setDivName(division.getDivName());
								user.setAccGrp((String) emp[3]);
								user.setFmsDbRef(division.getFmsDbRef());
								user.setSalesDbRef(division.getSalesDbRef());
								user.setHqRef(division.getHqRef());
								user.setFinStartDt(CommonUtils.getFinStrtDt(userDAO.getFinancialStartDate("FinStartDate"),user.getLoginMth(),user.getLoginYr()));
								user.setFinEndDt(CommonUtils.getFinEndDt(user.getFinStartDt(), user.getLoginMth(), user.getLoginYr()));
								user.setError("");
								user.setHqName("NA");
								user.setLvlDescMap(userDAO.getLevelDesc(division.getFmsDbRef()));
							}
						}else{
							user.setError("Could Not Get Employee Type From Database!!");
						}
					}
				}
		 }else{
				user.setError("Invalid Login Details!!");
		 }
		}else{
			user=new SessionUser();
			user.setError("Division Details Not Found!!");
		}
		return user;
	}
	public HashMap<Integer,String> getDivIdDesc(){
		return userDAO.getDivIdDesc();
	}
	
	public String getLockStatus(String yrMth, int divId){
		String stat = lockDAO.getLockStatus(yrMth,divId);
		System.out.println("stat : "+stat);
		return (null != stat ? stat : "N");
	}
	
}
