package com.example.fitness_app.models;

import com.example.fitness_app.constrants.AchievementTypes;
import com.google.firebase.Timestamp;

public class AchievementAccountManualEntity extends AchievementAccountEntity {
    private Boolean hasRequested;
    private String requestedDate;
    private String status;
    private String userDescription;

    public AchievementAccountManualEntity() {
        setType(AchievementTypes.MANUAL);
    }

    public AchievementAccountManualEntity(String id, String title, String description, Long achievementPoints, Boolean isCompleted, Timestamp completionDate, String whenToUpdate, Long amountToCompleteCount, Long completedCount, String type, Long totalPlayersCompleted, Boolean hasRequested, String requestedDate, String status, String userDescription) {
        super(id, title, description, achievementPoints, isCompleted, completionDate, whenToUpdate, amountToCompleteCount, completedCount, type, totalPlayersCompleted);
        this.hasRequested = hasRequested;
        this.requestedDate = requestedDate;
        this.status = status;
        this.userDescription = userDescription;
    }

    public Boolean getHasRequested() {
        return hasRequested;
    }

    public void setHasRequested(Boolean hasRequested) {
        this.hasRequested = hasRequested;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }
}
