package com.eis.wap.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.MgrEffectivenessParams;

public interface MgrEffectivenessParamDAO extends JpaRepository<MgrEffectivenessParams, Integer> {

	@Query(value = "Select yr, mth from MgrEffectivenessParams where isActive = 1 and yr in (?) " , nativeQuery = true)
	public List<Object[]> getYrMth(List<String> yrList);
	
	@Modifying
	@Query(value = "insert into Mgrcomments (MgrEffectivenessParamsId,Comment1) values (?1, ?2) " , nativeQuery = true)
	@Transactional
	public void saveComment(Integer MgrEffectivenessParamsId,String comment); 
	
	@Modifying
	@Query(value = "insert into Mgrcomments (MgrEffectivenessParamsId,Comment2) values (?1, ?2) " , nativeQuery = true)
	@Transactional
	public void saveComment2(Integer MgrEffectivenessParamsId,String comment);
	
	@Query(value = "select param1,param2,param3,param4,c.comment1 from MgrEffectivenessParams m "
			+ " inner join mgrcomments c on (m.MgrEffectivenessParamsId=c.MgrEffectivenessParamsId) "
			+ " where ecode=?1 and netid=?2 and yr=?3 and mth=?4 and divid=?5 " , nativeQuery = true)
	public List<Object[]> getmgr1Scores(String ecode,String netid,String yr,String mth,String divid);
	
	@Modifying
	@Query(value = "update MgrEffectivenessParams m1,mgrcomments m2 set mgr2Param1=?1,mgr2Param2=?2,mgr2Param3=?3,mgr2Param4=?4,mgrId2=?9,modDate=curDate(),"
			+ "modBy=?9,m2.comment2=?10 where m1.MgrEffectivenessParamsId=m2.MgrEffectivenessParamsId and ecode=?5 and netid=?6 and yr=?7 and mth=?8 and divid=?11" , 
			nativeQuery = true)
	@Transactional
	public int updateMgr2Scores(Float mgr2Param1,Float mgr2Param2,Float mgr2Param3,Float mgr2Param4,String ecode,String netid,String yr,String mth,
			String emp,String comment2,int divId);
	
	@Modifying
	@Query(value = "update Mgrcomments set Comment2=?2 where  MgrEffectivenessParamsId=?1" , nativeQuery = true)
	@Transactional
	public void updateComment2(Integer MgrEffectivenessParamsId,String comment);
	
	@Query(value = "select mgr2param1,mgr2Param2,mgr2Param3,mgr2Param4,c.comment2 from MgrEffectivenessParams m "
			+ " inner join mgrcomments c on (m.MgrEffectivenessParamsId=c.MgrEffectivenessParamsId) "
			+ " where ecode=?1 and netid=?2 and yr=?3 and mth=?4 and divid=?5 " , nativeQuery = true)
	public List<Object[]> getmgr2Scores(String ecode,String netid,String yr,String mth,String divid);
	
	@Query(value = "select if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param1 is null,0,m.Param1),mgr2param1) as param1,"+
			"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param2 is null,0,m.Param2),mgr2param2) as param2,"+
			"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param3 is null,0,m.Param3),mgr2param3) as param3,"+
			"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param4 is null,0,m.Param4),mgr2param4) as param4," +
			" if(c.comment1 is null,'',c.comment1) as comment1,if(c.comment2 is null,'',c.comment2) as comment2,MgrId2 "+
		    " from mgreffectivenessparams m left join mgrcomments c on (m.mgrEffectivenessParamsId=c.mgrEffectivenessParamsId) "+
		    " where ecode=?1 and netid=?2 and yr=?3 and mth=?4 and divid=?5 " , nativeQuery = true)
	public List<Object[]> getmgrScores(String ecode,String netid,String yr,String mth,String divid);
	
	@Modifying
	@Query(value = "update MgrEffectivenessParams m1,mgrcomments m2 set Param1=?1,Param2=?2,Param3=?3,Param4=?4,managerId=?9,modDate=curDate(),"
			+ "modBy=?9,m2.comment1=?10 where m1.MgrEffectivenessParamsId=m2.MgrEffectivenessParamsId and ecode=?5 and netid=?6 and yr=?7 and mth=?8 and divid=?11" , 
			nativeQuery = true)
	@Transactional
	public int updateMgr1Scores(Float mgr2Param1,Float mgr2Param2,Float mgr2Param3,Float mgr2Param4,String ecode,String netid,String yr,String mth,
			String emp,String comment2,int divId);
	
	@Modifying
	@Query(value = "update MgrEffectivenessParams set mgr2Param1=?1,mgr2Param2=?2,mgr2Param3=?3,mgr2Param4=?4,mgrId2=?9,modDate=curDate(),"
			+ "modBy=?9 where ecode=?5 and netid=?6 and yr=?7 and mth=?8 and divid=?10")
	@Transactional
	public int updateMgr2ScoresNotCommnt(Float mgr2Param1,Float mgr2Param2,Float mgr2Param3,Float mgr2Param4,String ecode,String netid,
			String yr,String mth,String emp,int divId);
	
	@Query(value = "Select MgrEffectivenessParamsId from MgrEffectivenessParams where ecode=?1 and netid=?2 and yr=?3 and mth=?4 and divid=?5 " , nativeQuery = true)
	public Integer getId1(String ecode,String netid,String yr,String mth,int divId);
	
}
