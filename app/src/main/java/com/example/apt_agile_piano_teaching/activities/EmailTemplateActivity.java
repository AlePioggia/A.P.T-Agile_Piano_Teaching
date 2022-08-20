package com.example.apt_agile_piano_teaching.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apt_agile_piano_teaching.databinding.ActivityEmailTemplateBinding;
import com.example.apt_agile_piano_teaching.models.EmailTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmailTemplateActivity extends AppCompatActivity {

    private ActivityEmailTemplateBinding binding;
    private FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailTemplateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveTemplateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.mailSubject.getText() != null
                && binding.mailText.getText() != null) {
                    setupMail();
                } else {
                    Toast.makeText(EmailTemplateActivity.this, "Compilare tutti i campi!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupMail() {
        EmailTemplate emailTemplate = new EmailTemplate(binding.mailSubject.getText().toString(), binding.mailText.getText().toString());

        mDbReference.collection("emailTemplates")
                .document(mAuth.getCurrentUser().getUid())
                .set(emailTemplate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EmailTemplateActivity.this, "Salvataggio del template avvenuto con successo!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}