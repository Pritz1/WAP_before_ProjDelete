package com.eis.wap.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.ParamMaster;

public interface ParamMasterDAO extends JpaRepository<ParamMaster, Integer>{ 
	
		
	//Delete employee : this will actually update the parameter record with isactive as 0 and set deldate and deleted by. 
	@Transactional
	@Modifying
	@Query(value = "update PARAM_MASTER set isActive = ?1, deldate = ?2 , deletedBy = ?3 where param_id = ?4 and level = ?5 ", nativeQuery = true)
	public void deleteParam(String isActive, Date delDate, String deletedBy, Integer paramId, Integer level);
	
	/*public void deleteParam(Parameters param)
    {
    	System.out.println("--3--"+param.getParam_id());
        String sql = "update PARAM_MASTER set isActive =?, deldate=?,deletedBy=? where param_id=? and level=?";
        Date date=getCurrentDateTime();
        jdbcTemplate.update(sql, new Object[]
        { "0", date, "EIS", param.getParam_id(),param.getEmpLevel() });
        
    }
	*/
	
	@Query(value = "select * from PARAM_MASTER where isActive != '0' and level = ?1 order by param_id", nativeQuery = true)
	public List<ParamMaster> getAllParams(String level);
	
	//paramId, paramName, weightage, addDate, addedBy, isActive 
	
   /* public List<Parameters> getAllParams(String emplvl)
    {
        String sql = "select * from PARAM_MASTER where isActive!='0' and level=? order by param_id";

        List<Parameters> paramList = jdbcTemplate.query(sql,new Object[]{emplvl}, new ResultSetExtractor<List<Parameters>>()
        {
            //@Override
            public List<Parameters> extractData(ResultSet rs) throws SQLException, DataAccessException
            {
                List<Parameters> list = new ArrayList<Parameters>();
                while (rs.next())
                {
                	Parameters param = new Parameters();
                	param.setParam_id(rs.getInt(1));
                	param.setParam_name(rs.getString(2));
                    param.setWeightage(rs.getFloat(3));
                    param.setAdd_date(rs.getDate(4));
                    param.setAddedBy(rs.getString(5));
                    param.setIsActive(rs.getString(6));
                    list.add(param);
                }
                return list;
            }

        });
        return paramList;
    }*/
	
	@Query(value = "select level , leveldesc from WAPEmpLevelMaster where deldate = '0000-00-00' ", nativeQuery = true)
	public List<Object[]> getEmpLvlAndDesc();
    
   /* public Map<String,String> getEmpLvlAndDesc()
    {
        String sql = "select level,leveldesc from WAPEmpLevelMaster where deldate='0000-00-00' "; 
        Map<String,String> map= jdbcTemplate.query(sql, new ResultSetExtractor<Map>(){
            public Map extractData(ResultSet rs) throws SQLException,DataAccessException {
                HashMap<String,String> mapRet= new HashMap<String,String>();
                while(rs.next()){
                    mapRet.put(rs.getString("level"),rs.getString("leveldesc"));
                }
                return mapRet;
            }
        });
        System.out.println("map of levels and emp : "+map);
        return map;
    }*/

	@Modifying
	@Transactional
	@Query(value = "update PARAM_MASTER set param_name = ?1, weightage = ?2 where param_id = ?3 ", nativeQuery = true)
	public int updateParam(String paramName, Float weightage, Integer paramId);
	
   /* public void updateParam(Parameters param)
    {
    	//System.out.println("--3--"+param.getParam_id());
        String sql = "update PARAM_MASTER set param_name =?, weightage=? where param_id=?";
        //Date date=getCurrentDateTime();
        jdbcTemplate.update(sql, new Object[]
        { param.getParam_name(), param.getWeightage(), param.getParam_id() });
    }*/
	
   
	@Query(value = "select param_id,param_name from param_master where isActive= '1' and level = ?1 order by param_id" , nativeQuery = true)
	public List<Object[]> getParamIdAndDesc(Integer level);
	
   /* public Map<String,String> getParamsIdDesc(String empLevel){
    	
    	String sql = "select param_id,param_name from param_master where isActive='1' and level=? "; 
        Map<String,String> map= jdbcTemplate.query(sql,new Object[]{empLevel}, new ResultSetExtractor<Map>(){
            public Map extractData(ResultSet rs) throws SQLException,DataAccessException {
                HashMap<String,String> mapRet= new HashMap<String,String>();
                while(rs.next()){
                    mapRet.put(rs.getString("param_id"),rs.getString("param_name"));
                }
                return mapRet;
            }
        });
        System.out.println("map of param id and desc : "+map);
        return map;
    }*/
	@Query(value = "select param_id,weightage from param_master where isActive= '1' and level = ?1 order by param_id" , nativeQuery = true)
	public List<Object[]> getParamIdAndWeightage(Integer level);
	
	@Query(value = "select paramId,paramName,weightage from ParamMaster where isActive= '1' and level = ?1 order by param_id")
	public List<Object[]> getParamIdNameAndWeightage(Integer level);

	
	
}
