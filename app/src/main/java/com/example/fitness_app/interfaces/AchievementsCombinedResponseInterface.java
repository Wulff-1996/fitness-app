package com.example.fitness_app.interfaces;

import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementEntryEntity;

import java.util.List;

public interface AchievementsCombinedResponseInterface {
    void onFailure();
    void onSuccess(List<AchievementEntryEntity> allAchievements, List<AchievementAccountEntity> allAccountAchievements);
}
