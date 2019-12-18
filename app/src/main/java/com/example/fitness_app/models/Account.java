package com.example.fitness_app.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account implements Serializable
{
    private String id;
    private String username;
    private Long experiencePoints;
    private Long achievementPoints;
    private String userType;
    private Map<String, Benchmark> benchmarks = new HashMap<>();
    private List<String> quests = new ArrayList<>();
    private Map<String, UserTask> tasks = new HashMap<>();

    public Account(Long exp, String userType)
    {
        this.experiencePoints = exp;
        this.userType = userType;
    }

    public Account(String id, Long experiencePoints, String userType, Map<String, Benchmark> benchmarks, Map<String, Quest> quests, Map<String, UserTask> tasks, String username) {
        this.id = id;
        this.experiencePoints = experiencePoints;
        this.userType = userType;
        this.benchmarks = benchmarks;
        this.quests = quests;
        this.tasks = tasks;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public Map<String, Benchmark> getBenchmarks()
    {
        return benchmarks;
    }

    public void setBenchmarks(Map<String, Benchmark> benchmarks)
    {
        this.benchmarks = benchmarks;
    }

    public List<String> getQuests()
    {
        return quests;
    }

    public void setQuests(List<String> quests)
    {
        this.quests = quests;
    }

    public Map<String, UserTask> getTasks()
    {
        return tasks;
    }

    public void setTasks(Map<String, UserTask> tasks)
    {
        this.tasks = tasks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}