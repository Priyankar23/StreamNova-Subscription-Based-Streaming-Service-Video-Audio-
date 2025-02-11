package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SubscriptionBasedStreamingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionBasedStreamingServiceApplication.class, args);
		 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        System.out.println("Encoded password for 'admin123': " + encoder.encode("admin123"));
	    }
	

}
