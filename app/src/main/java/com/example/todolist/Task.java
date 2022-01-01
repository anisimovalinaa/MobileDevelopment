package com.example.todolist;

public class Task {
    private String taskName, date;
    private boolean done;

    public Task(String taskName, boolean done, String date) {
        this.taskName = taskName;
        this.done = done;
        this.date = date;
    }

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

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
