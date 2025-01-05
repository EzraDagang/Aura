package com.example.aura.Courses;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aura.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private ArrayList<CustomModel> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> {
            finish(); // Close this activity and go back to the previous screen
        });

        // Get the category passed from MainActivity
        String categoryDocumentId = getIntent().getStringExtra("documentId"); // Fetch by document ID
        String categoryTitle = getIntent().getStringExtra("categoryTitle");
        setTitle(categoryTitle);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize course list and adapter
        courseList = new ArrayList<>();
        adapter = new CustomAdapter(this, courseList, categoryDocumentId); // Pass categoryDocumentId to the adapter
        recyclerView.setAdapter(adapter);

        // Fetch courses for the selected category
        fetchCoursesForCategory(categoryDocumentId);
    }

    private void fetchCoursesForCategory(String categoryDocumentId) {
        firestore.collection("categories")
                .document(categoryDocumentId)
                .collection("courses")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot) {
                        String courseTitle = document.getString("courseTitle") != null ? document.getString("courseTitle") : "No Title";
                        int numLessons = document.contains("numLessons") ? document.getLong("numLessons").intValue() : 0;
                        double rating = document.contains("rating") ? document.getDouble("rating") : 0.0;
                        String courseId = document.getId(); // Fetch the document ID

                        courseList.add(new CustomModel(
                                R.drawable.confident, // Replace with an appropriate image resource
                                courseTitle,
                                numLessons + " Lessons",
                                String.format("%.1f / 5.0", rating), // Format rating
                                courseId // Add courseId to the model
                        ));
                    }
                    adapter.notifyDataSetChanged(); // Notify adapter about the new data
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CourseListActivity.this, "Failed to load courses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button press
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
