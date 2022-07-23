package com.example.apt_agile_piano_teaching.models;

public class Assignment {

    private String exercise;
    private String bookName;
    private String pages;
    private double bpm;

    public Assignment(String exercise, String bookName, String pages, double bpm) {
        this.exercise = exercise;
        this.bookName = bookName;
        this.pages = pages;
        this.bpm = bpm;
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
