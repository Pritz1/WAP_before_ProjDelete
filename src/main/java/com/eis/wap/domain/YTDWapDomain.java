package com.eis.wap.domain;

	import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.eis.wap.dao.WapFinalScoreIncId;

	@Entity
	@Table(name = "YTDWap")
public class YTDWapDomain implements Serializable{

		private static final long serialVersionUID = -7278721193882105605L;

		@EmbeddedId
		private WapFinalScoreIncId wapFinalScoreIncId;
		
		private Double ysalesPts;
		private Double ysalesScore;
		private Double ypobPts;
		private Double ypobSalesScore;
		private Double ydocRchPts;
		private Double ydocReachScore;
		private Double ypulseChmRchPts;
		private Double ypulseChemReachScore;
		private Double ydocCovPts;
		private Double ydocCoverageScore;
		private Double yotherPts;
		private Double yotherScore;
		private String yrating;
		private Double yfinalScore;
		public WapFinalScoreIncId getWapFinalScoreIncId() {
			return wapFinalScoreIncId;
		}
		public void setWapFinalScoreIncId(WapFinalScoreIncId wapFinalScoreIncId) {
			this.wapFinalScoreIncId = wapFinalScoreIncId;
		}
		public Double getYsalesPts() {
			return ysalesPts;
		}
		public void setYsalesPts(Double ysalesPts) {
			this.ysalesPts = ysalesPts;
		}
		public Double getYsalesScore() {
			return ysalesScore;
		}
		public void setYsalesScore(Double ysalesScore) {
			this.ysalesScore = ysalesScore;
		}
		public Double getYpobPts() {
			return ypobPts;
		}
		public void setYpobPts(Double ypobPts) {
			this.ypobPts = ypobPts;
		}
		public Double getYpobSalesScore() {
			return ypobSalesScore;
		}
		public void setYpobSalesScore(Double ypobSalesScore) {
			this.ypobSalesScore = ypobSalesScore;
		}
		public Double getYdocRchPts() {
			return ydocRchPts;
		}
		public void setYdocRchPts(Double ydocRchPts) {
			this.ydocRchPts = ydocRchPts;
		}
		public Double getYdocReachScore() {
			return ydocReachScore;
		}
		public void setYdocReachScore(Double ydocReachScore) {
			this.ydocReachScore = ydocReachScore;
		}
		public Double getYpulseChmRchPts() {
			return ypulseChmRchPts;
		}
		public void setYpulseChmRchPts(Double ypulseChmRchPts) {
			this.ypulseChmRchPts = ypulseChmRchPts;
		}
		public Double getYpulseChemReachScore() {
			return ypulseChemReachScore;
		}
		public void setYpulseChemReachScore(Double ypulseChemReachScore) {
			this.ypulseChemReachScore = ypulseChemReachScore;
		}
		public Double getYdocCovPts() {
			return ydocCovPts;
		}
		public void setYdocCovPts(Double ydocCovPts) {
			this.ydocCovPts = ydocCovPts;
		}
		public Double getYdocCoverageScore() {
			return ydocCoverageScore;
		}
		public void setYdocCoverageScore(Double ydocCoverageScore) {
			this.ydocCoverageScore = ydocCoverageScore;
		}
		public Double getYotherPts() {
			return yotherPts;
		}
		public void setYotherPts(Double yotherPts) {
			this.yotherPts = yotherPts;
		}
		public Double getYotherScore() {
			return yotherScore;
		}
		public void setYotherScore(Double yotherScore) {
			this.yotherScore = yotherScore;
		}
		public String getYrating() {
			return yrating;
		}
		public void setYrating(String yrating) {
			this.yrating = yrating;
		}
		public Double getYfinalScore() {
			return yfinalScore;
		}
		public void setYfinalScore(Double yfinalScore) {
			this.yfinalScore = yfinalScore;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
}
