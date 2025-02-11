package com.example.demo.model;

public class ResponseMessage {
    private String message;
    private String role;
    private String token;
    private Long userId;
    private String username;
    private User user; // Add the User object

    public ResponseMessage(String message, String role, String token, Long userId, String username) {
        super();
        this.message = message;
        this.role = role;
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    public ResponseMessage(String message, String role, String token, Long userId) {
        super();
        this.message = message;
        this.role = role;
        this.token = token;
        this.userId = userId;
    }

    // Default constructor
    public ResponseMessage() {
        super();
    }

    // Constructor with User and optional null handling
    public ResponseMessage(String message, User user, String role) {
        this.message = message;
        this.user = user;
        this.role = (role != null) ? role : "";  // Default to empty string if role is null
        this.token = "";  // Set default value or keep it empty as required
        this.userId = (user != null) ? user.getId() : null; // Extract userId from User if available
        this.username = (user != null) ? user.getUsername() : ""; // Extract username from User if available
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
