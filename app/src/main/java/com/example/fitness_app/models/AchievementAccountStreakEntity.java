package com.example.fitness_app.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class AchievementAccountStreakEntity extends AchievementAccountEntity {
    private List<String> completedDatesStreak = new ArrayList<>();

    public AchievementAccountStreakEntity() {
        setType("streak");
    }

    public AchievementAccountStreakEntity(String id, String title, String description, Long achievementPoints, Boolean isCompleted, Timestamp completionDate, String whenToUpdate, int amountToCompleteCount, int completedCount, String type, List<String> completedDatesStreak) {
        super(id, title, description, achievementPoints, isCompleted, completionDate, whenToUpdate, amountToCompleteCount, completedCount, type);
        this.completedDatesStreak = completedDatesStreak;
    }

    public List<String> getCompletedDatesStreak() {
        return completedDatesStreak;
    }

    public void setCompletedDatesStreak(List<String> completedDatesStreak) {
        this.completedDatesStreak = completedDatesStreak;
    }
}
