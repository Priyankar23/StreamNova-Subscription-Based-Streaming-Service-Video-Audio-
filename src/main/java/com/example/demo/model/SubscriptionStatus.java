package com.example.demo.model;

public class SubscriptionStatus {
    private boolean hasSubscription;
    private long remainingDays;

    // Getters and Setters
    public boolean isHasSubscription() {
        return hasSubscription;
    }

    public void setHasSubscription(boolean hasSubscription) {
        this.hasSubscription = hasSubscription;
    }

    public long getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(long remainingDays) {
        this.remainingDays = remainingDays;
    }
}
