package com.example.fitness_app.entities;

public class TasksEntity {
    private String subject;
    private String durationType;
    private Long startDate;
    private Long endDate;
    private int taskAmount;
    private int completedTasks;

    public TasksEntity() {}

    public TasksEntity(String subject, String durationType, Long startDate, Long endDate, int taskAmount, int completedTasks) {
        this.subject = subject;
        this.durationType = durationType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskAmount = taskAmount;
        this.completedTasks = completedTasks;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public int getTaskAmount() {
        return taskAmount;
    }

    public void setTaskAmount(int taskAmount) {
        this.taskAmount = taskAmount;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }
}
