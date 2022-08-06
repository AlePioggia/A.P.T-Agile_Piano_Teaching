package com.example.apt_agile_piano_teaching.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.databinding.ActivityEditLessonsBinding;
import com.example.apt_agile_piano_teaching.models.Assignment;
import com.example.apt_agile_piano_teaching.models.Lesson;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditLessonsActivity extends AppCompatActivity {

    ActivityEditLessonsBinding binding;
    Lesson lesson;
    private FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditLessonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lesson = (Lesson) getIntent().getExtras().get("lesson");

        mDbReference.collection("lessons")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            if (queryDocumentSnapshot.get("id").equals(lesson.getId())) {
                                binding.editLessonShowDate.setText(lesson.getStartDate().toString());
                                binding.editLessonShowStartDate.setText(lesson.getStartDate().getHour() + " : " + lesson.getStartDate().getMinute());
                                binding.editShowLessonEndDate.setText(lesson.getEndDate().getHour() + " : " + lesson.getEndDate().getMinute());
                            }
                        }
                    }
                });
    }
}