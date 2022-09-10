package com.example.apt_agile_piano_teaching.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.adapters.AssignmentAdapter;
import com.example.apt_agile_piano_teaching.adapters.LessonAdapter;
import com.example.apt_agile_piano_teaching.adapters.LogAdapter;
import com.example.apt_agile_piano_teaching.databinding.ActivityAssignmentBinding;
import com.example.apt_agile_piano_teaching.databinding.ActivityLessonsBinding;
import com.example.apt_agile_piano_teaching.listeners.AssignmentListener;
import com.example.apt_agile_piano_teaching.models.Assignment;
import com.example.apt_agile_piano_teaching.models.Lesson;
import com.example.apt_agile_piano_teaching.models.UserLogTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignmentActivity extends AppCompatActivity implements AssignmentListener {

    private ActivityAssignmentBinding binding;
    private Lesson lesson;
    private FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();
    private ArrayList<Assignment> assignments;
    private AssignmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lesson = (Lesson) getIntent().getExtras().get("lesson");
        assignments = (ArrayList<Assignment>) getIntent().getExtras().get("assignments");

        if (lesson != null) {
            mDbReference.collection("assignments")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                if (queryDocumentSnapshot.getString("lessonId").equals(lesson.getId())) {
                                    Assignment assignment = new Assignment(queryDocumentSnapshot.getString("id"), queryDocumentSnapshot.getString("lessonId")
                                            ,queryDocumentSnapshot.getString("exercise"), queryDocumentSnapshot.getString("bookName"),
                                            queryDocumentSnapshot.getString("pages"), queryDocumentSnapshot.getDouble("bpm"));
                                    assignments.add(assignment);
                                }
                            }
                            if (assignments.size() > 0) {
                                adapter = new AssignmentAdapter(getApplicationContext(), assignments, this);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                binding.recyclerView.setAdapter(adapter);
                                binding.recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } else {
            if (assignments.size() > 0) {
                System.out.println(assignments);
                adapter = new AssignmentAdapter(getApplicationContext(), assignments, this);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onAssignmentClicked(Assignment assignment) {
        mDbReference.collection("assignments").document(assignment.getId()).delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assignments.remove(assignment);
            }
        });
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}