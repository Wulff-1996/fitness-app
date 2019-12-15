package com.example.fitness_app.models;

import com.example.fitness_app.constrants.AchievementTypes;

import java.util.UUID;

public class AchievementEntryEntity {
    private String id;
    private Long achievementPoints;
    private String title;
    private String description;
    private Long totalPlayersCompleted;
    private Long amountToCompleteCount;
    private String type;
    private String whenToUpdate;

    public AchievementEntryEntity() {}

    public AchievementEntryEntity(String id, Long achievementPoints, String title, String description, Long totalPlayersCompleted, Long amountToCompleteCount, String type, String whenToUpdate) {
        this.id = id;
        this.achievementPoints = achievementPoints;
        this.title = title;
        this.description = description;
        this.totalPlayersCompleted = totalPlayersCompleted;
        this.amountToCompleteCount = amountToCompleteCount;
        this.type = type;
        this.whenToUpdate = whenToUpdate;
    }

    public AchievementAccountEntity toAchievementAccountEntity (){
        switch (type){
            case AchievementTypes.STREAK:
                AchievementAccountStreakEntity streak = new AchievementAccountStreakEntity();
                streak.setId(UUID.randomUUID().toString());
                streak.setAchievementPoints(achievementPoints);
                streak.setTitle(title);
                streak.setDescription(description);
                streak.setAmountToCompleteCount(amountToCompleteCount);
                streak.setCompletedCount(0L);
                streak.setType(AchievementTypes.STREAK);
                streak.setIsCompleted(false);
                streak.setTotalPlayersCompleted(totalPlayersCompleted);
                streak.setWhenToUpdate(whenToUpdate);
                return streak;
            case AchievementTypes.TOTAL:
                AchievementAccountTotalEntity total = new AchievementAccountTotalEntity();
                total.setId(UUID.randomUUID().toString());
                total.setAchievementPoints(achievementPoints);
                total.setTitle(title);
                total.setDescription(description);
                total.setAmountToCompleteCount(amountToCompleteCount);
                total.setCompletedCount(0L);
                total.setType(AchievementTypes.TOTAL);
                total.setIsCompleted(false);
                total.setTotalPlayersCompleted(totalPlayersCompleted);
                total.setWhenToUpdate(whenToUpdate);
                return total;
            case AchievementTypes.MANUAL:
                AchievementAccountManualEntity once = new AchievementAccountManualEntity();
                once.setId(UUID.randomUUID().toString());
                once.setAchievementPoints(achievementPoints);
                once.setTitle(title);
                once.setDescription(description);
                once.setAmountToCompleteCount(amountToCompleteCount);
                once.setCompletedCount(0L);
                once.setType(AchievementTypes.MANUAL);
                once.setIsCompleted(false);
                once.setTotalPlayersCompleted(totalPlayersCompleted);
                once.setWhenToUpdate(whenToUpdate);
                return once;
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAchievementPoints() {
        return achievementPoints;
    }

    public void setAchievementPoints(Long achievementPoints) {
        this.achievementPoints = achievementPoints;
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

    public Long getTotalPlayersCompleted() {
        return totalPlayersCompleted;
    }

    public void setTotalPlayersCompleted(Long totalPlayersCompleted) {
        this.totalPlayersCompleted = totalPlayersCompleted;
    }

    public Long getAmountToCompleteCount() {
        return amountToCompleteCount;
    }

    public void setAmountToCompleteCount(Long amountToCompleteCount) {
        this.amountToCompleteCount = amountToCompleteCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWhenToUpdate() {
        return whenToUpdate;
    }

    public void setWhenToUpdate(String whenToUpdate) {
        this.whenToUpdate = whenToUpdate;
    }
}