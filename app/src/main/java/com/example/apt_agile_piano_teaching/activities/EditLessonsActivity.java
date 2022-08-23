package com.example.apt_agile_piano_teaching.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.databinding.ActivityEditLessonsBinding;
import com.example.apt_agile_piano_teaching.logger.Action;
import com.example.apt_agile_piano_teaching.logger.Category;
import com.example.apt_agile_piano_teaching.logger.CloudLogger;
import com.example.apt_agile_piano_teaching.models.Assignment;
import com.example.apt_agile_piano_teaching.models.Lesson;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class EditLessonsActivity extends AppCompatActivity {

    private ActivityEditLessonsBinding binding;
    private Lesson lesson;
    private FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();
    //Assingment fields
    private Spinner assignmentSpinner;
    private EditText assignmentBookName;
    private EditText assignmentPages;
    private EditText assignmentBpm;
    private EditText lessonNotes;
    private Button assignmentConfirmation;
    private Button assignmentCancel;
    //Assignments
    private ArrayList<Assignment> assignments = new ArrayList<>();

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private CloudLogger cloudLogger = new CloudLogger();


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
                                binding.editLessonSpinner.setSelection(editLessonAdapter.getPosition(lesson.getStudentMail()));
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

        binding.editLessonDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                DatePickerDialog dialog = new DatePickerDialog(EditLessonsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        startDate = LocalDateTime.of(year, month + 1, day, 0, 0, 0);
                        binding.editLessonShowDate.setText(startDate.format(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter()).toString());
                    }
                }
                        , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        binding.editLessonStartDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog dialog = new TimePickerDialog(EditLessonsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            startDate = startDate.withHour(hour).withMinute(minute);
                            Toast.makeText(EditLessonsActivity.this, startDate.toString(), Toast.LENGTH_SHORT).show();
                            binding.editLessonShowStartDate.setText(startDate.getHour() + " : " + startDate.getMinute());
                        }
                    }, hour, minute, true);
                    dialog.show();
                } else {
                    Toast.makeText(EditLessonsActivity.this, "Selezionare prima la data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.editLessonEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate != null) {
                    endDate = startDate;

                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog dialog = new TimePickerDialog(EditLessonsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            if (startDate.isAfter(endDate.withHour(hour).withMinute(minute))) {
                                Toast.makeText(EditLessonsActivity.this, "L'ora di fine deve essere maggiore rispetto a quella iniziale", Toast.LENGTH_SHORT).show();
                            } else {
                                endDate = endDate.withHour(hour).withMinute(minute);
                                binding.editShowLessonEndDate.setText(endDate.getHour() + " : " + endDate.getMinute());
                                Toast.makeText(EditLessonsActivity.this, endDate.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, hour, minute, true);
                    dialog.show();
                } else {
                    Toast.makeText(EditLessonsActivity.this, "Selezionare prima l'orario d'inizio!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.editShowAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditLessonsActivity.this, AssignmentActivity.class);
                if (lesson != null) {
                    intent.putExtra("lesson", lesson);
                    intent.putExtra("assignments", assignments);
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

        binding.editLessonConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate == null) {
                    startDate = lesson.getStartDate();
                }
                if (endDate == null) {
                    endDate = lesson.getStartDate();
                }
                Lesson newLesson = new Lesson(binding.editLessonSpinner.getSelectedItem().toString(),startDate, endDate
                        , binding.editLessonNotes.getText().toString(), "templates/piano.jpg");
                newLesson.setId(lesson.getId());

                for (Assignment assignment: assignments) {
                    final String assignmentId = UUID.randomUUID().toString();

                    if (assignment.getId().isEmpty()) {
                        assignment.setId(assignmentId);
                    }

                    if (assignment.getLessonId().isEmpty()) {
                        assignment.setLessonId(lesson.getId());
                    }

                    mDbReference.collection("assignments")
                            .document(assignmentId)
                            .set(assignment);
                }

                mDbReference.collection("lessons")
                        .document(lesson.getId())
                        .set(newLesson)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                cloudLogger.insertLog(Category.LESSON, Action.UPDATE);
                            }
                        });
                Intent intent = new Intent(EditLessonsActivity.this, LessonsActivity.class);
                startActivity(intent);
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