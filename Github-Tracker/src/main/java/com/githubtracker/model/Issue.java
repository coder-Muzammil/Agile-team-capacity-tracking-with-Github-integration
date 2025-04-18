package com.githubtracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
    @Id
    private Long id;

    private String title;
    private String author;
    private String createdAt;
    private String state;
    private String repoName;
}
