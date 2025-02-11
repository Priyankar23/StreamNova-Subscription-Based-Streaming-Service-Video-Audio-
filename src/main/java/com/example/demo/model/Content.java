package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String genre;
    private String rating;
    private String url;
    
    private String imageUrl;
    public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	// Default constructor
    public Content() {}

    // Full constructor
    public Content(Long id, String title, String description, String genre, String rating, String url,String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.url = url;
        this.imageUrl=imageUrl;
    }
    public Content(String title, String description, String genre, String rating, String url) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.url = url;
    }

    // New constructor for error messages
    public Content(String message) {
        this.title = message; // Use the title field to hold the message
        this.description = null;
        this.genre = null;
        this.rating = null;
        this.url = null;
    }

    // Getters and setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
