package com.example.fitness_app.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Account implements Serializable
{
    private String id;
    private String username;
    private String email;
    private Long experiencePoints;
    private Long achievementPoints;
    private Long level;
    private String userType;
    private Map<String, Benchmark> benchmarks = new HashMap<>();
    private Map<String, UserTask> tasks = new HashMap<>();

    public Account()
    {
    }

    public Account(Long exp, String userType)
    {
        this.experiencePoints = exp;
        this.userType = userType;
    }

    public Account(Long exp, String userType, String username)
    {
        this.experiencePoints = exp;
        this.userType = userType;
        this.username = username;
    }

    public Account(String id, String username, String email, Long experiencePoints, Long achievementPoints, Long level, String userType, Map<String, Benchmark> benchmarks, Map<String, UserTask> tasks) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.experiencePoints = experiencePoints;
        this.achievementPoints = achievementPoints;
        this.level = level;
        this.userType = userType;
        this.benchmarks = benchmarks;
        this.tasks = tasks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(Long experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public Long getAchievementPoints() {
        return achievementPoints;
    }

    public void setAchievementPoints(Long achievementPoints) {
        this.achievementPoints = achievementPoints;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Map<String, Benchmark> getBenchmarks() {
        return benchmarks;
    }

    public void setBenchmarks(Map<String, Benchmark> benchmarks) {
        this.benchmarks = benchmarks;
    }

    public Map<String, UserTask> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, UserTask> tasks) {
        this.tasks = tasks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}