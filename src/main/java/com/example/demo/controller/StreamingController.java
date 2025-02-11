package com.example.demo.controller;

import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AnalyticsService;
import com.example.demo.service.StreamingService;
import com.example.demo.util.JwtUtil;
import com.example.demo.model.ResponseMessage;

@RestController
@RequestMapping("/stream")
public class StreamingController {

    @Autowired
    private StreamingService streamingService;

    @Autowired
    private AnalyticsService analyticsService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/media/{filename:.+}")
    public ResponseEntity<Resource> streamMedia(@PathVariable String filename, @RequestParam Long userId,
    	    @RequestParam Long contentId) {
        try {
        	 String decodedFilename = URLDecoder.decode(filename, "UTF-8");
             Path filePath = Paths.get("media/").resolve(decodedFilename).normalize();
        	
        	 analyticsService.trackUserView(userId, contentId);
        	 
           
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found or not readable: " + filename);
            }

            String contentType = Files.probeContentType(filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
