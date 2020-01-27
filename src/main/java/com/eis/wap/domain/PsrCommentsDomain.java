package com.eis.wap.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.eis.wap.dao.OtherClassDocRchCovId;
import com.eis.wap.dao.PsrCommentsDomainId;

@Entity
@Table(name = "psrcomments")
public class PsrCommentsDomain implements Serializable{
private static final long serialVersionUID = 4069027217052616904L;
	

//Pranali : 22/01/2019 start
	@EmbeddedId
	private PsrCommentsDomainId psrCommentsId;
	
	private String comments;
	
	private int divid;
	
	public int getDivid() {
		return divid;
	}

	public void setDivid(int divid) {
		this.divid = divid;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public PsrCommentsDomainId getPsrCommentsId() {
		return psrCommentsId;
	}

	public void setPsrCommentsId(PsrCommentsDomainId psrCommentsId) {
		this.psrCommentsId = psrCommentsId;
	}
	
	
	
//Pranali : 22/01/2019 end

}
