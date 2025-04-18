package com.githubtracker.service;

import com.githubtracker.model.User;
import com.githubtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUser(String githubUsername) {
        return userRepository.findById(githubUsername);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }
}
