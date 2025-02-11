
package com.example.demo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Content;
import com.example.demo.service.ContentService;
import com.example.demo.util.JwtUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private JwtUtil jwtUtil;

    // Only admin should be able to add content
    @PostMapping
    public ResponseEntity<Content> addContent(@RequestBody Content content, @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String role = jwtUtil.extractRole(jwtToken);

            // Ensure the request is made by an admin
            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new Content(null));
            }

            // Perform the content addition
            return ResponseEntity.status(HttpStatus.CREATED).body(contentService.addContent(content));
        } catch (Exception e) {
        	  e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Content("An error occurred while adding content."));
        }
    }
    @PostMapping("/uploadMedia")
    public ResponseEntity<String> uploadMedia(@RequestParam("file") MultipartFile file, 
                                              @RequestParam("contentId") Long contentId,
                                              @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String role = jwtUtil.extractRole(jwtToken);

            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only admins can upload media.");
            }

            Content content = contentService.getContentById(contentId);

            // Save file locally
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadDir = "media/"; // Replace with your local folder path
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Update content with the file's path
            content.setUrl("http://localhost:8082/stream/media/" + fileName); // Relative URL
            contentService.updateContent(contentId, content);

            return ResponseEntity.ok("Media uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while uploading the media.");
        }
    }

    // Only admin should be able to update content
    @PutMapping("/{id}")
    public ResponseEntity<Content> updateContent(@PathVariable Long id, @RequestBody Content content, @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String role = jwtUtil.extractRole(jwtToken);

            // Ensure the request is made by an admin
            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new Content(null));
            }

            // Perform the content update
            return ResponseEntity.ok(contentService.updateContent(id, content));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Content("An error occurred while updating content."));
        }
    }

    // Only admin should be able to delete content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String role = jwtUtil.extractRole(jwtToken);

           
            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(null);
            }

            
            contentService.deleteContent(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Any authenticated user (admin or user) can view content by id
    @GetMapping("/{id}")
    public ResponseEntity<Content> getContent(@PathVariable Long id) {
        try {
            Content content = contentService.getContentById(id);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Content("Content not found."));
        }
    }

    // Any authenticated user (admin or user) can view all content
    @GetMapping
    public ResponseEntity<List<Content>> getAllContent() {
        try {
            List<Content> contentList = contentService.getAllContent();
            return ResponseEntity.ok(contentList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @PostMapping("/uploadimage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, 
                                              @RequestParam("contentId") Long contentId,
                                              @RequestHeader("Authorization") String token) {
        try {
            // Extract role from JWT token
            String jwtToken = token.replace("Bearer ", "");
            String role = jwtUtil.extractRole(jwtToken);

            // Ensure the request is made by an admin
            if (!"ROLE_ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You do not have permission to upload images.");
            }

            // Fetch content by ID
            Content content = contentService.getContentById(contentId);

            // Save the file to a directory (e.g., "uploads/")
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Update the content with the image URL
            content.setImageUrl("http://localhost:8082/content/images/" + fileName);
            contentService.updateContent(contentId, content);

            return ResponseEntity.ok("Image uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while uploading the image.");
        }
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads/").resolve(filename);
            System.out.println("Serving file: " + file.toAbsolutePath());
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
            	 String contentType = Files.probeContentType(file);
                return ResponseEntity.ok()
                		 .contentType(MediaType.parseMediaType(contentType)) 
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}