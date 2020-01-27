package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.UserDAO;
import com.eis.wap.model.AppraisedEmp;
import com.eis.wap.model.Employee;
import com.eis.wap.model.ReviewEmployee;
import com.eis.wap.model.SessionUser;
import com.eis.wap.service.EmployeeListService;

@Component
public class EmployeeListServiceImpl implements EmployeeListService {

	@Autowired
	UserDAO userDAO;

	public List<Employee> getEmployeeList(SessionUser sessionUser,String dataReqLvl,String option) {
		List<Employee> empList = null;
		if(null != sessionUser){
			Employee emp = null;
			List<Object[]> list = null;
			int lvl=(sessionUser.getLevel()!=null && !sessionUser.getLevel().equalsIgnoreCase("")?Integer.parseInt(sessionUser.getLevel()):0); 
			if(option.equalsIgnoreCase("review")){
				list=userDAO.getReviewEmployeeList(sessionUser.getLoginYr(),sessionUser.getLoginMth(),sessionUser.getHqRef(),
						sessionUser.getLevel(),sessionUser.getFmsDbRef(),dataReqLvl,sessionUser.getEmp(),sessionUser.getDivId());
			}else if(option.equalsIgnoreCase("update")){
				list=userDAO.getReviewedEmpList(sessionUser.getLoginYr(),sessionUser.getLoginMth(),sessionUser.getHqRef(),
						sessionUser.getLevel(),sessionUser.getFmsDbRef(),dataReqLvl,sessionUser.getEmp(),sessionUser.getDivId());
			}
			else{
				list=userDAO.getEmployeeList(sessionUser.getLoginYr(),sessionUser.getLoginMth(),sessionUser.getHqRef(),
						sessionUser.getLevel(),sessionUser.getFmsDbRef(),dataReqLvl,sessionUser.getEmp());
			}


			if(null != list && !list.isEmpty()) {
				empList = new ArrayList<Employee>();
				for(Object[] data : list) {
					emp = new Employee();
					emp.setEmpName((String) data[0]);
					emp.setNetId((String) data[1]);
					emp.seteCode((String) data[2]);
					emp.setHqName((String) data[3]);
					emp.setLevel(data[4].toString());
					emp.setCoCode((String) data[5].toString());
					if(lvl>2 && (option.equalsIgnoreCase("review") || option.equalsIgnoreCase("update")))
						emp.setFmCode((String) (data[6]!=null ? data[6].toString() : ""));
					empList.add(emp);
				}
			}
		}
		return empList;
	}

	public List<ReviewEmployee> getEmployeeListForReview(SessionUser sessionUser,String dataReqLvl) {
		List<ReviewEmployee> empList = null;
		if(null != sessionUser){
			ReviewEmployee emp = null;
			List<Object[]> list = null;
			//int lvl=(sessionUser.getLevel()!=null && !sessionUser.getLevel().equalsIgnoreCase("")?Integer.parseInt(sessionUser.getLevel()):0); 
			list=userDAO.getEmployeeListForReviewStatus(sessionUser.getLoginYr(),sessionUser.getLoginMth(),sessionUser.getHqRef(),
					sessionUser.getLevel(),sessionUser.getFmsDbRef(),dataReqLvl,sessionUser.getEmp(),sessionUser.getDivId());

			if(null != list && !list.isEmpty()) {
				empList = new ArrayList<ReviewEmployee>();
				for(Object[] data : list) {
					emp = new ReviewEmployee();
					emp.setPsrEmp((String) data[0]);
					emp.setPsrName((String) data[1]);
					emp.setHname((String) data[2]);
					emp.setMgr1Emp((String) data[3]);
					emp.setMgr2Emp((String) data[4]);
					if(data[6]!=null)
						emp.setReviewDate(data[6].toString());
					else if(data[5]!=null)
						emp.setReviewDate(data[5].toString());
					else
						emp.setReviewDate("0000-00-00");
					emp.setPsrCmts((String) (data[7]!=null ? data[7] : ""));
					empList.add(emp);
				}
			}
		}
		return empList;
	}

	//to get employees whose wap process is done.
	public List<AppraisedEmp> getAppraisedEmployees(SessionUser sessionUser,String dataReqLvl){
		List<AppraisedEmp> empList = null;
		if(null != sessionUser){
			AppraisedEmp emp = null;
			List<Object[]> list = null;
			if(sessionUser.getLevel().equalsIgnoreCase("1")){
				list=userDAO.getAppraisedEmpDtls(sessionUser.getLoginYr(),sessionUser.getLoginMth(),sessionUser.getDivId(),
						sessionUser.getEmp(),sessionUser.getFmsDbRef(),sessionUser.getAfm());
			}else{
				list=userDAO.getAppraisedEmployees(sessionUser.getHqRef(),sessionUser.getLoginYr(),sessionUser.getLoginMth(),
						dataReqLvl,sessionUser.getDivId(),sessionUser.getEmp(),sessionUser.getFmsDbRef(),sessionUser.getLevel());
			}
			if(null != list && !list.isEmpty()) {
				empList = new ArrayList<AppraisedEmp>();
				for(Object[] data : list) {
					emp = new AppraisedEmp();
					emp.setNetId((String) data[0]);
					emp.setEcode((String) data[1]);
					emp.setEname((String) data[2]);
					emp.setHname((String) data[3]);
					emp.setCoCode((String) data[4]);
					emp.setJoinDate(data[5].toString());
					emp.setMgrName((String) data[6]);
					emp.setGrade((String) data[7]);
					empList.add(emp);
				}
			}
		}
		return empList;
	}

	public AppraisedEmp getAppraisedEmp(SessionUser sessionUser,String dataReqLvl){
		AppraisedEmp emp = null;
		if(null != sessionUser){
			List<Object[]> list =userDAO.getAppraisedEmpDtls(sessionUser.getLoginYr(),sessionUser.getLoginMth(),sessionUser.getDivId(),
					sessionUser.getEmp(),sessionUser.getFmsDbRef(),sessionUser.getAfm());

			if(null != list && !list.isEmpty()) {

				for(Object[] data : list) {
					emp = new AppraisedEmp();
					emp.setCoCode((String) data[0]);
					emp.setJoinDate((String) data[1].toString());
					emp.setMgrName((String) data[2]);
					emp.setGrade((String) data[3]);
				}
			}
		}
		return emp;
	}
	
	public List<AppraisedEmp> getAppraisedEmpWithDTls(SessionUser sessionUser,String dataReqLvl){
		List<AppraisedEmp> empList = null;
		if(null != sessionUser){
			AppraisedEmp emp = null;
			List<Object[]> list = null;
			if(sessionUser.getLevel().equalsIgnoreCase("1")){
				
				list=userDAO.getAppraisedEmpDtls2(sessionUser.getLoginYr(),sessionUser.getLoginMth(),sessionUser.getDivId(),
						sessionUser.getEmp(),sessionUser.getFmsDbRef(),sessionUser.getHqRef(),sessionUser.getLevel());
			}else{
				list=userDAO.getAppraisedEmployees(sessionUser.getHqRef(),sessionUser.getLoginYr(),sessionUser.getLoginMth(),
						dataReqLvl,sessionUser.getDivId(),sessionUser.getEmp(),sessionUser.getFmsDbRef(),sessionUser.getLevel());
			}
			if(null != list && !list.isEmpty()) {
				empList = new ArrayList<AppraisedEmp>();
				for(Object[] data : list) {
					emp = new AppraisedEmp();
					emp.setNetId((String) data[0]);
					emp.setEcode((String) data[1]);
					emp.setEname((String) data[2]);
					emp.setHname((String) data[3]);
					emp.setCoCode((String) data[4]);
					emp.setJoinDate(data[5].toString());
					emp.setGrade((String) data[7]);
					empList.add(emp);
				}
			}
		}
		return empList;
	}


}
