package com.githubtracker.repository;

import com.githubtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // Optional: Add custom methods if needed
}
