package com.example.fitness_app.services;

import com.example.fitness_app.interfaces.AchievementCompletedListener;

import java.util.ArrayList;
import java.util.List;

public abstract class UpdateEntity {
    private List<String> updateKeys = new ArrayList<>();
    private AchievementCompletedListener completedListener;

    public UpdateEntity() {}

    public abstract void onUpdate();

    public void setUpdateKeys(List<String>updateKeys){this.updateKeys = updateKeys;}
    public List<String> getUpdateKeys(){return this.updateKeys;}

    public AchievementCompletedListener getCompletedListener() {
        return completedListener;
    }

    public void setCompletedListener(AchievementCompletedListener completedListener) {
        this.completedListener = completedListener;
    }
}
