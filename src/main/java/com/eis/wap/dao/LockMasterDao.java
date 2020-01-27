package com.eis.wap.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.LockMasterDomain;

public interface LockMasterDao extends JpaRepository<LockMasterDomain, Integer>{

	/*public List<LockMaster> getAllLocklist(String startdate,String endDate,String condn)
	{//todo get financial start date from session and current date in yrmth
		startdate=	startdate.split("-")[2]+startdate.split("-")[1]; //from yrmth
		//System.out.println(startdate);
		endDate= endDate.split("-")[2]+endDate.split("-")[1]; //to yrmth
		//System.out.println(endDate);
		String sql="";
		if(condn.equalsIgnoreCase("all")){
			sql = "select * from lockmaster where yrmth>=? and yrmth<=? order by yrmth";
		}else if(condn.equalsIgnoreCase("locked")){
			sql = "select * from lockmaster where yrmth>=? and yrmth<=? and isdataLocked='Y' order by yrmth";
		}
		else{
			sql = "select * from lockmaster where yrmth>=? and yrmth<=? and isdataLocked!='Y' order by yrmth";
		}
		List<LockMaster> lockList = jdbcTemplate.query(sql,new Object[] {startdate,endDate}, new ResultSetExtractor<List<LockMaster>>()
		{
			// @Override
			public List<LockMaster> extractData(ResultSet rs) throws SQLException, DataAccessException
			{
				List<LockMaster> list = new ArrayList<LockMaster>();
				while (rs.next())
				{
					LockMaster lockmst = new LockMaster();
					lockmst.setLockId(rs.getInt(1));
					lockmst.setDivId(rs.getString(2));
					lockmst.setDiv_name(rs.getString(3));
					lockmst.setMth(rs.getString(4));
					lockmst.setMonthnm(CommonUtils.getMonth(lockmst.getMth()));
					lockmst.setYr(rs.getString(5));
					lockmst.setYrmth(rs.getString(6));
					lockmst.setIsdataLocked(rs.getString(7));
					lockmst.setLastModBy(rs.getString(8));
					lockmst.setLastModDt(rs.getString(9));
					list.add(lockmst);
				}
				return list;
			}
		});
		return lockList;
	}
*/
	
	@Query(value = "select * from lockmaster where yrmth >= ?1 and yrmth< = ?2 and divid=?3 order by yrmth", nativeQuery = true)
	public List<LockMasterDomain> getAllLockMasterData(String startYrMth, String endYrMth,int divId);
	
	
	@Query(value = "select * from lockmaster where yrmth>=?1 and yrmth<=?2 and isdataLocked='Y' and divid=?3 order by yrmth" , nativeQuery = true)
	public List<LockMasterDomain> getLockedData(String startYrMth, String endYrMth,int divId);
	
	@Query(value = "select * from lockmaster where yrmth>=?1 and yrmth<=?2 and isdataLocked!='Y' and divid=?3 order by yrmth" , nativeQuery = true)
	public List<LockMasterDomain> getUnlockedData(String startYrMth, String endYrMth,int divId);
	
	@Modifying
	@Transactional
	@Query(value = "update lockmaster set isDataLocked='N', lastModBy=?1 , lastmodDt=current_timestamp() where  yrmth>=?2 and isDataLocked='Y' and divId=?3 ", nativeQuery = true)
	public int unlockLockedEntries(String modBy, String yrMth, int divId);

	/*public String unlockLockedEntries(String mth,String yr){
		 System.out.println("--unlockLockedEntries--");
		 ParamDao param=new ParamDao();
		 Date date=param.getCurrentDateTime();
		 String yrmth=yr+mth;
		 String sql="update lockmaster set isDataLocked='N',lastModBy=?,lastmodDt=? where  yrmth>=? and isDataLocked='Y'";
		 jdbcTemplate.update(sql,new Object[] {"EIS",date,yrmth});
		 return "success";
	 }
	 */
	
	@Modifying
	@Transactional
	@Query(value = "update lockmaster set isDataLocked ='Y', lastModBy = ?1 , lastmodDt = ?2, noOfAttmpt =(noOfAttmpt+1) where  yrmth< = ?3 and isDataLocked! = 'Y' and divId=?4 " , nativeQuery = true)
	public int lockUnlockedEntries(String modBy, Date modDate, String yrMth, int divId);
	
	@Query(value = "select isDataLocked from lockmaster where mth=?1 and yr=?2  and divid=?3" , nativeQuery = true)
	public String isWapProcessed(String mth, String yr, int divId);
	
	@Query(value = "select yrmth from lockmaster where yrmth>=?1 and yrmth<=?2 and isdataLocked='Y'  and divid=?3 order by yrmth" , nativeQuery = true)
	public List<String> getLockedYrMth(String startYrMth, String endYrMth, int divId);
	
	@Query(value = "select mth from lockmaster where yrmth>=?1 and yrmth<=?2 and isdataLocked='Y'  and divid=?3 order by yrmth" , nativeQuery = true)
	public List<String> getLockedMthForFinYr(String startYrMth, String endYrMth, int divId);
	
	@Modifying
	@Transactional
	@Query(value = "update lockmaster set isDataLocked =?1, lastModBy =?2 , lastmodDt =?3 , noOfAttmpt =(noOfAttmpt+1) where  yrmth=?4  and divId=?5 " , nativeQuery = true)
	public int updateToLock(String isDataLocked,String modBy, Date modDate, String yrMth, int divId);
	
	
	@Query(value = "select isDataLocked from LockMasterDomain where yrmth=?1  and divid=?2")
	public String getLockStatus(String yrMth, int divId);
	
	/*@Query(value = "insert into lockmaster (divID,Mth,Yr,YrMth,isDataLocked,latModeBy,LastModDt) values(?1,?2,?3,?4,?5,?6,?7)" , nativeQuery = true)
	public List<String> insertIntoLockMaster(String startYrMth, String endYrMth);*/
}
