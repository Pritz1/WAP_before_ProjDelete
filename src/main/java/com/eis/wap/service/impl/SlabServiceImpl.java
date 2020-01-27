package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.SlabDao;
import com.eis.wap.model.Slabs;
import com.eis.wap.service.SlabService;
import com.eis.wap.util.CommonUtils;

@Component
public class SlabServiceImpl implements SlabService {

	@Autowired
	SlabDao slabDAO;


	public List<Slabs> getSlabListByParamId(Integer paramId) {
		List<Slabs> slabsList = null;
		Slabs slab = null;
		List<com.eis.wap.domain.Slabs> slabsDataList = slabDAO.getSlabListByParamId(paramId);
		if(null != slabsDataList && !slabsDataList.isEmpty()) {
			slabsList = new ArrayList<Slabs>();
			for(com.eis.wap.domain.Slabs slabData : slabsDataList) {
				slab = new Slabs();
				slab.setSlabId(slabData.getSlabId());
				slab.setSlabName(slabData.getSlabName());
				slab.setRangeMin(slabData.getRangeMin());
				slab.setRangeMax(slabData.getRangeMax());
				slab.setPoints(slabData.getPoints());
				slab.setParamId(slabData.getParamId());
				slab.setSlabAddDate(CommonUtils.getCurrentDate(slabData.getSlabAddDate()));
				slab.setAddedBy(slabData.getAddedBy());
				slab.setLastModBy(slabData.getLastModBy());
				slab.setLastModDt(slabData.getLastModDate());
				slabsList.add(slab);
			}
		}

		return slabsList;
	}

	public boolean addSlab(Slabs slab) {
		com.eis.wap.domain.Slabs slabData = new com.eis.wap.domain.Slabs();
		if(null != slab) {
			//slabData.setSlabId(getNextSlabId());
			slabData.setSlabName(slab.getSlabName());
			slabData.setRangeMin(slab.getRangeMin());
			slabData.setRangeMax(slab.getRangeMax());
			slabData.setPoints(slab.getPoints());
			slabData.setParamId(slab.getParamId());
			slabData.setSlabAddDate(CommonUtils.getCurrentDateTime()); //TODO format date time
			slabData.setAddedBy("EIS");	//TODO get from session
			slabDAO.save(slabData);
		}
		return true;
	}

	public boolean updateSlab(Slabs slab) {
		boolean statusFlag = false;
		if(null != slab) {
			int count = slabDAO.updateSlab(slab.getSlabName(), slab.getRangeMin(), slab.getRangeMax(), slab.getPoints(), CommonUtils.getCurrentDateTime(),
					"EIS", slab.getParamId(), slab.getSlabId());
			 
			if(count > 0) {
				statusFlag = true;
			}
		}
		return statusFlag;
	}

	public void deleteSlab(Integer paramId, Integer slabId) {
		slabDAO.deleteSlab(paramId, slabId);
	}
	//TODO get this as sequence
	public int getNextSlabId(){
		int slabId=slabDAO.getNextSlabId();
		return slabId;
	}

}
