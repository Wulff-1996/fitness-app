package com.example.fitness_app.models;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Account
{
    private int exp;
    private String userType;
    private Map<String, Benchmark> benchmarks;
    private Map<String, Quest> quests;
    private Map<String, Task> tasks = new HashMap<>();

    /*
    Post account to database example
    Map questmap = new HashMap();
        questmap.put("testQuestID1", new Quest("testcategory", "testtitle", 1000, 3, 10));
        questmap.put("testQuestID2", new Quest("testcategory", "testtitle", 5000, 15, 1));
        Map benchmarkmap = new HashMap();
        benchmarkmap.put(String.valueOf(benchmarkmap.size()), new Benchmark("November 18, 2019 at 12:00:00 AM UTC+1", "Benchpress", (float) 80));
        benchmarkmap.put(String.valueOf(benchmarkmap.size()), new Benchmark("November 21, 2019 at 12:00:00 AM UTC+1", "Benchpress", (float) 102));

        Account account = new Account(500, "TestUser", benchmarkmap, questmap);
        FirestoreRepository.postObject("accounts", "test1234@gmail.com", account);
     */

    public Account()
    {
    }

    public Account(int exp, String userType)
    {
        this.exp = exp;
        this.userType = userType;
    }

    public Account(int exp, String userType, Map<String, Benchmark> benchmarks, Map<String, Quest> quests, Map<String, Task> tasks)
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

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
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