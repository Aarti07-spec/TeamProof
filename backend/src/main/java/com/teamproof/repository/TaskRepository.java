package com.teamproof.repository;

import com.teamproof.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TaskRepository handles database operations for the Task entity.
 * Spring Data JPA automatically provides standard CRUD methods (findAll, findById, save, deleteById).
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Custom query methods derived from method names
    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssignedTo(Long assignedTo);

    List<Task> findByStatus(String status);
}
