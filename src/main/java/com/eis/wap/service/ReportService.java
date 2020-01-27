package com.eis.wap.service;

import javax.servlet.http.HttpServletResponse;

import jxl.write.WritableWorkbook;

import org.springframework.stereotype.Service;

import com.eis.wap.model.Report;
import com.eis.wap.model.SessionUser;

@Service
public interface ReportService {

	public WritableWorkbook generateReport(HttpServletResponse response, String designstion, SessionUser user,Report report);
}
