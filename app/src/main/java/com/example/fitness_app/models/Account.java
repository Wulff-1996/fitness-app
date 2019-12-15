package com.example.fitness_app.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Account implements Serializable
{
    private String id;
    private String username;
    private int experiancePoints;
    private String userType;
    private Map<String, Benchmark> benchmarks = new HashMap<>();
    private Map<String, Quest> quests = new HashMap<>();
    private Map<String, UserTask> tasks = new HashMap<>();

    public Account() {}

    public Account(int exp, String userType)
    {
        this.experiancePoints = exp;
        this.userType = userType;
    }

    public Account(String id, int experiancePoints, String userType, Map<String, Benchmark> benchmarks, Map<String, Quest> quests, Map<String, UserTask> tasks, String username) {
        this.id = id;
        this.experiancePoints = experiancePoints;
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

    public int getExperiancePoints() {
        return experiancePoints;
    }

    public void setExperiancePoints(int experiancePoints) {
        this.experiancePoints = experiancePoints;
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

    public Map<String, Quest> getQuests()
    {
        return quests;
    }

    public void setQuests(Map<String, Quest> quests)
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