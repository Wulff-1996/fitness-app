package com.example.fitness_app.models;

import java.util.List;

public abstract class AchievementAccountEntity {
    private String id;
    private String title;
    private String description;
    private Long achievementPoints;
    private Boolean isCompleted;
    private Long completionDate;
    private List<String> whenToUpdate;
    private String type;
    private Long totalPlayersCompleted;

    public AchievementAccountEntity() {}

    public AchievementAccountEntity(String id, String title, String description, Long achievementPoints, Boolean isCompleted, Long completionDate, List<String> whenToUpdate, String type, Long totalPlayersCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.achievementPoints = achievementPoints;
        this.isCompleted = isCompleted;
        this.completionDate = completionDate;
        this.whenToUpdate = whenToUpdate;
        this.type = type;
        this.totalPlayersCompleted = totalPlayersCompleted;
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

    public Long getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Long completionDate) {
        this.completionDate = completionDate;
    }

    public List<String> getWhenToUpdate() {
        return whenToUpdate;
    }

    public void setWhenToUpdate(List<String> whenToUpdate) {
        this.whenToUpdate = whenToUpdate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTotalPlayersCompleted() {
        return totalPlayersCompleted;
    }

    public void setTotalPlayersCompleted(Long totalPlayersCompleted) {
        this.totalPlayersCompleted = totalPlayersCompleted;
    }
}
