package com.example.fitness_app.models;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class UserTask
{
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("entries")
    private Map<String, TaskEntry> entries = new HashMap<>();

    public UserTask() {}

    public UserTask(HashMap<String, TaskEntry> entries, String title, String id){
        this.id = id;
        this.title = title;
        this.entries = entries;
    }

    public UserTask(String id, String title, HashMap<String, TaskEntry> entries) {
        this.id = id;
        this.title = title;
        this.entries = entries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, TaskEntry> getEntries() {
        return entries;
    }

    public void setEntries(Map<String, TaskEntry> entries) {
        this.entries = entries;
    }
}
