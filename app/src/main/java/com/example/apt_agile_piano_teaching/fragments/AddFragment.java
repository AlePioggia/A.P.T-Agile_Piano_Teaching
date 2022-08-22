package com.example.apt_agile_piano_teaching.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.activities.RegistrationActivity;
import com.example.apt_agile_piano_teaching.logger.Action;
import com.example.apt_agile_piano_teaching.logger.Category;
import com.example.apt_agile_piano_teaching.logger.CloudLogger;
import com.example.apt_agile_piano_teaching.models.Assignment;
import com.example.apt_agile_piano_teaching.models.Lesson;
import com.example.apt_agile_piano_teaching.utils.Preference;
import com.example.apt_agile_piano_teaching.utils.Settings;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    private final CloudLogger cloudLogger = new CloudLogger();
    private Settings mailSettings;

    private TextView showDate;
    private TextView showStartDate;
    private TextView showEndDate;

    private Button lessonDateBtn;
    private Button lessonStartTimeButton;
    private Button lessonEndTimeButton;
    private Button assignmentButton;
    private Button lessonConfirmButton;
    private Button cancelButton;

    //Assingment fields
    private Spinner lessonSpinner;
    private Spinner assignmentSpinner;
    private EditText assignmentBookName;
    private EditText assignmentPages;
    private EditText assignmentBpm;
    private EditText lessonNotes;
    private Button assignmentConfirmation;
    private Button assignmentCancel;

    //Date values
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String mailSubject;
    private String mailText;

    //Assignments
    private List<Assignment> assignments = new ArrayList<>();

    //Firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mailSettings = new Settings(getActivity(), Preference.EMAIL_PREFERENCE);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        lessonConfirmButton = view.findViewById(R.id.lessonConfirmBtn);
        assignmentButton = view.findViewById(R.id.selectAssignmentButton);
        lessonDateBtn = view.findViewById(R.id.lessonDateBtn);
        lessonNotes = view.findViewById(R.id.lessonNotes);
        showDate = view.findViewById(R.id.showDate);
        showStartDate = view.findViewById(R.id.showStartDate);
        showEndDate = view.findViewById(R.id.showEndDate);
        lessonSpinner = view.findViewById(R.id.lessonSpinner);

        setMailData();

        List<String> items = new ArrayList<>();
        mDbReference.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    queryDocumentSnapshots.getDocuments().forEach(document -> {
                        items.add(document.get("mail").toString());
                    });
                }
                String[] itemsArray = items.toArray(new String[items.size()]);
                ArrayAdapter<String> lessonAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, itemsArray);
                lessonSpinner.setAdapter(lessonAdapter);
            }
        });

        lessonConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lesson lesson = new Lesson(lessonSpinner.getSelectedItem().toString(),startDate, endDate
                        , lessonNotes.getText().toString(), "templates/piano.jpg");

                lesson.setId(UUID.randomUUID().toString());

                for (Assignment assignment: assignments) {
                    final String assignmentId = UUID.randomUUID().toString();

                    assignment.setLessonId(lesson.getId());
                    assignment.setId(assignmentId);

                    mDbReference.collection("assignments")
                            .document(assignmentId)
                            .set(assignment);
                }

                mDbReference.collection("lessons")
                        .document()
                        .set(lesson)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (mailSubject != null && mailText != null
                                && mailSettings.getPreference() == true) {
                                    Toast.makeText(getActivity(), "value: " + mailSettings.getPreference(), Toast.LENGTH_SHORT).show();
                                    sendMail(lesson.getStudentMail(), mailSubject, mailText);
                                }
                                cloudLogger.insertLog(Category.LESSON, Action.INSERT);
                                Toast.makeText(getActivity(), "Inserimento della lezione avvenuto con successo", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        lessonDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Toast.makeText(getActivity(), "Data settata correttamente", Toast.LENGTH_SHORT).show();
                        startDate = LocalDateTime.of(year, month, day, 0, 0, 0);
                        showDate.setText(startDate.format(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter()).toString());
                    }
                }
                        , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        lessonStartTimeButton = view.findViewById(R.id.lessonStartTimeBtn);
        lessonStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            startDate = startDate.withHour(hour).withMinute(minute);
                            Toast.makeText(getActivity(), startDate.toString(), Toast.LENGTH_SHORT).show();
                            showStartDate.setText(startDate.getHour() + " : " + startDate.getMinute());
                        }
                    }, hour, minute, true);
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "Selezionare prima la data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lessonEndTimeButton = view.findViewById(R.id.lessonEndTimeBtn);
        lessonEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate != null) {
                    endDate = startDate;

                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                            if (startDate.isAfter(endDate.withHour(hour).withMinute(minute))) {
                                Toast.makeText(getActivity(), "L'ora di fine deve essere maggiore rispetto a quella iniziale", Toast.LENGTH_SHORT).show();
                            } else {
                                endDate = endDate.withHour(hour).withMinute(minute);
                                showEndDate.setText(endDate.getHour() + " : " + endDate.getMinute());
                                Toast.makeText(getActivity(), endDate.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, hour, minute, true);
                    dialog.show();
                } else {
                    Toast.makeText(getActivity(), "Selezionare prima l'orario d'inizio!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        return view;
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,items);

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

                    Toast.makeText(getActivity(), assignments.toString(), Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Compila tutti i campi", Toast.LENGTH_SHORT).show();
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

    private void setMailData() {
        mDbReference.collection("emailTemplates")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        mailSubject = documentSnapshot.getString("subject");
                        mailText = documentSnapshot.getString("text");
                    }
                });
    }

    private void sendMail(String receiver, String subject, String text) {
        final Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{receiver});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, text);
        //need this to prompts email client only
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

}