package com.example.apt_agile_piano_teaching.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class RegistrationActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();
    private EditText firstName;
    private EditText surname;
    private EditText mail;
    private EditText password;
    private EditText confirmPassword;
    private Button chooseProfileImageButton;
    private Button registerBtn;
    private TextView loginNowBtn;
    private ImageView profileImage;
    private Uri mImageUri;

    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        mail = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        chooseProfileImageButton = findViewById(R.id.btnChooseProfileImg);
        registerBtn = findViewById(R.id.registerBtn);
        loginNowBtn = findViewById(R.id.logInNowBtn);
        profileImage = findViewById(R.id.profileImage);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        chooseProfileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // get data from EditTexts into strings
                final String firstNameTxt = firstName.getText().toString();
                final String surnameTxt = surname.getText().toString();
                final String mailTxt = mail.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String confirmPasswordTxt = confirmPassword.getText().toString();

                //check if user did fill all the fields before sendig data to firebase
                if (firstNameTxt.isEmpty() || surnameTxt.isEmpty() || mailTxt.isEmpty() || passwordTxt.isEmpty() || profileImage == null) {
                    Toast.makeText(RegistrationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(confirmPasswordTxt)) {
                    Toast.makeText(RegistrationActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(mailTxt, passwordTxt)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(RegistrationActivity.this, "Authentication success!",
                                                Toast.LENGTH_SHORT).show();
                                        uploadImage();
                                        User currentUser = new User(mAuth.getCurrentUser().getEmail(), firstNameTxt, surnameTxt);

                                        mDbReference.collection("users")
                                                .document(mAuth.getCurrentUser().getUid())
                                                .set(currentUser)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(RegistrationActivity.this, "inserimento nel db avvenuto correttamente!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
                data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(profileImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(mail.getText().toString() + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(RegistrationActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                    mStorageRef.getFile(new File("alexpioggia@gmail.com.jpg")).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(RegistrationActivity.this, "successo!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(RegistrationActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}