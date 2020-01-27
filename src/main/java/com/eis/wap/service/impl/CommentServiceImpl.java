package com.eis.wap.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.CommentsDao;
import com.eis.wap.dao.PsrCommentsDomainId;
import com.eis.wap.domain.PsrCommentsDomain;
 
import com.eis.wap.model.PsrComments;
import com.eis.wap.service.CommentService;

@Component
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	CommentsDao commentdao;

	@Override
	public void add(PsrComments comments) {
 
			PsrCommentsDomain obj=new PsrCommentsDomain();
			PsrCommentsDomainId idObj=new PsrCommentsDomainId();
			obj.setComments(comments.getComments());
			//obj.setEcode(comments.getEcode());
			idObj.setEcode(comments.getEcode());
			idObj.setMth(comments.getMth());
			idObj.setYr(comments.getYr());
			idObj.setNetid(comments.getNetId());
			obj.setPsrCommentsId(idObj);
			obj.setDivid(comments.getDivid());
		    //commentdao.add(comments);
		    commentdao.saveAndFlush(obj);		
	}

	@Override
	public String getCommentByEcode(String emp,String netId,String loginYr,String loginMth,int divid) {
		String comment=null;
		comment=commentdao.getCommentByEcode(emp,netId,loginMth,loginYr,divid);		
		System.out.println(comment);
		return comment;
	}
	
	public int update(PsrComments comments) {
		 int i=commentdao.updateComment(comments.getEcode(),comments.getNetId(),comments.getYr(),comments.getMth(),comments.getComments());	
		 return i;
		 
		 //Pranali : 22/01/2019 end
}
}
