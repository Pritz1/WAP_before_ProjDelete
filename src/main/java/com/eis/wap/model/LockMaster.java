package com.eis.wap.model;

public class LockMaster {
	
	private Integer lockId;
	private int divId;
	private String div_name;
	private String mth; 
	private String yr;
	private String isdataLocked;
	private String lastModBy;
	private String lastModDt;
	private String monthnm;
	private String fromdate;
	private String todate;
	private String yrmth;
	
	public Integer getLockId() {
		return lockId;
	}
	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}
	
	
	public int getDivId() {
		return divId;
	}
	public void setDivId(int divId) {
		this.divId = divId;
	}
	public String getDiv_name() {
		return div_name;
	}
	public void setDiv_name(String div_name) {
		this.div_name = div_name;
	}
	
	public String getMonthnm() {
		return monthnm;
	}
	public void setMonthnm(String monthnm) {
		this.monthnm = monthnm;
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
	public String getYrmth() {
		return yrmth;
	}
	public void setYrmth(String yrmth) {
		this.yrmth = yrmth;
	}
}
