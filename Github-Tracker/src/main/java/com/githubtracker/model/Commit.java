package com.githubtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commit {

    private String sha; // commit ID (unique)
    private String authorName;
    private String authorLogin;
    private String message;
    private String date;
    private String repoName;
}
