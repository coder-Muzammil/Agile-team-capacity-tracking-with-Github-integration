package com.githubtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PullRequest {

    private Long id;
    private String title;
    private String author;
    private String createdAt;
    private String repoName;
}
