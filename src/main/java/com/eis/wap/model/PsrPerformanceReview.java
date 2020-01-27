package com.eis.wap.model;

import java.util.List;

public class PsrPerformanceReview {

	private List<Employee> empList;
	private String psrEmp;
	private String psrNetid;
	private float score1;
	private float score2;
	private float score3;
	private float score4;
	private String comment=null;
	private String comment2=null;
	private String update;

	public List<Employee> getEmpList() {
		return empList;
	}

	public void setEmpList(List<Employee> empList) {
		this.empList = empList;
	}

	public float getScore1() {
		return score1;
	}

	public void setScore1(float score1) {
		this.score1 = score1;
	}

	public float getScore2() {
		return score2;
	}

	public void setScore2(float score2) {
		this.score2 = score2;
	}

	public float getScore3() {
		return score3;
	}

	public void setScore3(float score3) {
		this.score3 = score3;
	}

	public float getScore4() {
		return score4;
	}

	public void setScore4(float score4) {
		this.score4 = score4;
	}

	public void setScore4(int score4) {
		this.score4 = score4;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPsrEmp() {
		return psrEmp;
	}

	public void setPsrEmp(String psrEmp) {
		this.psrEmp = psrEmp;
	}

	public String getPsrNetid() {
		return psrNetid;
	}

	public void setPsrNetid(String psrNetid) {
		this.psrNetid = psrNetid;
	}

	public String getComment2() {
		return comment2;
	}

	public void setComment2(String comment2) {
		this.comment2 = comment2;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}
}
