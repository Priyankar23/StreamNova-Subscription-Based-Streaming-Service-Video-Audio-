package com.example.demo.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Content;
import com.example.demo.model.PaymentRequest;
import com.example.demo.model.Subscription;
import com.example.demo.model.SubscriptionContent;
import com.example.demo.model.User;
import com.example.demo.model.UserSubscription;
import com.example.demo.repo.ContentRepository;
import com.example.demo.repo.SubscriptionContentRepository;
import com.example.demo.repo.SubscriptionRepository;
import com.example.demo.repo.UserSubscriptionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserSubscriptionService {

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private SubscriptionContentRepository subscriptionContentRepository;

    @Autowired
    private PaymentService paymentService;

    public UserSubscription purchaseSubscription(Long userId, Long subscriptionId, PaymentRequest paymentRequest) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
        		
                .orElseThrow(() -> new RuntimeException("Subscription not found with id " + subscriptionId));

        if (!paymentService.processPayment(paymentRequest)) {
            throw new RuntimeException("Payment failed. Subscription purchase aborted.");
        }

        // Create UserSubscription manually
        User user = new User();
        user.setId(userId); // Set user ID only (assuming it's retrieved elsewhere)

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setUser(user);
        userSubscription.setSubscription(subscription);
        userSubscription.setStartDate(LocalDate.now());
        userSubscription.setEndDate(LocalDate.now().plusDays(subscription.getDurationInDays()));

        // Save UserSubscription
        UserSubscription savedSubscription = userSubscriptionRepository.save(userSubscription);

        // Link all content to this subscription
        linkAllContentToSubscription(userId, subscription.getDurationInDays());

        return savedSubscription;
    }

    private void linkAllContentToSubscription(Long userId, int durationInDays) {
        List<Content> allContent = contentRepository.findAll();

        for (Content content : allContent) {
            SubscriptionContent subscriptionContent = new SubscriptionContent();
            subscriptionContent.setUserId(userId);
            subscriptionContent.setContentId(content.getId());
            subscriptionContent.setValidUntil(LocalDate.now().plusDays(durationInDays).atStartOfDay());

            subscriptionContentRepository.save(subscriptionContent);
        }
    }
    public UserSubscription renewSubscription(Long userId, Long subscriptionId, PaymentRequest paymentRequest) {
        // First, check if the user has the given subscription
        Optional<UserSubscription> userSubscriptionOpt = userSubscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId);
        if (userSubscriptionOpt.isEmpty()) {
            throw new RuntimeException("No active subscription found for the user with subscription ID " + subscriptionId);
        }
        UserSubscription userSubscription = userSubscriptionOpt.get();

        // Proceed with payment and renewal
        if (!paymentService.processPayment(paymentRequest)) {
            throw new RuntimeException("Payment failed. Subscription renewal aborted.");
        }

        // Update the end date for the subscription
        userSubscription.setEndDate(userSubscription.getEndDate().plusDays(userSubscription.getSubscription().getDurationInDays()));
        UserSubscription updatedSubscription = userSubscriptionRepository.save(userSubscription);

        // Extend content validity or link new content
        extendContentValidity(userId, userSubscription.getSubscription().getDurationInDays());

        return updatedSubscription;
    }


    private void extendContentValidity(Long userId, int durationInDays) {
        List<Content> allContent = contentRepository.findAll();

        for (Content content : allContent) {
            SubscriptionContent subscriptionContent = subscriptionContentRepository.findByUserIdAndContentId(userId, content.getId())
                    .orElseGet(() -> {
                        SubscriptionContent newSubscriptionContent = new SubscriptionContent();
                        newSubscriptionContent.setUserId(userId);
                        newSubscriptionContent.setContentId(content.getId());
                        return newSubscriptionContent;
                    });

            subscriptionContent.setValidUntil(LocalDate.now().plusDays(durationInDays).atStartOfDay());
            subscriptionContentRepository.save(subscriptionContent);
        }
    }
 // Check if the user has an active subscription
    public boolean hasActiveSubscription(Long userId) {
        return userSubscriptionRepository.existsByUserId(userId); // Assuming this method checks for an active subscription
    }
    public long getRemainingSubscriptionDays(Long userId) {
        Optional<UserSubscription> userSubscriptionOpt = userSubscriptionRepository.findByUserId(userId);
        if (userSubscriptionOpt.isPresent()) {
            UserSubscription userSubscription = userSubscriptionOpt.get();
            LocalDate endDate = userSubscription.getEndDate();
            LocalDate today = LocalDate.now();
            
            // Calculate remaining days
            return today.isBefore(endDate) ? java.time.temporal.ChronoUnit.DAYS.between(today, endDate) : 0;
        }
        return 0;
    }

    
}
