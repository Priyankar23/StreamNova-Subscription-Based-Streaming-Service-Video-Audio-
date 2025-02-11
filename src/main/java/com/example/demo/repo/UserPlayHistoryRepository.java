package com.example.demo.repo;

import com.example.demo.model.UserPlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPlayHistoryRepository extends JpaRepository<UserPlayHistory, Long> {
    List<UserPlayHistory> findByUserId(Long userId);
    
    boolean existsById(Long id);
}
