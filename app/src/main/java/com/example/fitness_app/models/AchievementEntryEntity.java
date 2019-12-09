package com.example.fitness_app.models;

public class AchievementEntryEntity {
    private String id;
    private Long achievementPoints;
    private String title;
    private String description;
    private Long totalPlayersCompleted;
    private int amountToCompleteCount;
    private String type;

    public AchievementEntryEntity() {}

    public AchievementEntryEntity(String id, Long achievementPoints, String title, String description, Long totalPlayersCompleted, int amountToCompleteCount, String type) {
        this.id = id;
        this.achievementPoints = achievementPoints;
        this.title = title;
        this.description = description;
        this.totalPlayersCompleted = totalPlayersCompleted;
        this.amountToCompleteCount = amountToCompleteCount;
        this.type = type;
    }

    public AchievementAccountEntity toAchievementAccountEntity (){
        switch (type){
            case "streak":
                AchievementAccountStreakEntity streak = new AchievementAccountStreakEntity();
                streak.setId(id);
                streak.setAchievementPoints(achievementPoints);
                streak.setTitle(title);
                streak.setDescription(description);
                streak.setAmountToCompleteCount(amountToCompleteCount);
                streak.setCompletedCount(0);
                streak.setType("streak");
                streak.setIsCompleted(false);
                return streak;
            case "total":
                AchievementAccountTotalEntity total = new AchievementAccountTotalEntity();
                total.setId(id);
                total.setAchievementPoints(achievementPoints);
                total.setTitle(title);
                total.setDescription(description);
                total.setAmountToCompleteCount(amountToCompleteCount);
                total.setCompletedCount(0);
                total.setType("streak");
                total.setIsCompleted(false);
                return total;
            case "once":
                AchievementAccountOnceEntity once = new AchievementAccountOnceEntity();
                once.setId(id);
                once.setAchievementPoints(achievementPoints);
                once.setTitle(title);
                once.setDescription(description);
                once.setAmountToCompleteCount(amountToCompleteCount);
                once.setCompletedCount(0);
                once.setType("streak");
                once.setIsCompleted(false);
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

    public int getAmountToCompleteCount() {
        return amountToCompleteCount;
    }

    public void setAmountToCompleteCount(int amountToCompleteCount) {
        this.amountToCompleteCount = amountToCompleteCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}