package com.teamproof.service;

import com.teamproof.dto.MemberContributionDto;
import com.teamproof.entity.Submission;
import com.teamproof.entity.Task;
import com.teamproof.entity.User;
import com.teamproof.repository.SubmissionRepository;
import com.teamproof.repository.TaskRepository;
import com.teamproof.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * ContributionService computes explainable, transparent activity indicators.
 * Note: These metrics represent observable project activities, not an absolute
 * measurement of individual student worth or cognitive effort.
 */
@Service
public class ContributionService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final SubmissionRepository submissionRepository;

    @Autowired
    public ContributionService(UserRepository userRepository,
                               TaskRepository taskRepository,
                               SubmissionRepository submissionRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<MemberContributionDto> getProjectContributions(Long projectId) {
        List<User> allUsers = userRepository.findAll();
        List<Task> projectTasks = (projectId != null)
                ? taskRepository.findByProjectId(projectId)
                : taskRepository.findAll();

        List<Submission> allSubmissions = submissionRepository.findAll();

        Map<Long, MemberContributionDto> summaryMap = new HashMap<>();

        for (User user : allUsers) {
            MemberContributionDto dto = new MemberContributionDto();
            dto.setUserId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());
            summaryMap.put(user.getId(), dto);
        }

        // Aggregate task metrics
        for (Task task : projectTasks) {
            if (task.getAssignedTo() != null && summaryMap.containsKey(task.getAssignedTo())) {
                MemberContributionDto dto = summaryMap.get(task.getAssignedTo());
                dto.setAssignedTasksCount(dto.getAssignedTasksCount() + 1);

                BigDecimal hours = (task.getEstimatedHours() != null) ? task.getEstimatedHours() : BigDecimal.ZERO;
                dto.setTotalEstimatedHours(dto.getTotalEstimatedHours().add(hours));

                if ("COMPLETED".equalsIgnoreCase(task.getStatus())) {
                    dto.setCompletedTasksCount(dto.getCompletedTasksCount() + 1);
                    dto.setCompletedEstimatedHours(dto.getCompletedEstimatedHours().add(hours));
                }
            }
        }

        // Aggregate submission metrics
        for (Submission sub : allSubmissions) {
            if (sub.getSubmittedBy() != null && summaryMap.containsKey(sub.getSubmittedBy())) {
                MemberContributionDto dto = summaryMap.get(sub.getSubmittedBy());
                dto.setSubmissionsCount(dto.getSubmissionsCount() + 1);
                if ("APPROVED".equalsIgnoreCase(sub.getStatus())) {
                    dto.setApprovedSubmissionsCount(dto.getApprovedSubmissionsCount() + 1);
                }
            }
        }

        return new ArrayList<>(summaryMap.values());
    }
}
