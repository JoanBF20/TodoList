package com.example.todolist;

import java.io.Serializable;

public class Task implements Serializable {

    private String title;
    private String description;
    private boolean complete;

    public Task(String title, String description, boolean complete) {

        this.title = title;
        this.description = description;
        this.complete = complete;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

}
