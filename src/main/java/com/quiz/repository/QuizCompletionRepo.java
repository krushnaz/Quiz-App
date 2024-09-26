package com.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.quiz.model.QuizCompletion;
import com.quiz.model.Users;
import com.quiz.model.Quiz;

import java.util.Optional;

public interface QuizCompletionRepo extends JpaRepository<QuizCompletion, Long> {
    Optional<QuizCompletion> findByUserAndQuiz(Users user, Quiz quiz);
    boolean existsByUserIdAndQuizId(Long userId, Long quizId);
    @Query("SELECT qc FROM QuizCompletion qc WHERE qc.user.id = :userId AND qc.quiz.id = :quizId")
    QuizCompletion findByUserIdAndQuizId(@Param("userId") Long userId, @Param("quizId") Long quizId);
    
    @Query("SELECT COUNT(qc) FROM QuizCompletion qc WHERE qc.completed = true")
    long countCompletedQuizzes();

}
