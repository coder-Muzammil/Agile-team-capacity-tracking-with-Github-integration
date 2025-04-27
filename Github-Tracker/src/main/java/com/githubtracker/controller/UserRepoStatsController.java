package com.githubtracker.controller;

import com.githubtracker.dto.UserContributionsDTO;
import com.githubtracker.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")  // <-- allow your HTML to call backend
@RestController
@RequestMapping("/api/stats")
public class UserRepoStatsController {

    @Autowired
    private GitHubService gitHubService;

    // ✅ It will live fetch the latest contributions from GitHub containing: all commits, all pull requests, all issues for that specific user in that repo.
    @GetMapping("/{repoOwner}/user/{username}/repo/{repo}/contributions")
    public UserContributionsDTO fetchRealTimeContributions(
            @PathVariable String repoOwner,
            @PathVariable String username,
            @PathVariable String repo) {
        return gitHubService.fetchRealTimeUserContributions(repoOwner,username, repo);
    }

    // ✅ New endpoint to fetch all contributors (users) for a specific repository
    @GetMapping("/repo/{owner}/{repo}/contributors")
    public List<String> fetchRepoContributors(
            @PathVariable String owner,  // repository owner
            @PathVariable String repo) { // repository name
        return gitHubService.getRepoContributors(owner, repo);
    }
}
