package com.eis.wap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eis.wap.domain.YTDWapDomain;

public interface YTDWapDao extends JpaRepository<YTDWapDomain, WapFinalScoreIncId>{

}
