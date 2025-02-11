package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>  {

}
