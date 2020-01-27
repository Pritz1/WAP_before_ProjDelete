package com.eis.wap.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.events.Comment;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eis.wap.constants.CommonConstants;
import com.eis.wap.dao.CommentsDao;
import com.eis.wap.dao.IncrementDao;
import com.eis.wap.dao.LockMasterDao;
import com.eis.wap.dao.ParamMasterDAO;
import com.eis.wap.dao.SlabDao;
import com.eis.wap.dao.WapCalDao;
import com.eis.wap.dao.WapCalDaoCustom;
import com.eis.wap.domain.LockMasterDomain;
import com.eis.wap.model.AppraisedEmp;
import com.eis.wap.model.ChangeDate;
import com.eis.wap.model.EmpList;
import com.eis.wap.model.Employee;
import com.eis.wap.model.Increment;
import com.eis.wap.model.LockMaster;
import com.eis.wap.model.Parameters;
import com.eis.wap.model.ProcessWap;
import com.eis.wap.model.Product;
import com.eis.wap.model.PsrComments;
import com.eis.wap.model.PsrPerformanceReview;
import com.eis.wap.model.Report;
import com.eis.wap.model.ReviewEmployee;
import com.eis.wap.model.ReviewStatus;
import com.eis.wap.model.SessionUser;
import com.eis.wap.model.Slabs;
import com.eis.wap.model.WapCalculation;
import com.eis.wap.service.CommentService;
import com.eis.wap.service.EmployeeListService;
import com.eis.wap.service.IncrementService;
import com.eis.wap.service.LockMasterService;
import com.eis.wap.service.LoginService;
import com.eis.wap.service.MgrEffectivenessParamService;
import com.eis.wap.service.ParamService;
import com.eis.wap.service.ProcessWapService;
import com.eis.wap.service.ReportService;
import com.eis.wap.service.SlabService;
import com.eis.wap.service.WapProcessService;
import com.eis.wap.util.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@Scope("session")

public class WapMenuController { 
	
	
	int count=0;
	private static final Logger logger = Logger.getLogger(WapMenuController.class);	
		
//	@Autowired
//	PsrCommentDao commentDAO;

	@Autowired
	ParamMasterDAO paramDAO;

	@Autowired
	SlabDao slabdao;

	@Autowired
	WapCalDao wapdao;

	@Autowired
	IncrementDao incrdao;

	@Autowired
	LockMasterDao lockMasterDAO;

	/*@Autowired
	ProcessWapDao pwapdao;*/
	
//	@Autowired
//	PsrCommentService commentService;

	@Autowired
	CommentsDao commentDao;

	@Autowired
	ParamService paramService;

	@Autowired
	IncrementService incrementService;

	@Autowired
	SlabService slabService;
	
	@Autowired
	LockMasterService lockMasterService;
	
	@Autowired
	WapProcessService wapProcessService;
	
	@Autowired
	ProcessWapService processWapService;
	
	@Autowired
	EmployeeListService empListService;
	
	@Autowired
	MgrEffectivenessParamService mgrEffectivenessParamService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	LoginService loginService;
	
	@RequestMapping("/parameters")
	public ModelAndView showParams(@ModelAttribute("parameter") Parameters params, HttpSession session) {
		ModelAndView model = new ModelAndView("parameters");
		SessionUser user = (SessionUser) session.getAttribute("user");
		
		try{
			logger.info("Custom Info - showParams : "+user.getEmployeeName());
		Map<String,String> empLvlDescMap = paramService.getEmpLvlAndDesc();
		model.addObject("empLvlDescMap", empLvlDescMap);
		}catch(Exception e){
			logger.error("Custom Error - showParams : ",e);
		}
		return model;
	}  

	@GetMapping(value="/getparameters")
	public ResponseEntity<String> getParams(HttpSession session,@RequestParam(value="empLevel") String empLevel)
	{
		//System.out.println("Emplevel : "+empLevel);		  
		List<Parameters> paramList = paramService.getAllParams(empLevel);
		//System.out.println("\n paramList : "+paramList);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String jsonString = gson.toJson(paramList);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.CREATED); 
	}

	@PostMapping(value="/addUpdateParam")
	public ModelAndView addUpdateParam(@ModelAttribute("parameter") Parameters param, HttpSession session) 
	{
		ModelAndView model = null;
		SessionUser user = (SessionUser) session.getAttribute("user");
		if(null != user) {
			logger.info("Custom Info - addUpdateParam : "+user.getEmployeeName());	
		if(param.getParam_id()!=null && ((param.getAddEdit()).equalsIgnoreCase("edit"))){ 
			if(param.getParam_id()!=0){
				paramService.updateParam(param);
			}
		}
		else if((param.getAddEdit()).equalsIgnoreCase("add"))
		{
			if(param.getParam_name()!=null && !param.getParam_name().equalsIgnoreCase("") && param.getWeightage()!=null)
					paramService.addParam(param, user.getLoginYr());
			logger.info("Custom Info - addUpdateParam PARAM ADDED : "+user.getEmployeeName());	
			//else error
		} //else show error
		List<Parameters> paramList = paramService.getAllParams(param.getEmpLevel());
		Map<String,String> empLvlDescMap = paramService.getEmpLvlAndDesc();
			model = new ModelAndView("parameters");
		model.addObject("empLvlDescMap", empLvlDescMap);
			/*model.addObject("param",param);
			System.out.println(param.toString());*/
		model.addObject("paramList", paramList);
		//System.out.println("paramList : "+paramList);
		}else{
			model = new ModelAndView("parameters");// change view name to error page
			model.addObject("error","Session expired");
		}
		return model;
	}

	@RequestMapping(value = "/deleteParam", method = RequestMethod.GET)
	public ModelAndView deleteParam(@ModelAttribute("parameter") Parameters param) 
	{
		paramService.deleteParam(param);
		logger.info("Custom Info - addUpdateParam PARAM DELETED : "+param.getEmpLevel()+" param id : "+param.getParam_id());
		List<Parameters> paramList = paramService.getAllParams(param.getEmpLevel());
		ModelAndView model = new ModelAndView("parameters");
		Map<String,String> empLvlDescMap = paramService.getEmpLvlAndDesc();
		model.addObject("empLvlDescMap", empLvlDescMap);
		model.addObject("isDelete", "delete");
		model.addObject("paramList", paramList);
		return model;
	}
	/*request handling for parameters : End*/

	/*request handling for Slabs : Start*/
	@RequestMapping(value="/slabs", method = RequestMethod.GET)
	public ModelAndView showSlabs(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("slabs") Slabs slab) {

		ModelAndView model = new ModelAndView("slabs");
		Map<String,String> empLvlDescMap = paramService.getEmpLvlAndDesc();
		model.addObject("empLvlDescMap", empLvlDescMap);
		
		return model;
	}

	@RequestMapping(value="/getParams", method=RequestMethod.GET)
	public ResponseEntity<String> getParamsIdDesc(HttpSession session,@RequestParam(value="empLevel") String empLevel)
	{
		Map<String,String> paramMap = paramService.getParamsIdDesc(empLevel);
		com.google.gson.Gson gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
		String jsonString = gson.toJson(paramMap);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.CREATED); 
	}


	@RequestMapping(value="/getSlabs", method=RequestMethod.GET)
	public ResponseEntity<String> listParamWiseSlabs(HttpSession session,@RequestParam(value="paramId") Integer paramId)
	{
		//System.out.println("paramId : "+paramId);
		List<Slabs> slabList = slabService.getSlabListByParamId(paramId);
		//System.out.println("\n slabList : "+slabList);
		com.google.gson.Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String jsonString = ((null != slabList) ? gson.toJson(slabList) : gson.toJson(""));
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.CREATED); 
	}

	@RequestMapping(value="/addSlab", method = RequestMethod.POST)
	public ModelAndView addSlab(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("slabs") Slabs slab) {
		if(slab.getAddEdit()!=null && slab.getAddEdit().equalsIgnoreCase("edit") && slab.getSlabId()!=null && slab.getSlabId()!=0){ 
			slabService.updateSlab(slab);
		}else if(slab.getAddEdit().equalsIgnoreCase("add")){
			if(slab.getSlabName()!=null && !slab.getSlabName().equalsIgnoreCase("") && slab.getParamId()!=null)
				slabService.addSlab(slab);
		}else{
			logger.info("Custom Info - addSlab : Neither Edit Nor Delete Found : slab id - "+slab.getSlabId());
		}
	    slab.setAddEdit(slab.getAddEdit());
	    slab.setParamId(slab.getParamId());
		ModelAndView model = new ModelAndView("slabs");
		Map<String,String> paramMap = paramService.getParamsIdDesc(slab.getEmpLevel());
		model.addObject("paramMap", paramMap);
		Map<String,String> empLvlDescMap = paramService.getEmpLvlAndDesc();
		model.addObject("empLvlDescMap", empLvlDescMap);
		List<Slabs> slabList = slabService.getSlabListByParamId(slab.getParamId());
		model.addObject("slabList", slabList);
		model.addObject("slab", slab);
		
		return model;
	}

	@RequestMapping(value = "/deleteSlab", method = RequestMethod.GET)
	public ModelAndView deleteSlab(@ModelAttribute("slabs") Slabs slab) {
		
		slabService.deleteSlab(slab.getParamId(), slab.getSlabId());
		logger.info("Custom Info - DeleteSlab : slab id - "+slab.getSlabId()+"Date : "+new java.util.Date());
		ModelAndView model = new ModelAndView("slabs");
		Map<String,String> paramMap = paramService.getParamsIdDesc(slab.getEmpLevel());
		model.addObject("paramMap", paramMap);
		Map<String,String> empLvlDescMap = paramService.getEmpLvlAndDesc();
		model.addObject("empLvlDescMap", empLvlDescMap);
		List<Slabs> slabList = slabService.getSlabListByParamId(slab.getParamId());
		model.addObject("slabList", slabList);
		model.addObject("slab", slab);
		return model;
	}
	/*request handling for Slabs : End*/

	/*request handling for Wap Demo : Start*/
	@RequestMapping("/wapcalc")
	public ModelAndView wapcalc(@ModelAttribute("wapCal") WapCalculation wapcal) {
		ModelAndView model = new ModelAndView("wapCalculation");
		List<WapCalculation> wapCalParamList = 	wapProcessService.getParams();
		// Add code service impl
		model.addObject("paramList", wapCalParamList);
		return model;
	}

	@RequestMapping(value ="/getWapScore",method = RequestMethod.GET)
	public @ResponseBody
	String getWapScore(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("wapCalform") WapCalculation wap) {
		//ModelAndView model = new ModelAndView("wapCalculation");
		Float score = 	wapProcessService.getScore(wap.getParam_id(),wap.getAchieve());
		//model.addObject("score", score);
		return score+"";
	}

	@RequestMapping(value ="/getRating",method = RequestMethod.GET)
	public @ResponseBody
	String getRating(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("wapCalform") WapCalculation wap) {
		//ModelAndView model = new ModelAndView("wapCalculation");
		String rating = 	wapdao.getRating(wap.getWap());
		//model.addObject("score", score);

		return rating;
	}

	@RequestMapping("/showincrement")
	public ModelAndView showIncrement(@ModelAttribute("increment") Increment incr) {
		ModelAndView model = new ModelAndView("increment");
		List<Increment> incrList = 	incrementService.showAllIncrement();
		model.addObject("incrList", incrList);
		return model;
	}
	
	@RequestMapping(value="/addUpdateincrement",method = RequestMethod.POST)
	public ModelAndView addUpdateIncrement(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("increment") Increment incr) {
		ModelAndView model = new ModelAndView("increment");
		List<Increment> incrList = null;
		if(null != incr.getIncr_id()){ 
			if(incr.getIncr_id()!=0){
				incrList = incrementService.updateIncrement(incr);
			}
		}else{
			if(null != incr.getMax_score() && null != incr.getIncr_desc() && !incr.getIncr_desc().equalsIgnoreCase("") &&
					null != incr.getMin_score()) {
				incrList = incrementService.addIncrement(incr);
			}
		}

		model.addObject("incrList", incrList);
		return model;
	}
	
	@RequestMapping("/deleteincrement")
	public ModelAndView deleteIncrement(@ModelAttribute("increment") Increment incr) {
		List<Increment> incrList = null;
		if(null != incr) {
			incrList = incrementService.deleteIncrement(incr.getIncr_id());
		}
		
		ModelAndView model = new ModelAndView("increment");
		model.addObject("incrList", incrList);
		return model;
	}

	/*request handling for Increment : END*/
	/*request handling for Lock Master : START*/

	@RequestMapping("/lockmaster")
	public ModelAndView showLockmaster(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("lockmaster") LockMaster lock,HttpSession session){
		
		ModelAndView model = new ModelAndView("lockmaster");
		List<LockMaster> locklist=null;
		//String currDt = CommonUtils.getCurrentDate(new Date());
	
		if(null != session) {
			SessionUser user=(SessionUser) session.getAttribute("user");
			locklist = lockMasterService.getAllLocklist(user.getFinStartDt(),user.getFinEndDt(),"locked",user.getDivId());
		}
		model.addObject("locklist", locklist);
		return model;
	}
///////
	@RequestMapping("/unlockEntries")
	public ModelAndView unlockEntries(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("lockmaster") LockMaster lock,HttpSession session)
	{
		SessionUser user = (SessionUser) session.getAttribute("user");
		String success= lockMasterService.unlockLockedEntries((lock.getMth().length( )== 1 ? "0" + lock.getMth() : lock.getMth()),
															lock.getYr(),user.getDivId());
		ModelAndView model = new ModelAndView("lockmaster");

		//List<LockMaster> locklist;
		String currDt = CommonUtils.getCurrentDate(new Date());
		String finStartDate=user.getFinStartDt(); 
		//locklist = lockMasterService.getAllLocklist(finStartDate,currDt,"locked");
		request.setAttribute("fromDt", finStartDate);
		request.setAttribute("toDt", currDt);
		//}
		//model.addObject("locklist", locklist);
		model.addObject("mode", "unlock");
		
		String stat = loginService.getLockStatus(user.getLoginYr()+user.getLoginMth(),user.getDivId());
		user.setLockStatus(null != stat ? stat : "N");
		
		return model;
	}

	/*request handling for Lock Master : END*/

	/*request handling for Process WAP : START*/
    @RequestMapping("/showprocessWap")
	public ModelAndView showProcessWap(@ModelAttribute("processWap") ProcessWap pwap,HttpSession session) {
		ModelAndView model = new ModelAndView("processWap");
		//System.out.println("showProcessWap");
		SessionUser user = (SessionUser) session.getAttribute("user");
		logger.info("showProcessWap : user : "+user.getEmp()+ " user.getFinStartDt() : "+user.getFinStartDt()+" user.getLoginYr() : "+user.getLoginYr());
		//System.out.println("user.getFinStartDt() : "+user.getFinStartDt()+" user.getLoginYr() : "+user.getLoginYr());
		LinkedHashMap<String,String> finYrMonthsMap=CommonUtils.months(user.getFinStartDt(),user.getLoginYr()); //"2018-04-01","2018";
		List<String> processedYrMthList=lockMasterService.getLockedYrMth(user.getFinStartDt().split("-")[0]+user.getFinStartDt().split("-")[1],user.getFinEndDt().split("-")[0]+user.getFinEndDt().split("-")[0],user.getDivId());
		pwap.setYrMth(pwap.getMth()+pwap.getYr());
		model.addObject("finYrMonthsMap",finYrMonthsMap);
		model.addObject("logYrMth",user.getLoginYr()+user.getLoginMth());
		model.addObject("processedYrMthList",processedYrMthList);
		//System.out.println("processedYrMthList : "+processedYrMthList);
	    return model;
	}
    
    @RequestMapping("/processWap")
	public	ModelAndView processWap(@ModelAttribute("processWap") ProcessWap pwap,HttpSession session) 
    {
    	ModelAndView model = new ModelAndView("processWap");
    	try{
    	HashMap<String,String> error=new HashMap<String,String>();
    	System.out.println("year : "+pwap.getYr() +"Month : "+pwap.getMth()+" MTH NAME : "+pwap.getMonthnm());
    	//String isWapProcessed=lockMasterService.isWapProcessed(pwap.getMth(),pwap.getYr());
    	SessionUser user = (SessionUser) session.getAttribute("user");
    	logger.info("Custom Info - Process WAP for year : "+pwap.getYr() +"Month : "+pwap.getMth()+" MTH NAME : "+pwap.getMonthnm()+" Date : "+new java.util.Date()+" USER : "+user.getEmp());
		LinkedHashMap<String,String> finYrMonthsMap=CommonUtils.months(user.getFinStartDt(),user.getLoginYr());
		List<String> processedYrMthList=lockMasterService.getLockedYrMth(user.getFinStartDt().split("-")[0]+user.getFinStartDt().split("-")[1],user.getFinEndDt().split("-")[0]+user.getFinEndDt().split("-")[0],user.getDivId());
		//System.out.println("printing max yrmth in list----->"+processedYrMthList.get(processedYrMthList.size()-1));
		if(null != processedYrMthList && !processedYrMthList.isEmpty()){
			String prevYrMth=(String)processedYrMthList.get(processedYrMthList.size()-1);
		if(processedYrMthList.contains(pwap.getYrMth())){
			error.put("errCode", CommonConstants.ERROR_CODE_SSE); 
        	error.put("errDesc", "WAP is Already Processed for This Month : "+pwap.getMonthnm()+"-"+pwap.getYr());
		}else if(Integer.parseInt(prevYrMth) != (Integer.parseInt(pwap.getYrMth())-1)) { //to check whether prev month's WAP is processed or not.
			if(prevYrMth.substring(4).equalsIgnoreCase("12") && 
					(((Integer.parseInt(pwap.getYrMth())-1)+"").substring(4)).equalsIgnoreCase("00")) //if previous month is 12th month of yr.
			{                                                                                         // as pwap yrmth-1 gives YYYY00 if month is 01
				error=processWapService.processAndLockWap(pwap,user,finYrMonthsMap);
			}else{
			error.put("errCode", CommonConstants.ERROR_CODE_SSE); 
        	error.put("errDesc", "WAP is Not Processed For Last Month: "+(Integer.parseInt(pwap.getYrMth())-1));
			}
		}else{
    		error=processWapService.processAndLockWap(pwap,user,finYrMonthsMap);
    		//System.out.println("WAP is not Processed");
    	}
    }else{
		error=processWapService.processAndLockWap(pwap,user,finYrMonthsMap);
		//System.out.println("WAP is not Processed");
	}
    	/*if(isWapProcessed!=null && isWapProcessed.equalsIgnoreCase("Y")){
    		error.put("errCode", CommonConstants.ERROR_CODE_SSE); 
        	error.put("errDesc", "WAP is Already Processed for This Month : "+pwap.getMonthnm()+"-"+pwap.getYr());
    	}else{
    		error=processWapService.processAndLockWap(pwap,user,finYrMonthsMap);
    		System.out.println("WAP is not Processed");
    	}*/
    	
//    	pwap.setYrMth(pwap.getMth()+pwap.getYr());
    	model.addObject("finYrMonthsMap",finYrMonthsMap);
		model.addObject("error",error);
		model.addObject("pwap",pwap);
		model.addObject("logYrMth",user.getLoginYr()+user.getLoginMth());
		model.addObject("processedYrMthList",processedYrMthList);
		System.out.println("list : "+pwap.getStList());
    	}catch(Exception e){
    		logger.error("Custom Error - ProcessWapServiceImpl : "+new java.util.Date(),e);
    	}
        return model;
    }
    
    @RequestMapping("/processWapContinue")
	public	ModelAndView processWapContinueAfterError(HttpSession session,
			@ModelAttribute("processWap") ProcessWap pwap) {
    	
    	System.out.println("year : "+pwap.getYr() +"Month : "+pwap.getMth()+" MTH NAME : "+pwap.getMonthnm());
    	ModelAndView model = new ModelAndView("processWap");
    	HashMap<String,String> error=new HashMap<String, String>();
    	SessionUser user = (SessionUser) session.getAttribute("user");
    	logger.info("Custom Info - processWapContinue for year : "+pwap.getYr() +"Month : "+pwap.getMth()+" MTH NAME : "+pwap.getMonthnm()+" Date : "+new java.util.Date()+" USER : "+user.getEmp());
    	LinkedHashMap<String,String> finYrMonthsMap=CommonUtils.months(user.getFinStartDt(),user.getLoginYr());//"2018-04-01","2018"
    	int i=0,cnt=0;
    	String err="";
    	if(null != pwap.getErrCode() && pwap.getErrCode().equalsIgnoreCase(CommonConstants.SUCCESS_CODE)){
    		try{
    			if(pwap.getIsdataLocked().equalsIgnoreCase("Y")){
    				
    				cnt=wapProcessService.getPsrNotReviewedCnt(pwap.getYr(),pwap.getMth(),user.getDivId(),user.getFmsDbRef(),user.getEmp());
    				cnt=0;
    				if(cnt>0){
    				//if wap is to be locked then put the final focus products in WAP's focProd table.
    					pwap.setIsdataLocked("W"); //not lock WAP unless all PSRs are reviewed as per Partho Sir's req.
    					err="NOTE : All PSRs ARE NOT REVIEWED FOR THE MONTH THEREFORE WAP IS NOT LOCKED.<br> COUNT : "+cnt;
    				}else{
    					i=wapProcessService.addFinalFocusProdForMth(pwap.getYr(),pwap.getMth(),user.getFmsDbRef(),user.getHqRef(),user.getDivId());
    				}
    				
    			}
	    		int chkUpdt=lockMasterDAO.updateToLock(pwap.getIsdataLocked(),user.getEmp(),CommonUtils.getCurrentDateTime(),pwap.getYrMth(),user.getDivId());
	    		
	    		if(chkUpdt<1){
					LockMasterDomain obj=new LockMasterDomain();//divId,mth,yr,yrmth,"Y",loginid,CommonUtils.getCurrentDateTime());
					obj.setDivId(user.getDivId());
					obj.setMth(pwap.getMth());
					obj.setIsDataLocked(pwap.getIsdataLocked());
					obj.setLastModBy(user.getEmp());
					obj.setLastModDt(CommonUtils.getCurrentDateTime());
					obj.setYr(pwap.getYr());
					obj.setYrmth(pwap.getYrMth());
					obj.setNoOfAttmpt(1);
					lockMasterDAO.save(obj);
	    		}
	    		
	    		if(user.getLoginYr().equalsIgnoreCase(pwap.getYr()) && user.getLoginMth().equalsIgnoreCase(pwap.getMth()))
	    			user.setLockStatus(pwap.getIsdataLocked()); //change lock status in session
	    		
	    		error.put("errCode", CommonConstants.SUCCESS_LOCK_CODE);
	    		
	    		if(i<1 && pwap.getIsdataLocked().equalsIgnoreCase("Y")){
	    			err=" But Focus Products Are Not Added. Please Inform Administrator To Add Manually";
	    		}
	    		if(null != pwap.getIsdataLocked() && pwap.getIsdataLocked().equalsIgnoreCase("W"))
	    			error.put("errDesc", " WAP IS NOT LOCKED.<br> WAP is Processed And Lock Is Updated To Wait State. "+err);
	    		else if (null != pwap.getIsdataLocked() && pwap.getIsdataLocked().equalsIgnoreCase("Y")){
	    			error.put("errDesc", " WAP is Processed And Locked Successfully !! "+err);
	    		}
    		}catch(Exception e){
    			
    			error.put("errCode", CommonConstants.ERROR_CODE_HSE); 
            	error.put("errDesc", " Could Not Update Lock!! : "+e.getMessage());
            	logger.error("Custom Error - processWapContinueAfterError : "+new java.util.Date(),e);
            	//e.printStackTrace();
    		}
    	}
    	else{
    		lockMasterService.unlockLockedEntries(pwap.getMth(),pwap.getYr(),user.getDivId());
    		error=new HashMap<String,String>();
    		error=processWapService.processAndLockWap(pwap,user,finYrMonthsMap);
    	}
    	
    	List<String> processedYrMthList=lockMasterService.getLockedYrMth(user.getFinStartDt().split("-")[0]+user.getFinStartDt().split("-")[1],user.getFinEndDt().split("-")[0]+user.getFinEndDt().split("-")[0],user.getDivId());
    	pwap.setYrMth(pwap.getMth()+pwap.getYr());
    	
    	model.addObject("finYrMonthsMap",finYrMonthsMap);
		model.addObject("error",error);
		model.addObject("pwap",pwap);
		model.addObject("stlist",pwap.getStList());
		model.addObject("processedYrMthList",processedYrMthList);
		model.addObject("logYrMth",user.getLoginYr()+user.getLoginMth());
        return model;
    }
    
    /*request handling for Process WAP : END*/

	@RequestMapping("/showPsrPerformanceReview")
	public ModelAndView showMgrEval(@ModelAttribute("psrPerformanceReview") PsrPerformanceReview review, HttpSession session) {
		ModelAndView model = new ModelAndView("psrPerformanceReview");
		if(null != session) {
			//SessionUser sessionUser = (SessionUser) session.getAttribute("user");
			review.setEmpList(empListService.getEmployeeList((SessionUser) session.getAttribute("user"),"1","review"));//dataReqLvl
			LinkedHashMap<String,String> paramIdNameWeightageMap=paramService.getParamIdNameAndWeightage(1);//for level=1
			 if(null != paramIdNameWeightageMap){
					model.addObject("paramIdNameWeightageMap",paramIdNameWeightageMap);
					model.addObject("paramMapSize",paramIdNameWeightageMap.size());
					model.addObject("update","");
				}
			//Employee emp=review.getEmpList().get(1);
			//System.out.println("NNNNNNNNNNNNNNNNNNNNNNn : "+emp.getEmpName());
		}else {
			model.addObject("error", "Session expired");
		}
		return model;
	} 
	

	@PostMapping(value="/reviewConfirmed")
	public ModelAndView addPerfReview(@ModelAttribute("psrPerformanceReview") PsrPerformanceReview review, HttpSession session) 
	{
		ModelAndView model =  new ModelAndView("psrPerformanceReview");
		if(null != session) {
		SessionUser user = (SessionUser) session.getAttribute("user");
			if(Integer.parseInt(user.getLevel())>2)
				mgrEffectivenessParamService.updateRecordForMgr2(review, user);
			else
				mgrEffectivenessParamService.insertRecord(review,user);
			
		logger.info("Custom Info - reviewConfirmed : for PSR : "+review.getPsrEmp()+" By USER : "+user.getEmp()+" actual login Date : "+user.getCurrDate());	
		review.setEmpList(empListService.getEmployeeList((SessionUser) session.getAttribute("user"),"1","review"));//dataReqLvl
		if(review.getEmpList()!=null && review.getEmpList().size()>0){
		LinkedHashMap<String,String> paramIdNameWeightageMap=paramService.getParamIdNameAndWeightage(1);//for level=1
		 if(null != paramIdNameWeightageMap){
				model.addObject("paramIdNameWeightageMap",paramIdNameWeightageMap);
				model.addObject("paramMapSize",paramIdNameWeightageMap.size());
				model.addObject("update","");
			}
		}else{
			model.addObject("error", "No Employees To Review");
		}
		}else{
			model.addObject("error", "Session expired");
		}
		return model;
	}
	
	
	@RequestMapping("/updatePsrPerformanceReview")
	public ModelAndView updateMgrEval(@ModelAttribute("psrPerformanceReview") PsrPerformanceReview review, HttpSession session) {
		ModelAndView model = new ModelAndView("psrPerformanceReview");
		if(null != session) {
			SessionUser sessionUser = (SessionUser) session.getAttribute("user");
			review.setEmpList(empListService.getEmployeeList(sessionUser,"1","update"));//dataReqLvl
			LinkedHashMap<String,String> paramIdNameWeightageMap=paramService.getParamIdNameAndWeightage(1);//for level=1
			 if(null != paramIdNameWeightageMap){
					model.addObject("paramIdNameWeightageMap",paramIdNameWeightageMap);
					model.addObject("paramMapSize",paramIdNameWeightageMap.size());
				}
			 logger.info("Custom Info - updatePsrPerformanceReview : for PSR : "+review.getPsrEmp()+" By USER : "+sessionUser.getEmp()+" actual login Date : "+sessionUser.getCurrDate());
			//Employee emp=review.getEmpList().get(1);
			//System.out.println("NNNNNNNNNNNNNNNNNNNNNNn : "+emp.getEmpName());
			 model.addObject("update", "update");
		}else {
			model.addObject("error", "Session expired");
		}
		return model;
	} 
	
	@PostMapping(value="/reviewUpdated")
	public ModelAndView updatePerfReview(@ModelAttribute("psrPerformanceReview") PsrPerformanceReview review, HttpSession session) 
	{
		ModelAndView model =  new ModelAndView("psrPerformanceReview");
		String temp="";
		if(null != session) {
		SessionUser user = (SessionUser) session.getAttribute("user");
			if(Integer.parseInt(user.getLevel())>2)
				temp=mgrEffectivenessParamService.updateMgr2Record(review, user);
			else
				temp=mgrEffectivenessParamService.updateMgr1Record(review,user);
			logger.info("Custom Info - updatePsrPerformanceReview : for PSR : "+review.getPsrEmp()+" By USER : "+user.getEmp()+" actual login Date : "+user.getCurrDate());
		}else{
			model.addObject("error", "Session expired");
		}
		
		model.addObject("response", temp);
		return model;
	}
	
	
	@RequestMapping(value="/getMgr1Scores", method=RequestMethod.GET)
	public ResponseEntity<String> getMgr1Scores(HttpSession session,@RequestParam(value="psrEmp") String emp,@RequestParam(value="psrNetid") String netid,
			@RequestParam(value="divId") String divId,@RequestParam(value="loginMth") String loginMth,@RequestParam(value="loginYr") String loginYr)
	{
		List list = mgrEffectivenessParamService.getMgr1Scores(netid, emp,divId,loginMth,loginYr);
		StringBuffer sb=new StringBuffer();
		com.google.gson.Gson gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
		for(int i=0; i<list.size(); i++){
			if(i==0)
				sb.append(list.get(i));
			else
				sb.append("~"+list.get(i));
		}
		String jsonString = gson.toJson(sb.toString());
		//String jsonString = gson.toJson(list);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.CREATED); 
	}
	
	@RequestMapping(value="/getMgrScores", method=RequestMethod.GET)
	public ResponseEntity<String> getMgrScores(HttpSession session,@RequestParam(value="psrEmp") String emp,@RequestParam(value="psrNetid") String netid,
			@RequestParam(value="divId") String divId,@RequestParam(value="loginMth") String loginMth,@RequestParam(value="loginYr") String loginYr,@RequestParam(value="lvl") String lvl)
	{
		List list = mgrEffectivenessParamService.getMgrScores(netid, emp,divId,loginMth,loginYr,lvl);
		StringBuffer sb=new StringBuffer();
		com.google.gson.Gson gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
		for(int i=0; i<list.size(); i++){
			if(i==0)
				sb.append(list.get(i));
			else
				sb.append("~"+list.get(i));
		}
		String jsonString = gson.toJson(sb.toString());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.CREATED); 
	}
	
	@RequestMapping(value="/getPsrAch", method=RequestMethod.GET)
	public ResponseEntity<String> getPsrAch(HttpSession session,@RequestParam(value="psrEmp") String emp,@RequestParam(value="psrNetid") String netid,
			@RequestParam(value="divId") String divId,@RequestParam(value="loginMth") String loginMth,@RequestParam(value="loginYr") String loginYr)
	{
		String jsonString = "";
		List list = wapProcessService.getInitWapDetailsOfEmp(loginYr,loginMth,netid,emp,Integer.parseInt(divId)); ////getInitWapDetailsOfEmp1
		StringBuffer sb=new StringBuffer();
		com.google.gson.Gson gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
		for(int i=0; i<list.size(); i++){
			if(i==0)
				sb.append(list.get(i));
			else
				sb.append("~"+list.get(i));
		}
		jsonString = gson.toJson(sb.toString());
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.CREATED); 
	}
	
	@RequestMapping("/showReviewStatus")
	public ModelAndView showReviewStatus(@ModelAttribute("reviewStatus") ReviewStatus status, HttpSession session){
		
		ModelAndView model = new ModelAndView("reviewStatus");
		if(null != session) {
			status.setEmpList(empListService.getEmployeeListForReview((SessionUser) session.getAttribute("user"),"1"));
			/*ReviewEmployee emp=status.getEmpList().get(1);
			System.out.println("NNNNNNNNNNNNNNNNNNNNNNn : "+emp.getPsrEmp());*/
		}else {
			model.addObject("error", "Session expired");
		}
		return model;
	}
	
	@RequestMapping("/showAppraisal")
	public String showAppraisal(Model model, HttpSession session){
		//EmpList emp=new EmpList();
		//ModelAndView model = new ModelAndView("EmpList");
		try{
		if(null != session) {
			SessionUser user=(SessionUser) session.getAttribute("user");
			List<AppraisedEmp> empList=null;
			AppraisedEmp emp=null;
			if(!user.getLevel().equalsIgnoreCase("1")){
				empList=empListService.getAppraisedEmployees((SessionUser) session.getAttribute("user"),"1");
				model.addAttribute("empList",empList);
			}else{
				emp=empListService.getAppraisedEmp((SessionUser) session.getAttribute("user"),"1");
				model.addAttribute("emp",emp);
			}
			model.addAttribute("mthnm",CommonUtils.getMonthName(Integer.parseInt(user.getLoginMth())));
			if((null != empList && !empList.isEmpty() && user.getLevel()!=null && !user.getLevel().equalsIgnoreCase("")
					&& Integer.parseInt(user.getLevel())!=1) || (null != emp  && user.getLevel().equalsIgnoreCase("1")) ){
				LinkedHashMap<String,String> paramIdNameWeightageMap=paramService.getParamIdNameAndWeightage(1);//for level=1
				if(null != paramIdNameWeightageMap){
					model.addAttribute("paramIdNameWeightageMap",paramIdNameWeightageMap);
					model.addAttribute("noOfParams",paramIdNameWeightageMap.size());
					LinkedHashMap<String,String> opEffMap=wapProcessService.getOpEffNameWeightageInMap();
					model.addAttribute("opEffMap",opEffMap);
					if(null != opEffMap && !opEffMap.isEmpty()){
						model.addAttribute("totOpEffScore",CommonConstants.TOTAL_OP_EFF_SCORE);
						model.addAttribute("eachIpEffScore",CommonConstants.EACH_OP_EFF_SCORE);
					}
				}else{
					model.addAttribute("error", "Unable To Fetch Parameters!!");
				}
			}else{
				model.addAttribute("error", "No Employees are Appraised this Month");
			}
			logger.info("Custom Info - showAppraisal :  By USER : "+user.getEmp()+" actual login Date : "+user.getCurrDate());
		}else {
			model.addAttribute("error", "Session expired");
		}
		}catch(Exception e){
			logger.error("Custom Error - showAppraisal : "+new java.util.Date(),e);
		}
		
		return "appraisal";
	}
	
	@RequestMapping(value="/getAppraisal", method=RequestMethod.GET)
	public ResponseEntity<String> getAppraisal(HttpSession session,@RequestParam(value="psrEmp") String emp,@RequestParam(value="psrNetid") String netid,
			@RequestParam(value="divId") String divId,@RequestParam(value="loginMth") String loginMth,@RequestParam(value="loginYr") String loginYr)
	{
		List<Object[]> list = wapProcessService.getWapDetailsOfEmp(loginYr,loginMth,netid,emp,divId); 
		
		StringBuffer sb=new StringBuffer();
		com.google.gson.Gson gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
		for(int i=0; i<list.size(); i++){
			if(i==0)
				sb.append(list.get(i));
			else
				sb.append("~"+list.get(i));
		}
		String jsonString = gson.toJson(sb.toString());
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.CREATED); 
	}
	
	@RequestMapping("/showChangeDate")
	public String showParams() {
		return "changeDate";
	}
	
	@RequestMapping("/confirmChangeDate")
	public ModelAndView showParams(@ModelAttribute("changeDate") ChangeDate cdate, HttpSession session) {
		ModelAndView model = new ModelAndView("changeDate");
		SessionUser user = (SessionUser) session.getAttribute("user");
		user.setLoginMth(cdate.getLoginMth());
		user.setLoginYr(cdate.getLoginYr());
		user.setLockStatus(loginService.getLockStatus(user.getLoginYr()+user.getLoginMth(),user.getDivId()));
		user.setFinStartDt(CommonUtils.getFinStrtDt(user.getFinStartDt(),user.getLoginMth(),user.getLoginYr()));
		user.setFinEndDt(CommonUtils.getFinEndDt(user.getFinStartDt(), user.getLoginMth(), user.getLoginYr()));
		model.addObject("res","Date Changed Successfully to "+user.getLoginMth()+"/"+user.getLoginYr());
		//System.out.println("Session value : " + user.getEmployeeName());
		return model;
	} 
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "logout";
	} 
	
	@RequestMapping("/about")
	public String showAbout() {
		return "about";
	} 

	@RequestMapping("/psrComments") 
	public ModelAndView showPsrComments(@ModelAttribute("psrComments") PsrComments comments,HttpSession session)
	{
		ModelAndView model=new ModelAndView("psrComments");
		SessionUser user=(SessionUser)session.getAttribute("user");		
		 //String comts=commentService.getCommentByEcode(user.getEmp(),user.getNetId(),user.getLoginYr(),user.getLoginMth(),user.getDivId());
		try{
		 List list = wapProcessService.getInitWapDetailsForPsrComnts(user.getLoginYr(),user.getLoginMth(),user.getNetId(),user.getEmp(),user.getDivId()); //getInitWapDetailsOfEmp 2
		 
		 if(null != list && list.size()>0){
			 model.addObject("list",list);
			 
			 LinkedHashMap<String,String> paramIdNameWeightageMap=paramService.getParamIdNameAndWeightage(1);//for level=1
			 if(null != paramIdNameWeightageMap){
					model.addObject("paramIdNameWeightageMap",paramIdNameWeightageMap);
					model.addObject("paramMapSize",paramIdNameWeightageMap.size());
				}
		 }	 
		 else
			 model.addObject("response","No Data Available For The Month : "+user.getLoginMth()+" and Year : "+user.getLoginYr());
		 //System.out.println("Printing Comments......................"+comts);
		 //model.addObject("commentsobj",comts);
		}catch(Exception e){
			logger.error("Custom Error - psrComments : "+new java.util.Date(),e);
		}
		return model;
	}

	@RequestMapping("/addPsrComments")
	public ModelAndView  addPsrComments(@ModelAttribute("psrComments") PsrComments comments,HttpSession session) {
		ModelAndView model=new ModelAndView("psrComments");
		SessionUser user = (SessionUser) session.getAttribute("user");
		try{
		if(user!=null){
		if(comments.getIsEdit()!=null && comments.getIsEdit().equalsIgnoreCase("")
				&& comments.getIsEdit().equals("edit"))
		{
			int i=commentService.update(comments);
			if(i>0)
			{
				model.addObject("response","Your Comment is Updated Successfully");
			}else{
				model.addObject("response","Error: Comment is not updated");
			}
		}else {
			commentService.add(comments);
			model.addObject("response","Your Comment is Added Successfully");
		}
		}else{
			model.addObject("response","Session Expired");
		}
		}catch(Exception e){
			logger.error("Custom Error - addPsrComments : "+new java.util.Date(),e);
		}
		return model;
	} 
	
	@RequestMapping("/mainmenu")
	public String showMainMenu(Model model) {
		model.addAttribute("login","false");
		return "mainmenu";
	}
	
	@RequestMapping("/showYtdAppraisal")
	public String showYtdAppraisal(Model model, HttpSession session){
		//EmpList emp=new EmpList();
		//ModelAndView model = new ModelAndView("EmpList");
		try{
		if(null != session) {
			SessionUser user=(SessionUser) session.getAttribute("user");
			List<AppraisedEmp> empList=null;
			AppraisedEmp emp=null;
			if(!user.getLevel().equalsIgnoreCase("1")){
				empList=empListService.getAppraisedEmpWithDTls((SessionUser) session.getAttribute("user"),"1");
				model.addAttribute("empList",empList);
			}/*else{
				emp=empListService.getAppraisedEmp((SessionUser) session.getAttribute("user"),"1");
				model.addAttribute("emp",emp);
			}*/
			model.addAttribute("mthnm",CommonUtils.getMonthName(Integer.parseInt(user.getLoginMth())));
			if((null != empList && !empList.isEmpty() && !user.getLevel().equalsIgnoreCase("1")) ||  (user.getLevel().equalsIgnoreCase("1"))){
				LinkedHashMap<String,String> paramIdNameWeightageMap=paramService.getParamIdNameAndWeightage(1);//for level=1
				if(null != paramIdNameWeightageMap){
					model.addAttribute("paramIdNameWeightageMap",paramIdNameWeightageMap);
					//model.addAttribute("noOfParams",paramIdNameWeightageMap.size());
					
					List<Object[]> rateList=incrementService.getRatings();
					model.addAttribute("rateList", rateList);
					model.addAttribute("rateListSize", rateList.size());
					model.addAttribute("aplusMinScr", CommonConstants.A_PLUS_MIN_SCORE);
					model.addAttribute("aplusMinTgt", CommonConstants.A_PLUS_MIN_TARGET/100000);
					model.addAttribute("aplusMinAchPer", CommonConstants.A_PLUS_MIN_ACH_PERC);
					
					/*Param wise slab's min range and score in map => Slabs table: starts*/
					List<Object[]> list=slabdao.getParamWiseSlabsScore();//{paramid={slabsMinRange1=score1,SlabsMinRange2=score2},....}

					HashMap<Integer, LinkedHashMap<Float, Float>> paramWiseSlabsPointsMap=new HashMap<Integer,LinkedHashMap<Float,Float>>();

					LinkedHashMap<Float,Float> innerSlabsMap=new LinkedHashMap<Float,Float>();//{paramid={slabsMinRange1=points1,SlabsMinRange2=points2},....}
					if(null != list && !list.isEmpty()){
						for(Object[] obj: list){
							if(paramWiseSlabsPointsMap.containsKey(obj[0])){
								innerSlabsMap=new LinkedHashMap<Float,Float>();
								innerSlabsMap=(LinkedHashMap<Float,Float>) paramWiseSlabsPointsMap.get(obj[0]);
								if(null != innerSlabsMap){
									innerSlabsMap.put((Float)obj[1],(Float)obj[3]);
								}else{
									innerSlabsMap=new LinkedHashMap<Float,Float>();
									innerSlabsMap.put((Float)obj[1],(Float)obj[3]);
								}
								paramWiseSlabsPointsMap.put((Integer)obj[0],innerSlabsMap);
							}else{
								innerSlabsMap=new LinkedHashMap<Float,Float>();
								innerSlabsMap.put((Float)obj[1],(Float)obj[3]);
								paramWiseSlabsPointsMap.put((Integer)obj[0],innerSlabsMap);
							}
						}
						LinkedHashMap<String,String> finYrMonthsMap=CommonUtils.months(user.getFinStartDt(),user.getLoginYr());
						model.addAttribute("finYrMonthsMap", finYrMonthsMap);
					}
					System.out.println("paramWiseSlabsScoreMap : "+paramWiseSlabsPointsMap);
					/*Param wise slab's min range and score in map => Slabs table: starts*/
					
					model.addAttribute("slabsMap", paramWiseSlabsPointsMap);
				
				}else{
					model.addAttribute("error", "Unable To Fetch Parameters!!");
				}
			}else{
				model.addAttribute("error", "No Employees are Appraised this Month");
			}
			logger.info("Custom Info - showAppraisal :  By USER : "+user.getEmp()+" actual login Date : "+user.getCurrDate());
		}else {
			model.addAttribute("error", "Session expired");
		}
		}catch(Exception e){
			logger.error("Custom Error - showAppraisal : "+new java.util.Date(),e);
		}
		
		return "ytdAppraisal";
	}
	
	@RequestMapping(value="/getCummAppraisal", method=RequestMethod.GET)
	public ResponseEntity<String> getYtdAppraisal(HttpSession session,@RequestParam(value="psrEmp") String emp,@RequestParam(value="psrNetid") String netid,
			@RequestParam(value="divId") String divId,@RequestParam(value="loginMth") String loginMth,@RequestParam(value="loginYr") String loginYr,
			@RequestParam(value="finStrt") String finStrt,@RequestParam(value="finEnd") String finEnd)
	{
		List<Object[]> list = wapProcessService.getCummWapDetailsOfEmp(finStrt.split("-")[0]+finStrt.split("-")[1],loginYr+loginMth,netid,emp,divId);
		//logger.info("Custom Info - getYtdAppraisal list : "+list);
		StringBuffer sb=new StringBuffer();
		com.google.gson.Gson gson = new com.google.gson.GsonBuilder().disableHtmlEscaping().create();
		
		for(Object[] obj:list){
			for(int i=0; i<obj.length; i++){
				if(i==0)
					sb.append(obj[i]);
				else{
					sb.append("~"+obj[i]);
					//logger.info("Custom Info - getYtdAppraisal obj[i] : "+obj[i]);
				}
			}
			sb.append("!@!");
			//logger.info("Custom Info - getYtdAppraisal 111 : "+sb.toString());
		}
		
		logger.info("Custom Info - getYtdAppraisal sb.toString() : "+sb.toString());
		
		//logger.info("Custom Info - getYtdAppraisal : "+sb.toString());
		String jsonString = gson.toJson(sb.toString());
		//logger.info("Custom Info - getYtdAppraisal jsonString : "+jsonString.toString());
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		return new ResponseEntity<String>(jsonString, responseHeaders, HttpStatus.CREATED); 
	}
	
	
}
