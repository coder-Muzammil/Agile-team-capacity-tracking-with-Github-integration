package com.example.agiletracker;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class AgileTrackerController {

    @GetMapping("/pull-requests")
    public List<String> getPullRequests() {
        return Arrays.asList("PR1", "PR2", "PR3");
    }

    @GetMapping("/commits")
    public List<String> getCommits() {
        return Arrays.asList("Commit1", "Commit2", "Commit3");
    }

    @GetMapping("/issues")
    public List<String> getIssues() {
        return Arrays.asList("Issue1", "Issue2", "Issue3");
    }
}
