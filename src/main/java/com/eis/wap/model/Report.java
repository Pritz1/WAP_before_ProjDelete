package com.eis.wap.model;

import java.util.List;

public class Report {
	
	private String viewType;
    private List<Employee> empList;
    private String levelSelected;
    private String selEmp;
    private String frmYrMth;
    private String toYrMth;
    
	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public List<Employee> getEmpList() {
		return empList;
	}

	public void setEmpList(List<Employee> empList) {
		this.empList = empList;
	}

	public String getLevelSelected() {
		return levelSelected;
	}

	public void setLevelSelected(String levelSelected) {
		this.levelSelected = levelSelected;
	}

	public String getSelEmp() {
		return selEmp;
	}

	public void setSelEmp(String selEmp) {
		this.selEmp = selEmp;
	}

	public String getFrmYrMth() {
		return frmYrMth;
	}

	public void setFrmYrMth(String frmYrMth) {
		this.frmYrMth = frmYrMth;
	}

	public String getToYrMth() {
		return toYrMth;
	}

	public void setToYrMth(String toYrMth) {
		this.toYrMth = toYrMth;
	}
	
}
