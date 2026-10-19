package com.teamproof.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents evidence submitted by a student for a task.
 */
@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "submitted_by", nullable = false)
    private Long submittedBy;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "evidence_url", length = 500)
    private String evidenceUrl;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, APPROVED, CHANGES_REQUESTED

    @Column(name = "submitted_at", updatable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    // Default constructor
    public Submission() {}

    // Convenience constructor
    public Submission(Long taskId, Long submittedBy, String description,
                      String evidenceUrl, String filePath, String fileName) {
        this.taskId = taskId;
        this.submittedBy = submittedBy;
        this.description = description;
        this.evidenceUrl = evidenceUrl;
        this.filePath = filePath;
        this.fileName = fileName;
        this.status = "PENDING";
        this.submittedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(Long submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvidenceUrl() {
        return evidenceUrl;
    }

    public void setEvidenceUrl(String evidenceUrl) {
        this.evidenceUrl = evidenceUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
