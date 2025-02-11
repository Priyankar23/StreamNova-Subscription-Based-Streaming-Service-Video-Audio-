package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Content;
import com.example.demo.model.SubscriptionContent;
import com.example.demo.repo.ContentRepository;
import com.example.demo.repo.SubscriptionContentRepository;

@Service
public class StreamingService {
    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private SubscriptionContentRepository subscriptionRepository;

    public String getStreamingUrl(Long contentId, Long userId) {
    	
    	
        // Validate subscription
        SubscriptionContent subscription = subscriptionRepository.findByUserIdAndContentId(userId, contentId)
            .orElseThrow(() -> new ResourceNotFoundException("No valid subscription found"));

        // Check if subscription is still valid
        if (subscription.getValidUntil().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("Subscription has expired");
        }

        // Return streaming URL
        Content content = contentRepository.findById(contentId)
            .orElseThrow(() -> new ResourceNotFoundException("Content not found"));
        return content.getUrl();
    }
}
