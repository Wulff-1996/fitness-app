package com.example.fitness_app.util;

import com.example.fitness_app.constrants.AchievementTypes;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementAccountManualEntity;
import com.example.fitness_app.models.AchievementAccountStreakEntity;
import com.example.fitness_app.models.AchievementAccountTotalEntity;
import com.example.fitness_app.models.AchievementEntryEntity;

import java.util.List;

public class AchievementUtil {

    public static AchievementEntryEntity getById(String id, List<AchievementEntryEntity> entities){
        for (AchievementEntryEntity entity: entities){
            if (entity.getId().equals(id)){
                return entity;
            }
        }
        return null;
    }

    public static void update(AchievementEntryEntity entryEntity, AchievementAccountEntity accountEntity){
        // update account entity
        switch (entryEntity.getType()){
            case AchievementTypes.STREAK:
                AchievementAccountStreakEntity streak = (AchievementAccountStreakEntity) accountEntity;
                streak.setAmountToCompleteCount(entryEntity.getAmountToCompleteCount());
                streak.setDescription(entryEntity.getDescription());
                streak.setTitle(entryEntity.getTitle());
                streak.setAchievementPoints(entryEntity.getAchievementPoints());
                streak.setWhenToUpdate(entryEntity.getWhenToUpdate());
                streak.setTotalPlayersCompleted(entryEntity.getTotalPlayersCompleted());
                break;
            case AchievementTypes.TOTAL:
                AchievementAccountTotalEntity total = (AchievementAccountTotalEntity) accountEntity;
                total.setAmountToCompleteCount(entryEntity.getAmountToCompleteCount());
                total.setDescription(entryEntity.getDescription());
                total.setTitle(entryEntity.getTitle());
                total.setAchievementPoints(entryEntity.getAchievementPoints());
                total.setWhenToUpdate(entryEntity.getWhenToUpdate());
                total.setTotalPlayersCompleted(entryEntity.getTotalPlayersCompleted());
                break;
            case AchievementTypes.MANUAL:
                AchievementAccountManualEntity manual = (AchievementAccountManualEntity) accountEntity;
                manual.setAmountToCompleteCount(entryEntity.getAmountToCompleteCount());
                manual.setDescription(entryEntity.getDescription());
                manual.setTitle(entryEntity.getTitle());
                manual.setAchievementPoints(entryEntity.getAchievementPoints());
                manual.setWhenToUpdate(entryEntity.getWhenToUpdate());
                manual.setTotalPlayersCompleted(entryEntity.getTotalPlayersCompleted());
                break;
        }
    }
}
