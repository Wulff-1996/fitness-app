package com.example.fitness_app.models;

public class AchievementApprovalRequest {
    private String id;
    private String achievementId;
    private String achievementTitle;
    private String achievementDescription;
    private Long achievementPoints;
    private String userEmail;
    private String username;
    private Long requestDate;
    private String userDescription;
    private String status;

    public AchievementApprovalRequest() {
    }

    public AchievementApprovalRequest(String id, String achievementId, String achievementTitle, String achievementDescription, Long achievementPoints, String userEmail, String username, Long requestDate, String userDescription, String status) {
        this.id = id;
        this.achievementId = achievementId;
        this.achievementTitle = achievementTitle;
        this.achievementDescription = achievementDescription;
        this.achievementPoints = achievementPoints;
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

    public String getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(String achievementId) {
        this.achievementId = achievementId;
    }

    public String getAchievementTitle() {
        return achievementTitle;
    }

    public void setAchievementTitle(String achievementTitle) {
        this.achievementTitle = achievementTitle;
    }

    public String getAchievementDescription() {
        return achievementDescription;
    }

    public void setAchievementDescription(String achievementDescription) {
        this.achievementDescription = achievementDescription;
    }

    public Long getAchievementPoints() {
        return achievementPoints;
    }

    public void setAchievementPoints(Long achievementPoints) {
        this.achievementPoints = achievementPoints;
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


    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Long getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Long requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
