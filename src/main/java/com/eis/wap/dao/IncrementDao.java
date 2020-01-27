package com.eis.wap.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.Increment;

public interface IncrementDao extends JpaRepository<Increment, Integer>{

	@Query(value = "select * from Increment order by incr_id ", nativeQuery = true)	
	public List<Increment> showAllIncrement();


	/*{
      String sql = "select * from Increment order by incr_id";
      List<Increment> incrList = jdbcTemplate.query(sql, new ResultSetExtractor<List<Increment>>()
      {
         // @Override
          public List<Increment> extractData(ResultSet rs) throws SQLException, DataAccessException
          {

              List<Increment> list = new ArrayList<Increment>();
              while (rs.next())
              {
            	Increment incr = new Increment();
            	try{
              	incr.setIncr_id(rs.getInt(1));
              	incr.setIncr_desc(rs.getString(2));
              	incr.setMin_score(rs.getFloat(3));
              	incr.setMax_score(rs.getFloat(4));
              	incr.setRating(rs.getString(5));
              	incr.setIncr_amount(rs.getFloat(6));
            	}catch(EmptyResultDataAccessException e){
                  	incr.setIncr_id(0);
                  	incr.setIncr_desc("No Data Found");
                  	incr.setMin_score(0.0F);
                  	incr.setMax_score(0.0F);
                  	incr.setRating("");
                  	incr.setIncr_amount(0.0F);
                  	//return incr;
                  }
              	list.add(incr);
              }
              return list;
          }
      });
      return incrList;
  }
	 */
	/*	public void addIncrement(Increment incr) {
		System.out.println("--1--");
		String sql = "INSERT INTO INCREMENT(incr_id,incr_desc,min_score,max_score,rating,incr_amount,addedby,add_date) values(?,?,?,?,?,?,?,?)";
		Integer id=getNextParamID();
		Date date=param.getCurrentDateTime();
		jdbcTemplate.update(sql, new Object[] { id,
				incr.getIncr_desc(),incr.getMin_score(),incr.getMax_score(),incr.getRating(),incr.getIncr_amount(),"EIS",date});
	}

	 */

	/*	
	public void updateIncrement(Increment incr)
	{
		System.out.println("--3--"+incr.getIncr_id());
		String sql = "update Increment set INCR_DESC=?,MIN_SCORE=?,MAX_SCORE=?,RATING=?,INCR_AMOUNT=?,LAST_MOD_BY=?,LAST_MOD_DT=? where INCR_ID=?";
		Date date=param.getCurrentDateTime();
		jdbcTemplate.update(sql, new Object[]
				{incr.getIncr_desc(), incr.getMin_score(),incr.getMax_score(),incr.getRating(),incr.getIncr_amount(),"EIS",date,incr.getIncr_id()});
	}

	 */
	
	@Modifying
	@Transactional
	@Query(value = "update Increment set INCR_DESC = ?1 , MIN_SCORE = ?2 , MAX_SCORE = ?3 ,"
			+ "RATING = ?4 , INCR_AMOUNT = ?5 , LAST_MOD_BY = ?6 , LAST_MOD_DT = ?7  where INCR_ID = ?8 " , nativeQuery = true)
	public int updateIncrement(String incrDesc, Float minScore, Float maxScore, String rating, Float incrAmount, String lastModBy,
			Date lastModDt, Integer incrId);


	@Modifying
	@Transactional
	@Query(value = "Delete from Increment where incr_id = ?1 ", nativeQuery = true)
	public void deleteIncrement(Integer incrementId);

	@Query(value = "select min_score,max_score,rating,incr_amount from Increment order by min_score desc ", nativeQuery = true)	
	public List<Object[]> getScoreWiseRatingIncr();
	
	@Query(value = "select minScore,maxScore,rating from Increment order by minScore ")	
	public List<Object[]> getRatings();
	
	
}
