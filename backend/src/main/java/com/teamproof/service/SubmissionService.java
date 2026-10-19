package com.teamproof.service;

import com.teamproof.entity.Submission;
import com.teamproof.entity.Task;
import com.teamproof.repository.SubmissionRepository;
import com.teamproof.repository.TaskRepository;
import com.teamproof.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SubmissionService handles logic for submitting task evidence and reviewing submissions.
 */
@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository,
                             TaskRepository taskRepository,
                             UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public Optional<Submission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }

    public List<Submission> getSubmissionsByTaskId(Long taskId) {
        return submissionRepository.findByTaskId(taskId);
    }

    public Submission createSubmission(Submission submission) {
        if (submission.getTaskId() == null) {
            throw new IllegalArgumentException("Task ID is required");
        }
        Task task = taskRepository.findById(submission.getTaskId())
                .orElseThrow(() -> new IllegalArgumentException("Referenced Task does not exist with ID: " + submission.getTaskId()));

        if (submission.getSubmittedBy() == null) {
            throw new IllegalArgumentException("SubmittedBy user ID is required");
        }
        if (!userRepository.existsById(submission.getSubmittedBy())) {
            throw new IllegalArgumentException("Referenced User does not exist with ID: " + submission.getSubmittedBy());
        }
        if (submission.getDescription() == null || submission.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Submission description or evidence notes cannot be empty");
        }

        submission.setStatus("PENDING");
        submission.setSubmittedAt(LocalDateTime.now());
        Submission saved = submissionRepository.save(submission);

        // When a student submits work, mark task status as UNDER_REVIEW
        task.setStatus("UNDER_REVIEW");
        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        return saved;
    }

    public Submission reviewSubmission(Long submissionId, String decision) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found with ID: " + submissionId));

        if (decision == null || decision.trim().isEmpty()) {
            throw new IllegalArgumentException("Review decision is required (APPROVED or CHANGES_REQUESTED)");
        }

        String normalized = decision.trim().toUpperCase();
        if (!normalized.equals("APPROVED") && !normalized.equals("CHANGES_REQUESTED")) {
            throw new IllegalArgumentException("Invalid review decision: must be APPROVED or CHANGES_REQUESTED");
        }

        submission.setStatus(normalized);
        Submission updated = submissionRepository.save(submission);

        // Update task status according to review
        taskRepository.findById(submission.getTaskId()).ifPresent(task -> {
            if ("APPROVED".equals(normalized)) {
                task.setStatus("COMPLETED");
            } else {
                task.setStatus("IN_PROGRESS");
            }
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);
        });

        return updated;
    }
}
