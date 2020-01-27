package com.eis.wap.model;

public class PsrComments {

//Pranali : 22/01/2019 start

private String ecode;
private String netId;
private String yr;
private String mth;
private String comments;
private String isEdit;
private int divid;
 
public PsrComments() {
	super();
	// TODO Auto-generated constructor stub
}


public int getDivid() {
	return divid;
}


public void setDivid(int divid) {
	this.divid = divid;
}


public String getEcode() {
	return ecode;
}

public void setEcode(String ecode) {
	this.ecode = ecode;
}

public String getNetId() {
	return netId;
}

public void setNetId(String netId) {
	this.netId = netId;
}

public String getYr() {
	return yr;
}

public void setYr(String yr) {
	this.yr = yr;
}

public String getMth() {
	return mth;
}

public void setMth(String mth) {
	this.mth = mth;
}

public String getComments() {
	return comments;
}

public void setComments(String comments) {
	this.comments = comments;
}


public String getIsEdit() {
	return isEdit;
}

public void setIsEdit(String isEdit) {
	this.isEdit = isEdit;
}

//Pranali : 22/01/2019 end

}
