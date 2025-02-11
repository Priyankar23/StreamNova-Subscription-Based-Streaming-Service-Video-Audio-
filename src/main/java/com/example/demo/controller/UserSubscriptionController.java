package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.PaymentRequest;
import com.example.demo.model.UserSubscription;
import com.example.demo.service.UserSubscriptionService;
import com.example.demo.util.JwtUtil;

@RestController
@RequestMapping("/subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;
    private final JwtUtil jwtUtil;  // Assuming you have a JwtUtil to extract roles and usernames

    public UserSubscriptionController(UserSubscriptionService userSubscriptionService, JwtUtil jwtUtil) {
        this.userSubscriptionService = userSubscriptionService;
        this.jwtUtil = jwtUtil;
    }

    // Purchase subscription - Only USER role is allowed
    @PostMapping("/purchase")
    public ResponseEntity<UserSubscription> purchaseSubscription(
            @RequestBody PaymentRequest paymentRequest,
            @RequestHeader("Authorization") String token) {
        
        try {
            // Extract role from token
            String jwtToken = token.replace("Bearer ", "");
            String role = jwtUtil.extractRole(jwtToken);

            // Ensure the request is made by a user (not admin)
            if (!"ROLE_USER".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(null); // Forbidden for non-users (admins)
            }

            // Proceed with purchase
            return ResponseEntity.ok(userSubscriptionService.purchaseSubscription(
                    paymentRequest.getUserId(),
                    paymentRequest.getSubscriptionId(),
                    paymentRequest
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Handle exception and return internal server error
        }
    }

    
    @PutMapping("/renew")
    public ResponseEntity<UserSubscription> renewSubscription(
            @RequestBody PaymentRequest paymentRequest,
            @RequestHeader("Authorization") String token) {
        
        try {
          
            String jwtToken = token.replace("Bearer ", "");
            String role = jwtUtil.extractRole(jwtToken);

           
            if (!"ROLE_USER".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(null); 
            }

            // Proceed with renewal
            return ResponseEntity.ok(userSubscriptionService.renewSubscription(
                    paymentRequest.getUserId(),
                    paymentRequest.getSubscriptionId(),
                    paymentRequest
            ));
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Handle exception and return internal server error
        }
    }
    
    @GetMapping("/check/{userId}")
    public ResponseEntity<Boolean> checkSubscriptionStatus(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
    	 try {
             
             String jwtToken = token.replace("Bearer ", "");
             String role = jwtUtil.extractRole(jwtToken);

            
             if (!"ROLE_USER".equals(role)) {
                 return ResponseEntity.status(HttpStatus.FORBIDDEN)
                         .body(null); 
             }

        boolean hasSubscription = userSubscriptionService.hasActiveSubscription(userId);
        return ResponseEntity.ok(hasSubscription);
    	 }catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
    }
}
    @GetMapping("/remaining-days/{userId}")
    public ResponseEntity<Long> getRemainingSubscriptionDays(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String role = jwtUtil.extractRole(jwtToken);

            if (!"ROLE_USER".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(null);
            }

            long remainingDays = userSubscriptionService.getRemainingSubscriptionDays(userId);
            return ResponseEntity.ok(remainingDays);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
