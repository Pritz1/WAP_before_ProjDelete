package com.eis.wap.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LockMaster")
public class LockMasterDomain implements Serializable {

	private static final long serialVersionUID = 4069027217052616904L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer lockId;
	
	@Column(name = "divID")
	private int divId;

	/*@Column(name = "div_name")
	private String divName;*/

	private String mth;

	private String yr;

	private String yrmth;

	private String isDataLocked;

	private String lastModBy;

	private Date lastModDt;
	
	private int noOfAttmpt;

	/*public LockMaster(int divId,String mth,String yr,String yrmth,String isDataLocked,String lastModBy,Date lastModDt){
		divId=this.divId;
		mth=this.mth;
		yr=this.yr;
		yrmth=this.yrmth;
		isDataLocked=this.isDataLocked;
		lastModBy=this.lastModBy;
		lastModDt=this.lastModDt;
	}
	LockMaster(){
		
	}*/

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

	public String getIsDataLocked() {
		return isDataLocked;
	}

	public Date getLastModDt() {
		return lastModDt;
	}
	public void setLastModDt(Date lastModDt) {
		this.lastModDt = lastModDt;
	}
	public void setIsDataLocked(String isDataLocked) {
		this.isDataLocked = isDataLocked;
	}

	public String getLastModBy() {
		return lastModBy;
	}

	public void setLastModBy(String lastModBy) {
		this.lastModBy = lastModBy;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getNoOfAttmpt() {
		return noOfAttmpt;
	}

	public void setNoOfAttmpt(int noOfAttmpt) {
		this.noOfAttmpt = noOfAttmpt;
	}

}
