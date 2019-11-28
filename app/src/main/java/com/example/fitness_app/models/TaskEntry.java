package com.example.fitness_app.models;

public class TaskEntry {
    private String completionDate;

    public TaskEntry() {}

    public TaskEntry(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }
}
