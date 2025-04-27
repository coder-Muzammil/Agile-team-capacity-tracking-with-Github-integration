package com.githubtracker.service;

import com.githubtracker.dto.UserContributionsDTO;
import com.githubtracker.model.Commit;
import com.githubtracker.model.Issue;
import com.githubtracker.model.PullRequest;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GitHubService {

    private static final String GITHUB_API_URL = "https://api.github.com";
    @Value("${github.api.token}")
    private String TOKEN;  // inject from application.properties
    private final RestTemplate restTemplate = new RestTemplate();

    public UserContributionsDTO fetchRealTimeUserContributions(String repoOwner,String username, String repoName) {
        HttpHeaders headers = getGitHubHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // ✅ Now fetch contributions safely
        List<Commit> commits = getCommitsByUserInRepo(username, repoName, repoOwner);
        List<PullRequest> pullRequests = getPullRequestsByUserInRepo(username, repoName, repoOwner);
        List<Issue> issues = getIssuesByUserInRepo(username, repoName, repoOwner);

        return new UserContributionsDTO(
                commits,
                pullRequests,
                issues,
                commits.size(),
                pullRequests.size(),
                issues.size()
        );
    }

    // Helper method to fetch all contributors (users) in a repo
    public List<String> getRepoContributors(String repoOwner, String repoName) {
        HttpHeaders headers = getGitHubHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Fetch contributors from the repository
        ResponseEntity<Object[]> contributorsResponse = restTemplate.exchange(
                GITHUB_API_URL + "/repos/" + repoOwner + "/" + repoName + "/contributors",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        List<String> contributors = new ArrayList<>();
        if (contributorsResponse.getBody() != null) {
            for (Object obj : contributorsResponse.getBody()) {
                Map<String, Object> contributor = (Map<String, Object>) obj;
                String login = (String) contributor.get("login");
                contributors.add(login);
            }
        }

        return contributors;
    }

    // Helper method to fetch commits using the dynamic repo owner
    private List<Commit> getCommitsByUserInRepo(String username, String repoName, String repoOwner) {
        HttpHeaders headers = getGitHubHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Fetch commits from the repository for the given user and repo
        ResponseEntity<Object[]> commitResponse = restTemplate.exchange(
                GITHUB_API_URL + "/repos/" + repoOwner + "/" + repoName + "/commits",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        List<Commit> commits = new ArrayList<>();
        for (Object obj : commitResponse.getBody()) {
            Map<String, Object> commitMap = (Map<String, Object>) obj;
            Map<String, Object> commitDetails = (Map<String, Object>) commitMap.get("commit");
            Map<String, Object> authorDetails = (Map<String, Object>) commitDetails.get("author");
            Map<String, Object> author = (Map<String, Object>) commitMap.get("author");

            if (author == null) continue; // skip commits with no author (bot commits)

            String authorLogin = (String) author.get("login");
            if (!username.equals(authorLogin)) continue; // ✅ Filter: only matching username

            String sha = (String) commitMap.get("sha");
            String message = (String) commitDetails.get("message");
            String authorName = authorDetails != null ? (String) authorDetails.get("name") : "unknown";
            String date = (String) authorDetails.get("date");

            commits.add(new Commit(sha, authorName, authorLogin, message, date, repoName));
        }

        return commits;
    }

    // Helper method to fetch pull requests using the dynamic repo owner
    private List<PullRequest> getPullRequestsByUserInRepo(String username, String repoName, String repoOwner) {
        HttpHeaders headers = getGitHubHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Fetch pull requests from the repository for the given user and repo
        ResponseEntity<Object[]> prResponse = restTemplate.exchange(
                GITHUB_API_URL + "/repos/" + repoOwner + "/" + repoName + "/pulls?state=all",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        List<PullRequest> pullRequests = new ArrayList<>();
        for (Object obj : prResponse.getBody()) {
            Map<String, Object> pr = (Map<String, Object>) obj;
            Map<String, Object> prUser = (Map<String, Object>) pr.get("user");

            if (prUser == null) continue;

            String prAuthor = (String) prUser.get("login");
            if (!username.equals(prAuthor)) continue; // ✅ Filter: only matching username

            Long id = ((Number) pr.get("id")).longValue();
            String title = (String) pr.get("title");
            String createdAt = (String) pr.get("created_at");

            pullRequests.add(new PullRequest(id, title, prAuthor, createdAt, repoName));
        }

        return pullRequests;
    }

    // Helper method to fetch issues using the dynamic repo owner
    private List<Issue> getIssuesByUserInRepo(String username, String repoName, String repoOwner) {
        HttpHeaders headers = getGitHubHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Fetch issues from the repository for the given user and repo
        ResponseEntity<Object[]> issueResponse = restTemplate.exchange(
                GITHUB_API_URL + "/repos/" + repoOwner + "/" + repoName + "/issues?state=all",
                HttpMethod.GET,
                entity,
                Object[].class
        );

        List<Issue> issues = new ArrayList<>();
        for (Object obj : issueResponse.getBody()) {
            Map<String, Object> issue = (Map<String, Object>) obj;

            if (issue.containsKey("pull_request")) continue; // skip PRs shown in issues

            Map<String, Object> issueUser = (Map<String, Object>) issue.get("user");

            if (issueUser == null) continue;

            String issueAuthor = (String) issueUser.get("login");
            if (!username.equals(issueAuthor)) continue; // ✅ Filter: only matching username

            Long id = ((Number) issue.get("id")).longValue();
            String title = (String) issue.get("title");
            String createdAt = (String) issue.get("created_at");
            String state = (String) issue.get("state");

            issues.add(new Issue(id, title, issueAuthor, createdAt, state, repoName));
        }

        return issues;
    }

    // Helper method to get GitHub headers
    private HttpHeaders getGitHubHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", TOKEN);
        headers.set("Accept", "application/vnd.github+json");
        return headers;
    }
}
