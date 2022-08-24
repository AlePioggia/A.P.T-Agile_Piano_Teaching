package com.example.apt_agile_piano_teaching.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.adapters.LogAdapter;
import com.example.apt_agile_piano_teaching.databinding.FragmentMenuBinding;
import com.example.apt_agile_piano_teaching.models.UserLogTemplate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private LogAdapter adapter;
    private FirebaseFirestore db;
    private ArrayList<UserLogTemplate> logs;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
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
        binding = FragmentMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        db = FirebaseFirestore.getInstance();

        getRecords();

        return view;
    }

    private void getRecords() {
        db.collection("logs")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        logs = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            UserLogTemplate userLog = new UserLogTemplate(queryDocumentSnapshot.getString("email"), queryDocumentSnapshot.getString("action"),
                                    queryDocumentSnapshot.getString("category"), queryDocumentSnapshot.getString("message"), queryDocumentSnapshot.getTimestamp("createdDateTime").toDate());
                            logs.add(userLog);
                        }
                        if (logs.size() > 0) {
                            adapter = new LogAdapter(getActivity(), logs);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            binding.recyclerView.setAdapter(adapter);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}