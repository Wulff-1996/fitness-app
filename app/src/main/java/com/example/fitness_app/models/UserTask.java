package com.example.fitness_app.models;

import java.util.HashMap;
import java.util.Map;

public class UserTask
{
    private String title;
    private Map<String, Boolean> dateStatuses;

    public UserTask()
    {
    }

    public UserTask(String description)
    {
        this.title = description;
        dateStatuses = new HashMap<>();
    }

    public UserTask(String description, Map<String, Boolean> dateStatuses)
    {
        this.title = description;
        this.dateStatuses = dateStatuses;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Map<String, Boolean> getDateStatuses()
    {
        return dateStatuses;
    }

    public void setDateStatuses(Map<String, Boolean> dateStatuses)
    {
        this.dateStatuses = dateStatuses;
    }

    @Override
    public String toString()
    {
        return "UserTask{" +
                "title='" + title + '\'' +
                ", dateStatuses=" + dateStatuses +
                '}';
    }
}
