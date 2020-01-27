package com.eis.wap.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.ParamMaster;
import com.eis.wap.domain.PsrCommentsDomain;
 
 
public interface CommentsDao extends JpaRepository<PsrCommentsDomain, PsrCommentsDomainId>{
	 
	 
	 /*Pranali: 22/01/2019  start*/
	@Query(value="select Comments from psrcomments where ecode=?1 and netid=?2 and yr=?4 and mth=?3 and divid=?5",nativeQuery=true)
	public String getCommentByEcode(String ecode,String netId,String mth,String yr,int divid);
	
	@Transactional
	@Modifying
	@Query(value = "update psrcomments set Comments=?6  where ecode=?1 and netid=?2 and yr=?3 and mth=?4 " , nativeQuery = true)
	public int updateComment(String ecode,String netId,String yr,String mth,String comment);
	
	
	/*Pranali: 22/01/2019  end*/
}
