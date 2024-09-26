package com.quiz.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quiz.model.Admins;
import com.quiz.model.Quiz;
import com.quiz.model.Users;
import com.quiz.service.AdminService;
import com.quiz.service.QuizService;
import com.quiz.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private QuizService quizService;
	
	@GetMapping("/adminDashboard")
	public String showDashboard(HttpSession session,Model model, RedirectAttributes redirectAtt) {
		redirectAtt.addFlashAttribute("errorMessage", "You can not access any page without login! Login First!...");
		if (session.getAttribute("username") == null) {
			return "redirect:/login";
		}
	    List<Quiz> quizzes = quizService.getAllQuizzes(); // Fetching all quizzes

		
		  long totalQuizzes = quizService.countTotalQuizzes();
	        long completedQuizzes = quizService.countCompletedQuizzes();
	        long totalUsers = userService.countTotalUsers();

	        model.addAttribute("totalQuizzes", totalQuizzes);
	        model.addAttribute("completedQuizzes", completedQuizzes);
	        model.addAttribute("totalUsers", totalUsers);
	        model.addAttribute("quizzes", quizzes); // Adding quizzes to model

		return "adminDashboard";
	}

	@GetMapping("/adminProfile")
	public String getAdminProfile(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");

		if (username != null) {
			Admins admin = adminService.getAdminDetails(username);
			if (admin != null) {
				model.addAttribute("admin", admin); // Ensure 'admin' is not null
			} else {
				model.addAttribute("errorMessage", "Admin not found.");
			}
		} else {
			model.addAttribute("errorMessage", "No admin is logged in.");
		}

		return "adminProfile";
	}

	@GetMapping("/manageUser")
	public String getManageUser(Model model) {
		List<Users> userList = adminService.getUserDetails();
		model.addAttribute("users", userList);
		model.addAttribute("user", new Users()); 
		return "manageUser";
	}


	 @PostMapping("/user/delete")
	    public String deleteUser(@RequestParam("id") Long id, Model model) {
	        boolean isDeleted = userService.deleteUserById(id);
	        if (isDeleted) {
	            model.addAttribute("message", "User deleted successfully!");
	        } else {
	            model.addAttribute("message", "Failed to delete the user.");
	        }
	        return "redirect:/manageUser";
	    }
	 
}
