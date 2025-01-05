package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.aura.Courses.DiscoverScreen;
import com.example.aura.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class education extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        Button button = findViewById(R.id.viewAll);
        Button readMore1 = findViewById(R.id.readMore1);
        Button readMore2 = findViewById(R.id.readMore2);
        ImageView hotlineButtonNew = findViewById(R.id.hotlineButton);
        ImageView selfdefenseButton = findViewById(R.id.selfdefenseButton);

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_notifications);

        if (bottomNavigationView == null) {
            Log.e("DiscoverScreen", "BottomNavigationView is null");
        } else {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(education.this, DiscoverScreen.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_phone) {
                    // Navigate to Emergency Activity
                    Log.d("DiscoverScreen", "Navigating to Emergency");
                    Intent intent = new Intent(education.this, Emergency.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_notifications) {

                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Handle profile navigation (optional)
                    Intent intent = new Intent(education.this, SettingsActivity.class);
                    startActivity(intent);

                    return true;
                }
                return false;
            });
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(education.this, lawfull.class);
                startActivity(intent);
            }});

        selfdefenseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create intent to open self defense activity
                Intent intent = new Intent(education.this, selfdefense.class);
                startActivity(intent);
        }
        });

        readMore1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(education.this, lawdetails1.class);
                startActivity(intent);
            }});

        readMore2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(education.this, lawdetails2.class);
                startActivity(intent);
            }});



        hotlineButtonNew.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("Education", "Hotline button clicked");
                // Create intent to open hotline activity
                Intent intent = new Intent(education.this, hotline.class);
                startActivity(intent);
            }
        });
        }
    }
