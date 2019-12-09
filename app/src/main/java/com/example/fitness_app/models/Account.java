package com.example.fitness_app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account
{
    @SerializedName("id")
    private String id;
    @SerializedName("experience_points")
    private int experiancePoints;
    @SerializedName("user_type")
    private String userType;
    @SerializedName("benchmarks")
    private Map<String, Benchmark> benchmarks = new HashMap<>();
    @SerializedName("quests")
    private Map<String, Quest> quests = new HashMap<>();
    @SerializedName("tasks")
    private Map<String, UserTask> tasks = new HashMap<>();
    @SerializedName("achievementEntryEntities")
    private List<AchievementEntryEntity> achievementEntryEntities = new ArrayList<>();

    public Account() {}

    public Account(int exp, String userType)
    {
        this.experiancePoints = exp;
        this.userType = userType;
    }

    public Account(String id, int experiancePoints, String userType, Map<String, Benchmark> benchmarks, Map<String, Quest> quests, Map<String, UserTask> tasks, List<AchievementEntryEntity> achievementEntryEntities) {
        this.id = id;
        this.experiancePoints = experiancePoints;
        this.userType = userType;
        this.benchmarks = benchmarks;
        this.quests = quests;
        this.tasks = tasks;
        this.achievementEntryEntities = achievementEntryEntities;
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

    public List<AchievementEntryEntity> getAchievementEntryEntities() {
        return achievementEntryEntities;
    }

    public void setAchievementEntryEntities(List<AchievementEntryEntity> achievementEntryEntities) {
        this.achievementEntryEntities = achievementEntryEntities;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", experiancePoints=" + experiancePoints +
                ", userType='" + userType + '\'' +
                ", benchmarks=" + benchmarks +
                ", quests=" + quests +
                ", tasks=" + tasks +
                ", achievementEntryEntities=" + achievementEntryEntities +
                '}';
    }
}