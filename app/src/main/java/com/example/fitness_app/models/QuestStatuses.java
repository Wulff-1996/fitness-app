package com.example.fitness_app.models;

import java.util.List;

public class QuestStatuses
{
    private List<String> statuses;

    public QuestStatuses()
    {
    }

    public QuestStatuses(List<String> statuses)
    {
        this.statuses = statuses;
    }

    public List<String> getStatuses()
    {
        return statuses;
    }

    public void setStatuses(List<String> statuses)
    {
        this.statuses = statuses;
    }

    @Override
    public String toString()
    {
        return "QuestStatuses{" +
                "statuses=" + statuses +
                '}';
    }
}
