package com.githubtracker.repository;

import com.githubtracker.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitRepository extends JpaRepository<Commit, String> {
}
