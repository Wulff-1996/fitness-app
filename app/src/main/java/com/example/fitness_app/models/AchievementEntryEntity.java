package com.example.fitness_app.models;

import com.example.fitness_app.constrants.AchievementStatusTypes;
import com.example.fitness_app.constrants.AchievementTypes;

import java.util.List;

public class AchievementEntryEntity {
    private String id;
    private Long achievementPoints;
    private String title;
    private String description;
    private Long totalPlayersCompleted;
    private Long amountToCompleteCount;
    private String type;
    private List<String> whenToUpdate;

    public AchievementEntryEntity() {}

    public AchievementEntryEntity(String id, Long achievementPoints, String title, String description, Long totalPlayersCompleted, Long amountToCompleteCount, String type, List<String> whenToUpdate) {
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
            case AchievementTypes.AUTOMATIC:
                AchievementAccountAutomaticEntity auto = new AchievementAccountAutomaticEntity();
                auto.setId(id);
                auto.setAchievementPoints(achievementPoints);
                auto.setTitle(title);
                auto.setDescription(description);
                auto.setAmountToCompleteCount(amountToCompleteCount);
                auto.setCompletedCount(0L);
                auto.setType(AchievementTypes.AUTOMATIC);
                auto.setIsCompleted(false);
                auto.setTotalPlayersCompleted(totalPlayersCompleted);
                auto.setWhenToUpdate(whenToUpdate);
                return auto;
            case AchievementTypes.MANUAL:
                AchievementAccountManualEntity manual = new AchievementAccountManualEntity();
                manual.setId(id);
                manual.setAchievementPoints(achievementPoints);
                manual.setTitle(title);
                manual.setDescription(description);
                manual.setType(AchievementTypes.MANUAL);
                manual.setIsCompleted(false);
                manual.setHasRequested(false);
                manual.setStatus(AchievementStatusTypes.NOT_REQUESTED);
                manual.setTotalPlayersCompleted(totalPlayersCompleted);
                manual.setWhenToUpdate(whenToUpdate);
                return manual;
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

    public List<String> getWhenToUpdate() {
        return whenToUpdate;
    }

    public void setWhenToUpdate(List<String> whenToUpdate) {
        this.whenToUpdate = whenToUpdate;
    }
}