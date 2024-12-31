package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
                // Create intent to open hotline activity
                Intent intent = new Intent(education.this,hotline.class);
                startActivity(intent);

            }
        });
        }
    }