package com.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.quiz.model.Quiz;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Long> {
	  @Query("SELECT COUNT(q) FROM Quiz q")
	    long countTotalQuizzes();

	    
}