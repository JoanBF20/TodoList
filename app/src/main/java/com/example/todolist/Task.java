package com.example.todolist;

import java.io.Serializable;

public class Task implements Serializable {

    private int id;
    private int id_categoria;
    private String title;
    private String description;
    private boolean complete;

    public Task(int id, String title, String description, int id_categoria, boolean complete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.id_categoria = id_categoria;
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getIdCategoria() {
        return id_categoria;
    }

    public void setIdCategoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

}
