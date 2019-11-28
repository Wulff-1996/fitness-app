package com.example.fitness_app.models;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private String id;
    private String subject;
    private List<TaskEntry> entries = new ArrayList<>();

    public Task() {}

    public Task(String id, String subject, List<TaskEntry> entries) {
        this.id = id;
        this.subject = subject;
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<TaskEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TaskEntry> entries) {
        this.entries = entries;
    }
}
