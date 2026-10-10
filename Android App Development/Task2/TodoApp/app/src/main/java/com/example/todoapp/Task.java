package com.example.todoapp;

/** A single to-do item belonging to one user. */
public class Task {
    public final long id;
    public final String title;
    public final String notes;
    public final boolean completed;

    public Task(long id, String title, String notes, boolean completed) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.completed = completed;
    }
}
