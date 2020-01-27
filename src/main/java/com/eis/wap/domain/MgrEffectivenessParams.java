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
@Table(name = "MgrEffectivenessParams")
public class MgrEffectivenessParams implements Serializable {

	private static final long serialVersionUID = 4069027217052616904L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)  
	private Integer mgrEffectivenessParamsId;
	@Column(name = "YR")
	private String yr;
	@Column(name = "MTH")
	private String mth;
	@Column(name = "ManagerID")
	private String managerId;
	private String mgrId2;
	@Column(name = "NetId")
	private String netid;
	@Column(name = "Ecode")
	private String ecode;
	//private String empName;
	@Column(name = "Param1")
	private Float param1;
	@Column(name = "Param2")
	private Float param2;
	@Column(name = "Param3")
	private Float param3;
	@Column(name = "Param4")
	private Float param4;
	
	private Float mgr2Param1;
	private Float mgr2Param2;
	private Float mgr2Param3;
	private Float mgr2Param4;
	
	@Column(name = "AddDate")
	private Date addDate;
	@Column(name = "AddedBy")
	private String addedBy;
	@Column(name = "ModDate")
	private String modDate;
	@Column(name = "ModBy")
	private String modBy;
	
	@Column(name = "AchParam1")
	private double achParam1=0.0;
	@Column(name = "AchParam2")
	private double achParam2=0.0;
	@Column(name = "AchParam3")
	private double achParam3=0.0;
	@Column(name = "AchParam4")
	private double achParam4=0.0;
	
	@Column(name = "divId")
	private int div;
	
	
	public Integer getMgrEffectivenessParamsId() {
		return mgrEffectivenessParamsId;
	}
	public void setMgrEffectivenessParamsId(Integer mgrEffectivenessParamsId) {
		this.mgrEffectivenessParamsId = mgrEffectivenessParamsId;
	}
	
	public String getEcode() {
		return ecode;
	}
	public void setEcode(String ecode) {
		this.ecode = ecode;
	}
	/*public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}*/
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
	public Float getParam1() {
		return param1;
	}
	public void setParam1(Float param1) {
		this.param1 = param1;
	}
	public Float getParam2() {
		return param2;
	}
	public void setParam2(Float param2) {
		this.param2 = param2;
	}
	public Float getParam3() {
		return param3;
	}
	public void setParam3(Float param3) {
		this.param3 = param3;
	}
	public Float getParam4() {
		return param4;
	}
	public void setParam4(Float param4) {
		this.param4 = param4;
	}
	public String getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getModDate() {
		return modDate;
	}
	public void setModDate(String modDate) {
		this.modDate = modDate;
	}
	public String getModBy() {
		return modBy;
	}
	public void setModBy(String modBy) {
		this.modBy = modBy;
	}
	public double getAchParam1() {
		return achParam1;
	}
	public void setAchParam1(double achParam1) {
		this.achParam1 = achParam1;
	}
	public double getAchParam2() {
		return achParam2;
	}
	public void setAchParam2(double achParam2) {
		this.achParam2 = achParam2;
	}
	public double getAchParam3() {
		return achParam3;
	}
	public void setAchParam3(double achParam3) {
		this.achParam3 = achParam3;
	}
	public double getAchParam4() {
		return achParam4;
	}
	public void setAchParam4(double achParam4) {
		this.achParam4 = achParam4;
	}
	public String getNetid() {
		return netid;
	}
	public void setNetid(String netid) {
		this.netid = netid;
	}
	public int getDiv() {
		return div;
	}
	public void setDiv(int div) {
		this.div = div;
	}
	public Float getMgr2Param1() {
		return mgr2Param1;
	}
	public void setMgr2Param1(Float mgr2Param1) {
		this.mgr2Param1 = mgr2Param1;
	}
	public Float getMgr2Param2() {
		return mgr2Param2;
	}
	public void setMgr2Param2(Float mgr2Param2) {
		this.mgr2Param2 = mgr2Param2;
	}
	public Float getMgr2Param3() {
		return mgr2Param3;
	}
	public void setMgr2Param3(Float mgr2Param3) {
		this.mgr2Param3 = mgr2Param3;
	}
	public Float getMgr2Param4() {
		return mgr2Param4;
	}
	public void setMgr2Param4(Float mgr2Param4) {
		this.mgr2Param4 = mgr2Param4;
	}
	public String getMgrId2() {
		return mgrId2;
	}
	public void setMgrId2(String mgrId2) {
		this.mgrId2 = mgrId2;
	}
	
}
