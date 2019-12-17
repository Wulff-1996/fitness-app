package com.example.fitness_app.models;

import java.util.List;

public class AchievementAccountAutomaticEntity extends AchievementAccountEntity {
    private Long amountToCompleteCount;
    private Long completedCount;

    public AchievementAccountAutomaticEntity() {
    }

    public AchievementAccountAutomaticEntity(String id, String title, String description, Long achievementPoints, Boolean isCompleted, Long completionDate, List<String> whenToUpdate, String type, Long totalPlayersCompleted, Long dateCreated, Long amountToCompleteCount, Long completedCount) {
        super(id, title, description, achievementPoints, isCompleted, completionDate, whenToUpdate, type, totalPlayersCompleted, dateCreated);
        this.amountToCompleteCount = amountToCompleteCount;
        this.completedCount = completedCount;
    }

    public Long getAmountToCompleteCount() {
        return amountToCompleteCount;
    }

    public void setAmountToCompleteCount(Long amountToCompleteCount) {
        this.amountToCompleteCount = amountToCompleteCount;
    }

    public Long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(Long completedCount) {
        this.completedCount = completedCount;
    }
}
