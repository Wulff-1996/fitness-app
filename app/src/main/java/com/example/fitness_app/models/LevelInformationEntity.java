package com.example.fitness_app.models;

public class LevelInformationEntity {
    private int level;
    private int percentDone;
    private int currentExperiencePoints;
    private int experiencePointsToLevelUp;

    public LevelInformationEntity() {
    }

    public LevelInformationEntity(int level, int percentDone, int currentExperiencePoints, int experiencePointsToLevelUp) {
        this.level = level;
        this.percentDone = percentDone;
        this.currentExperiencePoints = currentExperiencePoints;
        this.experiencePointsToLevelUp = experiencePointsToLevelUp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPercentDone() {
        return percentDone;
    }

    public void setPercentDone(int percentDone) {
        this.percentDone = percentDone;
    }

    public int getCurrentExperiencePoints() {
        return currentExperiencePoints;
    }

    public void setCurrentExperiencePoints(int currentExperiencePoints) {
        this.currentExperiencePoints = currentExperiencePoints;
    }

    public int getExperiencePointsToLevelUp() {
        return experiencePointsToLevelUp;
    }

    public void setExperiencePointsToLevelUp(int experiencePointsToLevelUp) {
        this.experiencePointsToLevelUp = experiencePointsToLevelUp;
    }
}
