package com.teamproof.controller;

import com.teamproof.dto.MemberContributionDto;
import com.teamproof.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ContributionController provides transparent activity indicators for team members.
 */
@RestController
@RequestMapping("/api/contributions")
@CrossOrigin(origins = "*")
public class ContributionController {

    private final ContributionService contributionService;

    @Autowired
    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<Map<String, Object>> getProjectContributions(@PathVariable Long projectId) {
        List<MemberContributionDto> contributions = contributionService.getProjectContributions(projectId);

        Map<String, Object> response = new HashMap<>();
        response.put("projectId", projectId);
        response.put("disclaimer", "These metrics represent verifiable project activity indicators (tasks completed, verified submissions) and must not be used as an isolated evaluation of student competence.");
        response.put("contributions", contributions);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllContributions() {
        List<MemberContributionDto> contributions = contributionService.getProjectContributions(null);

        Map<String, Object> response = new HashMap<>();
        response.put("projectId", "ALL");
        response.put("disclaimer", "These metrics represent verifiable project activity indicators (tasks completed, verified submissions) and must not be used as an isolated evaluation of student competence.");
        response.put("contributions", contributions);

        return ResponseEntity.ok(response);
    }
}
