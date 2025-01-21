package com.example.beefficientapp;

public class Task {
    private String taskName;
    private String date;
    private String time;
    private String tags;

    // Constructor
    public Task(String taskName, String date, String time, String tags) {
        this.taskName = taskName;
        this.date = date;
        this.time = time;
        this.tags = tags;
    }

    // Getters and setters
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
