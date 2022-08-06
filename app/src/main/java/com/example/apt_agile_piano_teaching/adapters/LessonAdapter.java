package com.example.apt_agile_piano_teaching.adapters;

import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apt_agile_piano_teaching.activities.LessonsActivity;
import com.example.apt_agile_piano_teaching.databinding.LessonsCardBinding;
import com.example.apt_agile_piano_teaching.listeners.LessonListener;
import com.example.apt_agile_piano_teaching.models.Lesson;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonHolder> {

    private final List<Lesson> lessons;
    private final LessonListener lessonListener;
    private final Context context;

    public LessonAdapter(List<Lesson> lessons, LessonListener lessonListener, Context context) {
        this.lessons = lessons;
        this.lessonListener = lessonListener;
        this.context = context;
    }

    class LessonHolder extends RecyclerView.ViewHolder {

        LessonsCardBinding binding;

        public LessonHolder(LessonsCardBinding lessonsCardBinding) {
            super(lessonsCardBinding.getRoot());
            binding = lessonsCardBinding;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        void setLessonData(Lesson lesson) {
            binding.lessonStartDate.setText(lesson.getStartDate().format(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter()));
            binding.lessonTime.setText(lesson.getStartDate().getHour() + " : " + lesson.getStartDate().getMinute());
            Glide.with(context).load(FirebaseStorage.getInstance().getReference(lesson.getTemplateImage())).into(binding.lessonsImageView);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.LessonHolder holder, int position) {
        holder.setLessonData(lessons.get(position));
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }
}
