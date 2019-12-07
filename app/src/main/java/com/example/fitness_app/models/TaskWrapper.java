package com.example.fitness_app.models;

/**
 * class to wrap a isCompleted for today field to the entity UserTask
 * this is because the UserTask Entity have to mirror the database entity
 */
public class TaskWrapper {
    private UserTask userTask;
    private Boolean isCompletedToday;

    public TaskWrapper() {}

    public TaskWrapper(UserTask userTask, Boolean isCompletedToday) {
        this.userTask = userTask;
        this.isCompletedToday = isCompletedToday;
    }

    public UserTask getUserTask() {
        return userTask;
    }

    public void setUserTask(UserTask userTask) {
        this.userTask = userTask;
    }

    public Boolean getCompletedToday() {
        return isCompletedToday;
    }

    public void setCompletedToday(Boolean completedToday) {
        isCompletedToday = completedToday;
    }
}
