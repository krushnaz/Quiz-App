package com.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quiz.model.Quiz;
import com.quiz.model.Score;
import com.quiz.model.Users;

@Repository
public interface ScoreRepo extends JpaRepository<Score, Long> {
    // Find all scores for a given user
    List<Score> findByUser(Users user);

    // Find all scores for a given quiz
    List<Score> findByQuiz(Quiz quiz);

    // Find scores by username and quiz
    List<Score> findByUser_UsernameAndQuiz(String username, Quiz quiz);
    
    // Find scores by user and quiz (using User object)
    List<Score> findByUserAndQuiz(Users user, Quiz quiz);
    
    boolean existsByUserAndQuiz(Users user, Quiz quiz);

  
}
