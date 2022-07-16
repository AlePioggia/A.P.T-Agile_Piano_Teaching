package com.example.apt_agile_piano_teaching.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apt_agile_piano_teaching.activities.LoginActivity;
import com.example.apt_agile_piano_teaching.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private FirebaseUser user;
    private StorageReference mStorageRef;
    private ImageView mProfileImageShow;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView firstName = view.findViewById(R.id.firstName);
        TextView surname = view.findViewById(R.id.surname);
        TextView mail = view.findViewById(R.id.mail);
        Button logoutButton = view.findViewById(R.id.logoutButton);
        mProfileImageShow = view.findViewById(R.id.showProfileImage);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads/" + mAuth.getCurrentUser().getEmail() + ".jpg");
        Glide.with(getActivity()).load(mStorageRef).into(mProfileImageShow);

        firstName.setText(" empty name ");
        surname.setText(" empty surname ");
        mail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        StorageReference imageRef = mStorageRef.child("uploads/alexpioggia@gmail.com.jpg");

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return view;
    }
}