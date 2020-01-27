package com.eis.wap.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.Slabs;


public interface SlabDao extends JpaRepository<Slabs, Integer>{

	@Query(value = "select * from Slabs where param_id = ? order by slab_id" , nativeQuery = true)
	public List<Slabs> getSlabListByParamId(Integer paramId);

	/*public List<Slabs> getSlabListParamWise(int paramId)
    {
        String sql = "select * from Slabs where param_id=? order by slab_id";

        List<Slabs> slabList = jdbcTemplate.query(sql, new Object[]{paramId},new ResultSetExtractor<List<Slabs>>()
        {
            //@Override
            public List<Slabs> extractData(ResultSet rs) throws SQLException, DataAccessException
            {
                List<Slabs> list = new ArrayList<Slabs>();
                while (rs.next())
                {
                	Slabs slab = new Slabs();
                	slab.setSlabId(rs.getInt(1));
                	slab.setSlabName(rs.getString(2));
                	slab.setRangeMin(rs.getFloat(3));
                	slab.setRangeMax(rs.getFloat(4));
                	slab.setPoints(rs.getFloat(5));
                	slab.setParamId(rs.getInt(6));
                	slab.setSlabAddDt(rs.getDate(7));
                	slab.setAddedBy(rs.getString(8));
                	slab.setLastModDt(rs.getDate(9));
                	slab.setLastModBy(rs.getString(10));

                    list.add(slab);
                }
                return list;
            }

        });
        return slabList;
    }

	 */	 
	
	@Modifying
	@Transactional
	@Query(value = "update Slabs set slab_name = ?1, range_min = ?2 ,range_max = ?3 , points = ?4 ,"
			+ "last_mod_dt = ?5 ,last_mod_by = ?6 where param_id = ?7 and slab_id = ?8 ", nativeQuery = true)
	public int updateSlab(String slabName, Float minRange, Float maxRange, Float points, Date lastModDate, String lastModBy,
			Integer paramId, Integer slabId);

	/*public void updateSlab(Slabs slab)
	    {
	    	System.out.println("--3--"+slab.getSlabId());
	        String sql = "update slabs set slab_name=?, range_min=? ,range_max=?,points=?,last_mod_dt=?,last_mod_by=? where param_id=? and slab_id=?";
	        Date date=getCurrentDateTime();
	        jdbcTemplate.update(sql, new Object[]
	        {  slab.getSlabName(), slab.getRangeMin(),slab.getRangeMax(),slab.getPoints(),date,"EIS",slab.getParamId(),slab.getSlabId() });
	    }*/

	@Modifying
	@Transactional
	@Query(value = "Delete from Slabs where param_id = ? and slab_id = ?")
	public void deleteSlab(Integer paramId, Integer slabId);

	@Query(value = "select if((max(slab_id) is null),1,(max(slab_id)+1)) from Slabs", nativeQuery = true)
	public int getNextSlabId();
	
	@Query(value = "select param_id,range_min,range_max,points from Slabs order by param_id,range_min desc", nativeQuery = true)
	public List<Object[]> getParamWiseSlabsScore();
}
