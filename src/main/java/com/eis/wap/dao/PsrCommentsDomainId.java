package com.eis.wap.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PsrCommentsDomainId implements Serializable{

	private static final long serialVersionUID = 2411503547952123989L;
	
	@Column(name="netid")
	private String netid;
	@Column(name="ecode")
	private String ecode;
	@Column(name="yr")
	private String yr;
	@Column(name="mth")
	private String mth;
	
	public String getNetid() {
		return netid;
	}
	public void setNetid(String netid) {
		this.netid = netid;
	}
	public String getEcode() {
		return ecode;
	}
	public void setEcode(String ecode) {
		this.ecode = ecode;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
