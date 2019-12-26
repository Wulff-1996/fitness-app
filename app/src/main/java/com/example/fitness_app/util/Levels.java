package com.example.fitness_app.util;

import com.example.fitness_app.models.LevelInformationEntity;

public class Levels {

    public static LevelInformationEntity getLevelInformation(int experiencePoints)
    {
        int level = 1;
        int expNeeded = 100;
        int lastExp = 0;
        int maxLevel = 60;

        for (int i = level; i < maxLevel; i++)
        {
            if (experiencePoints < expNeeded)
            {
                break;
            }
            lastExp = expNeeded;
            if (i < 30)
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.10);
            }
            else if (i < 50)
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.05);
            }
            else
            {
                expNeeded = 100 * i + (int) (expNeeded * 1.025);
            }
            level++;
        }

        float percentDone = 0;

        if (level < maxLevel)
        {
            percentDone = (experiencePoints-lastExp)*100.0f/(expNeeded-lastExp);
        }
        else
        {
            percentDone = 100f;
        }

        LevelInformationEntity levelEntity = new LevelInformationEntity();
        levelEntity.setLevel(level);
        levelEntity.setPercentDone((int) percentDone);
        levelEntity.setCurrentExperiencePoints((experiencePoints-lastExp));
        levelEntity.setExperiencePointsToLevelUp((expNeeded-lastExp));
        return levelEntity;
    }
}
