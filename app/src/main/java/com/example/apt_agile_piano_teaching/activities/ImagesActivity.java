package com.example.apt_agile_piano_teaching.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.models.ImageUpload;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ImagesActivity extends AppCompatActivity {

    private FirebaseFirestore mDbReference;
    private List<ImageUpload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
    }
}