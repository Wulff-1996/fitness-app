package com.example.fitness_app.models;

import com.google.gson.annotations.SerializedName;

public class TaskEntry {
    @SerializedName("id")
    private String id;
    @SerializedName("completion_date")
    private String completionDate;

    public TaskEntry() {}

    public TaskEntry(String completionDate) {
        this.completionDate = completionDate;
    }

    public TaskEntry(String id, String completionDate) {
        this.id = id;
        this.completionDate = completionDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
