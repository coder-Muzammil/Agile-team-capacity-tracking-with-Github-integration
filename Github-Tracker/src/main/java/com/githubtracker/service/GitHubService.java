package com.githubtracker.service;

import com.githubtracker.model.Commit;
import com.githubtracker.model.Issue;
import com.githubtracker.model.PullRequest;
import com.githubtracker.model.User;
import com.githubtracker.repository.CommitRepository;
import com.githubtracker.repository.IssueRepository;
import com.githubtracker.repository.UserRepository;
import com.githubtracker.repository.PullRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class GitHubService {

    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String TOKEN = "Bearer Replace_with_your_personal_access_token";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private IssueRepository issueRepository;


    // ✅ 1. Get user repositories
    public List<Object> getUserRepos(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", TOKEN);
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object[]> response = restTemplate.exchange(
                GITHUB_API_URL + "/users/" + username + "/repos",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        return Arrays.asList(response.getBody());
    }

    // ✅ 2. Get user commits (view only)
    public List<Object> getUserCommits(String username, String repo) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", TOKEN);
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object[]> response = restTemplate.exchange(
                GITHUB_API_URL + "/repos/" + username + "/" + repo + "/commits",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        return Arrays.asList(response.getBody());
    }

    // ✅ 3. Fetch and save commits to MySQL
    public List<Commit> fetchAndSaveCommits(String username, String repo) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", TOKEN);
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object[]> response = restTemplate.exchange(
                GITHUB_API_URL + "/repos/" + username + "/" + repo + "/commits",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        Object[] commitObjects = response.getBody();
        List<Commit> savedCommits = new ArrayList<>();

        for (Object obj : commitObjects) {
            Map<String, Object> commitMap = (Map<String, Object>) obj;

            String sha = (String) commitMap.get("sha");
            Map<String, Object> commitDetails = (Map<String, Object>) commitMap.get("commit");
            Map<String, Object> authorDetails = (Map<String, Object>) commitDetails.get("author");
            Map<String, Object> author = (Map<String, Object>) commitMap.get("author");

            String message = (String) commitDetails.get("message");
            String authorName = authorDetails != null ? (String) authorDetails.get("name") : "unknown";
            String authorLogin = author != null ? (String) author.get("login") : "unknown";
            String date = (String) authorDetails.get("date");

            Commit commit = new Commit(
                    sha,
                    authorName,
                    authorLogin,
                    message,
                    date,
                    repo
            );

            // ✅ Prevent duplicate insertions
            if (!commitRepository.existsById(sha)) {
                commitRepository.save(commit);
                savedCommits.add(commit);

                // ✅ Update user's totalCommits
                userRepository.findById(authorLogin).ifPresentOrElse(
                        existingUser -> {
                            existingUser.setTotalCommits(existingUser.getTotalCommits() + 1);
                            userRepository.save(existingUser);
                        },
                        () -> {
                            User newUser = new User(authorLogin, authorName, 1, 0, 0);
                            userRepository.save(newUser);
                        }
                );
            }


        }

        return savedCommits;
    }

    public List<PullRequest> fetchAndSavePullRequests(String username, String repo) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", TOKEN);
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object[]> response = restTemplate.exchange(
                GITHUB_API_URL + "/repos/" + username + "/" + repo + "/pulls?state=all",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        Object[] prObjects = response.getBody();
        List<PullRequest> savedPRs = new ArrayList<>();

        for (Object obj : prObjects) {
            Map<String, Object> pr = (Map<String, Object>) obj;

            Long id = ((Number) pr.get("id")).longValue();
            String title = (String) pr.get("title");
            Map<String, Object> user = (Map<String, Object>) pr.get("user");
            String author = user != null ? (String) user.get("login") : "unknown";
            String createdAt = (String) pr.get("created_at");

            if (!pullRequestRepository.existsById(id)) {
                PullRequest pullRequest = new PullRequest(id, title, author, createdAt, repo);
                pullRequestRepository.save(pullRequest);
                savedPRs.add(pullRequest);

                // ✅ Update user's pullRequests
                userRepository.findById(author).ifPresentOrElse(
                        existingUser -> {
                            existingUser.setPullRequests(existingUser.getPullRequests() + 1);
                            userRepository.save(existingUser);
                        },
                        () -> {
                            User newUser = new User(author, author, 0, 0, 1);
                            userRepository.save(newUser);
                        }
                );
            }


        }

        return savedPRs;
    }

    public List<Issue> fetchAndSaveIssues(String username, String repo) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", TOKEN);
        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Object[]> response = restTemplate.exchange(
                GITHUB_API_URL + "/repos/" + username + "/" + repo + "/issues?state=all",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        Object[] issueObjects = response.getBody();
        List<Issue> savedIssues = new ArrayList<>();

        for (Object obj : issueObjects) {
            Map<String, Object> issue = (Map<String, Object>) obj;

            if (issue.containsKey("pull_request")) continue; // skip pull requests shown in issue list

            Long id = ((Number) issue.get("id")).longValue();
            String title = (String) issue.get("title");
            Map<String, Object> user = (Map<String, Object>) issue.get("user");
            String author = user != null ? (String) user.get("login") : "unknown";
            String createdAt = (String) issue.get("created_at");
            String state = (String) issue.get("state");

            if (!issueRepository.existsById(id)) {
                Issue issueObj = new Issue(id, title, author, createdAt, state, repo);
                issueRepository.save(issueObj);
                savedIssues.add(issueObj);

                // ✅ Only update if issue is closed
                if ("closed".equalsIgnoreCase(state)) {
                    userRepository.findById(author).ifPresentOrElse(
                            existingUser -> {
                                existingUser.setIssuesClosed(existingUser.getIssuesClosed() + 1);
                                userRepository.save(existingUser);
                            },
                            () -> {
                                User newUser = new User(author, author, 0, 1, 0);
                                userRepository.save(newUser);
                            }
                    );
                }
            }


        }

        return savedIssues;
    }


}
