package com.example.aura.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.aura.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "MainActivity3";

    private ImageView courseImageView, mentorProfile;
    private TextView courseTitleTextView, courseDetailsTextView, mentorName, mentorJob;
    private ExpandableListView lessonsExpandableListView;
    private Button enrollButton, unenrollButton;

    private FirebaseFirestore db;
    private List<String> moduleTitles = new ArrayList<>();
    private HashMap<String, List<String>> lessonsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        initializeUI();

        // Retrieve courseId and userId from the previous activity
        String courseId = getIntent().getStringExtra("courseID");
        String userId = getIntent().getStringExtra("userID");

        if (courseId == null || userId == null) {
            Log.e(TAG, "Course ID or User ID is missing");
            finish();
            return;
        }

        // Fetch course details using the courseId and userId passed from MyCoursesActivity
        fetchCourseDetails(userId, courseId);
    }

    private void initializeUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        courseImageView = findViewById(R.id.courseImageView);
        courseTitleTextView = findViewById(R.id.courseTitle);
        courseDetailsTextView = findViewById(R.id.tvAboutDetails);
        lessonsExpandableListView = findViewById(R.id.expandableListView);
        enrollButton = findViewById(R.id.enrollButton);
        mentorProfile = findViewById(R.id.mentorProfile);
        mentorName = findViewById(R.id.mentorName);
        mentorJob = findViewById(R.id.mentorJob);

        unenrollButton = new Button(this);
        unenrollButton.setText("Unenroll");
        unenrollButton.setVisibility(View.GONE);
        unenrollButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        ((ViewGroup) enrollButton.getParent()).addView(unenrollButton);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void fetchCourseDetails(String userId, String courseId) {
        db.collection("users").document(userId)
                .collection("enrollCourses").document(courseId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String categoryId = documentSnapshot.getString("categoryId");
                        String courseIdFromCategory = documentSnapshot.getId();

                        if (categoryId != null) {
                            fetchCourseFromCategories(categoryId, courseIdFromCategory);
                        } else {
                            Log.e(TAG, "Category ID is missing in enrollCourses document");
                        }
                    } else {
                        Log.e(TAG, "Course document does not exist in enrollCourses");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching enrollCourses document: " + e.getMessage()));
    }

    private void fetchCourseFromCategories(String categoryId, String courseId) {
        db.collection("categories").document(categoryId)
                .collection("courses").document(courseId)
                .get()
                .addOnSuccessListener(courseSnapshot -> {
                    if (courseSnapshot.exists()) {
                        String courseTitle = courseSnapshot.getString("courseTitle");
                        String description = courseSnapshot.getString("description");
                        String mentorJobStr = courseSnapshot.getString("mentorJob");
                        String mentorNameStr = courseSnapshot.getString("mentorName");

                        // Update UI with course details
                        courseTitleTextView.setText(courseTitle);
                        courseDetailsTextView.setText(description);
                        mentorName.setText(mentorNameStr);
                        mentorJob.setText(mentorJobStr);

                        // Placeholder for images
                        courseImageView.setImageResource(R.drawable.resume);
                        mentorProfile.setImageResource(R.drawable.profile_mentor_girl);

                        fetchModules((QueryDocumentSnapshot) courseSnapshot);
                    } else {
                        Log.e(TAG, "Course document does not exist in categories");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching course details from categories: " + e.getMessage()));
    }

    private void fetchModules(QueryDocumentSnapshot courseSnapshot) {
        db.collection("categories").document(courseSnapshot.getString("categoryId"))
                .collection("courses").document(courseSnapshot.getId())
                .collection("modules")
                .get()
                .addOnSuccessListener(moduleSnapshots -> {
                    for (QueryDocumentSnapshot moduleDoc : moduleSnapshots) {
                        String moduleTitle = moduleDoc.getString("moduleTitle");
                        moduleTitles.add(moduleTitle);

                        List<String> lessons = new ArrayList<>();
                        fetchLessons(moduleDoc, lessons);

                        lessonsMap.put(moduleTitle, lessons);
                    }
                    populateExpandableListView();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching modules: " + e.getMessage()));
    }

    private void fetchLessons(QueryDocumentSnapshot moduleDoc, List<String> lessons) {
        moduleDoc.getReference().collection("lessons").get()
                .addOnSuccessListener(lessonSnapshots -> {
                    for (QueryDocumentSnapshot lessonDoc : lessonSnapshots) {
                        String lessonTitle = lessonDoc.getString("lessonTitle");
                        String content = lessonDoc.getString("content");
                        String videoURL = lessonDoc.getString("videoURL");

                        String lessonDetails = lessonTitle + ": " + content + " (Video: " + videoURL + ")";
                        lessons.add(lessonDetails);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching lessons: " + e.getMessage()));
    }

    private void populateExpandableListView() {
        CourseDetailsExpandableListAdapter adapter = new CourseDetailsExpandableListAdapter(
                this,
                moduleTitles,
                lessonsMap
        );
        lessonsExpandableListView.setAdapter(adapter);
    }
}
