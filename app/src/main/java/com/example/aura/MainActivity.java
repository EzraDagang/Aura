package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        RecyclerView recyclerView;
        ArrayList<CustomModel> customModelArrayList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set custom toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get reference to 'CardView' in main.xml
        CardView careerCard = findViewById(R.id.careerCard);
        CardView selfDevelopmentCard = findViewById(R.id.selfDevelopmentCard);
        CardView personalGrowthCard = findViewById(R.id.personalGrowthCard);
        CardView healthCard = findViewById(R.id.healthCard);
        CardView advocacyCard = findViewById(R.id.advocacyCard);
        CardView relationshipCard = findViewById(R.id.relationshipCard);

        careerCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openCourseList("Career Advancement");
            }
        });

        careerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.navigateToCourseList(MainActivity.this, "Career Advancement");
            }
        });

        selfDevelopmentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseList("Self-Development");
            }
        });

        personalGrowthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseList("Personal Growth");
            }
        });

        healthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseList("Health and Wellness");
            }
        });

        advocacyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseList("Advocacy and Strength");
            }
        });

        relationshipCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseList("Relationship and Support");
            }
        });
    }

    private void openCourseList(String category) {
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Navigate back to DiscoverScreen
            Intent intent = new Intent(MainActivity.this, DiscoverScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Prevent navigating back to MainActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}