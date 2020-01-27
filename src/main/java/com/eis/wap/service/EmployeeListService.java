package com.eis.wap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eis.wap.model.AppraisedEmp;
import com.eis.wap.model.Employee;
import com.eis.wap.model.ReviewEmployee;
import com.eis.wap.model.SessionUser;

@Service
public interface EmployeeListService {

	public List<Employee> getEmployeeList(SessionUser sessionUser,String dataReqLvl,String option);

	public List<ReviewEmployee> getEmployeeListForReview(SessionUser attribute, String dataReqLvl);
	
	public List<AppraisedEmp> getAppraisedEmployees(SessionUser sessionUser,String dataReqLvl);
	
	public AppraisedEmp getAppraisedEmp(SessionUser sessionUser,String dataReqLvl);
	
	public List<AppraisedEmp> getAppraisedEmpWithDTls(SessionUser sessionUser,String dataReqLvl);
	
}
