package com.example.fitness_app.entities;

public class BottomSheetActionItemEntity {
    private String title;
    private Integer color;
    private String actionId;

    public BottomSheetActionItemEntity(String title) {
        this.title = title;
    }

    public BottomSheetActionItemEntity(String title, Integer color) {
        this.title = title;
        this.color = color;
    }

    public BottomSheetActionItemEntity(String title, Integer color, String actionId) {
        this.title = title;
        this.color = color;
        this.actionId = actionId;
    }

    public String getTitle() {
        return title;
    }

    public String getActionId() {
        return this.actionId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
