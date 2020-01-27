package com.eis.wap.domain;

	import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.eis.wap.dao.WapFinalScoreIncId;

	@Entity
	@Table(name = "Wap_Final_ScoreAndIncrement")
public class WapFinalScoreAndIncrementDomain implements Serializable{

		private static final long serialVersionUID = -7278721193882105605L;

		@EmbeddedId
		private WapFinalScoreIncId wapFinalScoreIncId;
		
		private Double salesPts;
		private Double salesScore;
		private Double pobPts;
		private Double pobSalesScore;
		private Double docRchPts;
		private Double docReachScore;
		private Double pulseChmRchPts;
		private Double pulseChemReachScore;
		private Double docCovPts;
		private Double docCoverageScore;
		private Double otherPts;
		private Double otherScore;
		private String rating;
		private Double finalScore;
		
		public Double getSalesScore() {
			return salesScore;
		}
		public void setSalesScore(Double salesScore) {
			this.salesScore = salesScore;
		}
		public Double getPobSalesScore() {
			return pobSalesScore;
		}
		public void setPobSalesScore(Double pobSalesScore) {
			this.pobSalesScore = pobSalesScore;
		}
		public Double getDocReachScore() {
			return docReachScore;
		}
		public void setDocReachScore(Double docReachScore) {
			this.docReachScore = docReachScore;
		}
		public Double getPulseChemReachScore() {
			return pulseChemReachScore;
		}
		public void setPulseChemReachScore(Double pulseChemReachScore) {
			this.pulseChemReachScore = pulseChemReachScore;
		}
		public Double getDocCoverageScore() {
			return docCoverageScore;
		}
		public void setDocCoverageScore(Double docCoverageScore) {
			this.docCoverageScore = docCoverageScore;
		}
		public Double getOtherScore() {
			return otherScore;
		}
		public void setOtherScore(Double otherScore) {
			this.otherScore = otherScore;
		}
		public String getRating() {
			return rating;
		}
		public void setRating(String rating) {
			this.rating = rating;
		}
		public Double getFinalScore() {
			return finalScore;
		}
		public void setFinalScore(Double finalScore) {
			this.finalScore = finalScore;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		public WapFinalScoreIncId getWapFinalScoreIncId() {
			return wapFinalScoreIncId;
		}
		public void setWapFinalScoreIncId(WapFinalScoreIncId wapFinalScoreIncId) {
			this.wapFinalScoreIncId = wapFinalScoreIncId;
		}
		public Double getSalesPts() {
			return salesPts;
		}
		public void setSalesPts(Double salesPts) {
			this.salesPts = salesPts;
		}
		public Double getPobPts() {
			return pobPts;
		}
		public void setPobPts(Double pobPts) {
			this.pobPts = pobPts;
		}
		public Double getDocRchPts() {
			return docRchPts;
		}
		public void setDocRchPts(Double docRchPts) {
			this.docRchPts = docRchPts;
		}
		public Double getPulseChmRchPts() {
			return pulseChmRchPts;
		}
		public void setPulseChmRchPts(Double pulseChmRchPts) {
			this.pulseChmRchPts = pulseChmRchPts;
		}
		public Double getDocCovPts() {
			return docCovPts;
		}
		public void setDocCovPts(Double docCovPts) {
			this.docCovPts = docCovPts;
		}
		public Double getOtherPts() {
			return otherPts;
		}
		public void setOtherPts(Double otherPts) {
			this.otherPts = otherPts;
		}
		
}
