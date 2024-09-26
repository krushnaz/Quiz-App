package com.quiz.Controller;

import java.io.IOException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quiz.model.Quiz;
import com.quiz.model.QuizCompletion;
import com.quiz.model.Users;
import com.quiz.repository.QuizCompletionRepo;
import com.quiz.service.QuizService;
import com.quiz.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	QuizService quizService;

	@Autowired
	QuizCompletionRepo quizCompletionRepo;

	@GetMapping({ "/", "/login" })
	public String showLoginPage() {
		return "login";
	}

	@PostMapping("/register")
	public String registerUser(@RequestParam String name, @RequestParam String username, @RequestParam String email,
			@RequestParam String password, RedirectAttributes redirectAtt) {

		try {
			Users user = new Users();
			user.setName(name);
			user.setUsername(username);
			user.setEmail(email);
			user.setPassword(password);

			userService.saveUser(user);
			redirectAtt.addFlashAttribute("successMessage", "Registration successful!");
			return "redirect:/login";
		} catch (Exception e) {
			redirectAtt.addFlashAttribute("errorMessage", "Something went wrong. Please try again.");
			return "redirect:/login";
		}

	}

	// Display the dashboard or home page
	@GetMapping("/userDashboard")
	public String showDashboard(HttpSession session, RedirectAttributes redirectAtt) {
		redirectAtt.addFlashAttribute("errorMessage", "You can not access any page without login! Login First!...");
		if (session.getAttribute("username") == null) {

			return "redirect:/login";
		}
		return "userDashboard";
	}

	@GetMapping("/quizzes")
	public String showQuizzes(Model model, HttpSession session) {
		// Get the username from the session
		String username = (String) session.getAttribute("username");

		if (username == null) {
			// Handle the case where the username is not available in the session
			return "redirect:/login"; // Redirect to login or an appropriate error page
		}

		// Fetch the user by username
		Users user = userService.findByUsername(username);

		if (user == null) {
			// Handle the case where the user is not found
			return "redirect:/login"; // Redirect to login or an appropriate error page
		}

		Long userId = user.getId();
		List<Quiz> quizzes = quizService.getAllQuizzes();

		// Map to hold quiz completion status
		Map<Long, Boolean> quizCompletionStatus = new HashMap<>();

		for (Quiz quiz : quizzes) {
			// Check if the quiz is completed by the current user
			QuizCompletion quizCompletion = quizCompletionRepo.findByUserIdAndQuizId(userId, quiz.getId());
			boolean completed = quizCompletion != null && quizCompletion.isCompleted();
			quizCompletionStatus.put(quiz.getId(), completed);
		}

		model.addAttribute("quizzes", quizzes);
		model.addAttribute("quizCompletionStatus", quizCompletionStatus);
		return "quizzes"; // The name of your Thymeleaf template
	}

	@GetMapping("/userProfile")
	public String showUserProfile(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");

		if (username != null) {
			Users user = userService.getUserDetails(username);
			if (user != null) {
				model.addAttribute("user", user); // Ensure 'admin' is not null
			} else {
				model.addAttribute("errorMessage", "Admin not found.");
			}
		} else {
			model.addAttribute("errorMessage", "No admin is logged in.");
		}

		return "userProfile";

	}

	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes redirectAtt) {
		session.invalidate(); // Clear the session
		redirectAtt.addFlashAttribute("successMessage", "Logout successful!");
		return "redirect:/login";
	}
//
//	@PostMapping("/upload-profile-pic")
//	public String uploadProfilePic(@RequestParam("profilePic") MultipartFile file, @RequestParam("username") String username, RedirectAttributes redirectAttributes) {
//	    try {
//	        Users user = userService.findByUsername(username);
//	        if (user != null) {
//	            userService.updateProfilePicture(user, file);
//	            user.setUsername(username);
//	            userService.saveUser(user);
//	            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
//	        } else {
//	            redirectAttributes.addFlashAttribute("error", "User not found!");
//	        }
//	    } catch (IOException e) {
//	        redirectAttributes.addFlashAttribute("error", "Failed to upload profile picture!");
//	        e.printStackTrace();
//	    }
//	    return "redirect:/userProfile";
//	}


}
