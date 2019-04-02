package com.auth0.todo;

public class ToDoItem {
    private final String id;
    private final String message;

    ToDoItem(String _id, String message) {
        this.id = _id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}