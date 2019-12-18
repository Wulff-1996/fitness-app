package com.example.fitness_app.util;

import com.example.fitness_app.constrants.AchievementTypes;
import com.example.fitness_app.models.AchievementAccountAutomaticEntity;
import com.example.fitness_app.models.AchievementAccountEntity;
import com.example.fitness_app.models.AchievementAccountManualEntity;
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
            case AchievementTypes.AUTOMATIC:
                AchievementAccountAutomaticEntity auto = (AchievementAccountAutomaticEntity) accountEntity;
                auto.setAmountToCompleteCount(entryEntity.getAmountToCompleteCount());
                auto.setDescription(entryEntity.getDescription());
                auto.setTitle(entryEntity.getTitle());
                auto.setAchievementPoints(entryEntity.getAchievementPoints());
                auto.setWhenToUpdate(entryEntity.getWhenToUpdate());
                auto.setTotalPlayersCompleted(entryEntity.getTotalPlayersCompleted());
                break;
            case AchievementTypes.MANUAL:
                AchievementAccountManualEntity manual = (AchievementAccountManualEntity) accountEntity;
                manual.setDescription(entryEntity.getDescription());
                manual.setTitle(entryEntity.getTitle());
                manual.setAchievementPoints(entryEntity.getAchievementPoints());
                manual.setWhenToUpdate(entryEntity.getWhenToUpdate());
                manual.setTotalPlayersCompleted(entryEntity.getTotalPlayersCompleted());
                break;
        }
    }
}
