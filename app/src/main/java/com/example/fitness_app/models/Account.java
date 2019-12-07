package com.example.fitness_app.models;

import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class Account
{
    private int exp;
    private String userType;
    private Map<String, Benchmark> benchmarks = new HashMap<>();
    private Map<String, Quest> quests = new HashMap<>();
    private Map<String, UserTask> tasks = new HashMap<>();

    public Account()
    {
    }

    public Account(int exp, String userType)
    {
        this.exp = exp;
        this.userType = userType;
    }

    public Account(int exp, String userType, Map<String, Benchmark> benchmarks, Map<String, Quest> quests, Map<String, UserTask> tasks)
    {
        this.exp = exp;
        this.userType = userType;
        this.benchmarks = benchmarks;
        this.quests = quests;
        this.tasks = tasks;
    }

    public int getExp()
    {
        return exp;
    }

    public void setExp(int exp)
    {
        this.exp = exp;
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

    @Override
    public String toString()
    {
        return "Account{" +
                "exp=" + exp +
                ", userType='" + userType + '\'' +
                ", benchmarks=" + benchmarks +
                ", quests=" + quests +
                '}';
    }
}