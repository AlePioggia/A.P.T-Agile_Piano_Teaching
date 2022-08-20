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

import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditLessonsActivity extends AppCompatActivity {

    private ActivityEditLessonsBinding binding;
    private Lesson lesson;
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
                        List<String> items = new ArrayList<>();

                        mDbReference.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    items.add(queryDocumentSnapshot.getString("mail"));
                                }
                                String[] itemsArray = items.toArray(new String[items.size()]);
                                ArrayAdapter<String> editLessonAdapter = new ArrayAdapter<>(EditLessonsActivity.this, android.R.layout.simple_spinner_dropdown_item, itemsArray);
                                binding.editLessonSpinner.setAdapter(editLessonAdapter);
                            }
                        });

                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            if (queryDocumentSnapshot.get("id").equals(lesson.getId())) {
                                binding.editLessonShowDate.setText(lesson.getStartDate().format(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter()));
                                binding.editLessonShowStartDate.setText(lesson.getStartDate().getHour() + " : " + lesson.getStartDate().getMinute());
                                binding.editShowLessonEndDate.setText(lesson.getEndDate().getHour() + " : " + lesson.getEndDate().getMinute());
                            }
                        }
                    }
                });
    }
}