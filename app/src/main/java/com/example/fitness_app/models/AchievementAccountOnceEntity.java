package com.example.fitness_app.models;

import com.google.firebase.Timestamp;

public class AchievementAccountOnceEntity extends AchievementAccountEntity {

    public AchievementAccountOnceEntity() {
        setType("once");
    }

    public AchievementAccountOnceEntity(String id, String title, String description, Long achievementPoints, Boolean isCompleted, Timestamp completionDate, String whenToUpdate, Long amountToCompleteCount, Long completedCount, String type, Long totalPlayersCompleted) {
        super(id, title, description, achievementPoints, isCompleted, completionDate, whenToUpdate, amountToCompleteCount, completedCount, type, totalPlayersCompleted);
    }

}
