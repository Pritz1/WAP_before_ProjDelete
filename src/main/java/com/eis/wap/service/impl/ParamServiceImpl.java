package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.ParamMasterDAO;
import com.eis.wap.domain.ParamMaster;
import com.eis.wap.model.Parameters;
import com.eis.wap.service.ParamService;
import com.eis.wap.util.CommonUtils;

@Component
public class ParamServiceImpl implements ParamService {

	@Autowired
	ParamMasterDAO paramDAO;

	public List<Parameters> getAllParams(String emplvl) {
		List<Parameters> parametersList = null;
		if(null != emplvl && !emplvl.equalsIgnoreCase("")) {
			List<ParamMaster> paramMasterList = paramDAO.getAllParams(emplvl);
			if(null != paramMasterList && !paramMasterList.isEmpty()) {
				parametersList = new ArrayList<Parameters>();
				Parameters parameter = null;
				for(ParamMaster paramMasterData : paramMasterList) {
					parameter = new Parameters();
					parameter.setParam_id(paramMasterData.getParamId());
					parameter.setParam_name(paramMasterData.getParamName());
					parameter.setWeightage(paramMasterData.getWeightage());
					parameter.setAddedBy(paramMasterData.getAddedBy());
					parameter.setAdd_date(paramMasterData.getAddDate());
					parameter.setIsActive(paramMasterData.getIsActive());
					parametersList.add(parameter);
				}
			}
		}

		return parametersList;
	}

	public Map<String,String> getEmpLvlAndDesc() {
		List<Object[]> dataList =  paramDAO.getEmpLvlAndDesc();
		String level = null;
		String levelDesc = null;
		Map<String, String> dataMap = null;
		if(null != dataList && !dataList.isEmpty()) {
			dataMap = new HashMap<String, String>();
			for(Object[] dataObject : dataList) {
				int lvl = (Integer) dataObject[0];
				level = String.valueOf(lvl);
				levelDesc = (String) dataObject[1];
				dataMap.put(level, levelDesc);
			}
		}
		return dataMap;
	}

	public Map<String,String> getParamsIdDesc(String empLevel) {
		Map<String, String> dataMap = null;
		if(null != empLevel) {
			Integer level = Integer.parseInt(empLevel);
			List<Object[]> dataList =  paramDAO.getParamIdAndDesc(level);
			Integer paramId = null;
			String paramName = null;
			if(null != dataList && !dataList.isEmpty()) {
				dataMap = new HashMap<String, String>();
				for(Object[] dataObject : dataList) {
					paramId = (Integer) dataObject[0];
					paramName = (String) dataObject[1];
					dataMap.put(String.valueOf(paramId), paramName);
				}
			}
		}

		return dataMap;
	}

	@Override
	public boolean addParam(Parameters parameter, String yr) {
		boolean statusFlag = false;
		ParamMaster parameterData = new ParamMaster();
		if(null != parameter) {
			parameterData.setParamName(parameter.getParam_name());
			parameterData.setWeightage(parameter.getWeightage());
			parameterData.setLevel(Integer.parseInt(parameter.getEmpLevel()));
			parameterData.setAddDate(CommonUtils.getCurrentDateTime());
			parameterData.setYr(yr);
			parameterData.setAddedBy(parameter.getAddedBy());
			parameterData.setIsActive("1");
			paramDAO.save(parameterData);
		}
		return statusFlag;
	}

	@Override
	public boolean updateParam(Parameters parameter) {
		boolean statusFlag = false;
		if(null != parameter) {
			int count = paramDAO.updateParam(parameter.getParam_name(), parameter.getWeightage(), parameter.getParam_id());
			if(count > 0) {
				statusFlag = true;
			}
		}
		return statusFlag;
	}

	@Override
	public void deleteParam(Parameters parameter) {
		if(null != parameter) {
			paramDAO.deleteParam("0", CommonUtils.getCurrentDateTime(), parameter.getDeletedBy(), 
					parameter.getParam_id(), Integer.parseInt(parameter.getEmpLevel()));
		}
		
	}
	
	public LinkedHashMap getParamIdNameAndWeightage(int level){
		List<Object[]> paramIdWeightageList=paramDAO.getParamIdNameAndWeightage(level);
		LinkedHashMap<Integer,String> paramIdWeightageMap=new LinkedHashMap<Integer,String>();
		if(null != paramIdWeightageList && !paramIdWeightageList.isEmpty()){
			for(Object[] obj: paramIdWeightageList){
				paramIdWeightageMap.put((Integer)obj[0], (String)obj[1]+"~"+(String)(obj[2].toString()));
			}
		}
		return paramIdWeightageMap;
    }
}
