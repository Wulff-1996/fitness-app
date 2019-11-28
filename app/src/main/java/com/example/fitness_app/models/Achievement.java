package com.example.fitness_app.models;

public class Achievement
{
    private int achievementPoints;
    private String description;

    public Achievement()
    {
    }

    public Achievement(int achievementPoints, String description)
    {
        this.achievementPoints = achievementPoints;
        this.description = description;
    }

    public int getAchievementPoints()
    {
        return achievementPoints;
    }

    public void setAchievementPoints(int achievementPoints)
    {
        this.achievementPoints = achievementPoints;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return "Achievement{" +
                "achievementPoints=" + achievementPoints +
                ", description='" + description + '\'' +
                '}';
    }
}
