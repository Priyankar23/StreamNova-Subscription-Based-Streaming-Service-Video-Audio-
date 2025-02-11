package com.example.demo.service;


import org.springframework.stereotype.Service;

import com.example.demo.model.Subscription;
import com.example.demo.repo.SubscriptionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // Retrieve all subscription plans
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    // Add a new subscription plan
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    // Update an existing subscription plan
    public Subscription updateSubscription(Long id, Subscription subscriptionDetails) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id " + id));
        subscription.setName(subscriptionDetails.getName());
        subscription.setDescription(subscriptionDetails.getDescription());
        subscription.setPrice(subscriptionDetails.getPrice());
        subscription.setDurationInDays(subscriptionDetails.getDurationInDays());
        return subscriptionRepository.save(subscription);
    }

    // Delete a subscription plan
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }
}
