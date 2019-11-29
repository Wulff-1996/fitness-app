package com.example.fitness_app.models;

import java.util.HashMap;
import java.util.Map;

public class Task {
    private String id;
    private String subject;
    private Map<String, TaskEntry> entries = new HashMap<>();

    public Task() {}

    public Task(HashMap<String, TaskEntry> entries, String subject, String id){
        this.id = id;
        this.subject = subject;
        this.entries = entries;
    }

    public Task(String id, String subject, HashMap<String, TaskEntry> entries) {
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

    public Map<String, TaskEntry> getEntries() {
        return entries;
    }

    public void setEntries(Map<String, TaskEntry> entries) {
        this.entries = entries;
    }
}
