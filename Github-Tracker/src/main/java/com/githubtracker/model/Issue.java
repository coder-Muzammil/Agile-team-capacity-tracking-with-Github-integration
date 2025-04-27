package com.githubtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    private Long id;
    private String title;
    private String author;
    private String createdAt;
    private String state;
    private String repoName;
}
