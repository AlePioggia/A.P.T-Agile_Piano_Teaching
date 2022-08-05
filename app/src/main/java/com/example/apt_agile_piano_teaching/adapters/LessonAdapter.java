package com.example.apt_agile_piano_teaching.adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apt_agile_piano_teaching.activities.LessonsActivity;
import com.example.apt_agile_piano_teaching.databinding.LessonsCardBinding;
import com.example.apt_agile_piano_teaching.listeners.LessonListener;
import com.example.apt_agile_piano_teaching.models.Lesson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonHolder> {

    private final List<Lesson> lessons;
    private final LessonListener lessonListener;

    public LessonAdapter(List<Lesson> lessons, LessonListener lessonListener) {
        this.lessons = lessons;
        this.lessonListener = lessonListener;
    }

    class LessonHolder extends RecyclerView.ViewHolder {

        LessonsCardBinding binding;

        public LessonHolder(LessonsCardBinding lessonsCardBinding) {
            super(lessonsCardBinding.getRoot());
            binding = lessonsCardBinding;
        }

        void setLessonData(Lesson lesson) {
            binding.lessonsTextView.setText(lesson.getStartDate().toString());
            binding.getRoot().setOnClickListener(v -> lessonListener.onLessonClicked(lesson));
        }
    }

    @NonNull
    @Override
    public LessonAdapter.LessonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LessonsCardBinding lessonsCardBinding = LessonsCardBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new LessonHolder(lessonsCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.LessonHolder holder, int position) {
        holder.setLessonData(lessons.get(position));
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }
}
