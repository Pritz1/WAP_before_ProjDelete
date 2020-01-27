package com.eis.wap.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eis.wap.domain.WapFinalScoreAndIncrementDomain;
import com.eis.wap.model.WapFinalScoreAndIncrement;

@Service
public interface WapFinalScoreAndIncrementService {

	public void addUsers(List<WapFinalScoreAndIncrement> list);
	public Map<String, WapFinalScoreAndIncrementDomain> getScoreAndRating(String yr,String mth,int divId);
}
