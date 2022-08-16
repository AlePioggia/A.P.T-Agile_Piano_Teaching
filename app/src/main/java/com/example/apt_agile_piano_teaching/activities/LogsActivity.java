package com.example.apt_agile_piano_teaching.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.adapters.LogAdapter;
import com.example.apt_agile_piano_teaching.databinding.ActivityLogsBinding;
import com.example.apt_agile_piano_teaching.models.UserLogTemplate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LogsActivity extends AppCompatActivity {

    private ActivityLogsBinding binding;
    private LogAdapter adapter;
    private FirebaseFirestore db;
    private ArrayList<UserLogTemplate> logs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        getRecord();
    }

    private void getRecord() {
        db.collection("logs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        logs = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            UserLogTemplate userLog = new UserLogTemplate(queryDocumentSnapshot.getString("email"), queryDocumentSnapshot.getString("action"),
                                    queryDocumentSnapshot.getString("category"), queryDocumentSnapshot.getString("message"));
                            logs.add(userLog);
                        }
                        if (logs.size() > 0) {
                            adapter = new LogAdapter(getApplicationContext(), logs);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            binding.recyclerView.setAdapter(adapter);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}