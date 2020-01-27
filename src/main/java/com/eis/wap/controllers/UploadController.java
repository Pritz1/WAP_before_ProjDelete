package com.eis.wap.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.eis.wap.service.UploadService;

@Controller
public class UploadController {

	@Autowired
	private UploadService uploadService;

	
	@RequestMapping("/upload")
	public String showUploadMngrOpt(){
		System.out.println("in method");
		return "uploadexcel";
	}

	@PostMapping(value="/fileUpload")
	public ModelAndView fileUpload(@RequestParam CommonsMultipartFile file){  
		List<String> statusList = null;
		if(null != file) {
			statusList = uploadService.uploadExcel(file);
		}
		return new ModelAndView("showFile", "message", statusList);  
	} 

}
