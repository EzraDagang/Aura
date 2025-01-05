package com.example.aura.Settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aura.Courses.DiscoverScreen;
import com.example.aura.Emergency;
import com.example.aura.R;
import com.example.aura.education;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Navigate to DiscoverScreen or Home
                Intent intent = new Intent(SettingsActivity.this, DiscoverScreen.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_phone) {

                Intent intent = new Intent(SettingsActivity.this, Emergency.class);
                startActivity(intent);

                return true;
            } else if (itemId == R.id.nav_notifications) {
                // Handle notifications navigation
                Intent intent = new Intent(SettingsActivity.this, education.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Handle profile navigation
                return true;
            }
            return false;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.pink)); // Change to pink
            window.setNavigationBarColor(getResources().getColor(R.color.black)); // Change to pink
        }
    }
}