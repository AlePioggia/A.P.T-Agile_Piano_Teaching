package com.example.apt_agile_piano_teaching.models;

import java.io.Serializable;

public class Assignment implements Serializable {

    private String id;
    private String lessonId;
    private String exercise;
    private String bookName;
    private String pages;
    private double bpm;

    public Assignment(String id, String lessonId, String exercise, String bookName, String pages, double bpm) {
        this.id = id;
        this.lessonId = lessonId;
        this.exercise = exercise;
        this.bookName = bookName;
        this.pages = pages;
        this.bpm = bpm;
    }

    public Assignment(String exercise, String bookName, String pages, double bpm) {
        this.id = "";
        this.lessonId = "";
        this.exercise = exercise;
        this.bookName = bookName;
        this.pages = pages;
        this.bpm = bpm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public double getBpm() {
        return bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "exercise='" + exercise + '\'' +
                ", bookName='" + bookName + '\'' +
                ", pages='" + pages + '\'' +
                ", bpm=" + bpm +
                '}';
    }
}
