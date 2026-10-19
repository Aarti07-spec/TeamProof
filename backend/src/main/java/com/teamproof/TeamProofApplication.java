package com.teamproof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the TeamProof Spring Boot application.
 * Starts the embedded Tomcat server on port 8080.
 */
@SpringBootApplication
public class TeamProofApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamProofApplication.class, args);
    }
}
