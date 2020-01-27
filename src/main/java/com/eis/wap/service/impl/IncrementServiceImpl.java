package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.IncrementDao;
import com.eis.wap.model.Increment;
import com.eis.wap.service.IncrementService;
import com.eis.wap.util.CommonUtils;

@Component
public class IncrementServiceImpl implements IncrementService {

	@Autowired
	private IncrementDao incrementDAO;

	@Override
	public List<Increment> showAllIncrement() {
		List<Increment> incrementList = new ArrayList<Increment>();
		Increment increment = null;
		List<com.eis.wap.domain.Increment> incrementDataList = incrementDAO.showAllIncrement();
		if(null != incrementDataList && !incrementDataList.isEmpty()) {
			for(com.eis.wap.domain.Increment incrementData : incrementDataList) {
				increment = new Increment();
				increment.setIncr_id(incrementData.getIncrId());
				increment.setIncr_desc(incrementData.getIncrDesc());
				increment.setMax_score(incrementData.getMaxScore());
				increment.setMin_score(incrementData.getMinScore());
				increment.setRating(incrementData.getRating());
				increment.setIncr_amount(incrementData.getIncrAmount());
				incrementList.add(increment);
			}
		}else {
			incrementList.add(getDefaultData());
		}
		return incrementList;
	}

	public Increment getDefaultData(){

		Increment increment = new Increment();
		increment.setIncr_id(0);
		increment.setIncr_desc("No Data Found");
		increment.setMin_score(0.0F);
		increment.setMax_score(0.0F);
		increment.setRating("");
		increment.setIncr_amount(0.0F);

		return increment;
	}

	@Override
	public List<Increment> addIncrement(Increment increment) {

		List<Increment> incrementList = null;
		com.eis.wap.domain.Increment incrementData = new com.eis.wap.domain.Increment();

		if(null != increment) {
			incrementData = prepareDataForDB(increment, "add");
			
			try {
				incrementDAO.save(incrementData);
				System.out.println("Data successfully inserted into increments");
				incrementList = showAllIncrement();
			}catch(Exception e) {
				System.out.println("Exception occured while adding increments");
				e.printStackTrace();
			}
		}
		return incrementList;
	}

	@Override
	public List<Increment> updateIncrement(Increment increment) {
		List<Increment> incrementList = null;
		if(null != increment) {
			try {
				incrementDAO.updateIncrement(increment.getIncr_desc(), increment.getMin_score(), increment.getMax_score(), increment.getRating(),
						increment.getIncr_amount(), increment.getLast_mod_by(), increment.getLast_mod_dt(), increment.getIncr_id());
				
				System.out.println("Data successfully updated into increments");
				
				incrementList = showAllIncrement();
				
			}catch(Exception e) {
				System.out.println("Exception occured while updating increments");
			}
		}

		return incrementList;
	}

	public com.eis.wap.domain.Increment prepareDataForDB(Increment increment, String operationType) {

		com.eis.wap.domain.Increment incrementData = new com.eis.wap.domain.Increment();

		incrementData.setIncrDesc(increment.getIncr_desc());
		incrementData.setIncrAmount(increment.getIncr_amount());
		incrementData.setMaxScore(increment.getMax_score());
		incrementData.setMinScore(increment.getMin_score());
		incrementData.setRating(increment.getRating());

		if("update".equalsIgnoreCase(operationType)) {
			incrementData.setIncrId(increment.getIncr_id());
			incrementData.setLastModDate(new Date());
			incrementData.setLastModBy("EIS");

		}else {
			incrementData.setAddDate(CommonUtils.getCurrentDateTime());
			incrementData.setAddedBy("EIS");
		}

		return incrementData;
	}

	@Override
	public List<Increment> deleteIncrement(Integer incrementId) {
		incrementDAO.deleteIncrement(incrementId);
		List<Increment> incrementList = showAllIncrement();
		return incrementList;
	}

	@Override
	public List<Object[]> getRatings() {
		
		return incrementDAO.getRatings();
	}
	
	

}
