package com.example.apt_agile_piano_teaching.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.apt_agile_piano_teaching.R;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    private Button lessonDateBtn;
    private Button lessonStartTimeButton;
    private Button lessonEndTimeButton;
    private Button assignmentButton;

    //Assingment fields
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        assignmentButton = view.findViewById(R.id.selectAssignmentButton);
        lessonDateBtn = view.findViewById(R.id.lessonDateBtn);

        lessonDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Toast.makeText(getActivity(), "Data settata correttamente", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startDate = LocalDateTime.of(year, month, day, 0, 0, 0);
                        }
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
         assignmentBpm = dialog.findViewById(R.id.assignmentBpm);
         //lessonNotes = dialog.findViewById(R.id.assignmentNotes);
         assignmentConfirmation = dialog.findViewById(R.id.assignmentConfirm);
         assignmentCancel = dialog.findViewById(R.id.assignmentCancel);

         String[] items = new String[]{"Solfeggio", "Tecnica", "Motivetto", "Brano classico", "Brano(melodia)", "Brano(accompagnamento)"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,items);

        assignmentSpinner.setAdapter(adapter);

         assignmentCancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dialog.dismiss();
             }
         });

        dialog.show();
    }

}