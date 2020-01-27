package com.eis.wap.model;

import java.util.Date;

public class Parameters {
	
	private Integer param_id;
	//private String param_id=null;
	private String param_name=null;
	private Float weightage;
	private Date add_date;
	//private String add_date=null;
	private String addedBy=null;
	private String isActive=null;
	private String deldate=null;
	private String deletedBy=null;
	private String empLevel=null;
	private String addEdit=null;
	
	
	public String getParam_name() {
		return param_name;
	}
	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}
	

	public String getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getAdd_date() {
		return add_date;
	}

	public void setAdd_date(Date add_date) {
		this.add_date = add_date;
	}
	
	public String getDeldate() {
		return deldate;
	}

	public void setDeldate(String deldate) {
		this.deldate = deldate;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}
	public Integer getParam_id() {
		return param_id;
	}
	public Float getWeightage() {
		return weightage;
	}
	public void setParam_id(Integer param_id) {
		this.param_id = param_id;
	}

	public void setWeightage(Float weightage) {
		this.weightage = weightage;
	}

	public String getEmpLevel() {
		return empLevel;
	}
	public void setEmpLevel(String empLevel) {
		this.empLevel = empLevel;
	}
	public String getAddEdit() {
		return addEdit;
	}
	public void setAddEdit(String addEdit) {
		this.addEdit = addEdit;
	}
	@Override
    public String toString()
    {
        return "Parameters [param_id=" + param_id + ", param_name=" + param_name + ", weightage=" + weightage + ", addedBy=" + addedBy + ", isActive="+isActive+", add_date="+add_date+",addEdit="+addEdit+"]";
    }
}	

