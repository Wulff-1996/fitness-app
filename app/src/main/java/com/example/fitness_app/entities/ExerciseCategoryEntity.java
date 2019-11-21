package com.example.fitness_app.entities;

public class ExerciseCategoryEntity {
    private String title;
    private int repitition;

    public ExerciseCategoryEntity() {}

    public ExerciseCategoryEntity(String title, int repitition) {
        this.title = title;
        this.repitition = repitition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRepitition() {
        return repitition;
    }

    public void setRepitition(int repitition) {
        this.repitition = repitition;
    }
}
