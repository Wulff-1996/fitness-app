package com.example.fitness_app.models;

public class QuestEntity {
    private String id;
    private Long ExperiencePoints;
    private String title;
    private String description;
    private Long dateCreated;
    private Long expireDate;
    private Long levelRequirement;

    public QuestEntity() {
    }

    public QuestEntity(String id, Long experiencePoints, String title, String description, Long dateCreated, Long expireDate, Long levelRequirement) {
        this.id = id;
        ExperiencePoints = experiencePoints;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.expireDate = expireDate;
        this.levelRequirement = levelRequirement;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getExperiencePoints() {
        return ExperiencePoints;
    }

    public void setExperiencePoints(Long experiencePoints) {
        ExperiencePoints = experiencePoints;
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

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public Long getLevelRequirement() {
        return levelRequirement;
    }

    public void setLevelRequirement(Long levelRequirement) {
        this.levelRequirement = levelRequirement;
    }
}
