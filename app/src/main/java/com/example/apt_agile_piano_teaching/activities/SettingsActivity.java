package com.example.apt_agile_piano_teaching.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.example.apt_agile_piano_teaching.databinding.ActivitySettingsBinding;
import com.example.apt_agile_piano_teaching.utils.Preference;
import com.example.apt_agile_piano_teaching.utils.Settings;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Settings emailSettings = new Settings(SettingsActivity.this, Preference.EMAIL_PREFERENCE);
        Settings imageTemplateSettings = new Settings(SettingsActivity.this, Preference.IMAGE_TEMPLATE_PREFERENCE);
        binding.emailSettingsSwitch.setChecked(emailSettings.getPreference());
        binding.imageTemplateSettingsSwitch.setChecked(imageTemplateSettings.getPreference());

        binding.emailSettingsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    emailSettings.setTrue();
                } else {
                    emailSettings.setFalse();
                }
            }
        });

        binding.imageTemplateSettingsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    imageTemplateSettings.setTrue();
                } else {
                    imageTemplateSettings.setFalse();
                }
            }
        });
    }

}