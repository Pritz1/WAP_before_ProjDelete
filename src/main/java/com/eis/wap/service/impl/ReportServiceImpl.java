package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.constants.CommonConstants;
import com.eis.wap.controllers.WapMenuController;
import com.eis.wap.dao.FocusProdDao;
import com.eis.wap.dao.FocusProdDaoCustom;
import com.eis.wap.dao.IncrementDao;
import com.eis.wap.dao.ParamMasterDAO;
import com.eis.wap.dao.SlabDao;
import com.eis.wap.dao.UserDAO;
import com.eis.wap.dao.WapCalDao;
import com.eis.wap.dao.WapFinalScoreAndIncrementDao;
import com.eis.wap.dao.WapFinalScoreIncId;
import com.eis.wap.domain.WapFinalScoreAndIncrementDomain;
import com.eis.wap.model.CummWapDetails;
import com.eis.wap.model.Report;
import com.eis.wap.model.SessionUser;
import com.eis.wap.service.ReportService;
import com.eis.wap.util.CommonUtils;

@Component
public class ReportServiceImpl implements ReportService {

	@Autowired
	ParamMasterDAO paramMasterDao;

	@Autowired
	UserDAO userDao;

	@Autowired
	WapCalDao wapCal;

	@Autowired
	WapFinalScoreAndIncrementDao wapScoreDao;

	@Autowired
	SlabDao slabDao;

	@Autowired
	IncrementDao incrDao;

	@Autowired 
	FocusProdDao focProdDao;
	
	private static final Logger logger = Logger.getLogger(WapMenuController.class);	
	
	public WritableWorkbook generateReport(HttpServletResponse response, String desig, SessionUser user,Report report) {

		WritableWorkbook writableWorkbook = null;
		try {
			String yr=user.getLoginYr();//"2018";
			String mth=user.getLoginMth();//"07";
			String fmsDbRef=user.getFmsDbRef();//"PALSONS1";//
			String loginId=user.getEmp();//"EIS";//
			String hqref=user.getHqRef(); //"A";//
			String level="1";
			int divId=user.getDivId();
			String divName=user.getDivName();
			String finStrtDt=user.getFinStartDt();
			String frmYrMth=report.getFrmYrMth();//finStrtDt.split("-")[0]+finStrtDt.split("-")[1];//"201804"; //financial start yrmth
			//String finYr="1819";
			String finYr=finStrtDt.split("-")[0].substring(2)+user.getFinEndDt().split("-")[0].substring(2);
			//System.out.println("finYr : "+finYr);
			String salesDbRef=user.getSalesDbRef();
			String fileName = getReportName(desig, user,salesDbRef,yr,mth);
			String logLvl=user.getLevel(); //Loggedin users level
			String type=null;

			//String toYrMth=yr+mth; //login yrmth
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			/*{paramId1=wightage,paramId2=weightage2...} => param_master table*/
			List<Object[]> paramIdDescList=paramMasterDao.getParamIdAndDesc(Integer.parseInt(desig));
			LinkedHashMap<Integer,String> paramIdDescMap=new LinkedHashMap<Integer,String>();
			if(null != paramIdDescList && !paramIdDescList.isEmpty()){
				for(Object[] obj: paramIdDescList){
					paramIdDescMap.put((Integer)obj[0], (String)obj[1]);
				}
			}
			//System.out.println("paramIdDescMap : "+paramIdDescMap);
			writableWorkbook = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet excelOutputsheet = writableWorkbook.createSheet("Report_Data", 0);
			WritableSheet excelOutputsheet2 = writableWorkbook.createSheet("Focus_Products", 1);
			List<Object[]>	list=new ArrayList<Object[]>();
			if(report.getViewType().equalsIgnoreCase("1")){
				if(logLvl.equalsIgnoreCase("1")){
					type="sel1";
					list=getSelEmpHierarchy(fmsDbRef,loginId,level,"'"+loginId+"'",yr,mth);
				}else{
					type="all";
					list = getAllEmpHierarchy(fmsDbRef,loginId,level,mth,yr,hqref,logLvl);
				}
			}else if(report.getViewType().equalsIgnoreCase("2")){
				if(report.getSelEmp()!=null){
					type="sel";
					list=getSelEmpHierarchy(fmsDbRef,loginId,level,report.getSelEmp(),yr,mth);
				}else{
					logger.error("Custom Error - ReportServiceImpl : report.getSelEmp() is null");
				}
			}else{
				logger.error("Custom Error - ReportServiceImpl : report.getViewType() is neither 1 nor 2");
			}

			WritableFont wf = new WritableFont(WritableFont.ARIAL, 8);
			WritableCellFormat wcf = new WritableCellFormat(wf);
			
			WritableFont wfb2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			WritableCellFormat cfb2 = new WritableCellFormat(wfb2);

			writeHeaders(excelOutputsheet,mth,yr,paramIdDescMap,divName,frmYrMth,report.getToYrMth(),cfb2,wfb2,user.getLvlDescMap()); 
			writeReportDataInExcel(excelOutputsheet,fmsDbRef,loginId,hqref, mth,yr,paramIdDescMap,level,finYr,salesDbRef,list,
					frmYrMth,report.getToYrMth(),divId,wcf,type,report.getSelEmp());//yr+mth 
			writeFocusProducts(excelOutputsheet2,frmYrMth,report.getToYrMth(),divId,fmsDbRef,wcf,cfb2);
			
			writableWorkbook.write();
			writableWorkbook.close();
			
			wapCal.dropTable(loginId);

		} catch (Exception e) {
			System.out.println("Error occured while creating Excel file"+ e);
			logger.error("Custom Error - ReportServiceImpl : "+new java.util.Date(),e);
		}

		return writableWorkbook;
	}


	private void writeHeaders(WritableSheet sheet,String mth,String yr,LinkedHashMap<Integer,String> paramIdDescMap,String divName,
			String frmYrMth,String toYrMth,WritableCellFormat cfb2,WritableFont wfb2,HashMap map)throws RowsExceededException, WriteException{
		// create header row
		int rowCount=0;
		int colCount=5;
		WritableFont wfb = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
		WritableCellFormat cfb = new WritableCellFormat(wfb);

		sheet.addCell(new Label(colCount, rowCount, "E-VALUATE SUMMARY FROM : "+frmYrMth+" TO : "+toYrMth+" DIV: "+divName, cfb));

		rowCount++;
		colCount=0;
		
		cfb2.setAlignment(jxl.format.Alignment.CENTRE);
		cfb2.setBackground(Colour.LIGHT_TURQUOISE);
		cfb2.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		cfb2.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);

		WritableCellFormat cfb3 = new WritableCellFormat(wfb2);
		cfb3.setAlignment(jxl.format.Alignment.CENTRE);
		cfb3.setBackground(Colour.IVORY);
		cfb3.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		cfb3.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);

		WritableCellFormat cfb4 = new WritableCellFormat(wfb2);
		cfb4.setAlignment(jxl.format.Alignment.CENTRE);
		cfb4.setBackground(Colour.GREY_25_PERCENT);
		cfb4.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		cfb4.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		cfb4.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);

		sheet.addCell(new Label(colCount, rowCount,"EMPLOYEE DETAILS", cfb2));
		sheet.mergeCells(colCount, rowCount,colCount+7,rowCount);
		colCount++;colCount++;colCount++;colCount++;colCount++;colCount++;colCount++;colCount++;

		ArrayList<String> paramDesc=null;
		if(paramIdDescMap!=null)
			paramDesc=new ArrayList<String>(paramIdDescMap.values());
		else{
			paramDesc=new ArrayList<String>();
			paramDesc.add("PRIMARY SALES");
			paramDesc.add("PB SALES");
			paramDesc.add("DOC REACH");
			paramDesc.add("PULSE CHEM");
			paramDesc.add("DOC COV");
			paramDesc.add("MANEGERIAL EFFETIVENESS");
		}

		for(String param : paramDesc ){//cannot be dynamic as the query written for getting data cant be dynamic
			sheet.addCell(new Label(colCount, rowCount,"CURR "+param.toUpperCase(), cfb3));
			sheet.mergeCells(colCount, rowCount,colCount+3,rowCount);
			colCount++;colCount++;colCount++;colCount++;
		}

		sheet.addCell(new Label(colCount, rowCount,"CURR MONTH RATING", cfb3));
		sheet.mergeCells(colCount, rowCount,colCount+1,rowCount);
		colCount++;colCount++;

		for(String param : paramDesc ){
			sheet.addCell(new Label(colCount, rowCount,"YTD "+param.toUpperCase(), cfb4));
			sheet.mergeCells(colCount, rowCount,colCount+3,rowCount);
			colCount++;colCount++;colCount++;colCount++;
		}

		sheet.addCell(new Label(colCount, rowCount,"YTD RATING", cfb4));
		sheet.mergeCells(colCount, rowCount,colCount+1,rowCount);
		colCount++;colCount++;colCount++;colCount++;

		WritableFont wf = new WritableFont(WritableFont.ARIAL, 9, WritableFont.BOLD);
		WritableCellFormat wcf = new WritableCellFormat(wf);
		//wcf.setBackground(Colour.LIGHT_TURQUOISE);
		wcf.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		wcf.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		wcf.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);

		WritableCellFormat wcf2 = new WritableCellFormat(wf);
		//wcf2.setBackground(Colour.VERY_LIGHT_YELLOW);
		wcf2.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		wcf2.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		wcf2.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);

		WritableCellFormat wcf3 = new WritableCellFormat(wf);
		//wcf3.setBackground(Colour.GRAY_25);
		wcf3.setBorder(jxl.format.Border.TOP, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		wcf3.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		wcf3.setBorder(jxl.format.Border.RIGHT, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);
		wcf3.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.MEDIUM, jxl.format.Colour.BLACK);

		
		rowCount++;
		colCount=0;
		sheet.addCell(new Label(colCount, rowCount,(String)map.get("Level5Desc"), wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, (String)map.get("Level4Desc"), wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, (String)map.get("Level3Desc"), wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, (String)map.get("Level2Desc"), wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, (String)map.get("Level1Desc"), wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, "ECODE", wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, "HNAME", wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, "MONTHS WORKED", wcf));
		colCount++;
		colCount=secondRowHeads(paramDesc.size(),rowCount,colCount,sheet,wcf2);
		colCount=secondRowHeads(paramDesc.size(),rowCount,colCount,sheet,wcf3);

	}

	private String writeReportDataInExcel(WritableSheet sheet,String fmsDbRef,String loginId,String hqref,String mth,String yr,
			HashMap paramIdDescMap,String level,String finYr,String salesDbRef,List<Object[]> list,String frmYrMth,String toYrMth,
			int divId,WritableCellFormat wcf,String type,String selEmp)throws RowsExceededException, WriteException{ 
		//TODO number format

		WritableCellFormat numFormat = new WritableCellFormat (NumberFormats.INTEGER);
		numFormat = new WritableCellFormat(NumberFormats.INTEGER);

		WritableCellFormat numFormat2 = new WritableCellFormat (NumberFormats.DEFAULT);
		numFormat2 = new WritableCellFormat(NumberFormats.FLOAT);

		if(null==toYrMth || toYrMth.equalsIgnoreCase("")){
			toYrMth="000000";
		}
		
		String toYr=toYrMth.substring(0, 4);
		String toMth=toYrMth.substring(4);
		int frmYr=Integer.parseInt(frmYrMth.substring(0, 4));
		int frmMth=Integer.parseInt(frmYrMth.substring(4));
		
		int maxToDay=CommonUtils.getMaxDayOfMonth(Integer.parseInt(toYr),Integer.parseInt(toMth),1);
		int noOfMths=CommonUtils.getmonthsBetweenDates(frmYr,frmMth,1,Integer.parseInt(toYr),Integer.parseInt(toMth),maxToDay) + 1;
		
		//System.out.println("noOfMths : "+noOfMths);
		/*WritableFont wf = new WritableFont(WritableFont.ARIAL, 8);
		WritableCellFormat wcf = new WritableCellFormat(wf);*/

		//HashMap<String,String> error=new HashMap<String,String>();
		

		LinkedHashMap<String,String> empDetailsMap=new LinkedHashMap<String,String>();
		//Ecode,Netid,hname,PSR,AFM,STATE,RM,ZM 
		if(null != list && !list.isEmpty()){
			//int i=0;
			for(Object[] object : list){
				//System.out.println("object[0] : "+(String)object[0]+"coumter : "+i++);
				empDetailsMap.put((String)object[0],(String)object[1]+"~"+(String)object[2]+"~"+(String)object[3]+"~"+
						(String)object[4]+"~"+(String)object[5]+"~"+(String)object[6]+"~"+(String)object[7]+"~"+((object[8]!=null ? object[8].toString() : "0000-00-00")));
			}
		}

		//System.out.println("*********empDetailsMap : "+empDetailsMap);
		/*ArrayList paramDesc=null;
		if(paramIdDescMap!=null)
			paramDesc=new ArrayList<String>(paramIdDescMap.values());
		else{
			return "error in getting params";
		}
*/

		list.clear();
		if(type.equalsIgnoreCase("all"))
			list=wapCal.getWapDetails(loginId, mth, yr, fmsDbRef,divId,toYr,toMth,"");
		else if(type.equalsIgnoreCase("sel"))
			list=wapCal.getWapDetails(loginId, mth, yr, fmsDbRef,divId,toYr,toMth,selEmp);
		else if(type.equalsIgnoreCase("sel1"))
			list=wapCal.getWapDetails(loginId, mth, yr, fmsDbRef,divId,toYr,toMth,"'"+loginId+"'");
		HashMap<String,HashMap<String,String>> empWapDetailsOuterMap=new HashMap<String,HashMap<String,String>>();//{eCode1={param1=target~acvh~achvPerc,param2=....},ecode2={..}...}
		HashMap<String,String> innerMap=new HashMap<String,String>();
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				innerMap=new HashMap<String,String>();
				innerMap.put(CommonConstants.SALES_PARAMID+"",object[1]+"~"+object[2]+"~"+object[3]);
				innerMap.put(CommonConstants.POB_PARAMID+"",object[4]+"~"+object[5]+"~"+object[6]);
				innerMap.put(CommonConstants.DOC_REACH_PARAMID+"",object[7]+"~"+object[8]+"~"+object[9]);
				innerMap.put(CommonConstants.COVERAGE_PARAMID+"",object[10]+"~"+object[11]+"~"+object[12]);
				innerMap.put(CommonConstants.PULSE_CHEM_REACH_PARAMID+"",object[13]+"~"+object[14]+"~"+object[15]);
				innerMap.put(CommonConstants.MANAGERIAL_EFF_PARAMID+"",object[16]+"~"+object[17]+"~"+object[18]+"~"+object[19]);
				empWapDetailsOuterMap.put((String)object[0], innerMap);
			}
		}else{
			return "Data Not Present For Current Month";
		}
		System.out.println("empWapDetailsOuterMap : "+empWapDetailsOuterMap);

		/*Param wise slab's min range and score in map => Slabs table: starts*/
		list.clear();
		list=slabDao.getParamWiseSlabsScore();
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
		}
		//System.out.println("paramWiseSlabsScoreMap : "+paramWiseSlabsPointsMap);
		/*Param wise slab's min range and score in map => Slabs table: end*/

		list.clear();
		list=incrDao.getScoreWiseRatingIncr();  
		TreeMap<Float,String> scoreRatingMap=new TreeMap<Float,String>(Collections.reverseOrder());/*key=min_score and value=rating*/
		if(list!=null){
			for(Object[] obj : list){
				scoreRatingMap.put((Float)obj[0],(String) obj[2]); //+"~"+obj[3]
			}
		}
		//System.out.println("scoreIncrementMap : "+scoreRatingMap);

		HashMap<String,Long> annualTargetMap=wapCal.getAnnualTargetNetidWise(finYr,salesDbRef); //get annual targets per netid. Key=netid,value=annualTarget 
		if(null == annualTargetMap || annualTargetMap.isEmpty()){
			return "Target Is Not Defined For The Year ";
		}

		LinkedHashMap<Float,Float> salesSlabMap=null; /*key=slab's min range, Value=Points => SLABS Table*/
		LinkedHashMap<Float,Float> pobSlabMap=null;
		LinkedHashMap<Float,Float> docRchSlabMap=null;
		LinkedHashMap<Float,Float> docCovSlabMap=null;
		LinkedHashMap<Float,Float> pulChmRchSlabMap=null;
		ArrayList<Float> slabsMinScores=null;
		ArrayList<Float> pobMinScores=null;
		ArrayList<Float> docrchMinScores=null;
		ArrayList<Float> docCovMinScores=null;
		ArrayList<Float> pulChmMinScores=null;

		if(paramWiseSlabsPointsMap!=null){
			salesSlabMap=paramWiseSlabsPointsMap.get(CommonConstants.SALES_PARAMID); /*{slabMinRange1=Points1,slabMinRange2=Points1....} for particular param based on paramId and range is sorted in descending order*/
			pobSlabMap=paramWiseSlabsPointsMap.get(CommonConstants.POB_PARAMID);
			docRchSlabMap=paramWiseSlabsPointsMap.get(CommonConstants.DOC_REACH_PARAMID);
			docCovSlabMap=paramWiseSlabsPointsMap.get(CommonConstants.COVERAGE_PARAMID);
			pulChmRchSlabMap=paramWiseSlabsPointsMap.get(CommonConstants.PULSE_CHEM_REACH_PARAMID);

			if(null != salesSlabMap)
				slabsMinScores=new ArrayList<Float>(salesSlabMap.keySet()); /*descending sorted keyset as list => to give points on result received after comparisons */
			else return "Slabs Not Defined For Sales Param ";
			if(null != pobSlabMap)
				pobMinScores=new ArrayList<Float>(pobSlabMap.keySet());
			else return "Slabs Not Defined For POB Param ";
			if(null != docRchSlabMap)
				docrchMinScores=new ArrayList<Float>(docRchSlabMap.keySet());
			else return "Slabs Not Defined For Doc Reach Param ";
			if(null != docCovSlabMap)
				docCovMinScores=new ArrayList<Float>(docCovSlabMap.keySet());
			else return "Slabs Not Defined For Doc Cov Param ";
			if(null != pulChmRchSlabMap)
				pulChmMinScores=new ArrayList<Float>(pulChmRchSlabMap.keySet());
			else return "Slabs Not Defined For Pulse Chem Reach Param ";
		}

		list.clear();
		list=paramMasterDao.getParamIdAndWeightage(Integer.parseInt(level));//{paramid1=weight1,param2=weight2....}
		HashMap<Integer,Float> paramIdWeightageMap=new HashMap<Integer,Float>();
		if(null != list && !list.isEmpty()){
			for(Object[] obj: list){
				paramIdWeightageMap.put((Integer)obj[0], (Float)obj[1]);
			}
		}

		Map<String, WapFinalScoreAndIncrementDomain> scoreRateMap=getScoreRatingEmpWise(toYr,toMth,divId);
		WapFinalScoreAndIncrementDomain scoreIncObj=null;
		int rowCount=3;
		int colCount=0;
		String ecode="";

		String[] empDtlsArr=new String[7];
		String[] empWapDtlsArr=new String[3];
		String temp="";
		innerMap=new HashMap<String,String>();
		list.clear();
		logger.error("Custom Info Report Service Impl yr : "+yr+" mth : "+mth+" Toyr : "+toYr+" Tomth : "+toMth);
		//list=wapCal.getCummWapDetails(frmYrMth,toYrMth,yr,mth,loginId,fmsDbRef,divId, selEmp);
		if(type.equalsIgnoreCase("all"))
			list=wapCal.getCummWapDetails(frmYrMth,toYrMth,yr,mth,loginId,fmsDbRef,divId,"");
		else if(type.equalsIgnoreCase("sel"))
			list=wapCal.getCummWapDetails(frmYrMth,toYrMth,yr,mth,loginId,fmsDbRef,divId, selEmp);
		else if(type.equalsIgnoreCase("sel1"))
			list=wapCal.getCummWapDetails(frmYrMth,toYrMth,yr,mth,loginId,fmsDbRef,divId, "'"+loginId+"'");
		
		Map<String,CummWapDetails> cummWapDetls=setAndGetCummWapMap(list,slabsMinScores,pobMinScores,docrchMinScores,
				docCovMinScores,pulChmMinScores,paramIdWeightageMap,salesSlabMap,pobSlabMap,docRchSlabMap,
				docCovSlabMap,pulChmRchSlabMap,scoreRatingMap,annualTargetMap,frmYrMth,toYrMth,noOfMths,maxToDay,
				Integer.parseInt(toYr),Integer.parseInt(toMth));

		//printing starts
		for(Map.Entry<String,String> entry : empDetailsMap.entrySet()){
			colCount=0;
			for(int i=0;i<empDtlsArr.length;i++)
			{
				empDtlsArr[i]="";
			}
			ecode=entry.getKey();

			/*Employee Details*/
			empDtlsArr=entry.getValue().split("~"); //array contains foll in seq : Netid,HNAME,PSR,AFM,STATE,RM,ZM,DOJ
			sheet.addCell(new Label(colCount, rowCount,empDtlsArr[6],wcf));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount,empDtlsArr[5],wcf));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount,empDtlsArr[4],wcf));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount,empDtlsArr[3],wcf));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount,empDtlsArr[2],wcf));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount,ecode));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount,empDtlsArr[1],wcf));
			colCount++;
			/*sheet.addCell(new Label(colCount, rowCount,empDtlsArr[7],wcf));
			colCount++;*/
			//printing no. of months worked since selected from yrmth or DOJ till to yrmth: start
			
			String jnYr="0000";String jnMth="00";int jnDay=0;
			try{
			if(empDtlsArr[7]!=null && !empDtlsArr[7].equalsIgnoreCase("")){
				jnYr=empDtlsArr[7].split("-")[0];
				jnMth=empDtlsArr[7].split("-")[1];
				jnDay=Integer.parseInt(empDtlsArr[7].split("-")[2]);
			}
			
			if(Integer.parseInt(jnYr+jnMth) < Integer.parseInt(frmYrMth)){ //if joining yr mth is less than from yr mth then print no of months between frm yr mth and to yr mth selected.
				sheet.addCell(new jxl.write.Number(colCount, rowCount,noOfMths,numFormat));
				colCount++;
			}
			else if(Integer.parseInt(jnYr+jnMth) == Integer.parseInt(frmYrMth)){ /*if joining yr mth and frm yr mth are equal then consider day of joining.
												if day of joining <= 15 then consider whole month, if joining day is after 15th of the month then don't consider 
												the joining month in total months worked*/
				if(jnDay<=15){
					sheet.addCell(new jxl.write.Number(colCount, rowCount,noOfMths,numFormat));
					colCount++;
				}else{
					sheet.addCell(new jxl.write.Number(colCount, rowCount,(noOfMths-1),numFormat));
					colCount++;
				}
			}else if(Integer.parseInt(jnYr+jnMth) > Integer.parseInt(toYrMth)){ //if joining yr mth is greater than to yr mth then print 0.
				sheet.addCell(new jxl.write.Number(colCount, rowCount,0,numFormat));
				colCount++;
			}
			else if(Integer.parseInt(jnYr+jnMth) > Integer.parseInt(frmYrMth)){ /* if join yr mth is greater than from yr mth but its less 
																				   than to yrmth (as checked in above condn) then calculate the months worked till toyrmth*/
				int temp1=CommonUtils.getmonthsBetweenDates(Integer.parseInt(jnYr),Integer.parseInt(jnMth),1,
						   Integer.parseInt(toYr),Integer.parseInt(toMth),maxToDay);
				if(jnDay<=15)
					sheet.addCell(new jxl.write.Number(colCount, rowCount,(temp1+1),numFormat));
				else
					sheet.addCell(new jxl.write.Number(colCount, rowCount,(temp1),numFormat));
				colCount++;
				
			}
			}catch(Exception e){
				e.printStackTrace();
				logger.error("Custom Error - ReportServiceImpl : no. of months worked : ",e);
			}
			//printing no. of months worked since selected from yrmth or DOJ till to yrmth: end
			
			/*Employee Details : end*/

			/* Current WAP Details*/
			innerMap=empWapDetailsOuterMap.get(ecode);
			scoreIncObj=scoreRateMap.get(ecode);
			if(null != innerMap && !innerMap.isEmpty()){
				for(int j=1;j<=6;j++){
					temp=innerMap.get(j+"");
					if(temp!=null){
						empWapDtlsArr= temp.split("~");
						if(j!=6){
							for(int i=0;i<3;i++){
								if(empWapDtlsArr[i]!=null && empWapDtlsArr[i]!="" && !empWapDtlsArr[i].equalsIgnoreCase("null")){
									try{
										sheet.addCell(new jxl.write.Number(colCount, rowCount,Double.parseDouble(empWapDtlsArr[i]),numFormat2)); //Printing-target,achv,ach%
									}catch(NumberFormatException e){
										e.printStackTrace();
										logger.error("Custom Error - NumberFormatException : ",e);
									}
								}
								colCount++;
							}
							if(null != scoreIncObj){
								if(j==CommonConstants.SALES_PARAMID){
									sheet.addCell(new jxl.write.Number(colCount, rowCount,scoreIncObj.getSalesScore(),numFormat2));
								}
								else if(j==CommonConstants.POB_PARAMID)
									sheet.addCell(new jxl.write.Number(colCount, rowCount,scoreIncObj.getPobSalesScore(),numFormat2));
								else if(j==CommonConstants.DOC_REACH_PARAMID)
									sheet.addCell(new jxl.write.Number(colCount, rowCount,scoreIncObj.getDocReachScore(),numFormat2));
								else if(j==CommonConstants.PULSE_CHEM_REACH_PARAMID)
									sheet.addCell(new jxl.write.Number(colCount, rowCount,scoreIncObj.getPulseChemReachScore(),numFormat2));
								else if(j==CommonConstants.COVERAGE_PARAMID)
									sheet.addCell(new jxl.write.Number(colCount, rowCount,scoreIncObj.getDocCoverageScore(),numFormat2));

								colCount++;
							}else{
								colCount++;
							}	
						}else{
							for(int i=0;i<4;i++){

								if(empWapDtlsArr[i]!=null && empWapDtlsArr[i]!="" && !empWapDtlsArr[i].equalsIgnoreCase("null")){
									try{
										sheet.addCell(new jxl.write.Number(colCount, rowCount,Double.parseDouble(empWapDtlsArr[i]),numFormat2));
									}catch(NumberFormatException e){
										e.printStackTrace();
										logger.error("Custom Error - NumberFormatException : ",e);
									}
								}	
								colCount++;
							}
							//Rating and Score for the month
							if(scoreIncObj!=null){
								sheet.addCell(new jxl.write.Number(colCount, rowCount,(scoreIncObj.getFinalScore()!=null?scoreIncObj.getFinalScore():0),numFormat2));
								colCount++;
								sheet.addCell(new Label(colCount, rowCount,(scoreIncObj.getRating()!=null? scoreIncObj.getRating():"")));
								colCount++;
							}else{
								sheet.addCell(new jxl.write.Number(colCount, rowCount,0,numFormat2));
								colCount++;
								sheet.addCell(new Label(colCount, rowCount,""));
								colCount++;
							}

							//sheet.addCell(new Label(colCount, rowCount,scoreIncObj.getOtherScore()));
						}	
					}else{
						if(j!=6){
							for(int i=0;i<3;i++){
								colCount++;
							}
						}else{
							for(int i=0;i<6;i++){ //4 manegerial+2 ratings
								colCount++;
							}
						}
					}
				}
			}

			innerMap.clear();
			/*WAP Details : end*/

			/*cummulative or YTD(year to date)*/
			CummWapDetails cummWapDtls=new CummWapDetails();
			cummWapDtls=cummWapDetls.get(ecode);
			if(null != cummWapDtls){

				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getSalesTarget(),numFormat));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getSalesAch(),numFormat));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getSalesAchPerc(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getSalesScore(),numFormat2));
				colCount++;

				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getPbTarget(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getPbAch(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getPbAchPerc(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getPbScore(),numFormat2));
				colCount++;

				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getDocRchTarget(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getDocRchAch(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getDocRchAchPerc(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getDocRchScore(),numFormat2));
				colCount++;

				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getPulChmTarget(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getPulChmAch(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getPulChmAchPerc(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getPulChmScore(),numFormat2));
				colCount++;

				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getDocCovTarget(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getDocCovAch(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getDocCovAchPerc(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getDocCovScore(),numFormat2));
				colCount++;

				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getOther1(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getOther2(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getOther3(),numFormat2));
				colCount++;
				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getOther4(),numFormat2));
				colCount++;

				sheet.addCell(new jxl.write.Number(colCount, rowCount,cummWapDtls.getTotalScore(),numFormat2));
				colCount++;
				sheet.addCell(new Label(colCount, rowCount,cummWapDtls.getRating(),numFormat2));
				colCount++;
			}
			/*cummulative or YTD(year to date) : end*/
			rowCount++;
		}		
		return "";
	}

	public String getReportName(String desig, SessionUser user,String div,String yr,String mth) {

		StringBuilder fileName = new StringBuilder();
		fileName.append("Eval_Summary_");
		fileName.append(desig).append("_");
		fileName.append(div).append("_"); // division from Session
		fileName.append(yr).append("_"); // year from Session
		fileName.append(mth).append("_"); // month from Session
		fileName.append(CommonUtils.getCurrentDateTime(new Date()));
		fileName.append(".xls");
		return fileName.toString();
	}
	public int secondRowHeads(int paramsize,int rowCount,int colCount,WritableSheet sheet,WritableCellFormat wcf) throws RowsExceededException, WriteException{
		for(int i=0; i<(paramsize-1);i++){
			sheet.setColumnView(colCount, 15);
			sheet.addCell(new Label(colCount, rowCount, "Target", wcf));
			colCount++;
			sheet.setColumnView(colCount, 15);
			sheet.addCell(new Label(colCount, rowCount, "Acheivement", wcf));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount, "Achv %", wcf));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount, "Score", wcf));
			colCount++;
		}

		sheet.addCell(new Label(colCount, rowCount, "TP COMP", wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, "Strat Comp", wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, "Detailing", wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, "Dr Conv", wcf));
		colCount++;
		/*sheet.addCell(new Label(colCount, rowCount, "Total", wcf));
			colCount++;*/

		sheet.addCell(new Label(colCount, rowCount, "Total Score", wcf));
		colCount++;
		sheet.addCell(new Label(colCount, rowCount, "Rating", wcf));
		colCount++;
		return colCount;
	}

	public Map<String,WapFinalScoreAndIncrementDomain> getScoreRatingEmpWise(String yr,String mth,int divId){
		Map<String,WapFinalScoreAndIncrementDomain> map=new HashMap<String,WapFinalScoreAndIncrementDomain>(); 
		List<Object[]> list=wapScoreDao.getScoreAndRating(yr, mth,divId);
		WapFinalScoreAndIncrementDomain obj=null;
		WapFinalScoreIncId idObj=null;
		Number num=0;
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				idObj=new WapFinalScoreIncId();
				obj=new WapFinalScoreAndIncrementDomain();
				//netid,ecode,yr,mth,salesScore,POBSalesScore,DocReachScore,PulseChemReachScore,DocCoverageScore,otherScore,rating,finalscore						
				idObj.setNetid((String)object[0]);
				idObj.setEcode((String)object[1]);
				idObj.setYr((String)object[2]);
				idObj.setMth((String)object[3]);
				obj.setWapFinalScoreIncId(idObj);

				num=(Number)object[4];
				obj.setSalesScore(num.doubleValue());

				num=(Number)object[5];
				obj.setPobSalesScore(num.doubleValue());

				num=(Number)object[6];
				obj.setDocReachScore(num.doubleValue());

				num=(Number)object[7];
				obj.setPulseChemReachScore(num.doubleValue());

				num=(Number)object[8];
				obj.setDocCoverageScore(num.doubleValue());

				num=(Number)object[9];
				obj.setOtherScore(num.doubleValue());

				obj.setRating((String)object[10]);

				num=(Number)object[11];
				obj.setFinalScore(num.doubleValue());

				map.put((String)object[1], obj);
			}
		}
		return map;
	}
	public Map<String, CummWapDetails> setAndGetCummWapMap(List<Object[]> list,ArrayList<Float> slabsMinScores,ArrayList<Float> pobMinScores,
			ArrayList<Float> docrchMinScores,ArrayList<Float> docCovMinScores,ArrayList<Float> pulChmMinScores,
			HashMap<Integer,Float> paramIdWeightageMap,LinkedHashMap<Float,Float> salesSlabMap,
			LinkedHashMap<Float,Float> pobSlabMap,LinkedHashMap<Float,Float> docRchSlabMap,LinkedHashMap<Float,Float> 
			docCovSlabMap,LinkedHashMap<Float,Float> pulChmRchSlabMap,TreeMap<Float,String> scoreIncrementMap,HashMap<String,Long> annualtargetMap,
			String frmYrMth,String  toYrMth,int noOfMths,int maxToDay,int toYr,int toMth){

		Map<String, CummWapDetails> cummWapDtlsMap=new HashMap<String, CummWapDetails>();//setAndGetCummWapMap(list);
		CummWapDetails cumWapObj=null;
		Number num=null;
		Double perc=0.0;
		Float weightage=0.0F;
		double totalScore=0.0;
		int mthWrkd=0;
		double aPlusMinScore=CommonConstants.A_PLUS_MIN_SCORE;
		String jnDate="0000-00-00";
		//double otherperc=25.0;
		ArrayList<Float> incrMinScoreList=null;
		if(null != scoreIncrementMap){
			incrMinScoreList=new ArrayList<Float>(scoreIncrementMap.keySet());    
		}


		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				mthWrkd=0;
				cumWapObj=new CummWapDetails();
				num=(Number) object[2];
				cumWapObj.setSalesTarget(num!=null ? num.intValue() : 0);
				num=(Number) object[3];
				cumWapObj.setSalesAch(num!=null ? num.intValue() : 0);
				if(cumWapObj.getSalesAch()==0 || cumWapObj.getSalesTarget()==0){
					perc=0.0;
				}
				else{
				perc=(cumWapObj.getSalesAch()/cumWapObj.getSalesTarget())*100;
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				}
				cumWapObj.setSalesAchPerc(perc);

				num=(Number) object[4];
				cumWapObj.setPbTarget(num!=null ? num.intValue() : 0);
				num=(Number) object[5];
				cumWapObj.setPbAch(num!=null ? num.intValue() : 0);
				if(cumWapObj.getPbAch()==0 || cumWapObj.getPbTarget()==0){
					perc=0.0;
				}
				else{
				perc=(cumWapObj.getPbAch()/cumWapObj.getPbTarget())*100;
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				}
				cumWapObj.setPbAchPerc(perc);

				num=(Number) object[6];
				cumWapObj.setDocRchTarget(num!=null ? num.doubleValue() : 0);
				num=(Number) object[7];
				cumWapObj.setDocRchAch(num!=null ? num.doubleValue() : 0);
				if(cumWapObj.getDocRchAch()==0 || cumWapObj.getDocRchTarget()==0){
					perc=0.0;
				}
				else{
				perc=(cumWapObj.getDocRchAch()/cumWapObj.getDocRchTarget())*100;
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				}
				cumWapObj.setDocRchAchPerc(perc);
				
				num=(Number) object[8];
				cumWapObj.setDocCovTarget(num!=null ? num.doubleValue() : 0);
				num=(Number) object[9];
				cumWapObj.setDocCovAch(num!=null ? num.doubleValue() : 0);
				if(cumWapObj.getDocCovAch()==0 || cumWapObj.getDocCovTarget()==0){
					perc=0.0;
				}
				else{
				perc=(cumWapObj.getDocCovAch()/cumWapObj.getDocCovTarget())*100;
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				}
				cumWapObj.setDocCovAchPerc(perc);

				num=(Number) object[10];
				cumWapObj.setPulChmTarget(num!=null ? num.doubleValue() : 0);
				num=(Number) object[11];
				cumWapObj.setPulChmAch(num!=null ? num.doubleValue() : 0);
				if(cumWapObj.getPulChmAch()==0 || cumWapObj.getPulChmTarget()==0){
					perc=0.0;
				}
				else{
				perc=(cumWapObj.getPulChmAch()/cumWapObj.getPulChmTarget())*100;
				perc = (double) Math.round(perc * 100);
				perc = perc/100;
				}
				cumWapObj.setPulChmAchPerc(perc);

				num=(Number) object[12];
				cumWapObj.setOther1(num!=null ? num.doubleValue() : 0);
				num=(Number) object[13];
				cumWapObj.setOther2(num!=null ? num.doubleValue() : 0);
				num=(Number) object[14];
				cumWapObj.setOther3(num!=null ? num.doubleValue() : 0);
				num=(Number) object[15];
				cumWapObj.setOther4(num!=null ? num.doubleValue() : 0);
				
				//printing no. of months worked since selected from yrmth or DOJ till to yrmth: start
				jnDate=(String)(object[16].toString());
				String jnYr="0000";String jnMth="00";int jnDay=0;
				try{
				if(jnDate!=null && !jnDate.equalsIgnoreCase("")){
					jnYr=jnDate.split("-")[0];
					jnMth=jnDate.split("-")[1];
					jnDay=Integer.parseInt(jnDate.split("-")[2]);
				}
				
				if(Integer.parseInt(jnYr+jnMth) < Integer.parseInt(frmYrMth)){ //if joining yr mth is less than from yr mth then print no of months between frm yr mth and to yr mth selected.
					mthWrkd=noOfMths;
				}
				else if(Integer.parseInt(jnYr+jnMth) == Integer.parseInt(frmYrMth)){ /*if joining yr mth and frm yr mth are equal then consider day of joining.
													if day of joining <= 15 then consider whole month, if joining day is after 15th of the month then don't consider 
													the joining month in total months worked*/
					if(jnDay<=15){
						mthWrkd=noOfMths;
						
					}else{
						mthWrkd=noOfMths-1;
						
					}
				}else if(Integer.parseInt(jnYr+jnMth) > Integer.parseInt(toYrMth)){ //if joining yr mth is greater than to yr mth then print 0.
					mthWrkd=0;
				}
				else if(Integer.parseInt(jnYr+jnMth) > Integer.parseInt(frmYrMth)){ /* if join yr mth is greater than from yr mth but its less 
																					   than to yrmth (as checked in above condn) then calculate the months worked till toyrmth*/
					int temp1=CommonUtils.getmonthsBetweenDates(Integer.parseInt(jnYr),Integer.parseInt(jnMth),1,
							   toYr,toMth,maxToDay);
					if(jnDay<=15)
						mthWrkd=(temp1+1);
					else
						mthWrkd=(temp1);
					
				}
				}catch(Exception e){
					e.printStackTrace();
					logger.error("Custom Error - ReportServiceImpl : no. of months worked : ",e);
				}
				//printing no. of months worked since selected from yrmth or DOJ till to yrmth: end
				
				
				if(mthWrkd!=0)
				cumWapObj.setOtherAch(((cumWapObj.getOther1()!=0 ? cumWapObj.getOther1()/mthWrkd : 0))+
						(cumWapObj.getOther2()!=0 ? cumWapObj.getOther2()/mthWrkd : 0)+
						(cumWapObj.getOther3()!=0 ? cumWapObj.getOther3()/mthWrkd : 0)+
						(cumWapObj.getOther4()!=0 ? cumWapObj.getOther4()/mthWrkd : 0));
				else
					cumWapObj.setOtherAch(0);
				cumWapObj.setAnnualTarget(annualtargetMap.get(cumWapObj.getNetid()));

				/*Set Points as per Acheivement Percent i.e. if acheivement percent lies within a a specific range a slab of a param then get points against it.*/
				if(cumWapObj.getSalesAchPerc()>0){
					for(Float minSlab : slabsMinScores){
						if(cumWapObj.getSalesAchPerc()>=minSlab){ //compares salesAch with the highest min slab range first,the 2nd highest so on...lowest
							//value in the last,if sales ach is greater than min slab range then that point is set.
							cumWapObj.setSalesPoints(salesSlabMap.get(minSlab));
							break;
						}
					}
				}
				if(cumWapObj.getPbAchPerc()>0 && null!=pobMinScores){
					for(Float minSlab : pobMinScores){
						if(cumWapObj.getPbAchPerc()>=minSlab){
							cumWapObj.setPbPoints(pobSlabMap.get(minSlab));
							break;
						}
					}
				}	
				if(cumWapObj.getDocRchAchPerc()>0 && null!=docrchMinScores){
					for(Float minSlab : docrchMinScores){
						if(cumWapObj.getDocRchAchPerc()>=minSlab){
							cumWapObj.setDocRchPoints(docRchSlabMap.get(minSlab));
							break;
						}
					}
				}	
				if(cumWapObj.getDocCovAchPerc()>0 && null!=docCovMinScores){
					for(Float minSlab : docCovMinScores){
						if(cumWapObj.getDocCovAchPerc()>=minSlab){
							cumWapObj.setDocCovPoints(docCovSlabMap.get(minSlab));
							break;
						}
					}
				}
				if(cumWapObj.getPulChmAchPerc()>0 && null!=pulChmMinScores){
					for(Float minSlab : pulChmMinScores){
						if(cumWapObj.getPulChmAchPerc()>=minSlab){
							cumWapObj.setPulChmPoints(pulChmRchSlabMap.get(minSlab));
							break;
						}
					}
					
					//as there are no slabs defined for others,hence points is : 25% of total (as given by palsons)		
					/*perc=(cumWapObj.getOtherAch() * (otherperc/100)); //TODO doubt -> why getting 25% again when already saved in DB 25% of the score.
					cumWapObj.setOtherPoints(perc);
					System.out.println("points acheived MGR : "+perc);*/

					cumWapObj.setOtherPoints(cumWapObj.getOtherAch());//both points and ach are same in this case
					/*Set Points as per Acheivement Percent i.e. if acheivement percent lies within a a specific range a slab of a param then get points against it. : end*/
					/*Set Score as per points acheived. Formula: if weightage of sale=45%
					 * Score = 4
					 * then sales%=45% of 4*/
					//1. sales
					weightage=(Float)paramIdWeightageMap.get(CommonConstants.SALES_PARAMID);
					cumWapObj.setSalesScore((cumWapObj.getSalesPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getSalesPoints()) : 0.0);			

					//2. POB Sales
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.POB_PARAMID);
					cumWapObj.setPbScore((cumWapObj.getPbPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getPbPoints()) : 0.0);

					//3.Docreach
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.DOC_REACH_PARAMID);
					cumWapObj.setDocRchScore((cumWapObj.getDocRchPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getDocRchPoints()) : 0.0);

					//4.Doc Coverage
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.COVERAGE_PARAMID);
					cumWapObj.setDocCovScore((cumWapObj.getDocCovPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getDocCovPoints()) : 0.0);

					//5. Pulse Chem Reach
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.PULSE_CHEM_REACH_PARAMID);
					cumWapObj.setPulChmScore((cumWapObj.getPulChmPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getPulChmPoints()) : 0.0);

					//6. Manager's Effectiveness : does not required points here,as slabs are not there for Manager's Eff
					weightage=(Float) paramIdWeightageMap.get(CommonConstants.MANAGERIAL_EFF_PARAMID);
					cumWapObj.setOtherScore((cumWapObj.getOtherPoints() != 0.0) ? CommonUtils.getScorePerc(weightage!=null ? weightage : 0.0,cumWapObj.getOtherPoints()) : 0.0);
					//System.out.println("MGR score : "+cumWapObj.getOtherScore());
					/*Set Score as per points acheived : end*/		
				}
				/*Get Rating : start*/
				totalScore=0.0;
				totalScore=cumWapObj.getOtherScore()+cumWapObj.getPulChmScore()+cumWapObj.getDocCovScore()+cumWapObj.getDocRchScore()+
						cumWapObj.getPbScore()+cumWapObj.getSalesScore();
				//System.out.println("totalScore : "+totalScore);
				cumWapObj.setTotalScore(totalScore);

				/*if(((String)object[1]).equalsIgnoreCase("1105") || ((String)object[1]).equalsIgnoreCase("01444")){
					System.out.println("purushotam : "+(String)object[1]);
				}*/

				cumWapObj.setAnnualTarget(annualtargetMap.get((String)object[0]));

				if(totalScore>=aPlusMinScore && cumWapObj.getAnnualTarget()!=null && 
						cumWapObj.getAnnualTarget()>=CommonConstants.A_PLUS_MIN_TARGET
						&& cumWapObj.getSalesAch()>=CommonConstants.A_PLUS_MIN_ACH_PERC){
					cumWapObj.setRating("A+");
				}
				else if(totalScore>0 && null!=incrMinScoreList){
					for(Float minIncrScore : incrMinScoreList){
						if(totalScore>=minIncrScore){
							cumWapObj.setRating(scoreIncrementMap.get(minIncrScore));
							break;
						}
					}
				}
				else{
					cumWapObj.setRating(scoreIncrementMap.get(0.0f));
					//System.out.println("totalScore is below 0: "+totalScore);
				}
				/*Get Rating : end*/
				cummWapDtlsMap.put((String)object[1],cumWapObj);
			}

		}
		return cummWapDtlsMap;
	}

	public List<Object[]> getAllEmpHierarchy(String fmsDbRef, String loginId,String level,String mth,String yr,String hqref,String logLvl){
		//wapCal.createTempNetidwithMaxjoinDate(loginId, mth, yr, hqref, fmsDbRef);
		wapCal.createTempNetidwithMaxjoinDateLvlWise(loginId, mth, yr, hqref, fmsDbRef, logLvl);
		List<Object[]> list=userDao.getAllEmpHierarchy(fmsDbRef, loginId,level);
		return list;
	}

	public List<Object[]> getSelEmpHierarchy(String fmsDbRef, String loginId,String level,String selEmp,String yr,String mth){
		List<Object[]> list=userDao.getSelEmpHierarchy(fmsDbRef, loginId,level,selEmp,yr,mth);
		return list;
	}

	public void writeFocusProducts(WritableSheet sheet,String frmYrMth,String toYrMth,int divId,String fmsDbRef,
			WritableCellFormat wcf,WritableCellFormat cfb2){

		int colCount=0;
		int rowCount=0;
		List<Object[]> list=focProdDao.getFocProducts(frmYrMth, toYrMth, divId, fmsDbRef);
		try{

			sheet.addCell(new Label(colCount, rowCount,"YEAR",cfb2));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount,"MONTH",cfb2));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount,"PROD ID",cfb2));
			colCount++;
			sheet.addCell(new Label(colCount, rowCount,"PROD NAME",cfb2));	
			rowCount++;
			if(null != list && !list.isEmpty()){
				for(Object[] object : list){
					colCount=0;
					sheet.addCell(new Label(colCount, rowCount,(String)object[0],wcf));
					colCount++;
					sheet.addCell(new Label(colCount, rowCount,(String)object[1],wcf));
					colCount++;
					sheet.addCell(new Label(colCount, rowCount,(String)object[2],wcf));
					colCount++;
					sheet.addCell(new Label(colCount, rowCount,(String)object[3],wcf));
					rowCount++;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Custom Error - ReportServiceImpl : ",e);
		}

	}


}
