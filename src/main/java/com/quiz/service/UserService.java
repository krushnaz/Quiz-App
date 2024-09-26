package com.quiz.service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import java.nio.file.Path;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quiz.model.Users;
import com.quiz.repository.UserRepo;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    
    @Value("${profile.pic.location}")
    private String profilePicLocation;

    @PostConstruct
    public void init() {
        File file = new File(profilePicLocation);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Users saveUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public boolean validateUser(String username, String password) {
        Users user = userRepo.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // If the username exists and the password matches, return true
            return true;
        }

        // If the username doesn't exist or password doesn't match, return false
        return false;
    }


    // Fetch user by ID
    public Users getUserById(Long id) {
        Optional<Users> optionalUser = userRepo.findById(id);
        return optionalUser.orElse(null);
    }

    // Update user
    public void updateUser(Users user) {
        // Check if the user exists in the database
        Optional<Users> existingUser = userRepo.findById(user.getId());
        if (existingUser.isPresent()) {
            Users userToUpdate = existingUser.get();
            // Update fields
            userToUpdate.setName(user.getName());
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setEmail(user.getEmail());
            // If password is being updated, encode it
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepo.save(userToUpdate);
        }
    }

    @Transactional
    public boolean deleteUserById(Long id) {
        if (userRepo.existsById(id)) {
        	userRepo.deleteById(id);
            return true;
        }
        return false;
    }

	public Users getUserDetails(String username) {
		return userRepo.findByUsername(username);
	}
	
	 public Users findByUsername(String username) {
	        return userRepo.findByUsername(username);
	    }
	 
//	 public void updateProfilePicture(Users user, MultipartFile file) throws IOException {
//	        if (file.isEmpty()) {
//	            throw new IOException("File is empty");
//	        }
//
//	        // Generate a unique filename
//	        String filename = file.getOriginalFilename();
//	        Path filePath = Paths.get(profilePicLocation, filename);
//
//	        // Ensure directories are created
//	        Files.createDirectories(filePath.getParent());
//
//	        // Write file to the path
//	        Files.write(filePath, file.getBytes());
//
//	        // Update user with new profile picture URL
//	        user.setProfilePicUrl("/img/avatars/profile-pics/" + filename);
//	        saveUser(user);
//	        
//	        System.out.println("Profile Picture Location: " + profilePicLocation);
//	        System.out.println("File Path: " + filePath.toString());
//
//	    }

	    public void changePassword(Users user, String newPassword) {
	        user.setPassword(passwordEncoder.encode(newPassword));
	        saveUser(user);
	    }

	    public void updateUserDetails(Users user, String username, String email) {
	        user.setUsername(username);
	        user.setEmail(email);
	        saveUser(user);
	    }
	    
	    public long countTotalUsers() {
	        return userRepo.countTotalUsers();
	    }
}
