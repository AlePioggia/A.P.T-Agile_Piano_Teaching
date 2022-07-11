package com.example.apt_agile_piano_teaching;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    //Inserimento dei dati nel db con firebase

    DatabaseReference dbReference  = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText firstName = findViewById(R.id.name);
        final EditText surname = findViewById(R.id.surname);
        final EditText mail = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText confirmPassword = findViewById(R.id.confirmPassword);

        final Button registerBtn = findViewById(R.id.registerBtn);
        final TextView loginNowBtn = findViewById(R.id.logInNowBtn);

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
                if (firstNameTxt.isEmpty() || surnameTxt.isEmpty() || mailTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordTxt.equals(confirmPasswordTxt)) {
                    Toast.makeText(RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                } else {
                    dbReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if the mail isn't already registered
                            if (snapshot.hasChild(mailTxt)) {
                                Toast.makeText(RegisterActivity.this, "Mail is already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                // using mail as unique identifier
                                dbReference.child("users").child(mailTxt).child("firstName").setValue(firstNameTxt);
                                dbReference.child("users").child(mailTxt).child("surname").setValue(surnameTxt);
                                dbReference.child("users").child(mailTxt).child("password").setValue(passwordTxt);

                                //show a success message then finish the activity
                                Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}