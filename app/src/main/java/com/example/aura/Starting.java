package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        btnDanger.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Emergency.class);
                startActivity(i);
            }
        }));

    }
}