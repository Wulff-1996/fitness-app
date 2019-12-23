package com.example.fitness_app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Account implements Serializable
{
    private String username;
    private String email;
    private int experiencePoints = 0;
    private Long achievementPoints = 0L;
    private String userType;
    private Map<String, Benchmark> benchmarks = new HashMap<>();
    private Map<String, UserTask> tasks = new HashMap<>();

    public Account()
    {
    }


    public Account(String userType, String username, String email)
    {
        this.userType = userType;
        this.username = username;
        this.email = email;
    }

    public Account(String username, String email, int experiencePoints, Long achievementPoints, String userType, Map<String, Benchmark> benchmarks, Map<String, UserTask> tasks) {
        this.username = username;
        this.email = email;
        this.experiencePoints = experiencePoints;
        this.achievementPoints = achievementPoints;
        this.userType = userType;
        this.benchmarks = benchmarks;
        this.tasks = tasks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public Long getAchievementPoints() {
        return achievementPoints;
    }

    public void setAchievementPoints(Long achievementPoints) {
        this.achievementPoints = achievementPoints;
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

    public ArrayList<Integer> retrieveLevelInformation()
    {
        int level = 1;
        int expNeeded = 100;
        int lastExp = 0;
        int maxLevel = 60;

        for (int i = level; i < maxLevel; i++)
        {
            if (experiencePoints < expNeeded)
            {
                break;
            }
            lastExp = expNeeded;
            if (i < 30)
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.10);
            }
            else if (i < 50)
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.05);
            }
            else
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.025);
            }
            level++;
        }

        float percentDone = 0;

        if (level < maxLevel)
        {
            percentDone = (experiencePoints-lastExp)*100.0f/(expNeeded-lastExp);
        }
        else
        {
            percentDone = 100f;
        }
        ArrayList<Integer> results = new ArrayList<>();
        results.add(level);
        results.add((int) percentDone);
        results.add((experiencePoints-lastExp));
        results.add((expNeeded-lastExp));
        return results;
    }
    @Override
    public String toString()
    {
        return "Account{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", experiencePoints=" + experiencePoints +
                ", achievementPoints=" + achievementPoints +
                ", userType='" + userType + '\'' +
                ", benchmarks=" + benchmarks +
                ", tasks=" + tasks +
                '}';
    }
}