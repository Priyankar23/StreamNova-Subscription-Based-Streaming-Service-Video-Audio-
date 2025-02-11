package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.SubscriptionContent;

public interface SubscriptionContentRepository extends JpaRepository<SubscriptionContent, Long> {
    Optional<SubscriptionContent> findByUserIdAndContentId(Long userId, Long contentId);
}