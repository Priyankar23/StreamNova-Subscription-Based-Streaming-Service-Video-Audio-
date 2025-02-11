package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.User;

import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user
    public User registerNewUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found")));
        user.setRoles(roles);

        return userRepository.save(user);
    }

    // Check if the username exists
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // Admin login check
    public User loginAsAdmin(String username, String password) {
        // Precomputed bcrypt hash for "admin123"
        String encodedAdminPassword = "$2a$10$Vd.lwjSiqSFyjUt1N1cReOi7G2w8z4pj6RF641cJdWoa14P4/Ffi.";
        if ("admin".equals(username) && passwordEncoder.matches(password, encodedAdminPassword)) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encodedAdminPassword);

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("Role not found")));
            admin.setRoles(roles);

            return admin;
        }
        return null;
    }



    // User login check
    public User loginAsUser(String username, String password) {
        return userRepository.findByUsername(username)
            .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_USER")))
            .filter(user -> passwordEncoder.matches(password, user.getPassword()))
            .orElse(null);
    }
    public User updateUserProfile(Long userId, String username, String email, String password, Long phoneNo) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(username);
            user.setEmail(email);
            if (password != null && !password.isEmpty()) {
                user.setPassword(passwordEncoder.encode(password)); // Update password if it's provided
            }
            user.setPhoneNo(phoneNo);  // Update phone number
            return userRepository.save(user);
        }
        return null;  // User not found
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

}