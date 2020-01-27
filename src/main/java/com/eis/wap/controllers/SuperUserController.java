package com.eis.wap.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eis.wap.dao.FocusProdDaoCustom;
import com.eis.wap.dao.FocusProdDao;
import com.eis.wap.model.AdminUser;
import com.eis.wap.model.Division;
import com.eis.wap.model.Product;
import com.eis.wap.model.SessionUser;
import com.eis.wap.model.UsersForm;
import com.eis.wap.service.DivisionService;
import com.eis.wap.service.FocusProductService;
import com.eis.wap.service.UserService;

@Controller
public class SuperUserController {
 
	@Autowired
	UserService userService;
	
	@Autowired
	DivisionService divisionService;
	
	@Autowired 
	FocusProductService focProdService;
	
	@RequestMapping("/users")
	public ModelAndView showUsers(@ModelAttribute("userform") UsersForm userform) {
		ModelAndView model = new ModelAndView("users");
		userform.setUsers(userService.getUsers());
		model.addObject("userform",userform);
		return model;
	}
	
	@RequestMapping(value="/adminUsers")
	public ModelAndView showAdminUsers(@ModelAttribute("adminUsers") AdminUser adminuser,HttpSession session,@RequestParam("presentEcodes") String presentEcodes) {
		ModelAndView model = new ModelAndView("adminUsers");
		if(null != session) {
			SessionUser user=(SessionUser) session.getAttribute("user");
			List<AdminUser> list = userService.getAdminUsers(user.getFmsDbRef(),presentEcodes);
			model.addObject("adminUserList",list);
		}
		
		return model;
	}
	
	@RequestMapping(value="/addUsers",method=RequestMethod.POST)
	public ModelAndView addUsers(@ModelAttribute("userform") UsersForm userform) {
		ModelAndView model = new ModelAndView("users");
		userService.addUsers(userform);
		model.addObject("users", userService.getUsers());
		model.addObject("userform", userform);
		return model;
	}
	
	@RequestMapping(value="/editUser",method=RequestMethod.POST)
	public ModelAndView editUser(@ModelAttribute("userform") UsersForm userform) {
		userService.updateUser(userform);
		ModelAndView model = new ModelAndView("users");
		userform.setUsers(userService.getUsers());
		model.addObject("userform",userform);
		return model;
	}
	
	@RequestMapping("/division")
	public ModelAndView showDiv(@ModelAttribute("division") Division divobj) {
		ModelAndView model = new ModelAndView("division");
		List<Division> list = divisionService.getAllDivisions();
		model.addObject("divList",list);
		return model;
	}
	
	@RequestMapping(value="/deleteDiv")
	public ModelAndView deleteDivision(@ModelAttribute("division") Division divobj,@RequestParam("idList") String idList) {
		divisionService.deleteDivisionById(idList);
		ModelAndView model = new ModelAndView("division");
		List<Division> list = divisionService.getAllDivisions();
		model.addObject("divList",list);
		return model;
	}
	
	@RequestMapping(value="/addUpdateDivision", method = RequestMethod.POST)
	public ModelAndView addDivision(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("division") Division divobj) {
		if(null!=divobj.getAddEdit() && divobj.getAddEdit().equalsIgnoreCase("edit") &&  null!=divobj.getDivId() && divobj.getDivId()!=0 ){
			divisionService.updateDivision(divobj);
		}else
		if(divobj.getAddEdit().equalsIgnoreCase("add")){
			if(divobj.getDivName()!=null && !divobj.getDivName().equalsIgnoreCase("") && 
			   divobj.getFmsDbRef()!=null && !divobj.getFmsDbRef().equalsIgnoreCase("") && 
			   divobj.getSalesDbRef()!=null && !divobj.getSalesDbRef().equalsIgnoreCase(""))
				divisionService.addDivision(divobj);
		} //TODO add exception in else
		
		ModelAndView model = new ModelAndView("division");
		List<Division> list=divisionService.getAllDivisions();
		model.addObject("divList",list);
		return model;
	}
	
	@RequestMapping("/lockHistory")
	public String showHistory() {
	    return "lockHistory";
	}
	
	@RequestMapping("/deleteWapData")
	public String deleteData() {
	    return "deleteWapData";
	}
	
	@RequestMapping("/cloneParam")
	public String cloneParam() {
	    return "cloneParam";
	}
	
	
	@RequestMapping("/showFocusProds")
	public ModelAndView showFocusProds(@ModelAttribute("product") Product prod,HttpSession session) {
		SessionUser user=(SessionUser)session.getAttribute("user");	
		ArrayList<Product> prodList=null;
		if(null != user.getLockStatus() && user.getLockStatus().equalsIgnoreCase("Y")){
			prodList=focProdService.getLockedFocProducts(user.getLoginYr(),user.getLoginMth(),user.getDivId(),user.getFmsDbRef());
		}else{
			prodList=focProdService.getFocProducts(user.getHqRef(),user.getFmsDbRef());
		}
		
		ModelAndView model = new ModelAndView("focusProds");
		model.addObject("prodList",prodList);
		return model;
	}
	
	@RequestMapping("/removeAllFocProd")
	public ModelAndView removeFocusProds(@ModelAttribute("product") Product prod,HttpSession session) {
		SessionUser user=(SessionUser)session.getAttribute("user");	
		int i=focProdService.removeAllFocProducts(user.getHqRef(),user.getFmsDbRef());
		ModelAndView model = new ModelAndView("focusProds");
		if(i>0){
			model.addObject("response","Removed "+i+" Products From Focus Product List");
		}
		
		return model;
	}
	
	@RequestMapping("/removeSelFocProd")
	public ModelAndView removeSelFocusProds(@ModelAttribute("product") Product prod,HttpSession session,
			@RequestParam(value="prodIdArr") String prodIdArr) {
		SessionUser user=(SessionUser)session.getAttribute("user");	
		int i=focProdService.removeSelFocProducts(user.getHqRef(),user.getFmsDbRef(),prodIdArr);
		ModelAndView model = new ModelAndView("focusProds");
		if(i>0){
			model.addObject("response","Removed "+i+" Products From Focus Product List");
		}else{
			model.addObject("response","No Product Updated");
		}
		
		return model;
	}
	
	@RequestMapping("/showAllProds")
	public ModelAndView showAllProd(@ModelAttribute("addProduct") Product prod,HttpSession session) {
		SessionUser user=(SessionUser)session.getAttribute("user");	
		ArrayList<Product> prodList=focProdService.getAllProds(user.getHqRef(),user.getFmsDbRef());
		ModelAndView model = new ModelAndView("addProduct");
		model.addObject("prodList",prodList);
		return model;
	}
	
	@RequestMapping("/addFocProds")
	public ModelAndView addAsFocProd(@ModelAttribute("addProduct") Product prod,HttpSession session,
			@RequestParam(value="prodIdArr") String prodIdArr) {
		SessionUser user=(SessionUser)session.getAttribute("user");	
		int i=focProdService.addFocProds(user.getHqRef(),user.getFmsDbRef(),prodIdArr);
		ModelAndView model = new ModelAndView("addProduct");
		if(i>0){
			model.addObject("response","Added "+i+" Products In Focus Product List");
		}else{
			model.addObject("response","No Product Added");
		}
		return model;
	}
}
