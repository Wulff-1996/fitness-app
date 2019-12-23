package com.example.fitness_app.util;

import com.example.fitness_app.constrants.QuestStatusTypes;
import com.example.fitness_app.models.QuestAccountEntity;
import com.example.fitness_app.models.QuestEntity;

public class QuestFormatter {

    public static QuestAccountEntity formatToQuestAccountEntity(QuestEntity quest){
        QuestAccountEntity questAccountEntity = new QuestAccountEntity();
        questAccountEntity.setId(quest.getId());
        questAccountEntity.setTitle(quest.getTitle());
        questAccountEntity.setDescription(quest.getDescription());
        questAccountEntity.setLevelRequirement(quest.getLevelRequirement());
        questAccountEntity.setExperiencePoints(quest.getExperiencePoints());
        questAccountEntity.setExpireDate(quest.getExpireDate());
        questAccountEntity.setDateCreated(quest.getDateCreated());
        questAccountEntity.setIsCompleted(false);
        questAccountEntity.setCompletedDate(null);
        questAccountEntity.setHasRequested(false);
        questAccountEntity.setStatus(QuestStatusTypes.NOT_REQUESTED);
        return questAccountEntity;
    }
}
