package com.teamproof.service;

import com.teamproof.entity.Project;
import com.teamproof.repository.ProjectRepository;
import com.teamproof.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ProjectService handles business logic and foreign-key validation for projects.
 */
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, TeamRepository teamRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public List<Project> getProjectsByTeamId(Long teamId) {
        return projectRepository.findByTeamId(teamId);
    }

    public Project createProject(Project project) {
        if (project.getName() == null || project.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        if (project.getTeamId() == null) {
            throw new IllegalArgumentException("Team ID is required");
        }
        if (!teamRepository.existsById(project.getTeamId())) {
            throw new IllegalArgumentException("Referenced Team does not exist with ID: " + project.getTeamId());
        }
        if (project.getStatus() == null || project.getStatus().trim().isEmpty()) {
            project.setStatus("ACTIVE");
        }
        project.setCreatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project updated) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + id));

        if (updated.getName() != null && !updated.getName().trim().isEmpty()) {
            existing.setName(updated.getName().trim());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        return projectRepository.save(existing);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }
}
