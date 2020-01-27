package com.eis.wap.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FocProd")
public class FocusProductDomain implements Serializable {

	private static final long serialVersionUID = 4069027217052616904L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String mth;

	private String yr;

	private String prodid;

	private String divid;

		
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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


	public String getProdid() {
		return prodid;
	}


	public void setProdid(String prodid) {
		this.prodid = prodid;
	}


	public String getDivid() {
		return divid;
	}


	public void setDivid(String divid) {
		this.divid = divid;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

