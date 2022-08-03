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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class StudentsActivity extends AppCompatActivity {

    private ListView mListView;
    private StorageReference mStorageRef;
    private FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();
    private Map<String, String> studentsMap = new HashMap<>();
    private String[] students = new String[10];
    private String[] names = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        mListView = findViewById(R.id.studentsListView);
        mDbReference.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    queryDocumentSnapshots.getDocuments().forEach(document -> {
                        studentsMap.put(document.get("mail").toString(), document.get("name") + " " + document.get("lastName"));
                    });
                }
                students = new String[studentsMap.size()];
                names = new String[studentsMap.size()];

                for (int i = 0; i < students.length; i++) {
                    students[i] = studentsMap.keySet().toArray()[i].toString();
                    names[i] = studentsMap.get(students[i]);
                }
            }
        });

        StudentAdapter studentAdapter = new StudentAdapter();
        mListView.setAdapter(studentAdapter);
    }

    public class StudentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return students.length;
        }

        @Override
        public Object getItem(int i) {
            mStorageRef = FirebaseStorage.getInstance().getReference("uploads/" + students[i] + ".jpg");
            return mStorageRef;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            System.out.print("Sono dentro la get view");

            view = getLayoutInflater().inflate(R.layout.card, viewGroup, false);
            ImageView mImageView = view.findViewById(R.id.studentImageView);
            TextView mTextView = view.findViewById(R.id.studentTextView);

            mTextView.setText(names[i]);
            mStorageRef = FirebaseStorage.getInstance().getReference("uploads/" + students[i] + ".jpg");
            Glide.with(view).load(mStorageRef).into(mImageView);

            return view;
        }
    }
}