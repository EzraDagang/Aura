package com.example.aura.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.aura.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set custom toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // References to CardViews
        CardView careerCard = findViewById(R.id.careerCard);
        CardView selfDevelopmentCard = findViewById(R.id.selfDevelopmentCard);
        CardView personalGrowthCard = findViewById(R.id.personalGrowthCard);
        CardView healthCard = findViewById(R.id.healthCard);
        CardView advocacyCard = findViewById(R.id.advocacyCard);
        CardView relationshipCard = findViewById(R.id.relationshipCard);

        // Set click listeners for each category
        setupCardClickListener(careerCard, "Career Advancement");
        setupCardClickListener(selfDevelopmentCard, "Self-Development");
        setupCardClickListener(personalGrowthCard, "Personal Growth");
        setupCardClickListener(healthCard, "Health and Wellness");
        setupCardClickListener(advocacyCard, "Advocacy and Strength");
        setupCardClickListener(relationshipCard, "Relationship and Support");
    }

    private void setupCardClickListener(CardView cardView, String categoryTitle) {
        cardView.setOnClickListener(v -> {
            firestore.collection("categories")
                    .whereEqualTo("categoryTitle", categoryTitle)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String documentId = document.getId();
                            openCourseListActivity(categoryTitle, documentId);
                        } else {
                            Toast.makeText(this, "No category found for: " + categoryTitle, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error loading category: " + categoryTitle, Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void openCourseListActivity(String categoryTitle, String documentId) {
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra("categoryTitle", categoryTitle);
        intent.putExtra("documentId", documentId);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
