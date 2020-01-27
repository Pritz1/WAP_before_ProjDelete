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
@Table(name = "Slabs")
public class Slabs implements Serializable {
	
	private static final long serialVersionUID = 4069027217052616904L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "slab_id")
	private Integer slabId;
	
	@Column(name = "slab_name")
	private String slabName;
	
	@Column(name = "range_min")
	private Float rangeMin;
	
	
	@Column(name = "range_max")
	private Float rangeMax;
	
	private Float points;
	
	@Column(name = "param_id")
	private Integer paramId;
	
	@Column(name = "slab_add_dt")
	private Date slabAddDate;
	
	private String addedBy;
	
	@Column(name = "last_mod_dt")
	private Date lastModDate;
	
	@Column(name = "last_mod_by")
	private String lastModBy;

	public Integer getSlabId() {
		return slabId;
	}

	public void setSlabId(Integer slabId) {
		this.slabId = slabId;
	}

	public String getSlabName() {
		return slabName;
	}

	public void setSlabName(String slabName) {
		this.slabName = slabName;
	}

	public Float getRangeMin() {
		return rangeMin;
	}

	public void setRangeMin(Float rangeMin) {
		this.rangeMin = rangeMin;
	}

	public Float getRangeMax() {
		return rangeMax;
	}

	public void setRangeMax(Float rangeMax) {
		this.rangeMax = rangeMax;
	}

	public Float getPoints() {
		return points;
	}

	public void setPoints(Float points) {
		this.points = points;
	}

	public Integer getParamId() {
		return paramId;
	}

	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}

	public Date getSlabAddDate() {
		return slabAddDate;
	}

	public void setSlabAddDate(Date slabAddDate) {
		this.slabAddDate = slabAddDate;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
