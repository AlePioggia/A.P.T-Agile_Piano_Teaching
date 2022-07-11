package com.example.apt_agile_piano_teaching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText phone = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registrationBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your mobile or password", Toast.LENGTH_SHORT).show();
                }

            }
        });


        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // open registration activity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }
}