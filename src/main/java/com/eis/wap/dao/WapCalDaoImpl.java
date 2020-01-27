package com.eis.wap.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import com.eis.wap.constants.CommonConstants;
import com.eis.wap.model.Stockist;
import com.eis.wap.model.WapFinalScoreAndIncrement;

public class WapCalDaoImpl implements WapCalDaoCustom {

	@Autowired
	EntityManager entityManager;

	public Long checkPrimarySalesForYrMth(String yrmth,String salesDbRef)
	{
		String sqlQuery ="select if(sum(value)=null,0,sum(value)) from "+salesDbRef+".sales where yrmth=:yrmth and type='s'";
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("yrmth", yrmth);
		Number obj = (Number)query.getSingleResult();
		long sum = 0;
		if(obj != null){
			sum=obj.longValue();
			//sum = Long.parseLong(String.valueOf(object[0]));
		}
		return sum;
	}	

	public List<Stockist> stockistsNotPresentInAllocationTbl(String yrmth,String logYrMth,String yr,String mth,String salesDbRef)
	{
		//whether stockist is present in allocation table or not 
		/*select distinct s1.stcode,stname,state from (select distinct stcode from "+salesDbRef+".sales s1 where s1.yrmth=:yrmth) as s1
		left join "+salesDbRef+".allocation a on  (a.stcode=s1.stcode and a.yrmth=:logYrMth)
		inner join A.stockmst s2 on (s1.stcode=s2.stcode and s2.deldate='0000-00-00')
		where a.stcode is null;*/
		
		String sqlQuery = "select distinct s1.stcode,stname,state from (select distinct stcode from "+salesDbRef+".sales s1 where s1.yrmth=:yrmth  and type='s') as s1 " +
				" left join "+salesDbRef+".allocation a on  (a.stcode=s1.stcode and a.yrmth=:logYrMth) " +
				" inner join "+salesDbRef+".stockmst s2 on (s1.stcode=s2.stcode and s2.deldate='0000-00-00') "+ //and (s2.deldate='0000-00-00'  or ((year(deldate) <=:yr ) and (month(deldate) <=:mth)) bcz sometimes we get sales of deleted stockist too and our system takes it.
				" where a.stcode is null ";
		System.out.println("stockistsNotPresentInAllocationTbl : "+sqlQuery);
		Query query = entityManager.createNativeQuery(sqlQuery);
		query.setParameter("yrmth", yrmth);
		query.setParameter("logYrMth", logYrMth);
		List<Object[]> list = query.getResultList();
		List<Stockist> stList = new ArrayList<Stockist>();
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				Stockist stObj = new Stockist();
				stObj.setStCode((String)object[0]);
				stObj.setStName((String)object[1]);
				stObj.setState((String)object[2]);
				stList.add(stObj);	
			}
		}
		return stList;				
	}

	public List<Stockist> checkStockistsAllocationNotHundredPerc(String yrmth,String logYrMth,String salesDbRef)
	{
		/*
		 * ii. check for 100% allocation		  * */
		String sql="select a.stcode,s.stname,sum(a.perc) as perc "
				+ "from (select distinct(stcode) from "+salesDbRef+".sales where yrmth =:yrmth and type='s') as ss "
				+ "inner join "+salesDbRef+".allocation a on (ss.stcode=a.stcode and a.yrmth =:logYrMth) "
				+ "inner join "+salesDbRef+".stockmst s on (s.stcode=a.stcode) " 
				+ "group by a.stcode having perc not like '100.00'";

		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("yrmth", yrmth);
		query.setParameter("logYrMth", logYrMth);
		System.out.println("query : "+query);
		List<Object[]> list = query.getResultList();
		List<Stockist> stList = new ArrayList<Stockist>();
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				Stockist stObj = new Stockist();
				stObj.setStCode((String)object[0]);
				stObj.setStName((String)object[1]);
				stObj.setPerc((Double)object[2]);
				stList.add(stObj);	
			}
		}
		return stList;
	}
	public LinkedHashMap<String, String> getRecordsFromSysprmInMap(String dbref,String svarname,String delimiter,int arrReq){ 
		/*@arrReq=> which part of array is required after splitting string.
		 * @delimiter => if splitting is required the specify the delimiter else ""*/
		
		String sql="select svarname,svartxt from "+dbref+".sysprm where svarname like '"+svarname+"' ";
		Query query = entityManager.createNativeQuery(sql);
		//query.setParameter("svarname", svarname);
		System.out.println("getRecordsFromSysprmInMap : "+query);
		List<Object[]> list = query.getResultList();
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		if(null != list && !list.isEmpty()){
			if(delimiter!=null && !delimiter.equalsIgnoreCase("")){
				String key="";
				for(Object[] object : list){
					key=((String)object[0]).split(delimiter)[arrReq];
					map.put(key,(String)object[1]);
				}
			}else{
				for(Object[] object : list){
					map.put((String)object[0],(String)object[1]);
				}
			}
		}
		return map;
	}
	@Modifying  
	@Transactional
	public void createTempNetidwithMaxjoinDate(String loginid,String mth,String yr,String hqref,String fmsDbRef){
		StringBuffer sb=new StringBuffer();
		sb.append("drop table if exists tempNetidWithMaxJnDate"+loginid);
		Query query = entityManager.createNativeQuery(sb.toString());
		query.executeUpdate();
		sb.delete(0, sb.length());
		sb.append("create table tempNetidWithMaxJnDate"+loginid+""); 
		sb.append(" Select n.NetID, Max(JoinDate) As JoinDate From "+fmsDbRef+".NetTrans n");
		sb.append(" inner join "+fmsDbRef+".netmst nm on (n.netid=nm.netid and level='1')");
		sb.append(" Where Yr = '"+yr+"' And Mth = '"+mth+"' ");
		sb.append(" and nm.hname like '%("+hqref+")%' and nm.deldate='0000-00-00' group By NetID");
		query = entityManager.createNativeQuery(sb.toString());
		query.executeUpdate();
		sb.delete(0, sb.length());
		sb.append("alter table tempNetidWithMaxJnDate"+loginid+" add index ind1(Netid,JoinDate)");
		query = entityManager.createNativeQuery(sb.toString());
		query.executeUpdate();
	}
	@Modifying  
	@Transactional
	public Integer deleteExistingrecords(String tblName,String yrmth,int divId){
		String sql = "delete from "+tblName+" where concat(yr,mth)>=:yrmth and divId=:divId";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("yrmth",yrmth);
		query.setParameter("divId",divId);
		Integer rows=query.executeUpdate();
		return rows;
	}
	@Modifying  
	@Transactional
	public void insertIntoWapSalesAchvmtPrimSale(String yrmth,String salesDbRef,String finYr,int finMthNo,String yr,String mth,
			int primSalesAchParamId,String loginId,int divId){
		String sql = "insert into WapSalesAchvmt select s.netid,n.level1,'"+yr+"','"+mth+"',"+primSalesAchParamId+",sale"+finMthNo+"cs,sale"+finMthNo+"ct,"
				+ "if((sale"+finMthNo+"ct<=0) or ((sale"+finMthNo+"cs<=0)) ,0,round(((sale"+finMthNo+"cs/sale"+finMthNo+"ct)*100),2)) as salespercent,'"+divId+"' from "
				+ salesDbRef+".salessummary"+finYr+" s inner join tempNetidWithMaxJnDate"+loginId+" t on (t.netid=s.netid)"
				+" inner join "+ salesDbRef+".nettrans n on (n.netid=t.netid and n.joindate=t.joindate)";
		System.out.println("insertIntoWapSalesAchvmtPrimSale : "+sql);
		Query query = entityManager.createNativeQuery(sql);
		query.executeUpdate();
	}
	
	@Modifying  
	@Transactional
	public void insertIntoWapSalesAchvmtPBSale(String yrmth,String salesDbRef,String finYr,int finMthNo,String fmsDbRef,String yr,
			String mth,int poSalesAchParamId,String loginId,int divId){
		StringBuffer sb = new StringBuffer();
		sb.append("insert into WapSalesAchvmt select s.netid,n.level1,'"+yr+"','"+mth+"',"+poSalesAchParamId+",");
		sb.append("sum(s.sale"+finMthNo+"cvs),sum(s.sale"+finMthNo+"cvt), ");
		sb.append("if((sum(sale"+finMthNo+"cvt)<=0) or ((sum(sale"+finMthNo+"cvs)<=0)) ,0,round(((sum(sale"+finMthNo+"cvs)/sum(sale"+finMthNo+"cvt))*100),2)) as salespercent,'"+divId+"' from ");
		sb.append(" tempNetidWithMaxJnDate"+loginId+" t  ");
		sb.append(" inner join ");
		sb.append(salesDbRef);
		sb.append(".salessummaryproductwise"+finYr+" s on (t.netid=s.netid) ");
		sb.append(" inner join "+ salesDbRef+".nettrans n on (n.netid=t.netid and n.joindate=t.joindate)");
		sb.append(" inner join "+fmsDbRef+".prodmst p on (powerbrand='Y' and s.prodid=p.prodid and p.deldate='0000-00-00') group by netid");
		System.out.println("insertIntoWapSalesAchvmtPBSale : "+sb.toString());
		Query query = entityManager.createNativeQuery(sb.toString());
		query.executeUpdate();
	}
	
	@Modifying  
	@Transactional
	public void calDocReachPerc(String salesDbRef,String fmsDbRef,String yr,String mth,int docReachAchParamId,String totDocClassWiseHeaders,String loginid,
			int divId,String docMetClassWiseHeaders){
		StringBuffer sb=new StringBuffer();
		System.out.println("--------calDocReachPerc----------");
		sb.append(" insert into WapOtherParamsAchvmt ");
		sb.append(" select s.netid,n.level1,s.yr,s.mth,'"+docReachAchParamId+"', ");
        sb.append(" ("+docMetClassWiseHeaders+") AS TOTDOCSMET,");//C_TOTAL_A_CLASS_DOCTORS_MET+C_TOTAL_B_CLASS_DOCTORS_MET+C_TOTAL_C_CLASS_DOCTORS_MET+C_TOTAL_D_CLASS_DOCTORS_MET+(if(o1.rchdDocs is not null,o1.rchdDocs,0)+(if(o2.rchdDocs is not null,o2.rchdDocs,0))
		sb.append(" ("+totDocClassWiseHeaders+") AS TOTDOCS,"); //+C_TOTAL_D_CLASS_DOCTORS+(if(o1.totdocs is not null,o1.totdocs,0))+(if(o2.totdocs is not null,o2.totdocs,0))
		sb.append(" round(((("+docMetClassWiseHeaders+")");//C_TOTAL_A_CLASS_DOCTORS_MET+C_TOTAL_B_CLASS_DOCTORS_MET+C_TOTAL_C_CLASS_DOCTORS_MET ");
														   //sb.append(" +C_TOTAL_D_CLASS_DOCTORS_MET+(if(o1.rchdDocs is not null,o1.rchdDocs,0))+(if(o2.rchdDocs is not null,o2.rchdDocs,0))
		sb.append(" /("+totDocClassWiseHeaders+"))*100),2) as AcheivementPerc,'"+divId+"' ");//C_TOTAL_A_CLASS_DOCTORS+C_TOTAL_B_CLASS_DOCTORS+C_TOTAL_C_CLASS_DOCTORS+C_TOTAL_D_CLASS_DOCTORS+(if(o1.totdocs is not null,o1.totdocs,0)) "); 
																	//sb.append(" +(if(o2.totdocs is not null,o2.totdocs,0))
		sb.append(" FROM tempNetidWithMaxJnDate"+loginid+" t");
		sb.append(" inner join "+fmsDbRef+".nettrans n on (n.netid=t.netid and n.joindate=t.joindate)");
		sb.append(" inner join "+fmsDbRef+".SUMMARY s on (s.netid=n.netid and s.emp=n.level1)");
		//sb.append(" left join otherclassdocrchandcov o1 on (o1.netid=n.netid and o1.ecode=n.level1 and o1.yr=n.yr  and o1.mth=n.mth and o1.docclass='E' )");
		//sb.append(" left join otherclassdocrchandcov o2 on (o2.netid=n.netid and o2.ecode=n.level1 and o2.yr=n.yr  and o2.mth=n.mth and o2.docclass='F' )");
		sb.append(" WHERE s.YR=:yr and s.MTH=:mth ORDER BY s.netid");
		Query query = entityManager.createNativeQuery(sb.toString());
		System.out.println("calDocReachPerc : "+sb.toString());
		query.setParameter("yr",yr);
		query.setParameter("mth",mth);
		query.executeUpdate();	
	}
	@Modifying  
	@Transactional
	public void calPulseChemReachPerc(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int pulseChemAchParamId,
			String docVisitedClassWiseHeaders,String totDocClassWiseHeaders,String loginid,int divId,String logYr,String logMth){
		/*below query will get only the pulse chemist reached by PSR*/
		StringBuffer sb=new StringBuffer(); //commented below query as pulscd2 is not required in calculation : rajeev sir : 23/05/2019
		/*sb.append(" insert into WapOtherParamsAchvmt(netid,emp,yr,mth,paramId,calculatedValue,divId) ");
		sb.append(" select netid,emp,'"+yr+"','"+mth+"','"+pulseChemAchParamId+"',count(cntcd),'"+divId+"' from ( ");
		sb.append(" select netid,emp,yr,mth,cntcd from( ");
		sb.append(" select h.netid,h.emp,n.yr,n.mth,d.cntcd  from tempNetidWithMaxJnDate"+loginid+" t");
		sb.append(" inner join palsons1.nettrans n on (n.netid=t.netid and n.joindate=t.joindate   )");//and n.yr=:yr and n.mth=:mth
		sb.append(" inner join palsons1.dcrh h on (h.netid=n.netid and h.emp=n.level1 and (h.dcrdate>=:startOfMth and h.dcrdate<=:endOfMth) and h.confdate!='0000-00-00')  ");
		sb.append(" inner join palsons1.dcrd d on (d.dcrno=h.dcrno and d.custflg='C') ");
		sb.append(" inner join palsons1.docoth d1 on (d1.pulscd=d.cntcd and d1.netid=h.netid and d1.pulscd!='') ");
		sb.append(" inner join palsons1.doched on (doched.netid=d1.netid and d1.cntcd=doched.cntcd  and (doched.deldate='0000-00-00' OR  ");
		sb.append(" CONCAT(SUBSTRING(doched.DELDATE,1,4),SUBSTRING(doched.DELDATE,6,2))>=:yrmth )) ");
		sb.append(" inner join palsons1.stoched on (d1.netid=stoched.netid and d1.pulscd=stoched.cntcd) ");
		sb.append(" where (stoched.deldate='0000-00-00' OR CONCAT(SUBSTRING(stoched.DELDATE,1,4),SUBSTRING(stoched.DELDATE,6,2))>=:yrmth )  ");
		sb.append(" group by netid,emp,d.cntcd ");
		sb.append(" union ");
		sb.append(" select h.netid,h.emp,n.yr,n.mth,d.cntcd  from tempNetidWithMaxJnDate"+loginid+" t  ");
		sb.append(" inner join palsons1.nettrans n on (n.netid=t.netid and n.joindate=t.joindate  ) ");//and n.yr=:yr and n.mth=:mth
		sb.append(" inner join palsons1.dcrh h on (h.netid=n.netid and h.emp=n.level1 and (h.dcrdate>=:startOfMth and h.dcrdate<=:endOfMth) and h.confdate!='0000-00-00') ");
		sb.append(" inner join palsons1.dcrd d on (d.dcrno=h.dcrno and d.custflg='C') ");
		sb.append(" inner join palsons1.docoth d1 on (d1.pulscd2=d.cntcd and d1.netid=h.netid and d1.pulscd2!='') ");
		sb.append(" inner join palsons1.doched on (doched.netid=d1.netid and d1.cntcd=doched.cntcd  and (doched.deldate='0000-00-00' OR ");
		sb.append(" CONCAT(SUBSTRING(doched.DELDATE,1,4),SUBSTRING(doched.DELDATE,6,2))>=:yrmth )) ");
		sb.append(" inner join palsons1.stoched on (d1.netid=stoched.netid and d1.pulscd2=stoched.cntcd) ");
		sb.append(" where (stoched.deldate='0000-00-00' OR CONCAT(SUBSTRING(stoched.DELDATE,1,4),SUBSTRING(stoched.DELDATE,6,2))>=:yrmth ) ");
		sb.append(" group by netid,emp,d.cntcd) as C group by netid,cntcd ) as D group by netid");*/
		sb.append(" insert into WapOtherParamsAchvmt(netid,emp,yr,mth,paramId,calculatedValue,divId) ");
		sb.append(" select netid,emp,'"+yr+"','"+mth+"','"+pulseChemAchParamId+"',count(cntcd),'"+divId+"' from ");
		sb.append(" (  select h.netid,h.emp,n.yr,n.mth,d.cntcd ");  
		sb.append(" from tempNetidWithMaxJnDate"+loginid+" t "); 
		sb.append(" inner join "+fmsDbRef+".nettrans n on (n.netid=t.netid and n.joindate=t.joindate and yr=:logYr and mth=:logMth  ) ");
		sb.append(" inner join "+fmsDbRef+".dcrh h on (h.netid=n.netid and h.emp=n.level1 and (h.dcrdate>=:startOfMth and h.dcrdate<=:endOfMth) and h.confdate!='0000-00-00') ");
		sb.append(" inner join "+fmsDbRef+".dcrd d on (d.dcrno=h.dcrno and d.custflg='C')   ");
		sb.append(" inner join "+fmsDbRef+".docoth d1 on (d1.pulscd=d.cntcd and d1.netid=h.netid and d1.pulscd!='')");
		sb.append(" inner join "+fmsDbRef+".doched on (doched.netid=d1.netid and d1.cntcd=doched.cntcd   ");
		sb.append(" and (doched.deldate='0000-00-00' OR   CONCAT(SUBSTRING(doched.DELDATE,1,4),SUBSTRING(doched.DELDATE,6,2))>=:yrmth ))");
		sb.append(" inner join "+fmsDbRef+".stoched on (d1.netid=stoched.netid and d1.pulscd=stoched.cntcd)   ");
		sb.append(" where (stoched.deldate='0000-00-00' OR CONCAT(SUBSTRING(stoched.DELDATE,1,4),SUBSTRING(stoched.DELDATE,6,2))>=:yrmth ) ");
		sb.append(" group by netid,emp,d.cntcd ) as D group by netid ");
		Query query = entityManager.createNativeQuery(sb.toString());
		/*query.setParameter("yr",yr);
		query.setParameter("mth",mth);*/
		query.setParameter("yrmth",yrmth);
		query.setParameter("startOfMth",yr+"-"+mth+"-01");
		query.setParameter("endOfMth",yr+"-"+mth+"-31");
		query.setParameter("logYr",logYr);
		query.setParameter("logMth",logMth);
		System.out.println("calPulseChemReachPerc : "+sb.toString());
		query.executeUpdate();

		getTotalPulseChem(yrmth,salesDbRef,fmsDbRef,yr,mth,pulseChemAchParamId,docVisitedClassWiseHeaders,totDocClassWiseHeaders,loginid,logYr,logMth);
		updateTotalPulseChem(yrmth,salesDbRef,fmsDbRef,yr,mth,pulseChemAchParamId,loginid,divId);
	}
	@Modifying  
	@Transactional
	public void getTotalPulseChem(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int pulseChemAchParamId,
			String docVisitedClassWiseHeaders,String totDocClassWiseHeaders,String loginid,String logYr,String logMth){
		
		//sb.append(" drop table if exists TotPulseChem"+loginid+" "); 
		Query query = entityManager.createNativeQuery("drop table if exists TotPulseChem"+loginid);
		query.executeUpdate();
		//sb.delete(0, sb.length());
		StringBuffer sb=new StringBuffer();
		sb.append("create table TotPulseChem"+loginid);
		/*sb.append(" SELECT netid,ecode,cntcd,pulscd,count(pulscd) AS COUNT_TOTAL FROM(");
		sb.append(" SELECT netid,cntcd,pulscd,ecode FROM(");
		sb.append(" SELECT d.netid,d.cntcd,n.level1 as ecode,d.pulscd");
		sb.append(" from tempNetidWithMaxJnDate"+loginid+" t,palsons1.nettrans n,palsons1.DOCOTH d,palsons1.DOCHED dh,palsons1.stoched s ");
		sb.append(" where (n.netid=t.netid and n.joindate=t.joindate )");//and n.yr=:yr and n.mth=:mth
		sb.append(" and (dh.netid=n.netid and (dh.deldate='0000-00-00' OR CONCAT(SUBSTRING(dh.DELDATE,1,4),SUBSTRING(dh.DELDATE,6,2))>=:yrmth))");
		sb.append(" and (d.netid=dh.netid and d.cntcd=dh.cntcd and d.pulscd!='' )  and ((s.cntcd=d.pulscd and s.netid=d.netid )");
		sb.append(" and (s.deldate='0000-00-00' OR CONCAT(SUBSTRING(s.DELDATE,1,4),SUBSTRING(s.DELDATE,6,2))>=:yrmth)) ");
		sb.append(" and pulscd!='' group by netid,pulscd");
		sb.append(" UNION ALL ");
		sb.append(" SELECT d.netid,d.cntcd,n.level1 as ecode,d.pulscd2 as pulscd");
		sb.append(" from tempNetidWithMaxJnDate"+loginid+" t,palsons1.nettrans n,palsons1.DOCOTH d,palsons1.DOCHED dh,palsons1.stoched s");
		sb.append(" where (n.netid=t.netid and n.joindate=t.joindate  ) and "); //and n.yr=:yr and n.mth=:mth
		sb.append(" (dh.netid=n.netid and (dh.deldate='0000-00-00' OR CONCAT(SUBSTRING(dh.DELDATE,1,4),SUBSTRING(dh.DELDATE,6,2))>=:yrmth))");
		sb.append(" and (d.netid=dh.netid and d.cntcd=dh.cntcd and d.pulscd2!='' )");
		sb.append(" and ((s.cntcd=d.pulscd and s.netid=d.netid ) and (s.deldate='0000-00-00' OR CONCAT(SUBSTRING(s.DELDATE,1,4),SUBSTRING(s.DELDATE,6,2))>=:yrmth))");
		sb.append(" and pulscd2!='' group by netid,pulscd2) ");
		sb.append(" as A GROUP BY netid,pulscd )");
		sb.append(" as B group by netid ");*/
		sb.append(" SELECT netid,ecode,cntcd,pulscd,count(pulscd) AS COUNT_TOTAL FROM( ");
		sb.append(" SELECT netid,cntcd,pulscd,ecode FROM( ");
		sb.append(" SELECT d.netid,d.cntcd,n.level1 as ecode,d.pulscd ");
		sb.append(" from tempNetidWithMaxJnDate"+loginid+" t,"+fmsDbRef+".nettrans n,"+fmsDbRef+".DOCOTH d,"+fmsDbRef+".DOCHED dh,"+fmsDbRef+".stoched s ");
		sb.append(" where (n.netid=t.netid and n.joindate=t.joindate and yr=:logYr and mth=:logMth ) ");
		sb.append(" and (dh.netid=n.netid and (dh.deldate='0000-00-00' OR CONCAT(SUBSTRING(dh.DELDATE,1,4),SUBSTRING(dh.DELDATE,6,2))>=:yrmth)) ");
		sb.append(" and (d.netid=dh.netid and d.cntcd=dh.cntcd and d.pulscd!='' )  and ((s.cntcd=d.pulscd and s.netid=d.netid ) ");
		sb.append(" and (s.deldate='0000-00-00' OR CONCAT(SUBSTRING(s.DELDATE,1,4),SUBSTRING(s.DELDATE,6,2))>=:yrmth)) ");
		sb.append(" and pulscd!='' group by netid,pulscd) as a) ");
		sb.append(" as B group by netid ");
		System.out.println("getTotalPulseChem : "+sb.toString());
		query = entityManager.createNativeQuery(sb.toString());
		/*query.setParameter("yr",yr);
		query.setParameter("mth",mth);*/
		query.setParameter("yrmth",yrmth);
		query.setParameter("logYr",logYr);
		query.setParameter("logMth",logMth);
		query.executeUpdate();
	}
	
	@Modifying  
	@Transactional
	public void updateTotalPulseChem(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int pulseChemAchParamId,String loginid,int divId){
		String sql="update WapOtherParamsAchvmt w inner join TotPulseChem"+loginid+" t on (w.netid=t.netid and w.emp=t.ecode) "
				+ " set total=if(COUNT_TOTAL=null or COUNT_TOTAL<=0,0,COUNT_TOTAL),"
				+ "achievementPerc=(if(calculatedValue<=0 or COUNT_TOTAL<=0,0,round((calculatedValue/COUNT_TOTAL)*100,2)))  "
				+ " where paramid='"+pulseChemAchParamId+"' and mth=:mth and yr=:yr and divId=:divId";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("yr",yr);
		query.setParameter("mth",mth);
		query.setParameter("divId",divId);
		int i=query.executeUpdate();
		
		//System.out.println("rows updated 1 : "+i);
		/*sql="update WapOtherParamsAchvmt w inner join TotPulseChem"+loginid+" t on (w.netid=t.netid and w.emp=t.ecode) "
			+ " set achievementPerc=(if(calculatedValue<=0 or total<=0,0,round((calculatedValue/total)*100,2)))  where paramid='"+pulseChemAchParamId+"' and mth=:mth and yr=:yr and divId=:divId";
		query = entityManager.createNativeQuery(sql);
		query.setParameter("yr",yr);
		query.setParameter("mth",mth);
		query.setParameter("divId",divId);*/
		
		/*String sql = " update WapOtherParamsAchvmt w "+
		" inner join (select COUNT_TOTAL,(if(calculatedValue<=0 or COUNT_TOTAL<=0,0,round((calculatedValue/COUNT_TOTAL)*100,2))) as perc,w2.netid,w2.emp "+
		" from WapOtherParamsAchvmt w2 "+
		" inner join TotPulseChemeis t on (w2.netid=t.netid and w2.emp=t.ecode) where paramid='"+pulseChemAchParamId+"' and mth=:mth and yr=:yr and divId=:divId) as temp "+
		" on (w.netid=temp.netid and w.emp=temp.emp) "+
		" set w.total=if(COUNT_TOTAL=null or COUNT_TOTAL<=0,0,COUNT_TOTAL),"+
		" achievementPerc=if(perc=null ,0,perc) "+
		" where paramid='"+pulseChemAchParamId+"' and mth=:mth and yr=:yr and divId=:divId ";
		
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("yr",yr);
		query.setParameter("mth",mth);
		query.setParameter("divId",divId);*/
		
		//int i=query.executeUpdate();
		
		System.out.println("rows updated : "+i);
		/*query = entityManager.createNativeQuery(" drop table if exists TotPulseChem"+loginid+" ");
		query.executeUpdate();*/

	}
	/*@Modifying  
	@Transactional
	public void calPulseChemAchvmntPerc(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int pulseChemAchParamId){
		String sql="update WapOtherParamsAchvmt w set achievementPerc=(if(calculatedValue<=0 or total<=0,0,(calculatedValue/total)*100)) "
				 + "where paramid='"+pulseChemAchParamId+"' and mth=:mth and yr=:yr";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("yr",yr);
		query.setParameter("mth",mth);
		query.executeUpdate();
	}*/
	@Modifying  
	@Transactional
	public void calDocCoveragePerc(String salesDbRef,String fmsDbRef,String yr,String mth,int docCovAchParamId,
			String docVisitedClassWiseHeaders,String totDocClassWiseHeaders,String loginid,int divID){ 
		//TODO get D class coverage.
		System.out.println("docVisitedClassWiseHeaders : "+docVisitedClassWiseHeaders);
		StringBuffer sb=new StringBuffer();
		sb.append("insert into WapOtherParamsAchvmt");
		sb.append(" select s.netid,s.emp,s.yr,s.mth,'"+docCovAchParamId+"',"); 
		sb.append(" ("+docVisitedClassWiseHeaders+" ) AS VISITEDDOCS, "); //(if(o1.covDocs is not null,o1.covDocs,0)) + (if(o2.covDocs is not null,o2.covDocs,0)) + (if(d.covDocs is not null,d.covDocs,0))
		sb.append(" ("+totDocClassWiseHeaders+") AS TOTDOCS, "); //+C_TOTAL_D_CLASS_DOCTORS+(if(o1.totdocs is not null,o1.totdocs,0))+(if(o2.totdocs is not null,o2.totdocs,0))
		sb.append(" ((("+docVisitedClassWiseHeaders+")"); //+ (if(d.covDocs is not null,d.covDocs,0)) + (if(o1.covDocs is not null,o1.covDocs,0)) + (if(o2.covDocs is not null,o2.covDocs,0))
		sb.append(" /("+totDocClassWiseHeaders+"))*100) "); //+C_TOTAL_D_CLASS_DOCTORS+(if(o1.totdocs is not null,o1.totdocs,0))+(if(o2.totdocs is not null,o2.totdocs,0))
		sb.append(" as AcheivementPerc,'"+divID+"' ");
		sb.append(" FROM tempNetidWithMaxJnDate"+loginid+" t ");
		sb.append(" inner join "+fmsDbRef+".nettrans n on (n.netid=t.netid and n.joindate=t.joindate)");
		sb.append(" inner join "+fmsDbRef+".SUMMARY s on (s.netid=n.netid and s.emp=n.level1)");
		//sb.append(" left join otherclassdocrchandcov o1 on (o1.netid=n.netid and o1.ecode=n.level1 and o1.yr=n.yr  and o1.mth=n.mth and o1.docclass='E' )"); //this for E class doc cov
		//sb.append(" left join otherclassdocrchandcov o2 on (o2.netid=n.netid and o2.ecode=n.level1 and o2.yr=n.yr  and o2.mth=n.mth and o2.docclass='F' )"); //this for F class doc cov
		//sb.append(" left join DClassDocCov d on (d.netid=n.netid and d.ecode=n.level1 and d.yr=n.yr  and d.mth=n.mth )"); //this for D class doc cov
		sb.append(" WHERE s.YR=:yr and s.MTH=:mth  ORDER BY s.netid");
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("yr",yr);
		query.setParameter("mth",mth);
		query.executeUpdate();
	}
	@Modifying  
	@Transactional
	public int updateLockMasterTolockMonth(String yrmth,String loginid,Date currdate,int divId){
		String sql="update lockmaster set isDataLocked='Y',lastModBy=:loginid,lastmodDt=:currdate where  yrmth<=:yrmth and isDataLocked!='Y' and divId=:divId";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("loginid",loginid);
		query.setParameter("currdate",currdate);
		query.setParameter("yrmth",yrmth);
		query.setParameter("divId",divId);
		int i=query.executeUpdate();
		return i;
	}

	@Modifying  
	@Transactional
	/*NOTE : vacant ids will not be processed,only present emp's score and rating will be calculated */
	public List<Object[]> getScoreAndIncrement(String yrmth,String salesDbRef,String fmsDbRef,String yr,String mth,int salesParamId,int pobParamId,
			int docRchParamId,int docCovParamId,int pulChmRchParamId,int noOfMEParams,String loginYr,String loginMth,int divId,String loginid){
		/*"select n.netid,n.level1 from tempNetidWithMaxJnDate"+loginId+" t inner join "+fmsDbRef+".nettrans n on (t.netid=n.netid and t.joindate=n.joinDate)"+
			   " where n.yr=:yr and mth=:mth and level1 not like 'V%' ";*/ //TODO not showing error
		StringBuffer sb=new StringBuffer();
		sb.append(" select n.netid,n.level1,if(sales.AcheivementPerc is null,0,sales.AcheivementPerc) as sales,");
		sb.append(" if(pob.acheivementPerc is null,0,pob.acheivementPerc) as POB,if(docrch.achievementPerc is null,0,docrch.achievementPerc) as Docreach,");
		sb.append("	if(docCov.AchievementPerc is null,0,docCov.AchievementPerc) as docCov,if(pulChmRch.achievementPerc is null,0,pulChmRch.achievementPerc) as PulseChemReach,(");
		for(int i=1;i<=noOfMEParams;i++){
			if(i==noOfMEParams)
				sb.append("AchParam"+i);
			else
			sb.append("AchParam"+i+"+");
		}
		sb.append(") as Others from tempNetidWithMaxJnDate"+loginid+" t ");
		sb.append(" inner join "+fmsDbRef+".nettrans n on (t.netid=n.netid and t.joindate=n.joinDate and n.yr=:loginYr and n.mth=:loginMth and level1 not like 'V%')");
		sb.append(" left join WapSalesAchvmt sales on (sales.param_id=:salesParamId and sales.yr=:yr and sales.mth=:mth and n.netid=sales.netid  and sales.ecode=n.level1 and sales.divId=:divId)");
		sb.append(" left join WapSalesAchvmt pob on (pob.param_id=:pobParamId and pob.yr=sales.yr and pob.mth=sales.mth and pob.netid=n.netid and pob.ecode=n.level1 and pob.divId=sales.divId )");
		sb.append(" left join WapOtherParamsAchvmt docrch on (sales.yr=docrch.yr and sales.mth=docrch.mth and n.netid=docrch.netid and docrch.emp=n.level1 and docrch.paramId=:docRchParamId and docrch.divId=sales.divId )");
		sb.append(" left join WapOtherParamsAchvmt docCov on (docCov.yr=sales.yr and sales.mth=docCov.mth and n.netid=docCov.netid and docCov.emp=n.level1 and docCov.paramId=:docCovParamId and docCov.divId=sales.divId )");
		sb.append(" left join WapOtherParamsAchvmt pulChmRch on (pulChmRch.yr=sales.yr and pulChmRch.mth=sales.mth and pulChmRch.netid=n.netid and pulChmRch.emp=n.level1 and  pulChmRch.paramId=:pulChmRchParamId and pulChmRch.divId=sales.divId )");
		sb.append(" left join mgreffectivenessparams m on (m.yr=sales.yr and m.mth=sales.mth  and m.ecode=n.level1 and m.divId=sales.divId )");
		sb.append(" order by n.netid");
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("yr",yr);
		query.setParameter("mth",mth);
		query.setParameter("loginYr",loginYr);
		query.setParameter("loginMth",loginMth);
		query.setParameter("salesParamId",salesParamId);
		query.setParameter("pobParamId",pobParamId);
		query.setParameter("docRchParamId",docRchParamId);
		query.setParameter("docCovParamId",docCovParamId);
		query.setParameter("pulChmRchParamId",pulChmRchParamId);
		query.setParameter("divId",divId);
		List<Object[]> list = query.getResultList();
		return list;
	}
	@Modifying  
	@Transactional
	/*sb.append("AchParam2=if(param2=0,0,(("+meParamsWeightageMap.get("p2")+"/100) * param2)),");
	sb.append("AchParam3=if(param3=0,0,(("+meParamsWeightageMap.get("p3")+"/100) * param3)),");
	sb.append("AchParam4=if(param4=0,0,(("+meParamsWeightageMap.get("p4")+"/100) * param4))");*/
	public void updateMEParamsAcheivement(HashMap<String,String> meParamsWeightageMap,String yr,String mth){
		StringBuffer sb=new StringBuffer();
		sb.append("update mgreffectivenessparams set ");
		for(int i=1;i<=meParamsWeightageMap.size();i++){ //if mgr2Param1 has value then only mgrparm's scores should be considered else param's to be considered which is of 1st line mgrs
			if(i==meParamsWeightageMap.size())
				//sb.append("AchParam"+i+"=if(param"+i+"=0,0,(("+meParamsWeightageMap.get("P"+i)+"/100) * param"+i+"))");
			sb.append("AchParam"+i+"=if(mgr2param1 is null or mgr2param1=0,(if(Param"+i+" is null,0,(("+meParamsWeightageMap.get("P"+i)+"/100) * param"+i+"))),(("+meParamsWeightageMap.get("P"+i)+"/100) * mgr2param"+i+"))");
			else
				//sb.append("AchParam"+i+"=if(param"+i+"=0,0,(("+meParamsWeightageMap.get("P"+i)+"/100) * param"+i+")),");
			sb.append("AchParam"+i+"=if(mgr2param1 is null or mgr2param1=0,(if(Param"+i+" is null,0,(("+meParamsWeightageMap.get("P"+i)+"/100) * param"+i+"))),(("+meParamsWeightageMap.get("P"+i)+"/100) * mgr2param"+i+")),");
		}
		sb.append(" where yr=:yr and mth=:mth");
		System.out.println("**updateMEParamsAcheivement : "+sb.toString());
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("yr",yr);
		query.setParameter("mth",mth);
		query.executeUpdate();
	}
	public HashMap<String,Long> getAnnualTargetNetidWise(String finYr,String salesDbRef)
	{
		HashMap<String,Long> targetMap=new HashMap<String,Long>();
		StringBuffer sb=new StringBuffer();
		sb.append(" select netid,(sale1ct+sale2ct+sale3ct+sale4ct+sale5ct+sale6ct+sale7ct+sale8ct+sale9ct+sale10ct+sale11ct+sale12ct) as yearlyTarget"); 
		sb.append(" from ");
		sb.append(salesDbRef);
		sb.append(".salessummary");
		sb.append(finYr);

		Query query = entityManager.createNativeQuery(sb.toString());
		List<Object[]> list = query.getResultList();
		if(null != list && !list.isEmpty()){
			for(Object[] object : list){
				targetMap.put((String)object[0], ((Number)object[1]).longValue());
			}
		}
		return targetMap;
	}
	
	public List<Object[]> getWapDetails(String loginId,String logMth, String logYr,String fmsDbRef,int divId,String toYr,String toMth,String selEmp){
		//TODO get param ids from constants
		StringBuffer sb=new StringBuffer();
		sb.append("select n.level1,if(sales.salesTarget is null,0,sales.salesTarget) as salestargt,if(sales.salesValue is null,0,sales.salesValue) as salesAch,if(sales.AcheivementPerc is null,0,sales.AcheivementPerc) as salesAchPer,");
		sb.append(" if(pob.salestarget is null,0,pob.salestarget) as pbtargt,if(pob.salesvalue is null,0,pob.salesvalue) as pbAch,if(pob.acheivementPerc is null,0,pob.acheivementPerc) as PBAchPer,");
		sb.append(" if(docrch.total is null,0,docrch.total) as docRchTargt,if(docrch.calculatedValue is null,0,docrch.calculatedValue) as docRchAch,if(docrch.achievementPerc is null,0,docrch.achievementPerc) as DocRchAchPer,");
		sb.append(" if(docCov.total is null,0,docCov.total) as docCovTargt,if(docCov.calculatedValue is null,0,docCov.calculatedValue) as docCovAch,if(docCov.AchievementPerc is null,0,docCov.AchievementPerc) as docCovAchPer,");
		sb.append(" if(pulChmRch.total is null,0,pulChmRch.total) as pulChmTargt,if(pulChmRch.calculatedValue is null,0,pulChmRch.calculatedValue) as pulChmAch,if(pulChmRch.achievementPerc is null,0,pulChmRch.achievementPerc) as pulChmAchPer,");
		sb.append(" if(m.achParam1 is null,0,m.achParam1) as achParam1 ,if(m.achparam2 is null,0,m.achparam2) as achParam2,if(m.achparam3 is null,0,m.achparam3) as achParam3,if(m.achparam4 is null,0,m.achparam4) as achParam4 ");
		if(null==selEmp || selEmp.equalsIgnoreCase("")){
		sb.append(" from tempNetidWithMaxJnDate"+loginId+" t");
		sb.append(" inner join "+fmsDbRef+".nettrans n on (t.netid=n.netid and t.joindate=n.joinDate and n.yr=:yr and n.mth=:mth and level1 not like 'V%')");
		sb.append(" left join WapSalesAchvmt sales on (sales.param_id='"+CommonConstants.SALES_PARAMID+"' and sales.yr=:toYr and sales.mth=:toMth and sales.netid=n.netid and sales.ecode=n.level1 and sales.divId=:divId )");
		}else{
			sb.append(" from "+fmsDbRef+".nettrans n ");
			sb.append(" left join WapSalesAchvmt sales on (sales.param_id='"+CommonConstants.SALES_PARAMID+"' and sales.yr=:toYr and sales.mth=:toMth and sales.netid=n.netid and sales.ecode=n.level1 and sales.divId=:divId )");
		}
		sb.append(" left join WapSalesAchvmt pob on (pob.param_id='"+CommonConstants.POB_PARAMID+"' and pob.yr=sales.yr and pob.mth=sales.mth and pob.netid=n.netid and pob.divId=sales.divId and pob.ecode=n.level1)");
		sb.append(" left join WapOtherParamsAchvmt docrch on (docrch.yr=sales.yr and docrch.mth=sales.mth and n.netid=docrch.netid and docrch.emp=n.level1 and docrch.divId=sales.divId and docrch.paramId='"+CommonConstants.DOC_REACH_PARAMID+"')");
		sb.append(" left join WapOtherParamsAchvmt docCov on (docCov.yr=sales.yr and docCov.mth=sales.mth and n.netid=docCov.netid and docCov.emp=n.level1 and docCov.divId=sales.divId and docCov.paramId='"+CommonConstants.COVERAGE_PARAMID+"')");
		sb.append(" left join WapOtherParamsAchvmt pulChmRch on (pulChmRch.yr=sales.yr and pulChmRch.mth=sales.mth and pulChmRch.netid=n.netid and pulChmRch.divId=sales.divId and pulChmRch.emp=n.level1 and  pulChmRch.paramId='"+CommonConstants.PULSE_CHEM_REACH_PARAMID+"')");
		sb.append(" left join mgreffectivenessparams m on (m.yr=sales.yr and m.mth=sales.mth  and m.ecode=n.level1 and m.divId=sales.divId )");//and deldate='0000-00-00'
		if(null!=selEmp && !selEmp.equalsIgnoreCase(""))
		sb.append(" where n.yr=:yr and n.mth=:mth and n.level1 in ("+selEmp+") ");
		sb.append(" order by n.netid");
		Query query = entityManager.createNativeQuery(sb.toString());
		System.out.println("getWapDetails : "+sb.toString());
		query.setParameter("yr",logYr);
		query.setParameter("mth",logMth);
		query.setParameter("toYr",toYr);
		query.setParameter("toMth",toMth);
		query.setParameter("divId",divId);
		List<Object[]> list = query.getResultList();
		return list;
	}
	
	public List<Object[]> getCummWapDetails(String frmYrMth,String toYrMth,String yr,String mth,String loginid,String fmsDbRef,int divId,String selEmp){
		//TODO add div id
		StringBuffer sb=new StringBuffer(); 
		sb.append(" select n.netid,n.level1,sum(sales.salesTarget) as salestargt,sum(sales.salesValue) as salesAch,");
		sb.append(" sum(pob.salestarget) as pbtargt,sum(pob.salesvalue) as pbAch,");
		sb.append(" sum(docrch.total) as docRchTargt,sum(docrch.calculatedValue) as docRchAch,");
		sb.append(" sum(docCov.total) as docCovTargt,sum(docCov.calculatedValue) as docCovAch,");
		sb.append(" sum(pulChmRch.total) as pulChmTargt,sum(pulChmRch.calculatedValue) as pulChmAch,");
		sb.append(" sum(m.AchParam1),sum(m.Achparam2),sum(m.AchParam3),sum(m.AchParam4),e.jnDate");
		
		if(null==selEmp || selEmp.equalsIgnoreCase("")){
			sb.append(" from tempNetidWithMaxJnDate"+loginid+" t");
			sb.append(" inner join "+fmsDbRef+".nettrans n on (n.netid=t.netid and n.joindate=t.joindate)");  
			}else{
				sb.append(" from "+fmsDbRef+".nettrans n ");
			}
		sb.append(" inner join "+fmsDbRef+".empmst e on (e.ecode=n.level1 and e.etype='1' and e.resdate='0000-00-00')");
		sb.append(" left join WapSalesAchvmt sales on (n.netid=sales.netid and concat(sales.yr,sales.mth)>=:frmYrMth and concat(sales.yr,sales.mth)<=:toYrMth and sales.param_id='"+CommonConstants.SALES_PARAMID+"' and sales.divId=:divId)");
		sb.append(" left join WapSalesAchvmt pob on (pob.yr=sales.yr and pob.mth=sales.mth and pob.netid=n.netid and pob.divId=sales.divId and  pob.param_id='"+CommonConstants.POB_PARAMID+"')");
		sb.append(" left join WapOtherParamsAchvmt docrch on (docrch.yr=sales.yr and docrch.mth=sales.mth and docrch.netid=n.netid and docrch.emp=n.level1 and docrch.divId=sales.divId and docrch.paramId='"+CommonConstants.DOC_REACH_PARAMID+"')");
		sb.append(" left join WapOtherParamsAchvmt docCov on (docCov.yr=sales.yr and docCov.mth=sales.mth and docCov.netid=n.netid and docCov.emp=n.level1 and docCov.divId=sales.divId and docCov.paramId='"+CommonConstants.COVERAGE_PARAMID+"')");
		sb.append(" left join WapOtherParamsAchvmt pulChmRch on (pulChmRch.yr=sales.yr and pulChmRch.mth=sales.mth and pulChmRch.netid=sales.netid and pulChmRch.emp=n.level1 and pulChmRch.divId=sales.divId and  pulChmRch.paramId='"+CommonConstants.PULSE_CHEM_REACH_PARAMID+"')");
		sb.append(" left join mgreffectivenessparams m on (m.yr=sales.yr and m.mth=sales.mth and m.ecode=n.level1 and pulChmRch.divId=sales.divId)");
		sb.append(" where  level1 not like 'V%' ");
		if(null!=selEmp && !selEmp.equalsIgnoreCase(""))
			sb.append(" and n.yr=:yr and n.mth=:mth and n.level1 in ("+selEmp+") ");
		sb.append(" group by sales.netid"); //sales.ecode
		Query query = entityManager.createNativeQuery(sb.toString());
		System.out.println("getCummWapDetails : "+sb.toString());
		query.setParameter("frmYrMth",frmYrMth); //201804 yrmth of financial year's start date
		query.setParameter("toYrMth",toYrMth); //201903 yrmth of login date
		if(null!=selEmp && !selEmp.equalsIgnoreCase("")){
		query.setParameter("yr",yr);
		query.setParameter("mth",mth);
		}
		query.setParameter("divId",divId);
		List<Object[]> list = query.getResultList();
		return list;
	}

	//get coverage for D,E,f as summary table captures only reach for D class and coverage for A,B,C 
	public List<Object[]> getDocCovForOtherClassDocs(String docCls,String vstReqd,String yr,String mth,String fmsdbref){ 
		StringBuffer sb=new StringBuffer();
		sb.append("select netid,emp,class,count(cntcd) as docsCovered from (");
		sb.append(" select netid,emp,class,cntcd,cnt from(");
		sb.append(" select netid,emp,class,cntcd,count(cntcd) as cnt from (");
		sb.append(" select h.dcrno,h.netid,emp,dcrdate,d.cntcd,wnetid,class from "+fmsdbref+".dcrh h");
		sb.append(" inner join "+fmsdbref+".dcrd d on (h.dcrno=d.dcrno)");
		sb.append(" inner join "+fmsdbref+".doched dh on (dh.netid=d.wnetid and dh.cntcd=d.cntcd and dh.class=:docCls)");
		sb.append(" where dcrdate between '"+yr+"-"+mth+"-01'  and '"+yr+"-"+mth+"-31' and confdate!='0000-00-00' and custflg='D' )");
		sb.append(" as a group by netid,cntcd)");
		sb.append(" as b group by netid,cntcd having cnt>=:vstReqd)");
		sb.append(" as c group by netid;");
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("docCls",docCls);
		query.setParameter("vstReqd",vstReqd);
		List<Object[]> list = query.getResultList();
		return list;
	}
	//gets class wise total doctors for a netid (as per class given in docCls)
		public List<Object[]> getTotOtherClassDocs(String docCls,String fmsDbRef,String yr,String mth,String loginid){ 
			StringBuffer sb=new StringBuffer();
			sb.append("select d.netid,n.level1,class,count(cntcd) from tempNetidWithMaxJnDate"+loginid+" t inner join "+fmsDbRef+".doched d on (t.netid=d.netid)");
			sb.append(" inner join "+fmsDbRef+".nettrans n on (t.netid=n.netid and t.joindate=n.joinDate and n.mth='"+mth+"' and n.yr='"+yr+"')");
			sb.append(" where (deldate='0000-00-00' or deldate>='"+yr+"-"+mth+"-31') and class in ("+docCls+") group by netid,class");
			
			//sb.append("select netid,class,count(cntcd) from "+fmsDbRef+".doched where deldate='0000-00-00' and class in ("+docCls+")group by netid,class");
			Query query = entityManager.createNativeQuery(sb.toString());
			List<Object[]> list = query.getResultList();
			return list;
	    }

		//Doc Reach for E and F class
		//TODO doubt ->dh.netid=d.wnetid  or dh.netid=h.netid ??  
		public List<Object[]> getDocReachForOtherClassDocs(String mth,String yr,String cls,String fmsDbRef){
			StringBuffer sb=new StringBuffer();
			sb.append("select netid,emp,class,count(cntcd) from(");
			sb.append("select h.dcrno,h.netid,emp,dcrdate,d.cntcd,wnetid,class from "+fmsDbRef+".dcrh h");
			sb.append(" inner join "+fmsDbRef+".dcrd d on (h.dcrno=d.dcrno)");
			sb.append(" inner join "+fmsDbRef+".doched dh on (dh.netid=d.wnetid and dh.cntcd=d.cntcd and dh.class in ("+cls+"))"); 
			sb.append(" where dcrdate between '"+yr+"-"+mth+"-01' and '"+yr+"-"+mth+"-31' and confdate!='0000-00-00'");
			sb.append(" and custflg='D'  group by netid,cntcd,class");
			sb.append(") as a group by netid,class order by netid");
			Query query = entityManager.createNativeQuery(sb.toString());
			List<Object[]> list = query.getResultList();
			return list;
	    }
		
		public ArrayList<String> getAllNetids(String loginid){
			String sql="select netid from tempNetidWithMaxJnDate"+loginid+"";
			Query query = entityManager.createNativeQuery(sql);
			ArrayList<String> list = (ArrayList<String>)query.getResultList();
			return list;
		}
		
		@Modifying  
		@Transactional
		public int insertIntoFocProd(String yr, String mth, int divId, String fmsDbRef, String hqRef){
			StringBuffer sb=new StringBuffer();
			sb.append("delete from FocProd where yr=:yr and mth=:mth and divid=:divId");
			Query query = entityManager.createNativeQuery(sb.toString());
			query.setParameter("yr",yr);
			query.setParameter("mth",mth);
			query.setParameter("divId",divId);
			query.executeUpdate();
			sb.delete(0, sb.length());
			sb.append("insert into FocProd (mth,yr,divid,prodid) (select '"+mth+"','"+yr+"','"+divId+"',p.prodid ");
			sb.append(" from "+fmsDbRef+".prodmst p inner join "+fmsDbRef+".prodtrans t on (p.prodid=t.prodid) where ptype='1' ");
			sb.append(" and p.d1d2=:hqRef and p.deldate='0000-00-00' and powerbrand='Y' )");
			System.out.println(sb.toString());
			query = entityManager.createNativeQuery(sb.toString());
			query.setParameter("hqRef",hqRef);
			return query.executeUpdate();
		}
		
		public int getPsrNotReviewedCnt(String yr, String mth, int divId,String fmsDbRef,String loginid){
			StringBuffer sb=new StringBuffer();
			sb.append("select count(n.netid) from tempNetidWithMaxJnDate"+loginid+" t"); 
			sb.append(" inner join "+fmsDbRef+".nettrans n on (t.netid=n.netid and n.joindate=t.joindate and n.level1 not like 'v%')");
			sb.append(" inner join "+fmsDbRef+".empmst as emp on  n.level1 = emp.ecode and emp.resdate='0000-00-00' ");
			sb.append(" left join mgreffectivenessparams m on (t.netid=m.netid and m.ecode=n.level1 and m.yr=:yr and m.mth=:mth)");
			sb.append(" where n.level1 not like 'v%' and mgreffectivenessparamsId is null");
			Query query = entityManager.createNativeQuery(sb.toString());
			query = entityManager.createNativeQuery(sb.toString());
			query.setParameter("yr",yr);
			query.setParameter("mth",mth);
			return ((Number) query.getSingleResult()).intValue();
		}
		@Modifying  
		@Transactional 
		public void createTempNetidwithMaxjoinDateLvlWise(String loginid,String mth,String yr,String hqref,String fmsDbRef,String lvl){
			StringBuffer sb=new StringBuffer();
			sb.append("drop table if exists tempNetidWithMaxJnDate"+loginid);
			Query query = entityManager.createNativeQuery(sb.toString());
			query.executeUpdate();
			sb.delete(0, sb.length());
			sb.append("create table tempNetidWithMaxJnDate"+loginid+""); 
			sb.append(" Select n.NetID, Max(JoinDate) As JoinDate From "+fmsDbRef+".NetTrans n");
			sb.append(" inner join "+fmsDbRef+".netmst nm on (n.netid=nm.netid and level='1')");
			sb.append(" Where Yr = '"+yr+"' And Mth = '"+mth+"' ");
			if(lvl!=null && Integer.parseInt(lvl)>=2 && Integer.parseInt(lvl)<7)
				sb.append(" and n.level"+lvl+"=:loginid ");
			sb.append(" and nm.hname like '%("+hqref+")%' and nm.deldate='0000-00-00' group By NetID");
			query = entityManager.createNativeQuery(sb.toString());
			if(lvl!=null && Integer.parseInt(lvl)>=2 && Integer.parseInt(lvl)<7)
				query.setParameter("loginid",loginid);
			query.executeUpdate();
			sb.delete(0, sb.length());
			sb.append("alter table tempNetidWithMaxJnDate"+loginid+" add index ind1(Netid,JoinDate)");
			query = entityManager.createNativeQuery(sb.toString());
			query.executeUpdate();
		}
		
		@Modifying  
		@Transactional 
		public void dropTable(String loginid){
			Query query = entityManager.createNativeQuery("drop table if exists tempNetidWithMaxJnDate"+loginid);
			query.executeUpdate();
		}
		

}
