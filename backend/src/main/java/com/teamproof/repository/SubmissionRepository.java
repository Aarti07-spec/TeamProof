package com.teamproof.repository;

import com.teamproof.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SubmissionRepository handles database queries for Task Submissions.
 */
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByTaskId(Long taskId);
    List<Submission> findBySubmittedBy(Long userId);
}
