package com.eis.wap.dao;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.constants.CommonConstants;
import com.eis.wap.domain.Slabs;

public interface WapCalDao extends JpaRepository<Slabs, Integer>, WapCalDaoCustom {

	@Query(value = "select param_id, param_name, weightage from Param_Master where isActive !='0' order by param_id", nativeQuery = true)
	public List<Object[]> getParams();
	  
	@Query(value = "select param_id, range_min, points from Slabs where param_id in (?1) order by points desc", nativeQuery = true)
	public List<Object[]> getMinRangeAndScoreByParamId(List<Integer> paramIdList);
	
	  /*public List<WapCalculation> getParams()
    {
        String sql = "select param_id,param_name,weightage from Parameters where isActive!='0' order by param_id";
        final String sql2="select range_min,points from slabs where param_id=? order by points desc limit 1";
        List<WapCalculation> paramList = jdbcTemplate.query(sql, new ResultSetExtractor<List<WapCalculation>>()
        {
            //@Override
            public List<WapCalculation> extractData(ResultSet rs) throws SQLException, DataAccessException
            {
            	
                List<WapCalculation> list = new ArrayList<WapCalculation>();
                while (rs.next())
                {
                	final WapCalculation wapCalparams = new WapCalculation();
                	wapCalparams.setParam_id(rs.getInt(1));
                	wapCalparams.setParam_name(rs.getString(2));
                	wapCalparams.setWeightage(rs.getFloat(3));
                	try{
                	jdbcTemplate.queryForObject(sql2,new Object[] { wapCalparams.getParam_id() }, new RowMapper<WapCalculation>() {
                		
                		public WapCalculation mapRow(ResultSet rs, int rowNum) throws SQLException {
                			wapCalparams.setAchieve(rs.getFloat(1));
                			wapCalparams.setScore(rs.getFloat(2));
                			return wapCalparams;
                		}
                		
                	});
                	}catch(EmptyResultDataAccessException e){
                    	wapCalparams.setAchieve(0.0F);
                    	wapCalparams.setScore(0.0F);
                    	//return wapCalparams;
                    }
                	list.add(wapCalparams);
                }
                return list;
            }
        });
        return paramList;
    }
	*/

	
	@Query(value = "select points from slabs where range_min <= ?1 and range_max >= ?1 and param_id = ?2 " , nativeQuery = true)
	public Float getScore(Float achieve, Integer paramId);
	
	/*public float getScore(int paramid,float achieve){
		 String query = "select points from slabs where range_min<=? and range_max>=? and param_id=?"; 
		 float score=0.0F;
		 try{
		 score = jdbcTemplate.queryForObject(query,new Object[] {achieve,achieve,paramid}, Float.class);
		 System.out.println("score : "+score);
	 }catch(EmptyResultDataAccessException e){
     	score=0.0F;
     }
		 return score;
	 }*/
	 
	@Query(value = "select rating from increment where min_score <= ?1 and max_score >= ?1 ", nativeQuery = true)
	public String getRating(Float wap);
	
/*	 public String getRating (float wap){
		 String query = "select rating from increment where min_score<=? and max_score>=?"; 
		 String rating="";
		 try{
			 rating = jdbcTemplate.queryForObject(query,new Object[] {wap,wap}, String.class);
		 System.out.println("rating : "+rating);
	 }catch(EmptyResultDataAccessException e){
     	rating="N";
     }
 
		 return rating;
	 }*/
	@Query(value = "select svarname,svartxt from sysprm where svarname like '"+CommonConstants.SYSPRM_SVARNAME_OPEFF+"' or  svarname like '"+CommonConstants.SYSPRM_OPEFF_NAMES+"' order by svarname ", nativeQuery = true)
	public  List<Object[]> getOpEffNameWeightage();
	
	
	@Query(value="select if(sales.salesTarget is null,0,sales.salesTarget) as salestargt,if(sales.salesValue is null,0,sales.salesValue) as salesAch,"+
" if(sales.AcheivementPerc is null,0,sales.AcheivementPerc) as salesAchPer,salespts,salesscore,"+
"if(pob.salestarget is null,0,pob.salestarget) as pbtargt,if(pob.salesvalue is null,0,pob.salesvalue) as pbAch,"+
"if(pob.acheivementPerc is null,0,pob.acheivementPerc) as PBAchPer,pobpts,pobsalesscore,"+
"if(docrch.total is null,0,docrch.total) as docRchTargt,if(docrch.calculatedValue is null,0,docrch.calculatedValue) as docRchAch,"+
"if(docrch.achievementPerc is null,0,docrch.achievementPerc) as DocRchAchPer,docRchpts,docreachscore,"+
"if(docCov.total is null,0,docCov.total) as docCovTargt,if(docCov.calculatedValue is null,0,docCov.calculatedValue) as docCovAch,"+
"if(docCov.AchievementPerc is null,0,docCov.AchievementPerc) as docCovAchPer,docCovpts,doccoveragescore,"+
"if(pulChmRch.total is null,0,pulChmRch.total) as pulChmTargt,if(pulChmRch.calculatedValue is null,0,pulChmRch.calculatedValue) as pulChmAch,"+
"if(pulChmRch.achievementPerc is null,0,pulChmRch.achievementPerc) as pulChmAchPer,pulseChmRchPts,pulsechemreachscore,"+
"(if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param1 is null,0,m.Param1),mgr2param1)+"+
"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param2 is null,0,m.Param2),mgr2param2)+"+
"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param3 is null,0,m.Param3),mgr2param3)+"+
"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param4 is null,0,m.Param4),mgr2param4)) as opEffAch,"+ /*NOTE : checking against mgr2param1 scores to avoid any 
																	discrepencies in future . i.e if param1 is taken from mgr2param1,
																	then all the params should be taken from mgr2 only. If 1st column is blank then none of column can have value.*/
" otherpts,otherpts as otherpts2,otherscore,m.Param1,m.mgr2param1,m.Param2,m.mgr2param2,m.Param3,m.mgr2param3,m.Param4,m.mgr2param4,"+
"if(c.comment1 is null,'',c.comment1),if(c.comment2 is null,'',c.comment2),w.finalscore,w.rating,y.Yfinalscore,y.Yrating,if(pc.comments is null,'',pc.comments)"+
" from WapSalesAchvmt sales"+ 
" left join WapSalesAchvmt pob on (pob.param_id='"+CommonConstants.POB_PARAMID+"' and pob.yr=sales.yr and pob.mth=sales.mth and pob.netid=sales.netid and pob.divId=sales.divId and pob.ecode=sales.ecode)"+
" left join WapOtherParamsAchvmt docrch on (sales.yr=docrch.yr and sales.mth=docrch.mth and sales.netid=docrch.netid and docrch.emp=sales.ecode and docrch.divId=sales.divId and docrch.paramId='"+CommonConstants.DOC_REACH_PARAMID+"')"+
" left join WapOtherParamsAchvmt pulChmRch on (pulChmRch.yr=sales.yr and pulChmRch.mth=sales.mth and pulChmRch.netid=sales.netid and pulChmRch.divId=sales.divId and pulChmRch.emp=sales.ecode and  pulChmRch.paramId='"+CommonConstants.PULSE_CHEM_REACH_PARAMID+"')"+
" left join WapOtherParamsAchvmt docCov on (docCov.yr=sales.yr and sales.mth=docCov.mth and sales.netid=docCov.netid and docCov.emp=sales.ecode and docCov.divId=sales.divId and docCov.paramId='"+CommonConstants.COVERAGE_PARAMID+"')"+
" left join mgreffectivenessparams m on (m.yr=sales.yr and m.mth=sales.mth  and m.ecode=sales.ecode and m.divId=sales.divId)"+
" left join mgrcomments c on (m.mgreffectivenessparamsId=c.mgreffectivenessparamsId) "+
" left join wap_final_scoreandincrement w on (w.netid=sales.netid and w.ecode=sales.ecode and w.yr=sales.yr and w.mth=sales.mth and w.divid=sales.divid)"+
" left join YTDWap y on (y.netid=sales.netid and y.ecode=sales.ecode and y.yr=sales.yr and y.mth=sales.mth and y.divid=sales.divid)"+
" left join  psrcomments pc on (pc.netid=sales.netid and pc.ecode=sales.ecode and pc.yr=sales.yr and pc.mth=sales.mth and pc.divid=sales.divid) "+
" where sales.param_id='"+CommonConstants.SALES_PARAMID+"' and sales.yr=?1 and sales.mth=?2 and sales.netid=?3 and sales.ecode=?4 and sales.divId=?5"+
" order by sales.netid", nativeQuery = true)
	public  List<Object[]> getWapDetailsOfEmp(String yr,String mth,String netid,String ecode,String div);

	
	@Query(value="select if(sales.salesTarget is null,0,sales.salesTarget) as salestargt,if(sales.salesValue is null,0,sales.salesValue) as salesAch,"+
" if(sales.AcheivementPerc is null,0,sales.AcheivementPerc) as salesAchPer,"+
"if(pob.salestarget is null,0,pob.salestarget) as pbtargt,if(pob.salesvalue is null,0,pob.salesvalue) as pbAch,"+
"if(pob.acheivementPerc is null,0,pob.acheivementPerc) as PBAchPer,"+
"if(docrch.total is null,0,docrch.total) as docRchTargt,if(docrch.calculatedValue is null,0,docrch.calculatedValue) as docRchAch,"+
"if(docrch.achievementPerc is null,0,docrch.achievementPerc) as DocRchAchPer,"+
"if(docCov.total is null,0,docCov.total) as docCovTargt,if(docCov.calculatedValue is null,0,docCov.calculatedValue) as docCovAch,"+
"if(docCov.AchievementPerc is null,0,docCov.AchievementPerc) as docCovAchPer,"+
"if(pulChmRch.total is null,0,pulChmRch.total) as pulChmTargt,if(pulChmRch.calculatedValue is null,0,pulChmRch.calculatedValue) as pulChmAch,"+
"if(pulChmRch.achievementPerc is null,0,pulChmRch.achievementPerc) as pulChmAchPer,"+
"(if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param1 is null,0,m.Param1),mgr2param1)+"+
"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param2 is null,0,m.Param2),mgr2param2)+"+
"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param3 is null,0,m.Param3),mgr2param3)+"+
"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param4 is null,0,m.Param4),mgr2param4)) as opEffAch,"+ /*NOTE : checking against mgr2param1 scores to avoid any 
																	discrepencies in future . i.e if param1 is taken from mgr2param1,
																	then all the params should be taken from mgr2 only. If 1st column is blank then none of column can have value.*/
" c.comments " +
" from WapSalesAchvmt sales"+ 
" left join WapSalesAchvmt pob on (pob.param_id='"+CommonConstants.POB_PARAMID+"' and pob.yr=sales.yr and pob.mth=sales.mth and pob.netid=sales.netid and pob.divId=sales.divId and pob.ecode=sales.ecode)"+
" left join WapOtherParamsAchvmt docrch on (sales.yr=docrch.yr and sales.mth=docrch.mth and sales.netid=docrch.netid and docrch.emp=sales.ecode and docrch.divId=sales.divId and docrch.paramId='"+CommonConstants.DOC_REACH_PARAMID+"')"+
" left join WapOtherParamsAchvmt pulChmRch on (pulChmRch.yr=sales.yr and pulChmRch.mth=sales.mth and pulChmRch.netid=sales.netid and pulChmRch.divId=sales.divId and pulChmRch.emp=sales.ecode and  pulChmRch.paramId='"+CommonConstants.PULSE_CHEM_REACH_PARAMID+"')"+
" left join WapOtherParamsAchvmt docCov on (docCov.yr=sales.yr and sales.mth=docCov.mth and sales.netid=docCov.netid and docCov.emp=sales.ecode and docCov.divId=sales.divId and docCov.paramId='"+CommonConstants.COVERAGE_PARAMID+"')"+
" left join mgreffectivenessparams m on (m.yr=sales.yr and m.mth=sales.mth  and m.ecode=sales.ecode and m.divId=sales.divId)"+
" left join psrcomments c on (c.yr=sales.yr and c.mth=sales.mth  and c.ecode=sales.ecode and c.divId=sales.divId) "+
" where sales.param_id='"+CommonConstants.SALES_PARAMID+"' and sales.yr=?1 and sales.mth=?2 and sales.netid=?3 and sales.ecode=?4 and sales.divId=?5"+
" order by sales.netid", nativeQuery = true)
	public  List<Object[]> getWapDetailsOfEmp2(String yr,String mth,String netid,String ecode,int div);
	
	//concatenation of double and string is not possible while getting data from DB.
	@Query(value="select sales.yr,sales.mth,'!!',if(sales.param_id is null,"+CommonConstants.SALES_PARAMID+",sales.param_id) as p1,if(sales.salesTarget is null,0,sales.salesTarget) as salestargt,if(sales.salesValue is null,0,sales.salesValue) as salesAch,"+
" if(sales.AcheivementPerc is null,0,sales.AcheivementPerc) as salesAchPer,salesscore,'!!' as sep2,if(pob.param_id is null,"+CommonConstants.POB_PARAMID+",pob.param_id) as p2,"+
" if(pob.salestarget is null,0,pob.salestarget) as pbtargt,if(pob.salesvalue is null,0,pob.salesvalue) as pbAch,"+
" if(pob.acheivementPerc is null,0,pob.acheivementPerc) as PBAchPer,pobsalesscore,"+
" '!!' as sep3,if(docrch.paramid is null,"+CommonConstants.DOC_REACH_PARAMID+",docrch.paramid) as p3,if(docrch.total is null,0,docrch.total) as docRchTargt,if(docrch.calculatedValue is null,0,docrch.calculatedValue) as docRchAch,"+
" if(docrch.achievementPerc is null,0,docrch.achievementPerc) as DocRchAchPer,docReachScore,"+
" '!!' as sep4,if(docCov.paramid is null,"+CommonConstants.COVERAGE_PARAMID+",docCov.paramid) as p4,if(docCov.total is null,0,docCov.total) as docCovTargt,if(docCov.calculatedValue is null,0,docCov.calculatedValue) as docCovAch,"+
" if(docCov.AchievementPerc is null,0,docCov.AchievementPerc) as docCovAchPer,DocCoverageScore,"+
" '!!' as sep5,if(pulChmRch.paramid is null,"+CommonConstants.PULSE_CHEM_REACH_PARAMID+",pulChmRch.paramid) as p5 ,if(pulChmRch.total is null,0,pulChmRch.total) as pulChmTargt,if(pulChmRch.calculatedValue is null,0,pulChmRch.calculatedValue) as pulChmAch,if(pulChmRch.AchievementPerc is null,0,pulChmRch.AchievementPerc) as pulChmRchAchPer,pulseChemReachScore,"+
" '@@' as sep6,otherscore,'@@' as sep7,finalScore,'@@',rating"+
" from WapSalesAchvmt sales"+
" left join WapSalesAchvmt pob on (pob.param_id='"+CommonConstants.POB_PARAMID+"' and pob.yr=sales.yr and pob.mth=sales.mth and pob.netid=sales.netid and pob.divId=sales.divId and pob.ecode=sales.ecode)"+
" left join WapOtherParamsAchvmt docrch on (sales.yr=docrch.yr and sales.mth=docrch.mth and sales.netid=docrch.netid and docrch.emp=sales.ecode and docrch.divId=sales.divId and docrch.paramId='"+CommonConstants.DOC_REACH_PARAMID+"')"+
" left join WapOtherParamsAchvmt pulChmRch on (pulChmRch.yr=sales.yr and pulChmRch.mth=sales.mth and pulChmRch.netid=sales.netid and pulChmRch.divId=sales.divId and pulChmRch.emp=sales.ecode and  pulChmRch.paramId='"+CommonConstants.PULSE_CHEM_REACH_PARAMID+"')"+
" left join WapOtherParamsAchvmt docCov on (docCov.yr=sales.yr and sales.mth=docCov.mth and sales.netid=docCov.netid and docCov.emp=sales.ecode and docCov.divId=sales.divId and docCov.paramId='"+CommonConstants.COVERAGE_PARAMID+"')"+
" left join mgreffectivenessparams m on (m.yr=sales.yr and m.mth=sales.mth  and m.ecode=sales.ecode and m.divId=sales.divId)"+
" left join wap_final_scoreandincrement w on (w.netid=sales.netid and w.ecode=sales.ecode and w.yr=sales.yr and w.mth=sales.mth and w.divid=sales.divid)"+
" where sales.param_id='"+CommonConstants.SALES_PARAMID+"'  and concat(sales.yr,sales.mth)>=?1 and concat(sales.yr,sales.mth)<=?2 and  sales.netid=?3 and sales.ecode=?4 and sales.divId=?5"+

" order by sales.yr,sales.mth", nativeQuery = true)
	public  List<Object[]> getCummWapDetailsOfEmp(String yrmth,String toyrmth,String netid,String ecode,String div);
	
	@Query(value="select if(sales.salesTarget is null,0,sales.salesTarget) as salestargt,if(sales.salesValue is null,0,sales.salesValue) as salesAch,"+
" if(sales.AcheivementPerc is null,0,sales.AcheivementPerc) as salesAchPer,"+
"if(pob.salestarget is null,0,pob.salestarget) as pbtargt,if(pob.salesvalue is null,0,pob.salesvalue) as pbAch,"+
"if(pob.acheivementPerc is null,0,pob.acheivementPerc) as PBAchPer,"+
"if(docrch.total is null,0,docrch.total) as docRchTargt,if(docrch.calculatedValue is null,0,docrch.calculatedValue) as docRchAch,"+
"if(docrch.achievementPerc is null,0,docrch.achievementPerc) as DocRchAchPer,"+
"if(pulChmRch.total is null,0,pulChmRch.total) as pulChmTargt,if(pulChmRch.calculatedValue is null,0,pulChmRch.calculatedValue) as pulChmAch,"+
"if(pulChmRch.achievementPerc is null,0,pulChmRch.achievementPerc) as pulChmAchPer,"+
"if(docCov.total is null,0,docCov.total) as docCovTargt,if(docCov.calculatedValue is null,0,docCov.calculatedValue) as docCovAch,"+
"if(docCov.AchievementPerc is null,0,docCov.AchievementPerc) as docCovAchPer,"+
"(if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param1 is null,0,m.Param1),mgr2param1)+"+
"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param2 is null,0,m.Param2),mgr2param2)+"+
"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param3 is null,0,m.Param3),mgr2param3)+"+
"if(m.mgr2param1 is null or m.mgr2param1=0,if(m.Param4 is null,0,m.Param4),mgr2param4)) as opEffAch,"+ /*NOTE : checking against mgr2param1 scores to avoid any 
																	discrepencies in future . i.e if param1 is taken from mgr2param1,
																	then all the params should be taken from mgr2 only. If 1st column is blank then none of column can have value.*/
" c.comments " +
" from WapSalesAchvmt sales"+ 
" left join WapSalesAchvmt pob on (pob.param_id='"+CommonConstants.POB_PARAMID+"' and pob.yr=sales.yr and pob.mth=sales.mth and pob.netid=sales.netid and pob.divId=sales.divId and pob.ecode=sales.ecode)"+
" left join WapOtherParamsAchvmt docrch on (sales.yr=docrch.yr and sales.mth=docrch.mth and sales.netid=docrch.netid and docrch.emp=sales.ecode and docrch.divId=sales.divId and docrch.paramId='"+CommonConstants.DOC_REACH_PARAMID+"')"+
" left join WapOtherParamsAchvmt pulChmRch on (pulChmRch.yr=sales.yr and pulChmRch.mth=sales.mth and pulChmRch.netid=sales.netid and pulChmRch.divId=sales.divId and pulChmRch.emp=sales.ecode and  pulChmRch.paramId='"+CommonConstants.PULSE_CHEM_REACH_PARAMID+"')"+
" left join WapOtherParamsAchvmt docCov on (docCov.yr=sales.yr and sales.mth=docCov.mth and sales.netid=docCov.netid and docCov.emp=sales.ecode and docCov.divId=sales.divId and docCov.paramId='"+CommonConstants.COVERAGE_PARAMID+"')"+
" left join mgreffectivenessparams m on (m.yr=sales.yr and m.mth=sales.mth  and m.ecode=sales.ecode and m.divId=sales.divId)"+
" left join psrcomments c on (c.yr=sales.yr and c.mth=sales.mth  and c.ecode=sales.ecode and c.divId=sales.divId) "+
" where sales.param_id='"+CommonConstants.SALES_PARAMID+"' and sales.yr=?1 and sales.mth=?2 and sales.netid=?3 and sales.ecode=?4 and sales.divId=?5"+
" order by sales.netid", nativeQuery = true)
	public  List<Object[]> getWapDetailsOfEmp3(String yr,String mth,String netid,String ecode,int div);	//for PSR Comments
	
	
}
