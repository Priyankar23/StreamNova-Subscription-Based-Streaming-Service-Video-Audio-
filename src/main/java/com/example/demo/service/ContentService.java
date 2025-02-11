package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Content;
import com.example.demo.repo.ContentRepository;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;

    public Content addContent(Content content) {
        return contentRepository.save(content);
    }

    public Content updateContent(Long id, Content updatedContent) {
        Content content = contentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Content not found"));
        content.setTitle(updatedContent.getTitle());
        content.setDescription(updatedContent.getDescription());
        content.setGenre(updatedContent.getGenre());
        content.setRating(updatedContent.getRating());
        content.setImageUrl(updatedContent.getImageUrl());
        if (updatedContent.getUrl() != null) {
            content.setUrl(updatedContent.getUrl());
        }
        return contentRepository.save(content);
    }

    public void deleteContent(Long id) {
        if (!contentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Content not found");
        }
        contentRepository.deleteById(id);
    }

    public Content getContentById(Long id) {
        return contentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Content not found"));
    }

    // Fetch all content from the database
    public List<Content> getAllContent() {
        return contentRepository.findAll();
    }
    
}
