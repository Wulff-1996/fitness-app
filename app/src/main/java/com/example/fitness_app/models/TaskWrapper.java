package com.example.fitness_app.models;

/**
 * class to wrap a isCompleted for today field to the entity Task
 * this is because the Task Entity have to mirror the database entity
 */
public class TaskWrapper {
    private Task task;
    private Boolean isCompletedToday;

    public TaskWrapper() {}

    public TaskWrapper(Task task, Boolean isCompletedToday) {
        this.task = task;
        this.isCompletedToday = isCompletedToday;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Boolean getCompletedToday() {
        return isCompletedToday;
    }

    public void setCompletedToday(Boolean completedToday) {
        isCompletedToday = completedToday;
    }
}
