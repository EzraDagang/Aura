package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.aura.Courses.DiscoverScreen;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Starting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        // Find the Danger button by its ID
        final Button btnDanger = findViewById(R.id.BtnDanger);

        // Set an OnClickListener for the Danger button
        btnDanger.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the Emergency activity
                Intent i = new Intent(getApplicationContext(), Emergency.class);
                startActivity(i);
            }
        });

        // Find the Start button by its ID
        final Button btnStart = findViewById(R.id.BtnStart);

        // Set an OnClickListener for the Start button
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the NextPageActivity
                Intent i = new Intent(getApplicationContext(), DiscoverScreen.class);
                startActivity(i);
            }
        });
    }
}


