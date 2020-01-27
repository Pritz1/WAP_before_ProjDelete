package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.DivisionDAO;
import com.eis.wap.domain.DivMaster;
import com.eis.wap.model.Division;
import com.eis.wap.service.DivisionService;

@Component
public class DivisionServiceImpl implements DivisionService {

	@Autowired
	DivisionDAO divMasterDAO;
	
	
	public List<Division> getAllDivisions() {
		List<Division> divisionList = null;
		Division division = null;
		List<DivMaster> divMasterList = divMasterDAO.findAll();
		if(null != divMasterList && !divMasterList.isEmpty()) {
			divisionList = new ArrayList<Division>();
			for(DivMaster div : divMasterList) {
				division = new Division();
				division.setDivId(div.getDivId());
				division.setDivName(div.getDivName());
				division.setFmsDbRef(div.getFmsDbRef());
				division.setSalesDbRef(div.getSalesDbRef());
				divisionList.add(division);
			}
		}
		return divisionList;
	}

	public int deleteDivisionById(String divIds) {
		List<Integer> divIdList = new ArrayList<Integer>();
		int count = 0;
		if(null != divIds && !divIds.isEmpty()) {
			List<String> idList = Arrays.asList(divIds.split(","));
			if(idList.size() > 1) {
				for(String divId : idList) {
					divIdList.add(Integer.parseInt(divId));
				}
			}else {
				divIdList.add(Integer.parseInt(idList.get(0)));
			}
			// call DB query to delete div_Master data
			count = divMasterDAO.deleteById(divIdList);
		}
		
		
		return count;
	}

	public void addDivision(Division division) {
		DivMaster divMaster = new DivMaster();
		if(null != division) {
			divMaster.setDivName(division.getDivName());
			divMaster.setFmsDbRef(division.getFmsDbRef());
			divMaster.setSalesDbRef(division.getSalesDbRef());
			divMasterDAO.saveAndFlush(divMaster);
		}
		
	}

	public int updateDivision(Division division) {
		int count = divMasterDAO.updateDivMaster(division.getDivName(), division.getFmsDbRef(), division.getSalesDbRef(), division.getDivId());
		return count;
	}
	
	public DivMaster getDivisionDetails(int divId) {
		DivMaster divObject = divMasterDAO.getDivisionDetails(divId);
		return divObject;
	}

}
