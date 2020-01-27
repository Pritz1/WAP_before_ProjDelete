package com.eis.wap.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eis.wap.model.AdminUser;
import com.eis.wap.model.Users;
import com.eis.wap.model.UsersForm;

@Service
public interface UserService {

	public List<Users> getUsers();
	public List<AdminUser> getAdminUsers(String dbname,String presentEcodes);
	public void addUsers(UsersForm userForm);
	public int updateUser(UsersForm userForm);
}
