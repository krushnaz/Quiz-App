package com.quiz.Controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quiz.dto.Answer;
import com.quiz.model.Questions;
import com.quiz.model.Quiz;
import com.quiz.model.QuizCompletion;
import com.quiz.model.Score;
import com.quiz.model.Users;
import com.quiz.repository.QuizCompletionRepo;
import com.quiz.service.AnswerService;
import com.quiz.service.QuestionService;
import com.quiz.service.QuizCompletionService;
import com.quiz.service.QuizService;
import com.quiz.service.ScoreService;
import com.quiz.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/quiz")
public class QuizController {

	@Autowired
	private QuizService quizService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	UserService userService;

	@Autowired
	QuizCompletionService quizCompletionService;

	@Autowired
	ScoreService scoreService;

	@GetMapping("/manage")
	public String manageQuizzes(Model model) {
		model.addAttribute("quizzes", quizService.getAllQuizzes());
		return "manageQuiz";
	}

	@PostMapping("/create")
	public String createQuiz(@ModelAttribute Quiz quiz, RedirectAttributes redirectAttributes) {
		quizService.saveQuiz(quiz);
		redirectAttributes.addFlashAttribute("message", "Quiz created successfully!");
		return "redirect:/quiz/manage";
	}

	@PostMapping("/update")
	public String updateQuiz(@RequestParam Long id, @RequestParam String title, @RequestParam String description,
			@RequestParam String date, RedirectAttributes redirectAttributes) {

		try {
			// Convert the date from String to LocalDate
			LocalDate localDate;
			try {
				localDate = LocalDate.parse(date);
			} catch (DateTimeParseException e) {
				redirectAttributes.addFlashAttribute("errorMessage", "Invalid date format.");
				return "redirect:/quiz/manage"; // Redirect to an appropriate page
			}

			// Find the existing quiz
			Quiz quiz = quizService.findById(id);
			if (quiz != null) {
				// Update quiz details
				quiz.setTitle(title);
				quiz.setDescription(description);
				quiz.setDate(localDate); // Set LocalDate instead of String

				// Save updated quiz
				quizService.saveQuiz(quiz);

				redirectAttributes.addFlashAttribute("successMessage", "Quiz updated successfully.");
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "Quiz not found.");
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "Error updating quiz.");
			e.printStackTrace(); // Log the exception for debugging
		}

		return "redirect:/quiz/manage"; // Redirect to a page showing the list of quizzes or another appropriate page
	}

	@PostMapping("/delete")
	public String deleteQuiz(@RequestParam Long id) {
		quizService.deleteQuiz(id);
		return "redirect:/quiz/manage";
	}

	@GetMapping("/{id}")
	public String getQuizById(@PathVariable("id") Long id, Model model) {
		// Fetch the quiz based on the provided ID
		Optional<Quiz> quizOpt = quizService.getQuizById(id);
		if (quizOpt.isPresent()) {
			Quiz quiz = quizOpt.get();
			model.addAttribute("quiz", quiz);

			// Fetch the list of questions for the quiz
			List<Questions> questions = questionService.getQuestionsByQuizId(id);
			model.addAttribute("questions", questions);

			// Fetch the list of answers for the quiz (if applicable)
//			List<Answer> answers = answerService.getAnswersForQuiz(id);
//			model.addAttribute("answers", answers);

			// Return the view for displaying the quiz
			return "quizStarted"; // This is the Thymeleaf template
		} else {
			model.addAttribute("errorMessage", "Quiz not found");
			return "error"; // Return an error page if quiz not found
		}
	}

	@PostMapping("/submitQuiz")
	public String submitQuiz(@RequestParam Long quizId, @RequestParam Map<String, String> userAnswers,
	                         HttpSession session, Model model, RedirectAttributes redirectAttributes) {
	    try {
	        // Get the current user from the session
	        String username = (String) session.getAttribute("username");

	        if (username == null) {
	            return "redirect:/login"; // Redirect to login if session expired
	        }

	        // Fetch correct answers
	        Map<Long, String> correctAnswers = quizService.getCorrectAnswersByQuizId(quizId);

	        // Convert userAnswers keys from String to Long by stripping off the prefix and suffix
	        Map<Long, String> convertedUserAnswers = new HashMap<>();
	        for (Map.Entry<String, String> entry : userAnswers.entrySet()) {
	            try {
	                // Extract the question ID from the key (e.g., "userAnswers[1]" -> "1")
	                String key = entry.getKey();
	                if (key.startsWith("userAnswers[")) {
	                    key = key.substring(12, key.length() - 1);
	                }
	                Long questionId = Long.parseLong(key); // Convert String to Long
	                convertedUserAnswers.put(questionId, entry.getValue());
	            } catch (NumberFormatException e) {
	                System.err.println("Invalid question ID format: " + entry.getKey());
	            }
	        }

	        // Calculate score
	        int score = quizService.calculateScore(correctAnswers, convertedUserAnswers);

	        // Save QuizCompletion record
	        Quiz quiz = quizService.findById(quizId);
	        QuizCompletion quizCompletion = new QuizCompletion();
	        quizCompletion.setUser(userService.getUserDetails(username));
	        quizCompletion.setQuiz(quiz);
	        quizCompletion.setCompleted(true);
	        quizCompletionService.save(quizCompletion);

	        // Save the score in the score table
	        Score userScore = new Score();
	        userScore.setUser(userService.getUserDetails(username));
	        userScore.setQuiz(quiz);
	        userScore.setScore(score);
	        userScore.setSubmissionDate(LocalDateTime.now()); // Set submission date to current date and time
	        scoreService.save(userScore);

	        // Add flash attributes
	        redirectAttributes.addFlashAttribute("successMessage", "Quiz submitted successfully.");
	        redirectAttributes.addFlashAttribute("showSuccessModal", true);

	        return "redirect:/quizzes";

	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Error occurred while submitting quiz!");
	        return "redirect:/quizzes";
	    }
	}

}
