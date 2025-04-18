package com.githubtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.githubtracker.controller", "com.githubtracker.service"})
public class GithubTrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GithubTrackerApplication.class, args);
	}
}
