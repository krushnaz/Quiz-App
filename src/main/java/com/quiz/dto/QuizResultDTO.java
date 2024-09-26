package com.quiz.dto;

import java.util.List;

import com.quiz.model.Quiz;
import com.quiz.model.Score;

public class QuizResultDTO {
    private Quiz quiz;
    private List<Score> scores;

    // Constructor
    public QuizResultDTO(Quiz quiz, List<Score> scores) {
        this.quiz = quiz;
        this.scores = scores;
    }

    // Getters and setters
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}
