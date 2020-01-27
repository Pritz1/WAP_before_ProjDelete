package com.eis.wap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eis.wap.domain.DivMaster;
import com.eis.wap.model.Division;

@Service
public interface DivisionService {

	public List<Division> getAllDivisions();
	
	public int deleteDivisionById(String divIds);
	
	public void addDivision(Division division);
	
	public int updateDivision(Division divisionn);
	
	public DivMaster getDivisionDetails(int divId);
}
