package com.example.aura;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.core.view.WindowInsetsCompat;

public class education extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        Button button = findViewById(R.id.viewAll);
        ImageView hotlineButtonNew = findViewById(R.id.hotlineButton);
        ImageView selfdefenseButton = findViewById(R.id.selfdefenseButton);

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

        hotlineButtonNew.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Create intent to open hotline activity
                Intent intent = new Intent(education.this,hotline.class);
                startActivity(intent);

            }
        });
        }
    }
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_education);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
   */