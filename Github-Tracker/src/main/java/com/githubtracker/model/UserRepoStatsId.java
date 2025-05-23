package com.githubtracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRepoStatsId implements Serializable {
    private String githubUsername;
    private String repoName;
}
