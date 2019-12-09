package com.example.fitness_app.models;

import com.google.firebase.Timestamp;

public abstract class AchievementAccountEntity {
    private String id;
    private String title;
    private String description;
    private Long achievementPoints;
    private Boolean isCompleted;
    private Timestamp completionDate;
    private String whenToUpdate;
    private int amountToCompleteCount;
    private int completedCount;
    private String type;

    public AchievementAccountEntity() {}

    public AchievementAccountEntity(String id, String title, String description, Long achievementPoints, Boolean isCompleted, Timestamp completionDate, String whenToUpdate, int amountToCompleteCount, int completedCount, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.achievementPoints = achievementPoints;
        this.isCompleted = isCompleted;
        this.completionDate = completionDate;
        this.whenToUpdate = whenToUpdate;
        this.amountToCompleteCount = amountToCompleteCount;
        this.completedCount = completedCount;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAchievementPoints() {
        return achievementPoints;
    }

    public void setAchievementPoints(Long achievementPoints) {
        this.achievementPoints = achievementPoints;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Timestamp getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Timestamp completionDate) {
        this.completionDate = completionDate;
    }

    public String getWhenToUpdate() {
        return whenToUpdate;
    }

    public void setWhenToUpdate(String whenToUpdate) {
        this.whenToUpdate = whenToUpdate;
    }

    public int getAmountToCompleteCount() {
        return amountToCompleteCount;
    }

    public void setAmountToCompleteCount(int amountToCompleteCount) {
        this.amountToCompleteCount = amountToCompleteCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
