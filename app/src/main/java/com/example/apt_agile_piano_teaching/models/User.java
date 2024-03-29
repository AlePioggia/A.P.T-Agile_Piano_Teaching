package com.example.apt_agile_piano_teaching.models;

import java.sql.Timestamp;
import java.util.Date;

public class User {

    private String mail;
    private String name;
    private String lastName;

    public User(String mail, String name, String lastName) {
        this.mail = mail;
        this.name = name;
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
