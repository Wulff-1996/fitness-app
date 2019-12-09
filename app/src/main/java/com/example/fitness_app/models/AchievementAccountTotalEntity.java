package com.example.fitness_app.models;

import com.google.firebase.Timestamp;

public class AchievementAccountTotalEntity extends AchievementAccountEntity {
    private int completedCount;
    private int totalToCompleteCount;

    public AchievementAccountTotalEntity() {
        setType("total");
    }

    public AchievementAccountTotalEntity(String id, String title, String description, Long achievementPoints, Boolean isCompleted, Timestamp completionDate, String whenToUpdate, int amountToCompleteCount, int completedCount, String type, int completedCount1, int totalToCompleteCount) {
        super(id, title, description, achievementPoints, isCompleted, completionDate, whenToUpdate, amountToCompleteCount, completedCount, type);
        this.completedCount = completedCount1;
        this.totalToCompleteCount = totalToCompleteCount;
    }

    public int getCompletedCount(){
        return this.completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public int getTotalToCompleteCount() {
        return totalToCompleteCount;
    }

    public void setTotalToCompleteCount(int totalToCompleteCount) {
        this.totalToCompleteCount = totalToCompleteCount;
    }
}
