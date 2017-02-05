package com.niit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niit.dao.RoleDAO;
import com.niit.model.Roles;

@Service
public class RoleService {

	@Autowired
	private RoleDAO roleDAO;
	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}
	
	
	@Transactional
	public void add(Roles role) {
		this.roleDAO.add(role);
	}

}