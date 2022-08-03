package com.example.apt_agile_piano_teaching.activities;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.apt_agile_piano_teaching.fragments.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class LessonsActivity extends AppCompatActivity {

    private ListView mListView;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();
    private Map<String, String> studentsMap = new HashMap<>();
    private String[] students = new String[10];
    private String[] names = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        mListView = findViewById(R.id.lessonsListView);
    }

}