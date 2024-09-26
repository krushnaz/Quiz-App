package com.quiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quiz.model.Users;

@Repository
public interface UserRepo  extends JpaRepository<Users, Integer>{

	public Users findByUsername(String username);
	
	public void deleteById(long id);
	
	public Optional<Users> findById(long id);

	public boolean existsById(Long id);
	
	 @Query("SELECT COUNT(u) FROM Users u")
	    long countTotalUsers();

}
