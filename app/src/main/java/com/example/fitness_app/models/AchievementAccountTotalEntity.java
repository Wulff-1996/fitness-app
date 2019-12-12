package com.example.fitness_app.models;

import com.google.firebase.Timestamp;

public class AchievementAccountTotalEntity extends AchievementAccountEntity {

    public AchievementAccountTotalEntity() {
        setType("total");
    }

    public AchievementAccountTotalEntity(String id, String title, String description, Long achievementPoints, Boolean isCompleted, Timestamp completionDate, String whenToUpdate, Long amountToCompleteCount, Long completedCount, String type, Long totalPlayersCompleted) {
        super(id, title, description, achievementPoints, isCompleted, completionDate, whenToUpdate, amountToCompleteCount, completedCount, type, totalPlayersCompleted);
    }

}
