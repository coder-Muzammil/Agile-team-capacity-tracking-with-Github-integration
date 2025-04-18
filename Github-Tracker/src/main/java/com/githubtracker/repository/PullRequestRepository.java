package com.githubtracker.repository;

import com.githubtracker.model.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
}
