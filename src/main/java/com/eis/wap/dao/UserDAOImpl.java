package com.eis.wap.dao;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.eis.wap.model.Division;


public class UserDAOImpl implements UserDAOCustom {

	@Autowired
	EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAdminUsers(String dbName,String presentEcodes) {
		
		String sqlQuery = "select ecode, ename, etype from #table_name# "
				+ "where etype >= '7' and resdate = '0000-00-00' and deldate = '0000-00-00' and ecode not in ("+presentEcodes+")";
		sqlQuery = sqlQuery.replace("#table_name#", dbName);
		Query query = entityManager.createNativeQuery(sqlQuery);
		List<Object[]> list = query.getResultList();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getLoginDetails(String ecode, String password, String yr, String mth,String fmsDbref, boolean superpwd) { 
		String addQ="";
		if(!superpwd)
			addQ=" and emp.password =:pwd ";
		String sqlQuery = " select distinct emp.eCode, emp.ename, emp.etype, nettrans.netid, nettrans.level2, nettrans.level3, "+
						  " nettrans.level4, nettrans.level5,n.level,n.hname,emp.cocode,emp.jndate from "+fmsDbref+".empmst as emp "+
						  " Inner Join "+fmsDbref+".nettrans as nettrans ON nettrans.level1 = emp.ecode "+
						  " inner join "+fmsDbref+".netmst n on (nettrans.netid=n.netid)"+
						  " where emp.ecode =:ecode "+addQ+" and emp.resdate = '0000-00-00' and emp.deldate = '0000-00-00' " +
						  " and nettrans.yr =:yr and nettrans.mth =:mth and n.deldate='0000-00-00'";
		System.out.println("getLoginDetails : "+sqlQuery);
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("ecode", ecode);
		if(!superpwd)
		query.setParameter("pwd", password);
		
		query.setParameter("yr", yr);
		query.setParameter("mth", mth);
		List<Object[]> list = query.getResultList();
		return list;
	}

	@Override
	public Division getDivisionDetails(String divId) {
		String sql="select divId,divName,fmsDbRef,salesDbRef,hqRef from div_master where divId=:divId";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("divId", Integer.parseInt(divId));
		List<Object[]> list = query.getResultList();
		Division divObject=new Division();
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				divObject.setDivId((Integer)object[0]);
				divObject.setDivName((String)object[1]);
				divObject.setFmsDbRef((String)object[2]);
				divObject.setSalesDbRef((String)object[3]);
				divObject.setHqRef((String)object[4]);
			}
		}
		
		return divObject;
	}
	
	public List<Object[]> getEmployeeList(String year, String month, String hqRef, String level, String fmsDbRef,String dataReqLvl,String emp){

		StringBuilder sb = new StringBuilder();
		sb.append(" Select distinct emp.ename, t1.netId, t1.level1, netmst.hname, netmst.level,emp.cocode from ");
		sb.append(fmsDbRef);sb.append(".netmst as netmst");  
		sb.append(" Inner Join ");
		sb.append(fmsDbRef);sb.append(".nettrans t1 ON netmst.netId = t1.netId and netmst.level =:lvl");
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7)
		sb.append(" and t1.level"+level+"=:emp");
		sb.append(" LEFT JOIN "+fmsDbRef+".NETTRANS t2 ON t1.netid = t2.netid AND t1.yr=t2.yr and t1.mth=t2.mth and "); //to get max joineddate person only
		sb.append(" ((t1.joindate < t2.joindate) OR (t1.joindate = t2.joindate AND t1.netid < t2.netid))  ");
		sb.append(" Inner Join ");
		sb.append(" "+fmsDbRef+".empmst as emp on  t1.level1 = emp.ecode and emp.resdate='0000-00-00' ");
		sb.append(" Where t1.yr =:year and t1.mth =:month and netmst.hname like '%("+hqRef+")%' and t2.netid is null ");
		sb.append(" order by ename ");

		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.setParameter("lvl", dataReqLvl);
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7)
			query.setParameter("emp", emp);	
		System.out.println("getEmployeeList : "+sb.toString());
		List<Object[]> list = query.getResultList();
		return list;
	}
	
	public List<Object[]> getECodeList(List<String> eCodeList,String fmsDbRef) {
		
		String sqlQuery = "Select distinct emp.ecode from "+fmsDbRef+".empmst as emp where emp.ecode in (:list)";
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("list", eCodeList);
		List<Object[]> list = query.getResultList();
		return list;
	}
	
	public List<Object[]> getAllEmpHierarchy(String fmsDbRef,String loginId,String level) {
		
		StringBuffer sb=new StringBuffer();
		sb.append("select n.level1,n.netid,m.hname,e1.ename as PSR,e2.ename as AFM,e3.ename as STATE,e4.ename as RM,e5.ename as ZM,e1.jnDate");
		sb.append(" from tempNetidWithMaxJnDate"+loginId+" t");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".nettrans n on (t.netid=n.netid and t.joindate=n.joindate)");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e1 on (n.level1=e1.ecode)");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e2 on (n.level2=e2.ecode)");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e3 on (n.level3=e3.ecode)");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e4 on (n.level4=e4.ecode)");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e5 on (n.level5=e5.ecode)");
		sb.append(" inner join "); 
		sb.append(fmsDbRef);sb.append(".netmst m on (m.netid=n.netid and m.level=:level and m.deldate='0000-00-00')");
		sb.append(" where level1 not like 'V%' order by zm,rm,state,afm,psr");
		
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("level", level);
		List<Object[]> list = query.getResultList();
		return list;
	}
public List<Object[]> getSelEmpHierarchy(String fmsDbRef,String loginId,String level,String selEmp,String yr,String mth) {
		
		StringBuffer sb=new StringBuffer();
		sb.append("select n.level1,n.netid,m.hname,e1.ename as PSR,e2.ename as AFM,e3.ename as STATE,e4.ename as RM,e5.ename as ZM,e1.jnDate from ");
		sb.append(fmsDbRef);sb.append(".nettrans n ");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e1 on (n.level1=e1.ecode and n.level1 in ("+selEmp+"))");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e2 on (n.level2=e2.ecode)");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e3 on (n.level3=e3.ecode)");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e4 on (n.level4=e4.ecode)");
		sb.append(" inner join ");
		sb.append(fmsDbRef);sb.append(".empmst e5 on (n.level5=e5.ecode)");
		sb.append(" inner join "); 
		sb.append(fmsDbRef);sb.append(".netmst m on (m.netid=n.netid and m.level=:level and m.deldate='0000-00-00')");
		sb.append(" where n.yr=:yr and n.mth=:mth and level1 not like 'V%' order by zm,rm,state,afm,psr");
		
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("level", level);
		query.setParameter("yr", yr);
		query.setParameter("mth", mth);
		
		List<Object[]> list = query.getResultList();
		return list;
	}

	public List<Object[]> getEmpDetails(String userName, String password,String fmsDbRef,boolean superpwd){
		String addQ="";
		if(!superpwd)
			addQ=" and e.password =:pwd ";
		
		String sqlQuery = " select distinct e.ecode, e.ename,e.etype,t.RGrpNo from "+fmsDbRef+".empmst as e "+
				  		  " Inner Join "+fmsDbRef+".emptrans as t on e.ecode = t.ecode "+
				  		  " where ((e.cocode =:ecode and etype < '7') or (e.ecode =:ecode and etype >= '7')) "+addQ+"  and e.resdate = '0000-00-00' and e.deldate = '0000-00-00' ";
	    System.out.println("getEmpDetails : "+sqlQuery);
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("ecode",userName);
		if(!superpwd)
		query.setParameter("pwd", password);
		List<Object[]> list = query.getResultList();
		return list;	
	}
	
	public HashMap<Integer,String> getDivIdDesc() {
		String sql="select divId,divName from div_master";
		Query query = entityManager.createNativeQuery(sql);
		List<Object[]> list = query.getResultList();
		HashMap<Integer,String> hm=new HashMap<Integer,String>();
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				hm.put(((Integer)object[0]), (String)object[1]);
			}
		}
		return hm;
	}
	public HashMap<String,String> getLevelDesc(String fmsDbRef) {
		String sql="select svarname,svartxt from "+fmsDbRef+".sysprm where svarname like 'level%desc'";
		Query query = entityManager.createNativeQuery(sql);
		List<Object[]> list = query.getResultList();
		HashMap<String,String> hm=new HashMap<String,String>();
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				hm.put(((String)object[0]), (String)object[1]);
			}
		}
		return hm;
	}
	
	
	public List<Object[]> getReviewEmployeeList(String year, String month, String hqRef, String level, String fmsDbRef,
			String dataReqLvl,String emp,int divId){

		StringBuilder sb = new StringBuilder();
		sb.append(" Select distinct emp.ename, t1.netId, t1.level1, netmst.hname, netmst.level,emp.cocode");
		if(Integer.parseInt(level)>2){
			sb.append(",ManagerId");
		}
		sb.append(" from "+fmsDbRef+".netmst as netmst");  
		sb.append(" Inner Join ");
		sb.append(fmsDbRef);sb.append(".nettrans t1 ON netmst.netId = t1.netId and netmst.level =:lvl");
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7)
		sb.append(" and t1.level"+level+"=:emp");
		sb.append(" LEFT JOIN "+fmsDbRef+".NETTRANS t2 ON t1.netid = t2.netid AND t1.yr=t2.yr and t1.mth=t2.mth and "); //to get max joineddate person only
		sb.append(" ((t1.joindate < t2.joindate) OR (t1.joindate = t2.joindate AND t1.netid < t2.netid))  ");
		sb.append(" Inner Join ");
		sb.append(" "+fmsDbRef+".empmst as emp on  t1.level1 = emp.ecode and emp.resdate='0000-00-00' ");
		sb.append(" left join MgrEffectivenessParams m on (m.netid=t1.netid and m.ecode=t1.level1  AND m.yr=t1.yr and m.mth=t1.mth and m.divid=:divId) ");
		sb.append(" Where t1.yr =:year and t1.mth =:month and netmst.hname like '%("+hqRef+")%' and t2.netid is null and ");
		if(Integer.parseInt(level)>2){
			sb.append(" MgrId2 is null");
		}else if(Integer.parseInt(level)==2){
			sb.append(" ManagerId is null and MgrId2 is null"); //if mgr2 has already reviewed then mgr1 should not review.
		}
				//+ " t1.level1 not in (select distinct(ecode) from MgrEffectivenessParams where yr=:year and mth=:month and divid=:divId)");
		sb.append(" order by ename ");

		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.setParameter("lvl", dataReqLvl);
		query.setParameter("divId", divId);
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7)
			query.setParameter("emp", emp);	
		System.out.println("getEmployeeList : "+sb.toString());
		List<Object[]> list = query.getResultList();
		return list;
	}
	
	
	public List<Object[]> getEmployeeListForReviewStatus(String year, String month, String hqRef, String level, String fmsDbRef,
			String dataReqLvl,String emp,int divId){

		StringBuilder sb = new StringBuilder();
		sb.append("select t1.level1,emp.ename,hname,m.managerId,m.mgrId2,adddate,moddate,c.comments ");
		sb.append(" from "+fmsDbRef+".netmst nm inner join ");  
		sb.append(fmsDbRef);sb.append(".nettrans t1 ON nm.netId = t1.netId and nm.level =:lvl and t1.yr=:yr and t1.mth=:mth and nm.hname like '%("+hqRef+")%'");
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7)
			sb.append(" and t1.level"+level+"=:emp");
		sb.append(" LEFT JOIN "+fmsDbRef+".NETTRANS t2 ON t1.netid = t2.netid AND t1.yr=t2.yr and t1.mth=t2.mth and "); //to get max joineddate person only
		sb.append(" ((t1.joindate < t2.joindate) OR (t1.joindate = t2.joindate AND t1.netid < t2.netid))  ");
		sb.append(" Inner Join ");
		sb.append(" "+fmsDbRef+".empmst as emp on  t1.level1 = emp.ecode and emp.resdate='0000-00-00' ");
		sb.append(" left join MgrEffectivenessParams m on (m.netid=t1.netid and m.ecode=t1.level1  AND m.yr=t1.yr and m.mth=t1.mth and m.divid=:divId) "); 
		sb.append(" left join psrcomments c on (c.yr=t1.yr and c.mth=t1.mth  and c.ecode=t1.level1 and c.divId=:divId) ");
		sb.append(" Where t2.netid is null"); 
		sb.append(" order by t1.level4,t1.level2,ename ");

		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("yr", year);
		query.setParameter("mth", month);
		query.setParameter("lvl", dataReqLvl);
		query.setParameter("divId", divId);
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7)
			query.setParameter("emp", emp);	
		System.out.println("getEmployeeList : "+sb.toString());
		List<Object[]> list = query.getResultList();
		return list;
	}
	
	public List<Object[]> getAppraisedEmployees(String hqref,String yr,String mth,String dataReqLvl,int divId,String emp,
			String fmsDbRef,String level){
		
		StringBuilder sb = new StringBuilder();
		sb.append(" Select distinct  t1.netId, t1.level1,emp.ename,netmst.hname,emp.cocode,emp.jndate,emp2.ename as FmName,emp.grade ");
		sb.append(" from "+fmsDbRef+".netmst as netmst ");
		sb.append(" Inner Join "+fmsDbRef+".nettrans t1 ON netmst.netId = t1.netId and netmst.level =:lvl");
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7){
			sb.append(" and level"+level+"=:emp");
		}
		sb.append(" Inner Join  "+fmsDbRef+".empmst as emp on  t1.level1 = emp.ecode and emp.resdate='0000-00-00'");
		sb.append(" Inner Join  "+fmsDbRef+".empmst as emp2 on  t1.level2 = emp2.ecode");
		sb.append(" inner join wap_final_scoreandincrement m on (m.netid=t1.netid and m.ecode=t1.level1  AND m.yr=t1.yr and m.mth=t1.mth and m.divid=:divId) ");
		sb.append(" Where t1.yr =:yr and t1.mth =:mth and netmst.hname like '%("+hqref+")%' order by emp.ename");
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("yr", yr);
		query.setParameter("mth", mth);
		query.setParameter("lvl", dataReqLvl);
		query.setParameter("divId", divId);
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7)
			query.setParameter("emp", emp);
		System.out.println("getAppraisedEmployees : "+sb.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		return list;
	}
	
	public List<Object[]> getAppraisedEmpDtls(String yr,String mth,int divId,String emp,
			String fmsDbRef,String fmCode){
		
		StringBuilder sb = new StringBuilder();
		sb.append(" Select emp.cocode,emp.jndate, emp2.ename as FmName,emp.grade from ");
		sb.append(" wap_final_scoreandincrement w");
		sb.append(" inner join "+fmsDbRef+".empmst as emp on  (emp.ecode=w.ecode and emp.resdate='0000-00-00')");
		sb.append(" Inner Join  "+fmsDbRef+".empmst as emp2 on emp2.ecode=:fmCode ");
		sb.append(" Where w.yr =:yr and w.mth =:mth and w.divid=:divId and w.ecode=:ecode ");
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("yr", yr);
		query.setParameter("mth", mth);
		query.setParameter("divId", divId);
		query.setParameter("ecode", emp);
		query.setParameter("divId", divId);
		query.setParameter("fmCode", fmCode);

		System.out.println("getAppraisedEmployees for level 1: "+sb.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		return list;
	}
	
	public List<Object[]> getReviewedEmpList(String year, String month, String hqRef, String level, String fmsDbRef,
			String dataReqLvl,String emp,int divId){

		StringBuilder sb = new StringBuilder();
		sb.append(" Select distinct emp.ename, netmst.netId,m.ecode, netmst.hname, netmst.level,emp.cocode");
		if(Integer.parseInt(level)>2 && Integer.parseInt(level)<5){
			sb.append(",ManagerId");
		}else if(Integer.parseInt(level)>4){
			sb.append(",ManagerId,emp2.ename as mgr1,MgrId2,emp3.ename as mgr2");
		}
		sb.append(" from "+fmsDbRef+".netmst");  
		sb.append(" inner join MgrEffectivenessParams m on (m.netid=netmst.netid and  m.yr=:year and m.mth=:month and m.divid=:divId) ");
		sb.append(" inner join "+fmsDbRef+".empmst as emp on (emp.ecode=m.ecode and emp.resdate='0000-00-00') ");
		sb.append(" left join "+fmsDbRef+".empmst as emp2 on (emp2.ecode=m.ManagerId)");
		sb.append(" left join "+fmsDbRef+".empmst as emp3 on (emp3.ecode=m.MgrId2)");
		
		sb.append(" Where netmst.hname like '%("+hqRef+")%' ");
		if(Integer.parseInt(level)>2 && Integer.parseInt(level)<6){
			sb.append(" and MgrId2 =:emp ");
		}else if(Integer.parseInt(level)==2){
			sb.append(" and ManagerId =:emp "); //if mgr2 has already reviewed then mgr1 should not review.
		}else{
			sb.append(" and (ManagerId is not null or MgrId2 is not null) ");
		}
		sb.append(" order by emp.ename ");

		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("year", year);
		query.setParameter("month", month);
		//query.setParameter("lvl", dataReqLvl);
		query.setParameter("divId", divId);
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7)
			query.setParameter("emp", emp);	
		System.out.println("getEmployeeList : "+sb.toString());
		List<Object[]> list = query.getResultList();
		return list;
	}
	
	public List<Object[]> getAppraisedEmpDtls2(String yr,String mth,int divId,String emp,String fmsDbRef,String hqref,
			String level ){
		StringBuilder sb = new StringBuilder();
		sb.append(" Select t1.netId, t1.level1,emp.ename,netmst.hname,emp.cocode,emp.jndate,emp.grade ");
		sb.append(" from "+fmsDbRef+".netmst ");
		sb.append(" Inner Join "+fmsDbRef+".nettrans t1 ON netmst.netId = t1.netId and netmst.level ='1'");
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7){
			sb.append(" and level"+level+"=:emp");
		}
		sb.append(" Inner Join  "+fmsDbRef+".empmst as emp on  t1.level1 = emp.ecode and emp.resdate='0000-00-00'");
		sb.append(" inner join wap_final_scoreandincrement m on (m.netid=t1.netid and m.ecode=t1.level1  AND m.yr=t1.yr and m.mth=t1.mth and m.divid=:divId) ");
		sb.append(" Where t1.yr =:yr and t1.mth =:mth and netmst.hname like '%("+hqref+")%' order by emp.ename");
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("yr", yr);
		query.setParameter("mth", mth);
		query.setParameter("divId", divId);
		if(level!=null && !level.equalsIgnoreCase("") && Integer.parseInt(level)<7)
			query.setParameter("emp", emp);
		System.out.println("getAppraisedEmployees : "+sb.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> list = query.getResultList();
		return list;
	}
	
}
