package com.eis.wap.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eis.wap.domain.DClassDocCov;
import com.eis.wap.domain.OtherClassDocRchAndCov;
import com.eis.wap.domain.WapFinalScoreAndIncrementDomain;

public interface DClassDocCovDao extends JpaRepository<DClassDocCov, DClassDocCovId>{
}
