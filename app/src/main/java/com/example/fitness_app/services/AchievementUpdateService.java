package com.example.fitness_app.services;

import java.util.ArrayList;
import java.util.List;

//TODO call this class all places where you want to check for achievement completion

/**
 * this class contains a list of all achievement updates
 * when this class is called with the method onUpdateEvent it takes a list of eventUpdateKeys
 * it compares these keys to the updateEntities update keys if match it will begin those updates
 */
public class AchievementUpdateService {
    private static AchievementUpdateService instance;
    private static List<UpdateEntity> updates = new ArrayList<>();

    private AchievementUpdateService() {
    }

    public static AchievementUpdateService getInstance(){
        if (instance == null){
            instance = new AchievementUpdateService();
            setUpdates();
        }
        return instance;
    }

    public static void onUpdateEvent(List<String> updateKeys){
        // get all documents that matches the update event
        for(UpdateEntity update: updates){
            if (update.getUpdateKeys().containsAll(updateKeys)){
                update.onUpdate();
            }
        }

    }

    private static void setUpdates(){
        updates.add(new CompleteAnyTaskOnceUpdate());
    }
}
