package com.eis.wap.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.ParamMaster;
import com.eis.wap.domain.WapFinalScoreAndIncrementDomain;



public interface WapFinalScoreAndIncrementDao extends JpaRepository<WapFinalScoreAndIncrementDomain, WapFinalScoreIncId>{

	@Query(value = "select netid,ecode,yr,mth,salesScore,POBSalesScore,DocReachScore,PulseChemReachScore,DocCoverageScore,"
			+ "otherScore,rating,finalscore from wap_final_scoreandincrement where yr = ?1 and mth = ?2 and divId=?3", nativeQuery = true)
	public List<Object[]> getScoreAndRating(String yr,String mth,int divId);
}
