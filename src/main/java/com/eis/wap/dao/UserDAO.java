package com.eis.wap.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.eis.wap.domain.UserMaster;

public interface UserDAO extends JpaRepository<UserMaster, String> , UserDAOCustom {
	
	@Query(value = "select * from user_master where isActive!='0'", nativeQuery = true)
	public List<UserMaster> getUsers();
	
/*	public List<Users> getUsers(){
		String sql = "select * from user_master where isActive!='0'";
		List<Users> userList = jdbcTemplate.query(sql,new ResultSetExtractor<List<Users>>()
		{
			//@Override
			public List<Users> extractData(ResultSet rs) throws SQLException, DataAccessException
			{
				List<Users> list = new ArrayList<Users>();
				while (rs.next())
				{
					Users userobj = new Users();
					userobj.setEcode(rs.getString(1));
					userobj.setEname(rs.getString(2));
					userobj.setAddedBy(rs.getString(3));
					userobj.setUserStatus(rs.getString(4));
					userobj.setRole(rs.getString(5));
					list.add(userobj);
				}
				return list;
			}
		});
		return userList;
	}*/ 
	
	@Query(value = "Select Svartxt from Sysprm where svarname=? " , nativeQuery = true)
	public String getFinancialStartDate(String svarname);
	
	@Transactional
	@Modifying
	@Query(value = "update user_master set isActive=?1, role=?2 where ecode=?3 and division=?4 " , nativeQuery = true)
	public int updateUser(String status , Integer role, String ecode, String division);
	
}
