package com.example.fitness_app.models;

public class QuestApprovalRequestEntity {
    private String id;
    private String questId;
    private String questTitle;
    private String questDescription;
    private Long experiencePoints;
    private String userEmail;
    private String username;
    private Long requestDate;
    private String userDescription;
    private String status;

    public QuestApprovalRequestEntity() {
    }

    public QuestApprovalRequestEntity(String id, String questId, String questTitle, String questDescription, Long experiencePoints, String userEmail, String username, Long requestDate, String userDescription, String status) {
        this.id = id;
        this.questId = questId;
        this.questTitle = questTitle;
        this.questDescription = questDescription;
        this.experiencePoints = experiencePoints;
        this.userEmail = userEmail;
        this.username = username;
        this.requestDate = requestDate;
        this.userDescription = userDescription;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getQuestTitle() {
        return questTitle;
    }

    public void setQuestTitle(String questTitle) {
        this.questTitle = questTitle;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public void setQuestDescription(String questDescription) {
        this.questDescription = questDescription;
    }

    public Long getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(Long experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Long requestDate) {
        this.requestDate = requestDate;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
