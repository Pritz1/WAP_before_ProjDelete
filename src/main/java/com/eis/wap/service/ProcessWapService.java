package com.eis.wap.service;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

import com.eis.wap.model.ProcessWap;
import com.eis.wap.model.SessionUser;

@Service
public interface ProcessWapService {
	
	public HashMap<String, String> processAndLockWap(ProcessWap processWap,SessionUser user,LinkedHashMap monthsMap);

}
