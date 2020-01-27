package com.eis.wap.model;

import java.util.Date;

public class Increment {
	
	
	private Integer incr_id;
	private String incr_desc;
	private Float min_score;
	private Float max_score;
	private String rating;
	private Float incr_amount;
	private String addedBy;
	private Date add_date;
	private String last_mod_by;
	private Date last_mod_dt;
	
	
	public Integer getIncr_id() {
		return incr_id;
	}
	public void setIncr_id(Integer incr_id) {
		this.incr_id = incr_id;
	}
	public String getIncr_desc() {
		return incr_desc;
	}
	public void setIncr_desc(String incr_desc) {
		this.incr_desc = incr_desc;
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
	public Date getAdd_date() {
		return add_date;
	}
	public void setAdd_date(Date add_date) {
		this.add_date = add_date;
	}
	public String getLast_mod_by() {
		return last_mod_by;
	}
	public void setLast_mod_by(String last_mod_by) {
		this.last_mod_by = last_mod_by;
	}
	public Date getLast_mod_dt() {
		return last_mod_dt;
	}
	public void setLast_mod_dt(Date last_mod_dt) {
		this.last_mod_dt = last_mod_dt;
	}
	public void setMin_score(Float min_score) {
		this.min_score = min_score;
	}
	public void setMax_score(Float max_score) {
		this.max_score = max_score;
	}
	public void setIncr_amount(Float incr_amount) {
		this.incr_amount = incr_amount;
	}
	public Float getMin_score() {
		return min_score;
	}
	public Float getMax_score() {
		return max_score;
	}
	public Float getIncr_amount() {
		return incr_amount;
	}
	
}
