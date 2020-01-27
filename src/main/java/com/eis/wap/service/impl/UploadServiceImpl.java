package com.eis.wap.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.eis.wap.constants.CommonConstants;
import com.eis.wap.dao.MgrEffectivenessParamDAO;
import com.eis.wap.dao.UserDAO;
import com.eis.wap.domain.MgrEffectivenessParams;
import com.eis.wap.service.UploadService;

@Component
public class UploadServiceImpl implements UploadService {

	@Autowired
	private MgrEffectivenessParamDAO mgrEffectivenessParamDAO;

	@Autowired
	UserDAO userDAO;

	private static final String EXCEL_FILE_LOCATION = "C://Code//Data//Upload.xls";

	public List<String> uploadExcel(MultipartFile file) {
		Workbook workbook = null;
		List<MgrEffectivenessParams> parameterList = new ArrayList<MgrEffectivenessParams>();
		MgrEffectivenessParams parameter = null;
		List<String> statusList = new ArrayList<String>();

		System.out.println("Inside upload file");
		try {
			File currDir = new File(".");
			String path = currDir.getAbsolutePath();
			String fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
			if (null != fileLocation) {
				if (fileLocation.endsWith(".xlsx") || fileLocation.endsWith(".xls")) {
					workbook = Workbook.getWorkbook(new File(EXCEL_FILE_LOCATION));
					Sheet sheet = workbook.getSheet(0);
					statusList = validateMandatoryFields(sheet);
					if(null == statusList || statusList.isEmpty()) {
						for(int row = 1; row < sheet.getRows(); row++) {
							parameter = new MgrEffectivenessParams();
							Cell eCodeCell = sheet.getCell(CommonConstants.INT_ZERO, row);
							String eCode = eCodeCell.getContents(); 
							if(null != eCode) {
								if(eCode.length() < CommonConstants.ECODE_LENGHT) {
									eCode = getValidECode(eCode);
								}
								parameter.setEcode((eCodeCell.getContents()));
							}

							Cell empNameCell = sheet.getCell(CommonConstants.INT_ONE, row);
							/*if(null != empNameCell.getContents()) {
								parameter.setEmpName(empNameCell.getContents());
							}*/

							Cell yrCell = sheet.getCell(CommonConstants.INT_TWO, row);
							if(null != yrCell.getContents()) {
								parameter.setYr(yrCell.getContents());
							}

							Cell mthCell = sheet.getCell(CommonConstants.INT_THREE, row);
							if(null != mthCell.getContents()) {
								parameter.setMth(mthCell.getContents());
							}

							Cell p1Cell = sheet.getCell(CommonConstants.INT_FOUR, row);
							if(null != p1Cell.getContents()) {
								parameter.setParam1(Float.parseFloat(p1Cell.getContents()));
							}

							Cell p2Cell = sheet.getCell(CommonConstants.INT_FIVE, row);
							if(null != p2Cell.getContents()) {
								parameter.setParam2(Float.parseFloat(p2Cell.getContents()));
							}

							Cell p3Cell = sheet.getCell(CommonConstants.INT_SIX, row);
							if(null != p3Cell.getContents()) {
								parameter.setParam3(Float.parseFloat(p3Cell.getContents()));
							}

							Cell p4Cell = sheet.getCell(CommonConstants.INT_SEVEN, row);
							if(null != p4Cell.getContents()) {
								parameter.setParam4(Float.parseFloat(p4Cell.getContents()));
							}

							parameter.setAddDate(new Date());
							parameter.setAddedBy("EIS");
							//parameter.setActive(true);
							parameterList.add(parameter);
						}

						// calling DAO to insert manager effectiveness parameters in table 
						mgrEffectivenessParamDAO.save(parameterList);
						statusList.add( "File uploaded successfully");
					}

				} else {
					statusList.add("Not a valid excel file!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (workbook != null) {
				workbook.close();
			}
		}

		return statusList;
	}

	public List<String> validateMandatoryFields(Sheet sheet) {
		List<String> errorList = new ArrayList<String>();
		List<String> eCodeList = new ArrayList<String>();
		List<String> invalideCodeList = new ArrayList<String>();
		StringBuilder error = null;
		String eCode = null;
		boolean isHeaderValid = true;
		boolean isDataValid = true;
		int r = 0;

		for(String mandatoryField : CommonConstants.MANDATORY_FIELD_NAME_LIST) {
			for(int i = 0; i < sheet.getColumns(); i++) {
				if(!mandatoryField.equalsIgnoreCase((sheet.getCell(i, r).getContents()))){
					if(i == CommonConstants.MANDATORY_FIELD_NAME_LIST.size() - 1) {
						isHeaderValid = false;
						errorList.add("Mandatory Header is missing: " + mandatoryField);
						break;
					}
				}else{
					break;
				}
			}
		}

		if(isHeaderValid) {
			
			if(!validateYrMth(sheet).isEmpty()){
				errorList.addAll(validateYrMth(sheet));
				return errorList;
			}
			
			for(int row = 1; row < sheet.getRows(); row++) {
				for(int columnNo : CommonConstants.MANDATORY_FIELD_ID_LIST) {
					String content = sheet.getCell(columnNo, row).getContents();
					error = new StringBuilder("Invalid data value for ECode : ");
					if(null != content && !content.isEmpty()) {
						if(CommonConstants.INT_ZERO == columnNo) {
							if(content.length() < CommonConstants.ECODE_LENGHT) {
								content = getValidECode(content);
								eCode = content;
							}
							if(eCodeList.contains(content)) {
								errorList.add("Trying to enter duplicate ECode " + content);
								return errorList;
							}
							eCodeList.add(content);
						}

						if(Arrays.asList(4,5,6,7).contains(columnNo)) {
							float value = Float.parseFloat(content);
							if(value >= 0)
								continue;
							else
								isDataValid = false;
						}
					}
					else
						isDataValid = false;

					if(!isDataValid) {
						error.append(eCode);
						error.append(" at row - "+row+", column - "+columnNo); 
						errorList.add(error.toString());
					}
				}
			}

			invalideCodeList = validateECodes(eCodeList);
			if(null != invalideCodeList) {
				error = new StringBuilder("ECodes are not present in Database : " + invalideCodeList.toString());
				errorList.add(error.toString());
			}
		}
		return errorList;
	}

	public List<String> validateECodes(List<String> eCodeList) {
		List<String> codeList = new ArrayList<String>();
		if(null != eCodeList && !eCodeList.isEmpty()) {
			List<Object[]> list = userDAO.getECodeList(eCodeList,"palsons1");//TODO get from session
			if(null != list && !list.isEmpty()) {
				if(eCodeList.size() == list.size()) {
					eCodeList = null;
				}else {
					for(Object[] obj : list) {
						codeList.add((String) obj[0]);
					}
					eCodeList.removeAll(codeList);
				}
			}
		}
		return eCodeList;
	}

	public String getValidECode(String content) {
		StringBuilder eCodeBuilder = new StringBuilder();
		int noOfZeros = (CommonConstants.ECODE_LENGHT - content.length()) - 1;
		eCodeBuilder.append(CommonConstants.NO_OF_ZERO_LIST.get(noOfZeros));
		eCodeBuilder.append(content);
		content = eCodeBuilder.toString();
		return content;
	}

	public List<String> validateYrMth(Sheet sheet) {
		List<String> statusList = new ArrayList<String>();
		Map<String, List<String>> yrMthMap = new LinkedHashMap<String, List<String>>();
		List<String> mthList = null;
		for(int row = 1; row < sheet.getRows(); row++) {
			String yr = sheet.getCell(CommonConstants.INT_TWO, row).getContents();
			String mth = sheet.getCell(CommonConstants.INT_THREE, row).getContents();
			mthList = new ArrayList<String>();
			if(yrMthMap.containsKey(yr)) {
				mthList = yrMthMap.get(yr);
			}

			if(!mthList.contains(mth)) {
				mthList.add(mth);
			}
			yrMthMap.put(yr, mthList);
		}

		List<String> yrList = new ArrayList<String> (yrMthMap.keySet());
		List<Object[]> list = mgrEffectivenessParamDAO.getYrMth(yrList);
		if(null != list && !list.isEmpty()) {
			for(Object[] data : list) {
				String yr = (String) data[0];
				String mth = (String) data[1];
				if(yrMthMap.containsKey(yr) && yrMthMap.containsValue(mth)) {
					statusList.add("Given Year : "+yr+" and Month : "+mth+" already exists in Database");
				}
			}
		}

		return statusList;
	}
}
