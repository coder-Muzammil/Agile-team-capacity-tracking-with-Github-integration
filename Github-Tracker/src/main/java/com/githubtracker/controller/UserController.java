package com.githubtracker.controller;

import com.githubtracker.model.User;
import com.githubtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Get user by GitHub username
    @GetMapping("/{username}")
    public Optional<User> getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    // ✅ Create or update a user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // ✅ Delete a user
    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }
}
