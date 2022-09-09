package com.example.apt_agile_piano_teaching.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.adapters.LessonAdapter;
import com.example.apt_agile_piano_teaching.adapters.UserAdapter;
import com.example.apt_agile_piano_teaching.databinding.ActivityStudentsBinding;
import com.example.apt_agile_piano_teaching.models.Assignment;
import com.example.apt_agile_piano_teaching.models.Lesson;
import com.example.apt_agile_piano_teaching.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsActivity extends AppCompatActivity {

    private ActivityStudentsBinding binding;
    private UserAdapter adapter;
    FirebaseFirestore db;
    private List<User> users;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        displayUsersAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayUsersAdapter() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()) {
                            User user = new User(queryDocumentSnapshot.getString("mail")
                                    , queryDocumentSnapshot.getString("name"), queryDocumentSnapshot.getString("lastName"));
                            users.add(user);
                        }
                    }
                    if (users.size() > 0) {
                        adapter = new UserAdapter(users, getApplicationContext());
                        binding.studentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        binding.studentsRecyclerView.setAdapter(adapter);
                        binding.studentsRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }


}