package com.example.fitness_app.models;

public class QuestAccountEntity {
    private String id;
    private String title;
    private String description;
    private Long levelRequirement;
    private Long ExperiencePoints;
    private Long expireDate;
    private Long dateCreated;
    private boolean isCompleted;
    private Long completedDate;
    private Boolean hasRequested;
    private Long requestedDate;
    private String status;
    private String userDescription;

    public QuestAccountEntity() {
    }

    public QuestAccountEntity(String id, String title, String description, Long levelRequirement, Long experiencePoints, Long expireDate, Long dateCreated, boolean isCompleted, Long completedDate, Boolean hasRequested, Long requestedDate, String status, String userDescription) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.levelRequirement = levelRequirement;
        ExperiencePoints = experiencePoints;
        this.expireDate = expireDate;
        this.dateCreated = dateCreated;
        this.isCompleted = isCompleted;
        this.completedDate = completedDate;
        this.hasRequested = hasRequested;
        this.requestedDate = requestedDate;
        this.status = status;
        this.userDescription = userDescription;
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

    public Long getLevelRequirement() {
        return levelRequirement;
    }

    public void setLevelRequirement(Long levelRequirement) {
        this.levelRequirement = levelRequirement;
    }

    public Long getExperiencePoints() {
        return ExperiencePoints;
    }

    public void setExperiencePoints(Long experiencePoints) {
        ExperiencePoints = experiencePoints;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Long getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Long completedDate) {
        this.completedDate = completedDate;
    }

    public Boolean getHasRequested() {
        return hasRequested;
    }

    public void setHasRequested(Boolean hasRequested) {
        this.hasRequested = hasRequested;
    }

    public Long getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Long requestedDate) {
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
