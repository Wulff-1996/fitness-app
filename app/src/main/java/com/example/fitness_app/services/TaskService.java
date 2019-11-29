package com.example.fitness_app.services;

import android.text.format.DateUtils;

import com.example.fitness_app.models.TaskEntry;
import com.example.fitness_app.models.TaskWrapper;

import java.util.Map;

public class TaskService {

    public static TaskEntry getEntryForCurrentDay(TaskWrapper taskWrapper){
        for (TaskEntry entry: taskWrapper.getTask().getEntries().values()){
            if (DateUtils.isToday(Long.parseLong(entry.getCompletionDate()))) return entry;
        }
        throw new IndexOutOfBoundsException("element not in list");
    }

    public static boolean setIsCompletedToday(Map<String, TaskEntry> entries){
        for (TaskEntry entry: entries.values()) {
            if (DateUtils.isToday(Long.parseLong(entry.getCompletionDate()))) return true;
        }
        return false;
    }


}
