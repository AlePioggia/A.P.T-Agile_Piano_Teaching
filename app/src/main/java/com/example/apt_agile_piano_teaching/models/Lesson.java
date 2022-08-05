package com.example.apt_agile_piano_teaching.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Lesson implements Serializable {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Assignment> assignments;
    private String notes;

    public Lesson(LocalDateTime startDate, LocalDateTime endDate, List<Assignment> assignments, String notes) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignments = assignments;
        this.notes = notes;
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

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
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
                ", assignments=" + assignments +
                ", notes='" + notes + '\'' +
                '}';
    }
}
