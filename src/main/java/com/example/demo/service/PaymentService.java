package com.example.demo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.model.PaymentRequest;

@Service
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    // Mock payment processing
    public boolean processPayment(PaymentRequest paymentRequest) {
        LOGGER.info("Processing payment for User ID: {}, Subscription ID: {}, Method: {}, Amount: {}",
                paymentRequest.getUserId(), paymentRequest.getSubscriptionId(),
                paymentRequest.getPaymentMethod(), paymentRequest.getAmount());

        // Simulate random success or failure
        boolean success = Math.random() > 0.2; // 80% success rate
        if (success) {
            LOGGER.info("Payment successful for User ID: {}", paymentRequest.getUserId());
        } else {
            LOGGER.error("Payment failed for User ID: {}", paymentRequest.getUserId());
        }
        return success;
    }
}
