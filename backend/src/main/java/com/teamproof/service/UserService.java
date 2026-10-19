package com.teamproof.service;

import com.teamproof.entity.User;
import com.teamproof.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * UserService provides operations for managing users.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be empty");
        }
        if (userRepository.existsByEmail(user.getEmail().trim())) {
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("User full name cannot be empty");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty()) {
            // Default placeholder hash for testing
            user.setPasswordHash("student123");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("STUDENT");
        }
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}
