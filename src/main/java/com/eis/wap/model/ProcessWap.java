package com.eis.wap.model;

import java.util.List;

public class ProcessWap {
	
	private Integer lockId;
	private String divId;
	private String div_name;
	private String mth; 
	private String yr;
	private String isdataLocked;
	private String lastModBy;
	private String lastModDt;
	private String monthnm;
	private String fromdate;
	private String todate;
	private List<String> yrmth;
	private Long totPriSales;
	private List<Stockist> stList;
	private String yrMth;
	private String errCode;
	
	public Integer getLockId() {
		return lockId;
	}
	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}
	public String getDivId() {
		return divId;
	}
	public void setDivId(String divId) {
		this.divId = divId;
	}
	public String getDiv_name() {
		return div_name;
	}
	public void setDiv_name(String div_name) {
		this.div_name = div_name;
	}
	public String getMth() {
		return mth;
	}
	public void setMth(String mth) {
		this.mth = mth;
	}
	public String getYr() {
		return yr;
	}
	public void setYr(String yr) {
		this.yr = yr;
	}
	public String getIsdataLocked() {
		return isdataLocked;
	}
	public void setIsdataLocked(String isdataLocked) {
		this.isdataLocked = isdataLocked;
	}
	public String getLastModBy() {
		return lastModBy;
	}
	public void setLastModBy(String lastModBy) {
		this.lastModBy = lastModBy;
	}
	public String getLastModDt() {
		return lastModDt;
	}
	public void setLastModDt(String lastModDt) {
		this.lastModDt = lastModDt;
	}
	public String getMonthnm() {
		return monthnm;
	}
	public void setMonthnm(String monthnm) {
		this.monthnm = monthnm;
	}
	public String getFromdate() {
		return fromdate;
	}
	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}
	public String getTodate() {
		return todate;
	}
	public void setTodate(String todate) {
		this.todate = todate;
	}
	public List<String> getYrmth() {
		return yrmth;
	}
	public void setYrmth(List<String> yrmth) {
		this.yrmth = yrmth;
	}
	public Long getTotPriSales() {
		return totPriSales;
	}
	public void setTotPriSales(Long totPriSales) {
		this.totPriSales = totPriSales;
	}
	public List<Stockist> getStList() {
		return stList;
	}
	public void setStList(List<Stockist> stList) {
		this.stList = stList;
	}
	public String getYrMth() {
		return yrMth;
	}
	public void setYrMth(String yrMth) {
		this.yrMth = yrMth;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
}
