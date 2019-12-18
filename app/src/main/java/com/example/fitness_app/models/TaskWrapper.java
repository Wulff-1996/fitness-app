package com.example.fitness_app.models;

/**
 * class to wrap a isCompleted for today field to the entity UserTask
 * this is because the UserTask Entity have to mirror the database entity
 */
public class TaskWrapper {
    private UserTask userTask;
    private boolean isCompletedToday;

    public TaskWrapper() {}

    public TaskWrapper(UserTask userTask, boolean isCompletedToday) {
        this.userTask = userTask;
        this.isCompletedToday = isCompletedToday;
    }

    public UserTask getUserTask() {
        return userTask;
    }

    public void setUserTask(UserTask userTask) {
        this.userTask = userTask;
    }

    public boolean getCompletedToday() {
        return isCompletedToday;
    }

    public void setCompletedToday(boolean completedToday) {
        isCompletedToday = completedToday;
    }
}
