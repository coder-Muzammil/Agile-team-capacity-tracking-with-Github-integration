package com.githubtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRepoStats {

    private String githubUsername;
    
    private String repoName;

    private String displayName;
    private int totalCommits;
    private int issuesClosed;
    private int pullRequests;
}
