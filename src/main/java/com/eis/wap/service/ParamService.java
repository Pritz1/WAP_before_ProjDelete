package com.eis.wap.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eis.wap.model.Parameters;

@Service
public interface ParamService {
	 
	public List<Parameters> getAllParams(String emplvl);
	public Map<String,String> getEmpLvlAndDesc();
	public Map<String,String> getParamsIdDesc(String empLevel);
	public boolean addParam(Parameters parameter, String yr);
	public boolean updateParam(Parameters parameter);
	public void deleteParam(Parameters parameter);
	public LinkedHashMap getParamIdNameAndWeightage(int level);
}
