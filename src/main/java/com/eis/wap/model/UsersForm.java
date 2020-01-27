package com.eis.wap.model;

import java.util.List;

public class UsersForm {
	
	private List<Users> users;
	private String addEdit;

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

	public String getAddEdit() {
		return addEdit;
	}

	public void setAddEdit(String addEdit) {
		this.addEdit = addEdit;
	}
}
