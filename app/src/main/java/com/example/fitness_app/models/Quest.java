package com.example.fitness_app.models;

public class Quest
{
    private String exerciseCategory, title;
    private int expReward, levelRequired, repititions;

    public Quest()
    {
    }

    public Quest(String exerciseCategory, String title, int expReward, int levelRequired, int repititions)
    {
        this.exerciseCategory = exerciseCategory;
        this.title = title;
        this.expReward = expReward;
        this.levelRequired = levelRequired;
        this.repititions = repititions;
    }

    public String getExerciseCategory()
    {
        return exerciseCategory;
    }

    public void setExerciseCategory(String exerciseCategory)
    {
        this.exerciseCategory = exerciseCategory;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getExpReward()
    {
        return expReward;
    }

    public void setExpReward(int expReward)
    {
        this.expReward = expReward;
    }

    public int getLevelRequired()
    {
        return levelRequired;
    }

    public void setLevelRequired(int levelRequired)
    {
        this.levelRequired = levelRequired;
    }

    public int getRepititions()
    {
        return repititions;
    }

    public void setRepititions(int repititions)
    {
        this.repititions = repititions;
    }

    @Override
    public String toString()
    {
        return "Quest{" +
                "exerciseCategory='" + exerciseCategory + '\'' +
                ", title='" + title + '\'' +
                ", expReward=" + expReward +
                ", levelRequired=" + levelRequired +
                ", repititions=" + repititions +
                '}';
    }
}