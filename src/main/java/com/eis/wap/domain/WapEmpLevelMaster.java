package com.eis.wap.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WapEmpLevelMaster")
public class WapEmpLevelMaster implements Serializable {

	private static final long serialVersionUID = 4069027217052616904L;
	
	@Id
	private Integer level;
	
	@Column(name = "level_desc")
	private String levelDesc;
	
	private Date addDate;
	
	private String addedBy;
	
	private Date delDate;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getLevelDesc() {
		return levelDesc;
	}

	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Date getDelDate() {
		return delDate;
	}

	public void setDelDate(Date delDate) {
		this.delDate = delDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
