package com.eis.wap.service;

import org.springframework.stereotype.Service;

import com.eis.wap.model.PsrComments;

@Service
public interface CommentService {
	
	public void add(PsrComments comments);
	
	public String getCommentByEcode(String emp,String netId,String loginYr,String loginMth,int divId);
	
	public int update(PsrComments comments);
	
	//Pranali: 22/01/2019 end

}
