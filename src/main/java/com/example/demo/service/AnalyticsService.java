package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.model.UserPlayHistory;
import com.example.demo.repo.ContentRepository;
import com.example.demo.repo.UserPlayHistoryRepository;
import com.example.demo.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private UserPlayHistoryRepository userPlayHistoryRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private JwtUtil jwtUtil;
    public void trackUserView(Long userId, Long contentId) {
    	
        UserPlayHistory history = new UserPlayHistory();
        history.setUserId(userId);
        history.setContentId(contentId);
        history.setViewTime(LocalDateTime.now());
        userPlayHistoryRepository.save(history);
    }

    public List<Content> getRecommendations(Long userId) {
        // Fetch user's play history
        List<UserPlayHistory> history = userPlayHistoryRepository.findByUserId(userId);

        if (history.isEmpty()) {
            // If no history, recommend trending content
            return contentRepository.findAll().stream().limit(10).collect(Collectors.toList());
        }

        // Find most-watched genres
        Map<String, Long> genreCount = history.stream()
            .map(h -> contentRepository.findById(h.getContentId()).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(Content::getGenre, Collectors.counting()));

        String favoriteGenre = genreCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

        // Recommend content from the favorite genre
        return contentRepository.findAll().stream()
            .filter(content -> favoriteGenre.equals(content.getGenre()))
            .limit(10)
            .collect(Collectors.toList());
    }
    public String extractRoleFromToken(String token) {
        try {
            return jwtUtil.extractRole(token); // Assuming you have a JwtUtil class with this method
        } catch (Exception e) {
            throw new RuntimeException("Invalid token.", e);
        }
    }

}
