package com.githubtracker.dto;

import com.githubtracker.model.Commit;
import com.githubtracker.model.Issue;
import com.githubtracker.model.PullRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContributionsDTO {
    private List<Commit> commits;
    private List<PullRequest> pullRequests;
    private List<Issue> issues;

    private int totalCommits;
    private int totalPullRequests;
    private int totalIssues;
}
