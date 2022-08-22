package com.example.apt_agile_piano_teaching.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    //Assingment fields
    private Spinner lessonSpinner;
    private Spinner assignmentSpinner;
    private EditText assignmentBookName;
    private EditText assignmentPages;
    private EditText assignmentBpm;
    private EditText lessonNotes;
    private Button assignmentConfirmation;
    private Button assignmentCancel;
    //Assignments
    private List<Assignment> assignments = new ArrayList<>();

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

        binding.editShowAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditLessonsActivity.this, AssignmentActivity.class);
                if (lesson != null) {
                    intent.putExtra("lesson", lesson);
                }
                startActivity(intent);
            }
        });

        binding.editSelectAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(EditLessonsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.assignment_dialog);

        assignmentSpinner = dialog.findViewById(R.id.assignmentSpinner);
        assignmentBookName = dialog.findViewById(R.id.assignmentSpinnerBookName);
        assignmentPages = dialog.findViewById(R.id.assignmentPages);
        assignmentBpm = dialog.findViewById(R.id.assignmentBpm);
        assignmentConfirmation = dialog.findViewById(R.id.assignmentConfirm);
        assignmentCancel = dialog.findViewById(R.id.assignmentCancel);

        String[] items = new String[]{"Solfeggio", "Tecnica", "Motivetto", "Brano classico", "Brano(melodia)", "Brano(accompagnamento)"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditLessonsActivity.this, android.R.layout.simple_spinner_dropdown_item,items);

        boolean isValid = assignmentSpinner != null && assignmentBookName != null && assignmentBpm != null;

        assignmentSpinner.setAdapter(adapter);

        assignmentConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid) {
                    final Assignment assignment = new Assignment(
                            assignmentSpinner.getSelectedItem().toString(),
                            assignmentBookName.getText().toString(),
                            assignmentPages.getText().toString(),
                            Integer.valueOf(assignmentBpm.getText().toString()));

                    assignments.add(assignment);

                    Toast.makeText(EditLessonsActivity.this, assignments.toString(), Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                } else {
                    Toast.makeText(EditLessonsActivity.this, "Compila tutti i campi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        assignmentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}