package com.example.fitness_app.entities;

public class BottomSheetTitleEntity {
    private String headline;
    private String subline;

    public BottomSheetTitleEntity(String headline, String subline) {
        this.headline = headline;
        this.subline = subline;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSubline() {
        return subline;
    }

    public void setSubline(String subline) {
        this.subline = subline;
    }
}