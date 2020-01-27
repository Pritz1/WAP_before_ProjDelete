package com.eis.wap.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eis.wap.dao.UserDAO;
import com.eis.wap.domain.UserMaster;
import com.eis.wap.model.AdminUser;
import com.eis.wap.model.Users;
import com.eis.wap.model.UsersForm;
import com.eis.wap.service.UserService;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO userDAO;

	public List<Users> getUsers() {
		List<Users> usersList = null;
		Users user = null;
		List<UserMaster> userDataList = userDAO.getUsers();
		if(null != userDataList) {
			usersList = new ArrayList<Users>();
			for(UserMaster userMaster : userDataList) {
				user = new Users();
				user.setEcode(userMaster.geteCode());
				user.setEname(userMaster.geteName());
				user.setRole(String.valueOf(userMaster.getRole()));
				user.setUserStatus(userMaster.getIsActive());
				user.setAddedBy(userMaster.getAddedBy());
				usersList.add(user);
			}
		}

		return usersList;
	}


	public List<AdminUser> getAdminUsers(String dbname,String presentEcodes) {
		List<AdminUser> adminUserList = null;
		AdminUser adminUser = null;
		List<Object[]> adminUserDataList = userDAO.getAdminUsers(dbname+".empmst",presentEcodes);
		if(null != adminUserDataList && !adminUserDataList.isEmpty()) {
			adminUserList = new ArrayList<AdminUser>(); 
			for(Object[] adminData : adminUserDataList) {
				adminUser = new AdminUser();
				adminUser.setEcode((String) adminData[0]);
				adminUser.setEname((String) adminData[1]);
				adminUser.setEtype((String) adminData[2]);
				adminUserList.add(adminUser);
			}

		}
		return adminUserList;
	}

	public void addUsers(UsersForm userForm) {
		UserMaster userMaster = null;
		List<UserMaster> usersList = null;
		if(null != userForm && null != userForm.getUsers() && !userForm.getUsers().isEmpty()) {
			usersList = new ArrayList<UserMaster>();
			for(Users user : userForm.getUsers()) {
				userMaster = new UserMaster();
				userMaster.seteCode(user.getEcode());
				userMaster.seteName(user.getEname());
				userMaster.setAddedBy("EIS");
				userMaster.setIsActive(user.getUserStatus());
				userMaster.setRole(Integer.parseInt(user.getRole()));
				userMaster.setDivision("01"); // TODO: division should come from DB
				usersList.add(userMaster);
			}
			// saving all the users to DB
			userDAO.save(usersList);
		}
	}

	public int updateUser(UsersForm userForm) {
		Users user = null;
		int count = 0;
		if(null != userForm && null != userForm.getUsers() && !userForm.getUsers().isEmpty()) {
			user = userForm.getUsers().get(0);
			if(null != user) {
				count = userDAO.updateUser(user.getUserStatus(), Integer.parseInt(user.getRole()),
						user.getEcode(), "01");
				// TODO: get division from DB
			}
		}
		return count;
	}
}
