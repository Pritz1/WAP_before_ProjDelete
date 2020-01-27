package com.eis.wap.domain;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.eis.wap.dao.DClassDocCovId;

@Entity
@Table(name = "DClassDocCov")
public class DClassDocCov implements Serializable{

	private static final long serialVersionUID = 738058592724170473L;
	
	@EmbeddedId
	private DClassDocCovId dclassDocCovId;
		
		private int totDocs;
		private int covDocs;
		private int divId;
		
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
		public int getDivId() {
			return divId;
		}
		public void setDivId(int divId) {
			this.divId = divId;
		}
		public DClassDocCovId getDclassDocCovId() {
			return dclassDocCovId;
		}
		public void setDclassDocCovId(DClassDocCovId dclassDocCovId) {
			this.dclassDocCovId = dclassDocCovId;
		}
}
