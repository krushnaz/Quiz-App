package com.quiz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.model.Users;
import com.quiz.model.Admins;
import com.quiz.repository.AdminRepo;
import com.quiz.repository.UserRepo;

@Service
public class AdminService {
	@Autowired
	private AdminRepo adminRepo;

	@Autowired
	private UserRepo userRepo;
	
	public boolean validateAdmin(String username, String password) {
		Admins admin = adminRepo.findByUsername(username);
		if (admin != null && password.equals(admin.getPassword())) {
			return true;
		}
		return false;
	}

	public Admins getAdminDetails(String username) {
        return adminRepo.findByUsername(username); 
	}

	public List<Users> getUserDetails() {
		return userRepo.findAll();
	}
}
