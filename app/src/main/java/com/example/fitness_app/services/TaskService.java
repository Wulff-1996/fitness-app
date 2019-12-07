package com.example.fitness_app.services;

import android.text.format.DateUtils;

import com.example.fitness_app.models.TaskEntry;
import com.example.fitness_app.models.TaskWrapper;
import com.example.fitness_app.util.Dates;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

public class TaskService {

    public static TaskEntry getEntryForCurrentDay(TaskWrapper taskWrapper){
        for (TaskEntry entry: taskWrapper.getUserTask().getEntries().values()){
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

    /**
     *
     * @param targetDate the date of the task entry you wish to return
     * @param dates list of dates to get the task entry from
     * @return the task entry witch have the same day as the target
     */
    public static TaskEntry getTaskEntry(Calendar targetDate, Collection<TaskEntry> dates){
        for (TaskEntry entry: dates) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.valueOf(entry.getCompletionDate()));
            if (Dates.isSameDay(targetDate, cal)){
                return entry;
            }
        }
        throw new IndexOutOfBoundsException("Target date not in dates");
    }


}
