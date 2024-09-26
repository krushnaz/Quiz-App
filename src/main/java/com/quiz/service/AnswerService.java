package com.quiz.service;

import com.quiz.model.Questions;
import com.quiz.model.Quiz;
import com.quiz.repository.QuestionRepo;
import com.quiz.repository.QuizRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnswerService {

    @Autowired
    private QuestionRepo questionRepository;

    @Autowired
    private QuizRepo quizRepository;

    public int calculateScore(Long quizId, Map<Long, String> userAnswers) {
        int correctAnswersCount = 0;

        // Fetch the quiz
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz not found");
        }

        // Fetch questions for the quiz
        List<Questions> questions = questionRepository.findByQuizId(quizId);

        // Compare user answers with correct answers
        for (Questions question : questions) {
            String correctAnswer = question.getCorrectAnswer();
            String userAnswer = userAnswers.get(question.getId());

            if (correctAnswer.equals(userAnswer)) {
                correctAnswersCount++;
            }
        }

        return correctAnswersCount; // Return the number of correct answers
    }
}
