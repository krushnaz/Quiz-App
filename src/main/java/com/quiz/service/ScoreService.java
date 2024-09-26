package com.quiz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quiz.dto.QuizResultDTO;
import com.quiz.model.Quiz;
import com.quiz.model.Score;
import com.quiz.model.Users;
import com.quiz.repository.QuizRepo;
import com.quiz.repository.ScoreRepo;
import com.quiz.repository.UserRepo;

@Service
public class ScoreService {
	@Autowired
	private ScoreRepo scoreRepository;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private QuizRepo quizRepo;

	public void save(Score score) {
		scoreRepository.save(score);
	}

	public List<Score> findAll() {
		return scoreRepository.findAll();
	}

	public Score findById(Long id) {
		return scoreRepository.findById(id).orElse(null);
	}

	public List<Score> findByUser(Users user) {
		return scoreRepository.findByUser(user);
	}

	public List<Score> findByQuiz(Quiz quiz) {
		return scoreRepository.findByQuiz(quiz);
	}

	public List<Score> getQuizResult(String username, Quiz quiz) {
		return scoreRepository.findByUser_UsernameAndQuiz(username, quiz);
	}
	
	public List<QuizResultDTO> getAllCompletedQuizzesWithResults(String username) {
        Users user = userRepo.findByUsername(username);

        // Retrieve all quizzes
        List<Quiz> quizzes = quizRepo.findAll();

        // Filter quizzes that the user has completed and map them to QuizResultDTO
        return quizzes.stream()
	            .filter(quiz -> scoreRepository.existsByUserAndQuiz(user, quiz))
            .map(quiz -> {
                List<Score> scores = scoreRepository.findByUserAndQuiz(user, quiz);
                return new QuizResultDTO(quiz, scores);  // Assuming QuizResultDTO has a constructor accepting Quiz and List<Score>
            })
            .collect(Collectors.toList());
    }

}
