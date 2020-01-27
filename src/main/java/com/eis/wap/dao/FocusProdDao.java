package com.eis.wap.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.FocusProductDomain;
import com.eis.wap.domain.PsrCommentsDomain;


public interface FocusProdDao extends JpaRepository<FocusProductDomain, Integer>,FocusProdDaoCustom{
	
}
