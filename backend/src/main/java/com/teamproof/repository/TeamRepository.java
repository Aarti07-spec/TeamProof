package com.teamproof.repository;

import com.teamproof.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TeamRepository handles database queries for Teams.
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
