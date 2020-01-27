package com.eis.wap.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.DivMaster;

public interface DivisionDAO extends JpaRepository<DivMaster, Integer>{

	@Transactional
	@Modifying
	@Query(value = "delete from div_master where divId in (?1) " , nativeQuery = true)
	public int deleteById(List<Integer> divIdList);
	
	@Transactional
	@Modifying
	@Query(value = "update div_master set divName=?,FmsDbRef=?,SalesDbref=? where divId=?" , nativeQuery = true)
	public int updateDivMaster(String divName, String fmsDbRef, String salesDbRef, Integer divId);
	@Query(value = "select * from div_master where divId=?" , nativeQuery = true)
	public DivMaster getDivisionDetails(int divId);
}
