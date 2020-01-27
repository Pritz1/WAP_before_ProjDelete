package com.eis.wap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eis.wap.model.Increment;

@Service
public interface IncrementService {

	 public List<Increment> showAllIncrement();
	 
	 public List<Increment> addIncrement(Increment increment);
	 
	 public List<Increment> updateIncrement(Increment increment);
	 
	 public List<Increment> deleteIncrement(Integer incrementId);
	 
	 public List<Object[]> getRatings();
}
