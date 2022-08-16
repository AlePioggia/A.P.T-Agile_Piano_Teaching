package com.example.apt_agile_piano_teaching.models;

import java.util.Date;

public class UserLogTemplate {

    private final String email;
    private final String action;
    private final String category;
    private final String message;
    private final Date createdDateTime;

    public UserLogTemplate(String email, String action, String category, String message, Date createdDateTime) {
        this.email = email;
        this.action = action;
        this.category = category;
        this.message = message;
        this.createdDateTime = createdDateTime;
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

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

}
