package com.example.apt_agile_piano_teaching.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.apt_agile_piano_teaching.adapters.LessonAdapter;
import com.example.apt_agile_piano_teaching.databinding.ActivityLessonsBinding;
import com.example.apt_agile_piano_teaching.listeners.LessonListener;
import com.example.apt_agile_piano_teaching.models.Assignment;
import com.example.apt_agile_piano_teaching.models.Lesson;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class LessonsActivity extends AppCompatActivity implements LessonListener {

    private ActivityLessonsBinding binding;
    private LessonAdapter lessonAdapter;
    private FirebaseFirestore db;
    private String mImageTemplateUri;
    private List<Lesson> lessons;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mImageTemplateUri = "templates/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() +
                "_template.jpg";
        super.onCreate(savedInstanceState);
        binding = ActivityLessonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        displayUsersAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayUsersAdapter() {
        db.collection("lessons")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        lessons = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {
                            Map startDateMap = (Map) queryDocumentSnapshots.get("startDate");
                            Map endDateMap = (Map) queryDocumentSnapshots.get("endDate");

                            ArrayList<Assignment> assignments = (ArrayList<Assignment>) queryDocumentSnapshots.get("assignments");

                            int startDateYear = ((Number) startDateMap.get("year")).intValue();
                            int startDayOfMonth = ((Number) startDateMap.get("dayOfMonth")).intValue();
                            int startDateMonth = ((Number) startDateMap.get("monthValue")).intValue();
                            int startDateHour = ((Number) startDateMap.get("hour")).intValue();
                            int startDateMinute = ((Number) startDateMap.get("minute")).intValue();
                            int endDateYear = ((Number) endDateMap.get("year")).intValue();
                            int endDayOfMonth = ((Number) endDateMap.get("dayOfMonth")).intValue();
                            int endDateMonth = ((Number) endDateMap.get("monthValue")).intValue();
                            int endDateHour = ((Number) endDateMap.get("hour")).intValue();
                            int endDateMinute = ((Number) endDateMap.get("minute")).intValue();

                            Lesson lesson = new Lesson(queryDocumentSnapshots.get("studentMail").toString(),LocalDateTime.of(startDateYear, startDateMonth, startDayOfMonth, startDateHour, startDateMinute),
                                    LocalDateTime.of(endDateYear, endDateMonth, endDayOfMonth, endDateHour, endDateMinute),
                                    "note",
                                    mImageTemplateUri);
                            lesson.setId(queryDocumentSnapshots.get("id").toString());

                            lessons.add(lesson);
                        }
                        if (lessons.size() > 0) {
                            lessonAdapter = new LessonAdapter(lessons, this, getApplicationContext());
                            binding.lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            binding.lessonsRecyclerView.setAdapter(lessonAdapter);
                            binding.lessonsRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @Override
    public void onLessonClicked(Lesson lesson) {
        Intent intent = new Intent(LessonsActivity.this, EditLessonsActivity.class);
        intent.putExtra("lesson", lesson);
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(Lesson lesson) {
        db.collection("lessons").document(lesson.getId()).delete();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}