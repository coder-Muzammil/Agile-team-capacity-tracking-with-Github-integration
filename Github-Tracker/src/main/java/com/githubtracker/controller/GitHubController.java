package com.githubtracker.controller;

import com.githubtracker.model.Commit;
import com.githubtracker.model.Issue;
import com.githubtracker.model.PullRequest;
import com.githubtracker.service.GitHubService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @Autowired
    private GitHubService gitHubService;

    // ✅ Existing endpoint (optional)
    @GetMapping("/repos/{username}")
    public List<Object> getUserRepos(@PathVariable String username) {
        return gitHubService.getUserRepos(username);
    }

    @GetMapping("/commits/{username}/{repo}")
    public List<Object> getUserCommits(@PathVariable String username, @PathVariable String repo) {
        return gitHubService.getUserCommits(username, repo);
    }

    // ✅ NEW ENDPOINT to fetch and store commits
    @GetMapping("/save-commits/{username}/{repo}")
    public List<Commit> saveCommits(@PathVariable String username, @PathVariable String repo) {
        return gitHubService.fetchAndSaveCommits(username, repo);
    }

    @GetMapping("/save-prs/{username}/{repo}")
    public List<PullRequest> savePRs(@PathVariable String username, @PathVariable String repo) {
        return gitHubService.fetchAndSavePullRequests(username, repo);
    }

    @GetMapping("/save-issues/{username}/{repo}")
    public List<Issue> saveIssues(@PathVariable String username, @PathVariable String repo) {
        return gitHubService.fetchAndSaveIssues(username, repo);
    }

}
