package com.example.apt_agile_piano_teaching.fragments;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apt_agile_piano_teaching.activities.EditLessonsActivity;
import com.example.apt_agile_piano_teaching.activities.ImagesActivity;
import com.example.apt_agile_piano_teaching.activities.LessonsActivity;
import com.example.apt_agile_piano_teaching.activities.LoginActivity;
import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.activities.RegistrationActivity;
import com.example.apt_agile_piano_teaching.activities.StudentsActivity;
import com.example.apt_agile_piano_teaching.models.Assignment;
import com.example.apt_agile_piano_teaching.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private Button mEditProfileBtn;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();

    private EditText userName;
    private EditText userLastName;
    private Button confirmButton;
    private Button cancelButton;

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
        mEditProfileBtn = view.findViewById(R.id.editProfileButton);

        mEditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        Glide.with(getActivity()).load(mStorageRef).into(mProfileImageShow);

        mDbReference.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    firstName.setText(documentSnapshot.get("name", String.class));
                    surname.setText(documentSnapshot.get("lastName", String.class));
                    mail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        return view;
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.edit_profile_dialog);

        userName = dialog.findViewById(R.id.userName);
        userLastName = dialog.findViewById(R.id.userLastName);
        confirmButton = dialog.findViewById(R.id.userEditConfirm);
        cancelButton = dialog.findViewById(R.id.userEditCancel);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName != null
                && userLastName != null) {

                    User user = new User(mAuth.getCurrentUser().getEmail(), userName.getText().toString(), userLastName.getText().toString());

                    mDbReference.collection("users").document(mAuth.getCurrentUser().getUid())
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}