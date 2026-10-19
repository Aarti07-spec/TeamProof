package com.teamproof.service;

import com.teamproof.entity.Task;
import com.teamproof.repository.ProjectRepository;
import com.teamproof.repository.TaskRepository;
import com.teamproof.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TaskService contains business logic and input validation for Task operations.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // 1. Retrieve all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // 2. Retrieve a single task by ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // 3. Retrieve all tasks for a specific project
    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    // 4. Retrieve all tasks assigned to a specific user
    public List<Task> getTasksByAssignedUser(Long userId) {
        return taskRepository.findByAssignedTo(userId);
    }

    // 5. Create a new task with basic validation
    public Task createTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }
        if (task.getProjectId() == null) {
            throw new IllegalArgumentException("Project ID is required");
        }
        if (!projectRepository.existsById(task.getProjectId())) {
            throw new IllegalArgumentException("Referenced Project does not exist with ID: " + task.getProjectId());
        }
        if (task.getAssignedTo() != null && !userRepository.existsById(task.getAssignedTo())) {
            throw new IllegalArgumentException("Referenced User does not exist with ID: " + task.getAssignedTo());
        }
        if (task.getEstimatedHours() != null
                && task.getEstimatedHours().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Estimated hours cannot be negative");
        }

        // Apply sensible defaults if not provided
        if (task.getStatus() == null || task.getStatus().trim().isEmpty()) {
            task.setStatus("TODO");
        }
        if (task.getPriority() == null || task.getPriority().trim().isEmpty()) {
            task.setPriority("MEDIUM");
        }
        if (task.getEstimatedHours() == null) {
        task.setEstimatedHours(BigDecimal.ZERO);
        }
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    // 6. Update task details
    public Task updateTask(Long id, Task updatedData) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

        if (updatedData.getTitle() != null && !updatedData.getTitle().trim().isEmpty()) {
            existingTask.setTitle(updatedData.getTitle().trim());
        }
        if (updatedData.getDescription() != null) {
            existingTask.setDescription(updatedData.getDescription());
        }
        if (updatedData.getEstimatedHours() != null) {
        if (updatedData.getEstimatedHours().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Estimated hours cannot be negative");
        }
        existingTask.setEstimatedHours(updatedData.getEstimatedHours());
        }
        if (updatedData.getPriority() != null) {
            existingTask.setPriority(updatedData.getPriority());
        }
        if (updatedData.getStatus() != null) {
            existingTask.setStatus(updatedData.getStatus());
        }
        if (updatedData.getAssignedTo() != null) {
            if (!userRepository.existsById(updatedData.getAssignedTo())) {
                throw new IllegalArgumentException("Referenced User does not exist with ID: " + updatedData.getAssignedTo());
            }
            existingTask.setAssignedTo(updatedData.getAssignedTo());
        }
        if (updatedData.getDeadline() != null) {
            existingTask.setDeadline(updatedData.getDeadline());
        }

        existingTask.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(existingTask);
    }

    // 7. Update status only (TODO, IN_PROGRESS, UNDER_REVIEW, COMPLETED)
    public Task updateTaskStatus(Long id, String status) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        existingTask.setStatus(status.trim().toUpperCase());
        existingTask.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(existingTask);
    }

    // 8. Delete a task
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new IllegalArgumentException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
}
