package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.demo.model.UserSubscription;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    // Define the custom method to find UserSubscription by userId and subscriptionId
    Optional<UserSubscription> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId);

    // You can also keep this method if needed
    boolean existsByUserId(Long userId);
    Optional<UserSubscription> findByUserId(Long userId);
}
