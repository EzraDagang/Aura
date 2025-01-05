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
        //Yi Tong commented on Starting.java
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        final Button btnDanger = findViewById(R.id.BtnDanger);
        final Button btnStart = findViewById(R.id.BtnStart);

        btnDanger.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Emergency.class);
                startActivity(i);
            }
        }));


        btnStart.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to DiscoverScreen
                Intent i = new Intent(getApplicationContext(), DiscoverScreen.class);
                startActivity(i);
            }
        }));
    }

}