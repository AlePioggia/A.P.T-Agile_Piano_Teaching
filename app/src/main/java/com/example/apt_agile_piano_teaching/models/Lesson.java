package com.example.apt_agile_piano_teaching.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Lesson implements Serializable {
    private String id;
    private String studentMail;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String notes;
    private String templateImage;

    public Lesson(String studentMail, LocalDateTime startDate, LocalDateTime endDate, String notes, String templateImage) {
        this.studentMail = studentMail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.templateImage = templateImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentMail() {
        return studentMail;
    }

    public void setStudentMail(String studentMail) {
        this.studentMail = studentMail;
    }

    public String getTemplateImage() {
        return templateImage;
    }

    public void setTemplateImage(String templateImage) {
        this.templateImage = templateImage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", notes='" + notes + '\'' +
                ", templateImage='" + templateImage + '\'' +
                '}';
    }
}
