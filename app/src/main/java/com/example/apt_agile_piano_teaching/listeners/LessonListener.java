package com.example.apt_agile_piano_teaching.listeners;

import com.example.apt_agile_piano_teaching.models.Lesson;

public interface LessonListener {
    void onLessonClicked(Lesson lesson);
    void onDeleteClicked(Lesson lesson);
}
