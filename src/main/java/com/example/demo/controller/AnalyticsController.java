package com.example.demo.controller;

import com.example.demo.model.Content;
import com.example.demo.model.UserPlayHistory;
import com.example.demo.repo.UserPlayHistoryRepository;
import com.example.demo.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;
@Autowired
private UserPlayHistoryRepository userPlayHistoryRepository;
    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<Content>> getRecommendations(@PathVariable Long userId) {
        List<Content> recommendations = analyticsService.getRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }
    
    @PostMapping("/track")
    public ResponseEntity<String> trackUserActivity(
        @RequestParam Long userId,
        @RequestParam Long contentId,
        @RequestHeader("Authorization") String token) {
        try {
            // Extract role from JWT token
            String jwtToken = token.replace("Bearer ", "");
            String role = analyticsService.extractRoleFromToken(jwtToken);

            // Allow only admin users
            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                     .body("Access denied. Only admins can track user activity.");
            }

            // Track user activity
            analyticsService.trackUserView(userId, contentId);
            return ResponseEntity.ok("User activity tracked successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error tracking user activity.");
        }
    }
    @GetMapping("/history")
    public ResponseEntity<List<UserPlayHistory>> getUserPlayHistory(@RequestHeader("Authorization") String token) {
        try {
            // Extract role from JWT token
            String jwtToken = token.replace("Bearer ", "");
            String role = analyticsService.extractRoleFromToken(jwtToken);

            // Allow only admin users
            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                     .body(null);
            }

            List<UserPlayHistory> history = userPlayHistoryRepository.findAll();
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
