package com.quiz.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quiz.model.Questions;
import com.quiz.model.Quiz;
import com.quiz.service.QuestionService;
import com.quiz.service.QuizService;

@Controller
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuizService quizService;

	@PostMapping("/save")
	public String saveQuestion(@ModelAttribute Questions question) {
		questionService.saveQuestion(question);
		return "redirect:/quiz/manage";
	}

	@PostMapping("/delete/{id}")
	public String deleteQuestion(@PathVariable Long id) {
		questionService.deleteQuestion(id);
		return "redirect:/quiz/manage";
	}

	@PostMapping("/addQuestion")
	public String addQuestion(
	        @RequestParam("quizId") Long quizId, 
	        @RequestParam("questionText") String questionText,
	        @RequestParam("option1") String option1, 
	        @RequestParam("option2") String option2,
	        @RequestParam("option3") String option3, 
	        @RequestParam("option4") String option4,
	        @RequestParam("correctAnswer") String correctAnswer, 
	        RedirectAttributes redirectAttributes) {
	    
	    // Fetch the quiz by ID
	    Optional<Quiz> quizOpt = quizService.getQuizById(quizId);
	    
	    // Check if the quiz is present
	    if (!quizOpt.isPresent()) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Quiz not found!");
	        return "redirect:/quiz/manage";
	    }

	    Quiz quiz = quizOpt.get();

	    // Create and save the new question
	    Questions question = new Questions();
	    question.setQuiz(quiz);
	    question.setQuestionText(questionText);
	    
	    // Store options in a list
	    List<String> options = Arrays.asList(option1, option2, option3, option4);
	    question.setOptions(options);
	    
	    question.setCorrectAnswer(correctAnswer);
	    questionService.saveQuestion(question);

	    redirectAttributes.addFlashAttribute("message", "Question added successfully!");
	    return "redirect:/quiz/manage";
	}



	@GetMapping("/questions/{quizId}")
	public ResponseEntity<List<Questions>> getQuestionsByQuizId(@PathVariable Long quizId) {
		List<Questions> questions = questionService.getQuestionsByQuizId(quizId);
		return ResponseEntity.ok(questions);
	}

	@GetMapping("/manage")
	public String getAllQuestionsByQuizId(@RequestParam("quizId") Long quizId, Model model) {
		// Fetch the questions by quiz ID
		List<Questions> questions = questionService.getQuestionsByQuizId(quizId);

		// Add the questions to the model to be used in the view
		model.addAttribute("questions", questions);

		// Optionally, you can also add the quiz to the model if needed in the view
		Quiz quiz = quizService.findById(quizId);
		model.addAttribute("allQuiz", quiz);

		return "manageQuiz"; // Return the view name
	}

}