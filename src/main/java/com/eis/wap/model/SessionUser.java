package com.eis.wap.model;

import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class SessionUser {

private String emp; // ecode
private String employeeName;// employee name
private String finStartDt;
//private Date finEndDt;
private String finEndDt;
private String afm; // level2
private String rm; // level4
private String zm; // level5
private String state; // level3
private String level; //etype
private String fmsDbRef; // wap1.div_master
private String salesDbRef; // wap1.div_master
private String hqRef;
private String netId; //nettrans table
private String loginMth; //on login
private String loginYr; //on login
private String accGrp;
private String error; 
private int divId; 
private String divName; 
private String hqName; 
private HashMap<String,String> lvlDescMap;
private String lockStatus;
private String cocode;
private String desig;
private StringBuffer url;
private String currDate;
private String doj; //Date Of Joining

public String getEmp() {
	return emp;
}
public void setEmp(String emp) {
	this.emp = emp;
}

public String getEmployeeName() {
	return employeeName;
}
public void setEmployeeName(String employeeName) {
	this.employeeName = employeeName;
}


public String getFinEndDt() {
	return finEndDt;
}
public void setFinEndDt(String finEndDt) {
	this.finEndDt = finEndDt;
}
public String getAfm() {
	return afm;
}
public void setAfm(String afm) {
	this.afm = afm;
}
public String getRm() {
	return rm;
}
public void setRm(String rm) {
	this.rm = rm;
}
public String getZm() {
	return zm;
}
public void setZm(String zm) {
	this.zm = zm;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getLevel() {
	return level;
}
public void setLevel(String level) {
	this.level = level;
}
public String getFmsDbRef() {
	return fmsDbRef;
}
public void setFmsDbRef(String fmsDbRef) {
	this.fmsDbRef = fmsDbRef;
}
public String getSalesDbRef() {
	return salesDbRef;
}
public void setSalesDbRef(String salesDbRef) {
	this.salesDbRef = salesDbRef;
}
public String getNetId() {
	return netId;
}
public void setNetId(String netId) {
	this.netId = netId;
}
public String getLoginMth() {
	return loginMth;
}
public void setLoginMth(String loginMth) {
	this.loginMth = loginMth;
}
public String getLoginYr() {
	return loginYr;
}
public void setLoginYr(String loginYr) {
	this.loginYr = loginYr;
}
public String getFinStartDt() {
	return finStartDt;
}
public void setFinStartDt(String finStartDt) {
	this.finStartDt = finStartDt;
}
public String getHqRef() {
	return hqRef;
}
public void setHqRef(String hqRef) {
	this.hqRef = hqRef;
}
public String getAccGrp() {
	return accGrp;
}
public void setAccGrp(String accGrp) {
	this.accGrp = accGrp;
}
public String getError() {
	return error;
}
public void setError(String error) {
	this.error = error;
}
public String getDivName() {
	return divName;
}
public void setDivName(String divName) {
	this.divName = divName;
}
public int getDivId() {
	return divId;
}
public void setDivId(int divId) {
	this.divId = divId;
}
public String getHqName() {
	return hqName;
}
public void setHqName(String hqName) {
	this.hqName = hqName;
}
public HashMap<String, String> getLvlDescMap() {
	return lvlDescMap;
}
public void setLvlDescMap(HashMap<String, String> lvlDescMap) {
	this.lvlDescMap = lvlDescMap;
}
public String getLockStatus() {
	return lockStatus;
}
public void setLockStatus(String lockStatus) {
	this.lockStatus = lockStatus;
}
public String getCocode() {
	return cocode;
}
public void setCocode(String cocode) {
	this.cocode = cocode;
}
public String getDesig() {
	return desig;
}
public void setDesig(String desig) {
	this.desig = desig;
}
public StringBuffer getUrl() {
	return url;
}
public void setUrl(StringBuffer url) {
	this.url = url;
}
public String getCurrDate() {
	return currDate;
}
public void setCurrDate(String currDate) {
	this.currDate = currDate;
}
public String getDoj() {
	return doj;
}
public void setDoj(String doj) {
	this.doj = doj;
}

}