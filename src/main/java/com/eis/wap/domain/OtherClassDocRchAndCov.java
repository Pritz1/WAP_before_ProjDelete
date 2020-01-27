package com.eis.wap.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.eis.wap.dao.OtherClassDocRchCovId;
import com.eis.wap.dao.WapFinalScoreIncId;

@Entity
@Table(name = "OtherClassDocRchAndCov")
public class OtherClassDocRchAndCov implements Serializable{

	private static final long serialVersionUID = 738058592724170473L;

	
	@EmbeddedId
	private OtherClassDocRchCovId otherClassDocRchCovId;
		
		private int totDocs;
		private int rchdDocs;
		private int covDocs;
		private int divId;
		
		public int getRchdDocs() {
			return rchdDocs;
		}
		public void setRchdDocs(int rchdDocs) {
			this.rchdDocs = rchdDocs;
		}
		public int getTotDocs() {
			return totDocs;
		}
		public void setTotDocs(int totDocs) {
			this.totDocs = totDocs;
		}
		public int getCovDocs() {
			return covDocs;
		}
		public void setCovDocs(int covDocs) {
			this.covDocs = covDocs;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		public OtherClassDocRchCovId getOtherClassDocRchCovId() {
			return otherClassDocRchCovId;
		}
		public void setOtherClassDocRchCovId(OtherClassDocRchCovId otherClassDocRchCovId) {
			this.otherClassDocRchCovId = otherClassDocRchCovId;
		}
		public int getDivId() {
			return divId;
		}
		public void setDivId(int divId) {
			this.divId = divId;
		}
		
		
}
