package com.teamproof.service;

import com.teamproof.entity.Team;
import com.teamproof.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TeamService contains business logic for teams.
 */
@Service
public class TeamService {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    public Team createTeam(Team team) {
        if (team.getName() == null || team.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be empty");
        }
        team.setCreatedAt(LocalDateTime.now());
        return teamRepository.save(team);
    }

    public Team updateTeam(Long id, Team updated) {
        Team existing = teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + id));

        if (updated.getName() != null && !updated.getName().trim().isEmpty()) {
            existing.setName(updated.getName().trim());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription());
        }
        return teamRepository.save(existing);
    }

    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new IllegalArgumentException("Team not found with id: " + id);
        }
        teamRepository.deleteById(id);
    }
}
