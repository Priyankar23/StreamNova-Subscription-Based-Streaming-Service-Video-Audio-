package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.Userdto;
import com.example.demo.model.ResponseMessage;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Userdto userDTO) {
        if (userService.isUsernameTaken(userDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Username already taken!", null, null));
        }

        userService.registerNewUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail());
        return ResponseEntity.ok(new ResponseMessage("User registered successfully!", null, null));
    }

    @PostMapping("/admin-login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginDto loginDTO) {
        User admin = userService.loginAsAdmin(loginDTO.getUsername(), loginDTO.getPassword());
        if (admin != null) {
            String token = jwtUtil.generateToken(admin.getUsername(), "ROLE_ADMIN");
            return ResponseEntity.ok(new ResponseMessage("Login successful", "ROLE_ADMIN", token, admin.getId()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Invalid admin credentials!", null, null));
    }

    @PostMapping("/user-login")
    public ResponseEntity<?> userLogin(@RequestBody LoginDto loginDTO) {
        User user = userService.loginAsUser(loginDTO.getUsername(), loginDTO.getPassword());
        if (user != null) {
            String token = jwtUtil.generateToken(user.getUsername(), "ROLE_USER");
            return ResponseEntity.ok(new ResponseMessage("User login successful!", "ROLE_USER", token, user.getId(), user.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Invalid user credentials!", null, null));
    }

    @GetMapping("/protected-endpoint")
    public ResponseEntity<?> getProtectedData() {
        // Mock protected data for admin
        String protectedData = "This is protected data for the admin.";

        return ResponseEntity.ok(protectedData);
    }
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody Userdto userDTO, @RequestHeader("Authorization") String token) {
        try {
            // Extract and validate JWT token
            String jwtToken = token.replace("Bearer ", "");
            String username = jwtUtil.extractUsername(jwtToken);
            String role = jwtUtil.extractRole(jwtToken);

            // Ensure the request is made by a user and not an admin
            if (!"ROLE_USER".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ResponseMessage("Access denied. Profile update is only for users.", null, null));
            }

            // Validate that the username in the token matches the username in the request
            if (!username.equals(userDTO.getUsername())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseMessage("Invalid credentials for this action.", null, null));
            }

            // Perform the profile update
            User updatedUser = userService.updateUserProfile(userDTO.getUserId(), userDTO.getUsername(), 
                    userDTO.getEmail(), userDTO.getPassword(), userDTO.getPhoneNumber());

            if (updatedUser != null) {
                return ResponseEntity.ok(new ResponseMessage("Profile updated successfully!", null, null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("User not found!", null, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("An error occurred while updating the profile.", null, null));
        }
    }

}