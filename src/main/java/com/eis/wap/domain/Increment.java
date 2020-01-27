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
@Table(name = "Increment")
public class Increment implements Serializable {
	
	private static final long serialVersionUID = 4069027217052616904L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "incr_id")
	private Integer incrId;
	
	@Column(name = "incr_desc")
	private String incrDesc;
	
	@Column(name = "min_score")
	private float minScore;
	
	@Column(name = "max_score")
	private Float maxScore;
	
	private String rating; 
	
	private String addedBy;
	
	@Column(name = "add_date")
	private Date addDate;
	
	@Column(name = "last_mod_dt")
	private Date lastModDate;
	
	@Column(name = "last_mod_by")
	private String lastModBy;

	@Column(name = "incr_amount")
	private float incrAmount;

	public Integer getIncrId() {
		return incrId;
	}

	public void setIncrId(Integer incrId) {
		this.incrId = incrId;
	}

	public String getIncrDesc() {
		return incrDesc;
	}

	public void setIncrDesc(String incrDesc) {
		this.incrDesc = incrDesc;
	}

	public float getMinScore() {
		return minScore;
	}

	public void setMinScore(float minScore) {
		this.minScore = minScore;
	}

	public Float getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Float maxScore) {
		this.maxScore = maxScore;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
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

	public Date getLastModDate() {
		return lastModDate;
	}

	public void setLastModDate(Date lastModDate) {
		this.lastModDate = lastModDate;
	}

	public String getLastModBy() {
		return lastModBy;
	}

	public void setLastModBy(String lastModBy) {
		this.lastModBy = lastModBy;
	}

	public float getIncrAmount() {
		return incrAmount;
	}

	public void setIncrAmount(float incrAmount) {
		this.incrAmount = incrAmount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
