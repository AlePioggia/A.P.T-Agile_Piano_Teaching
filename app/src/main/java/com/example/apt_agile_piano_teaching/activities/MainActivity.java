package com.example.apt_agile_piano_teaching.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.apt_agile_piano_teaching.R;
import com.example.apt_agile_piano_teaching.databinding.ActivityMainBinding;
import com.example.apt_agile_piano_teaching.fragments.AddFragment;
import com.example.apt_agile_piano_teaching.fragments.HomeFragment;
import com.example.apt_agile_piano_teaching.fragments.MenuFragment;
import com.example.apt_agile_piano_teaching.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());
        this.replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.home:
                    replaceFragment(new HomeFragment());
                    return true;
                case R.id.add:
                    replaceFragment(new AddFragment());
                    return true;
                case R.id.menu:
                    replaceFragment(new MenuFragment());
                    return true;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    return true;
            }

            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_frame_layout, fragment);
        fragmentTransaction.commit();
    }

}