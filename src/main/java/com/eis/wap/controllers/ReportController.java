package com.eis.wap.controllers;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eis.wap.model.Employee;
import com.eis.wap.model.Report;
import com.eis.wap.model.SessionUser;
import com.eis.wap.service.EmployeeListService;
import com.eis.wap.service.ReportService;
import com.eis.wap.util.CommonUtils;

@Controller
public class ReportController {
	
	@Autowired
	EmployeeListService empListService;
	@Autowired
	ReportService reportService;

	@RequestMapping("/downloadReport")
	public ModelAndView showParams(@ModelAttribute("report") Report report, HttpSession session) {
		ModelAndView model = new ModelAndView("report");
		SessionUser user = (SessionUser) session.getAttribute("user");
		LinkedHashMap<String,String> finYrMonthsMap=CommonUtils.months(user.getFinStartDt(),user.getLoginYr());
		model.addObject("finYrMonthsMap",finYrMonthsMap);
		return model;
	} 
	
	@RequestMapping("/showEmployeeList")
	public ModelAndView showEmployeeList(@ModelAttribute("report") Report report, HttpSession session) {
		ModelAndView model = new ModelAndView("report");
		if(null != session) {
			SessionUser sessionUser = (SessionUser) session.getAttribute("user");
			if(null != report.getViewType() && report.getViewType().equalsIgnoreCase("2"))
				report.setEmpList(empListService.getEmployeeList(sessionUser,"1",""));//dataReqLvl
			LinkedHashMap<String,String> finYrMonthsMap=CommonUtils.months(sessionUser.getFinStartDt(),sessionUser.getLoginYr());
			model.addObject("finYrMonthsMap",finYrMonthsMap);
		}else {
			model.addObject("error", "Session expired");
		}
		return model;
	}
	
	@PostMapping("/report")
	public void generateReport(HttpServletResponse response, HttpSession session,
			@ModelAttribute("report") Report report) {
		ModelAndView model = new ModelAndView("report");
		if(null != session) {
			SessionUser sessionUser = (SessionUser) session.getAttribute("user");
			reportService.generateReport(response, report.getLevelSelected(), sessionUser,report);
			}
			else {
			model.addObject("error", "Session expired");
		}
	}
	
	
}
