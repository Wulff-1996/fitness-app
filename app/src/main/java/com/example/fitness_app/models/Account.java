package com.example.fitness_app.models;

import java.util.Map;

public class Account
{
    private int exp;
    private String userType;
    private Map<String, Object> benchmarks;
    private Map<String, Object> quests;

    /*
    Post account to database example
    Map questmap = new HashMap();
        questmap.put("testQuestID1", new Quest("testcategory", "testtitle", 1000, 3, 10));
        questmap.put("testQuestID2", new Quest("testcategory", "testtitle", 5000, 15, 1));
        Map benchmarkmap = new HashMap();
        benchmarkmap.put(String.valueOf(benchmarkmap.size()), new Benchmark("November 18, 2019 at 12:00:00 AM UTC+1", "Benchpress", (float) 80));
        benchmarkmap.put(String.valueOf(benchmarkmap.size()), new Benchmark("November 21, 2019 at 12:00:00 AM UTC+1", "Benchpress", (float) 102));

        Account account = new Account(500, "TestUser", benchmarkmap, questmap);
        Firestore.postObject("accounts", "test1234@gmail.com", account);
     */

    public Account()
    {
    }

    public Account(int exp, String userType)
    {
        this.exp = exp;
        this.userType = userType;
    }

    public Account(int exp, String userType, Map<String, Object> benchmarks, Map<String, Object> quests)
    {
        this.exp = exp;
        this.userType = userType;
        this.benchmarks = benchmarks;
        this.quests = quests;
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

    public Map<String, Object> getBenchmarks()
    {
        return benchmarks;
    }

    public void setBenchmarks(Map<String, Object> benchmarks)
    {
        this.benchmarks = benchmarks;
    }

    public Map<String, Object> getQuests()
    {
        return quests;
    }

    public void setQuests(Map<String, Object> quests)
    {
        this.quests = quests;
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