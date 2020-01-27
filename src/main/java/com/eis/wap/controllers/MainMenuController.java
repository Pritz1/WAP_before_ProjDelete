package com.eis.wap.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainMenuController {
	
	  @RequestMapping("/dashboard")  
	   public ModelAndView sayHello() {
	      
		  String message = "Spring MVC Hello World Example.";
	      return new ModelAndView("dashboard", "message", message);  
	  }

}
