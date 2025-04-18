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
public class Commit {

    @Id
    private String sha; // commit ID (unique)

    private String authorName;
    private String authorLogin;
    private String message;
    private String date;
    private String repoName;
}
