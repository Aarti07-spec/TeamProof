package com.teamproof.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing transparent activity indicators for a team member.
 * Designed explicitly to avoid opaque rankings and clearly communicate explainable evidence.
 */
public class MemberContributionDto {

    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private int assignedTasksCount;
    private int completedTasksCount;
    private BigDecimal totalEstimatedHours;
    private BigDecimal completedEstimatedHours;
    private int submissionsCount;
    private int approvedSubmissionsCount;

    public MemberContributionDto() {
        this.totalEstimatedHours = BigDecimal.ZERO;
        this.completedEstimatedHours = BigDecimal.ZERO;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAssignedTasksCount() {
        return assignedTasksCount;
    }

    public void setAssignedTasksCount(int assignedTasksCount) {
        this.assignedTasksCount = assignedTasksCount;
    }

    public int getCompletedTasksCount() {
        return completedTasksCount;
    }

    public void setCompletedTasksCount(int completedTasksCount) {
        this.completedTasksCount = completedTasksCount;
    }

    public BigDecimal getTotalEstimatedHours() {
        return totalEstimatedHours;
    }

    public void setTotalEstimatedHours(BigDecimal totalEstimatedHours) {
        this.totalEstimatedHours = totalEstimatedHours;
    }

    public BigDecimal getCompletedEstimatedHours() {
        return completedEstimatedHours;
    }

    public void setCompletedEstimatedHours(BigDecimal completedEstimatedHours) {
        this.completedEstimatedHours = completedEstimatedHours;
    }

    public int getSubmissionsCount() {
        return submissionsCount;
    }

    public void setSubmissionsCount(int submissionsCount) {
        this.submissionsCount = submissionsCount;
    }

    public int getApprovedSubmissionsCount() {
        return approvedSubmissionsCount;
    }

    public void setApprovedSubmissionsCount(int approvedSubmissionsCount) {
        this.approvedSubmissionsCount = approvedSubmissionsCount;
    }
}
