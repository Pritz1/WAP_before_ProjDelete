package com.eis.wap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eis.wap.model.Slabs;

@Service
public interface SlabService {

	public List<Slabs> getSlabListByParamId(Integer paramId);
	public boolean addSlab(Slabs slab);
	public boolean updateSlab(Slabs slab);
	public void deleteSlab(Integer paramId, Integer slabId);
	public int getNextSlabId();
}
