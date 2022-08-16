package com.example.apt_agile_piano_teaching.models;

public class UserLogTemplate {

    private final String email;
    private final String action;
    private final String category;
    private final String message;

    public UserLogTemplate(String email, String action, String category, String message) {
        this.email = email;
        this.action = action;
        this.category = category;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public String getAction() {
        return action;
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }
}
