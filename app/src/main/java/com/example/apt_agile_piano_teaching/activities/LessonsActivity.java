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
    private Map<String, String> lessonsMap = new HashMap<>();
    private String[] lessons = new String[10];
    private String[] names = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        mListView = findViewById(R.id.lessonsListView);
        mDbReference.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    queryDocumentSnapshots.getDocuments().forEach(document -> {
                        lessonsMap.put(document.get("mail").toString(), document.get("name") + " " + document.get("lastName"));
                    });
                }
                lessons = new String[lessonsMap.size()];
                names = new String[lessonsMap.size()];

                for (int i = 0; i < lessons.length; i++) {
                    lessons[i] = lessonsMap.keySet().toArray()[i].toString();
                    names[i] = lessonsMap.get(lessons[i]);
                }
            }
        });
    }

}