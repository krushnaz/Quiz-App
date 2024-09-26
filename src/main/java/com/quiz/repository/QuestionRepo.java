package com.quiz.repository;

import com.quiz.model.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Questions, Long> {
    
    // Find questions by quiz ID
    List<Questions> findByQuizId(Long quizId);
}