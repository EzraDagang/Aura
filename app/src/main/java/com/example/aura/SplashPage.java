package com.example.aura;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aura.LoginAndSignUp.LoginAndSignUpActivity;
import com.example.aura.onBoarding.onboardingActivity;

public class SplashPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check SharedPreferences before navigating
                SharedPreferences sharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE);
                boolean isOnboardingShown = sharedPreferences.getBoolean("onboardingShown", false);

                Intent intent;
                if (isOnboardingShown) {
                    // If onboarding has been shown already, go to the MainActivity/HomeActivity
                    intent = new Intent(SplashPage.this, LoginAndSignUpActivity.class); // Use your main activity here
                } else {
                    // If onboarding hasn't been shown yet, go to the OnboardingActivity
                    intent = new Intent(SplashPage.this, onboardingActivity.class);
                }

                startActivity(intent);   // Start the appropriate activity
                finish();                // Close SplashActivity
            }
        }, 1500); // 1.5-second delay
    }
}