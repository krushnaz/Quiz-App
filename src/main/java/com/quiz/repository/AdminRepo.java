package com.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quiz.model.Admins;

public interface AdminRepo extends JpaRepository<Admins, Integer>{

	Admins findByUsername(String username);
	
}
