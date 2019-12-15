package com.example.fitness_app.models;

public class AchievementApprovalRequest {
    private int id;
    private String achievementId;
    private String achievementTitle;
    private String ahcievementDescription;
    private Long achievementPoints;
    private String userId;
    private String userNickname; // or email maybe
    private String requestDate;
    private String userDescription;

    public AchievementApprovalRequest() {
    }

    public AchievementApprovalRequest(int id, String achievementId, String achievementTitle, String ahcievementDescription, Long achievementPoints, String userId, String userNickname, String requestDate, String userDescription) {
        this.id = id;
        this.achievementId = achievementId;
        this.achievementTitle = achievementTitle;
        this.ahcievementDescription = ahcievementDescription;
        this.achievementPoints = achievementPoints;
        this.userId = userId;
        this.userNickname = userNickname;
        this.requestDate = requestDate;
        this.userDescription = userDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getAhcievementDescription() {
        return ahcievementDescription;
    }

    public void setAhcievementDescription(String ahcievementDescription) {
        this.ahcievementDescription = ahcievementDescription;
    }

    public Long getAchievementPoints() {
        return achievementPoints;
    }

    public void setAchievementPoints(Long achievementPoints) {
        this.achievementPoints = achievementPoints;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }
}
