package com.quiz.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quiz.dto.QuizResultDTO;
import com.quiz.model.Quiz;
import com.quiz.model.Score;
import com.quiz.service.QuizService;
import com.quiz.service.ScoreService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ScoreController {
    @Autowired
    private ScoreService resultService;
    
   

    @GetMapping("/result")
    public String getAllResults(Model model, HttpSession session, RedirectAttributes redirectAtt) {
        // Check if the user is logged in
        if (session.getAttribute("username") == null) {
            redirectAtt.addFlashAttribute("errorMessage", "You cannot access any page without login! Login first!");
            return "redirect:/login";
        }

        // Retrieve username from session
        String username = (String) session.getAttribute("username");
        System.out.println(username);
        // Get all completed quizzes with their results
        List<QuizResultDTO> quizResults = resultService.getAllCompletedQuizzesWithResults(username);
        System.out.println("quiz :"+quizResults);
        // Add the quiz results to the model
        model.addAttribute("quizResults", quizResults);
        
        // Return the view for displaying results
        return "result";  // This should map to a "results.html" template in your view layer
    }
    

}
