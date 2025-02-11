package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
	Optional<Content> findByUrl(String url);
}


