package com.quiz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.model.Questions;
import com.quiz.model.Quiz;
import com.quiz.model.QuizCompletion;
import com.quiz.model.Users;
import com.quiz.repository.QuestionRepo;
import com.quiz.repository.QuizCompletionRepo;
import com.quiz.repository.QuizRepo;

@Service
public class QuizService {

	@Autowired
	private QuizRepo quizRepository;
	@Autowired
	QuizCompletionRepo quizCompletionRepo;
	@Autowired
	QuestionRepo questionRepo;
	
	@Autowired
	QuizCompletionRepo completionRepo;

	public List<Quiz> getAllQuizzes() {
		return quizRepository.findAll();
	}

	public Quiz saveQuiz(Quiz quiz) {
		return quizRepository.save(quiz);
	}

	public Optional<Quiz> getQuizById(Long id) {
		return quizRepository.findById(id);
	}

	public void deleteQuiz(Long id) {
		quizRepository.deleteById(id);
	}

	public Quiz findById(Long quizId) {
		Quiz quiz = quizRepository.findById(quizId).orElse(null);
		System.out.println("Quiz fetched: " + quiz); // Debugging
		return quiz;
	}

	public boolean isQuizCompleted(Users user, Quiz quiz) {
		return quizCompletionRepo.findByUserAndQuiz(user, quiz).map(QuizCompletion::isCompleted).orElse(false);
	}

	public void markQuizAsCompleted(Users user, Quiz quiz) {
		QuizCompletion quizCompletion = quizCompletionRepo.findByUserAndQuiz(user, quiz).orElseGet(() -> {
			QuizCompletion newQuizCompletion = new QuizCompletion();
			newQuizCompletion.setUser(user);
			newQuizCompletion.setQuiz(quiz);
			newQuizCompletion.setCompleted(false);
			return newQuizCompletion;
		});

		quizCompletion.setCompleted(true);
		quizCompletionRepo.save(quizCompletion);
	}

	public int calculateScore(Map<Long, String> correctAnswers, Map<Long, String> userAnswers) {
	    int score = 0;

	    for (Map.Entry<Long, String> entry : userAnswers.entrySet()) {
	        Long questionId = entry.getKey();
	        String userAnswer = entry.getValue();

	        // Check if the correctAnswers map has the key
	        if (correctAnswers.containsKey(questionId)) {
	            String correctAnswer = correctAnswers.get(questionId);
	            if (correctAnswer.equals(userAnswer)) {
	                score++;
	            }
	        }
	    }

	    return score;
	}


	
	public Map<Long, String> getCorrectAnswersByQuizId(Long quizId) {
	    List<Questions> questions = questionRepo.findByQuizId(quizId);
	    Map<Long, String> correctAnswers = new HashMap<>();

	    for (Questions question : questions) {
	        correctAnswers.put(question.getId(), question.getCorrectAnswer());
	    }

	    return correctAnswers;
	}

	
	 public long countTotalQuizzes() {
	        return quizRepository.countTotalQuizzes();
	    }

	    public long countCompletedQuizzes() {
	        return completionRepo.countCompletedQuizzes(); // Assuming you have a status or flag to check completed quizzes
	    }
}