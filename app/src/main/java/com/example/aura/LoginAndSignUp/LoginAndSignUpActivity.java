package com.example.aura.LoginAndSignUp;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aura.R;

public class LoginAndSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge functionality
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_login_and_sign_up);

        // Handle system bars (status bar and navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Change the status bar and navigation bar colors
        setStatusBarAndNavigationBarColors();
    }

    /**
     * Changes the status bar and navigation bar colors to pink.
     */
    private void setStatusBarAndNavigationBarColors() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            // Make the status bar and navigation bar fully transparent
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );

            // Change the status bar and navigation bar colors to pink
            window.setStatusBarColor(getResources().getColor(R.color.pink)); // Set status bar to pink
            window.setNavigationBarColor(getResources().getColor(R.color.pink)); // Set navigation bar to pink
        }
    }
}