package com.quiz.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quiz.model.Users;
import com.quiz.service.AdminService;
import com.quiz.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserService userService;

	@PostMapping("/loginUser")
	public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAtt,
			HttpSession session) {
		boolean isValidUser = userService.validateUser(username, password);
		session.setAttribute("username", username);
		if (adminService.validateAdmin(username, password)) {
			return "redirect:/adminDashboard";
		} else if (userService.validateUser(username, password)) {
			return "redirect:/userDashboard";
		} else {
			redirectAtt.addFlashAttribute("errorMessage", "Invalid username or password. Please try again.");
			return "redirect:/login";
		}
	}


}
