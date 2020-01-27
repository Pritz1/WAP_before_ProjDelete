package com.eis.wap.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Div_Master")
public class DivMaster implements Serializable {
	
	private static final long serialVersionUID = 4069027217052616904L;
	
	@Id
   @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer divId;
	
	private String divName;
	
	private String fmsDbRef;
	
	private String salesDbRef;
	
	private String hqRef;

	public Integer getDivId() {
		return divId;
	}

	public void setDivId(Integer divId) {
		this.divId = divId;
	}

	public String getDivName() {
		return divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
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

	public String getHqRef() {
		return hqRef;
	}

	public void setHqRef(String hqRef) {
		this.hqRef = hqRef;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
}