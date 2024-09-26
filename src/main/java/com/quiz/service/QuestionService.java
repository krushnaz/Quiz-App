package com.quiz.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.model.Questions;
import com.quiz.repository.QuestionRepo;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    // Save or update a question
    public Questions saveQuestion(Questions question) {
        return questionRepo.save(question);
    }

    // Find a question by its ID
    public Optional<Questions> getQuestionById(Long id) {
        return questionRepo.findById(id);
    }

    // Get all questions for a specific quiz
    public List<Questions> getQuestionsByQuizId(Long quizId) {
        List<Questions> questions = questionRepo.findByQuizId(quizId);
        System.out.println("Questions fetched: " + questions); // Debugging
        return questions;
    }

    // Delete a question by its ID
    public void deleteQuestion(Long id) {
    	questionRepo.deleteById(id);
    }
    
    public List<Questions> findByQuizId(Long quizId) {
        return questionRepo.findByQuizId(quizId);
    }

}