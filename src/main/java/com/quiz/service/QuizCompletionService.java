package com.quiz.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.model.Quiz;
import com.quiz.model.QuizCompletion;
import com.quiz.model.Users;
import com.quiz.repository.QuizCompletionRepo;

@Service
public class QuizCompletionService {

    @Autowired
    private QuizCompletionRepo quizCompletionRepository;

    public void markQuizAsCompleted(Users user, Quiz quiz) {
        QuizCompletion completion = new QuizCompletion();
        completion.setUser(user);
        completion.setQuiz(quiz);
        completion.setCompleted(true);
        quizCompletionRepository.save(completion);
    }

    public boolean checkQuizCompletion(Users user, Quiz quiz) {
        Optional<QuizCompletion> completion = quizCompletionRepository.findByUserAndQuiz(user, quiz);
        return completion.map(QuizCompletion::isCompleted).orElse(false);
    }
    
    public QuizCompletion save(QuizCompletion quizCompletion) {
        return quizCompletionRepository.save(quizCompletion);
    }
    
    public boolean checkIfQuizCompleted(Long userId, Long quizId) {
        return quizCompletionRepository.existsByUserIdAndQuizId(userId, quizId);
    }
    
}
