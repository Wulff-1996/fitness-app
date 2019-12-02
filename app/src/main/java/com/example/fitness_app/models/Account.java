package com.example.fitness_app.models;

import java.util.Map;

public class Account
{
    private int exp;
    private String userType;
    private Map<String, Benchmark> benchmarks;
    private Map<String, Quest> quests;
    private Map<String, UserTask> tasks;

    /*
    POST ACCOUNT EXAMPLE
        Map questmap = new HashMap();
        questmap.put("testQuestID1", new Quest("testcategory", "testtitle", 1000, 3, 10));
        questmap.put("testQuestID2", new Quest("testcategory", "testtitle", 5000, 15, 1));

        Map benchmarkmap = new HashMap();
        benchmarkmap.put("November 18, 2019 - Benchpress", new Benchmark("November 18, 2019 at 12:00:00 AM UTC+1", "Benchpress", (float) 80));
        benchmarkmap.put("November 21, 2019 - Benchpress", new Benchmark("November 21, 2019 at 12:00:00 AM UTC+1", "Benchpress", (float) 102));
        Map taskmap = new HashMap();

        Map cleanRoomTask = new HashMap();
        cleanRoomTask.put("01-08-2019", true);
        cleanRoomTask.put("02-08-2019", false);
        cleanRoomTask.put("03-08-2019", false);

        Map drinkVodkaTask = new HashMap();
        drinkVodkaTask.put("09-09-2019", false);
        drinkVodkaTask.put("11-09-2019", true);
        drinkVodkaTask.put("13-09-2019", false);

        taskmap.put("Clean room", new UserTask());
        taskmap.put("Drink vodka",new UserTask());

        Account account = new Account(500, "Super User", benchmarkmap, questmap, taskmap);
        Firestore.postObject("accounts", "test123@gmail.com", account, new FirebaseCallback()
        {
            @Override
            public void onSuccess(Object object)
            {

            }

            @Override
            public void onFailure(FirebaseFirestoreException.Code errorCode)
            {

            }

            @Override
            public void onFinish()
            {

            }
        });
     */

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