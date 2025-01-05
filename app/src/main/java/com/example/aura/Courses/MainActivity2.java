package com.example.aura.Courses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.aura.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = "MainActivity2";

    private ImageView courseImageView, mentorProfile;
    private TextView courseTitleTextView, courseDetailsTextView, mentorName, mentorJob;
    private ExpandableListView lessonsExpandableListView;
    private Button enrollButton, unenrollButton;
    private boolean isCourseStarted = false;

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

        // Retrieve courseId and category from the previous activity
        String courseId = getIntent().getStringExtra("courseID");
        String category = getIntent().getStringExtra("category");

        if (courseId == null || category == null) {
            Log.e(TAG, "Course ID or category is missing");
            finish();
            return;
        }

        // Load SharedPreferences for course state
        SharedPreferences sharedPreferences = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        isCourseStarted = sharedPreferences.getBoolean("isCourseStarted_" + courseId, false);

        // Update UI based on course state
        updateEnrollmentState(isCourseStarted, courseId, category, editor);

        // Fetch course details from Firestore
        fetchCourseDetails(courseId, category);
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

    private void updateEnrollmentState(boolean isCourseStarted, String courseId, String category, SharedPreferences.Editor editor) {
        enrollButton.setText(isCourseStarted ? "Start Lesson" : "Enroll Course");
        unenrollButton.setVisibility(isCourseStarted ? View.VISIBLE : View.GONE);

        enrollButton.setOnClickListener(v -> {
            if (!this.isCourseStarted) {
                // Enroll in the course
                Snackbar.make(v, "Enrolled in course", Snackbar.LENGTH_SHORT).show();
                enrollButton.setText("Start Lesson");
                unenrollButton.setVisibility(View.VISIBLE);

                editor.putBoolean("isCourseStarted_" + courseId, true);
                editor.apply();
                this.isCourseStarted = true;

                // Save the course to user enrollments
                saveCourseToUserEnrollments(courseId, category);

                // Notify MyCoursesActivity
                updateMyCoursesPage(courseId, true);
            } else {
                // Start lesson
                navigateToLessonActivity(courseId);
            }
        });

        unenrollButton.setOnClickListener(v -> showUnenrollConfirmationDialog(editor, courseId));
    }

    private void saveCourseToUserEnrollments(String courseId, String category) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userId != null) {
            db.collection("categories").document(category)
                    .collection("courses").document(courseId)
                    .get()
                    .addOnSuccessListener(courseSnapshot -> {
                        if (courseSnapshot.exists()) {
                            Map<String, Object> courseData = new HashMap<>(courseSnapshot.getData());

                            db.collection("categories").document(category)
                                    .collection("courses").document(courseId)
                                    .collection("modules")
                                    .get()
                                    .addOnSuccessListener(modulesSnapshot -> {
                                        List<Map<String, Object>> modulesList = new ArrayList<>();

                                        for (QueryDocumentSnapshot moduleDoc : modulesSnapshot) {
                                            Map<String, Object> moduleData = new HashMap<>(moduleDoc.getData());
                                            String moduleId = moduleDoc.getId();

                                            moduleDoc.getReference().collection("lessons")
                                                    .get()
                                                    .addOnSuccessListener(lessonsSnapshot -> {
                                                        List<Map<String, Object>> lessonsList = new ArrayList<>();
                                                        for (QueryDocumentSnapshot lessonDoc : lessonsSnapshot) {
                                                            lessonsList.add(lessonDoc.getData());
                                                        }

                                                        moduleData.put("lessons", lessonsList);
                                                        modulesList.add(moduleData);

                                                        if (modulesList.size() == modulesSnapshot.size()) {
                                                            courseData.put("modules", modulesList);

                                                            db.collection("users").document(userId)
                                                                    .collection("enrollCourses").document(courseId)
                                                                    .set(courseData)
                                                                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Course added to enrollments successfully"))
                                                                    .addOnFailureListener(e -> Log.e(TAG, "Error adding course to enrollments", e));
                                                        }
                                                    })
                                                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching lessons", e));
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching modules", e));
                        } else {
                            Log.e(TAG, "Course data not found");
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching course data", e));
        } else {
            Log.e(TAG, "User ID is null. Ensure the user is logged in.");
        }
    }

    private void fetchCourseDetails(String courseId, String category) {
        db.collection("categories").document(category)
                .collection("courses").document(courseId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        courseTitleTextView.setText(documentSnapshot.getString("courseTitle"));
                        courseDetailsTextView.setText(documentSnapshot.getString("description"));
                        mentorName.setText(documentSnapshot.getString("mentorName"));
                        mentorJob.setText(documentSnapshot.getString("mentorJob"));

                        courseImageView.setImageResource(R.drawable.resume);
                        mentorProfile.setImageResource(R.drawable.profile_mentor_girl);

                        fetchModulesAndLessons(courseId, category);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching course details", e));
    }

    private void fetchModulesAndLessons(String courseId, String category) {
        db.collection("categories").document(category)
                .collection("courses").document(courseId)
                .collection("modules")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot moduleDoc : querySnapshot) {
                        String moduleTitle = moduleDoc.getString("moduleTitle");
                        moduleTitles.add(moduleTitle);

                        moduleDoc.getReference().collection("lessons")
                                .get()
                                .addOnSuccessListener(lessonSnapshot -> {
                                    List<String> lessons = new ArrayList<>();
                                    for (QueryDocumentSnapshot lessonDoc : lessonSnapshot) {
                                        lessons.add(lessonDoc.getString("lessonTitle"));
                                    }
                                    lessonsMap.put(moduleTitle, lessons);
                                    populateExpandableListView();
                                })
                                .addOnFailureListener(e -> Log.e(TAG, "Error fetching lessons", e));
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching modules", e));
    }

    private void populateExpandableListView() {
        CourseDetailsExpandableListAdapter adapter = new CourseDetailsExpandableListAdapter(
                this,
                moduleTitles,
                lessonsMap
        );
        lessonsExpandableListView.setAdapter(adapter);
    }

    private void navigateToLessonActivity(String courseId) {
        Intent intent = new Intent(MainActivity2.this, FactsActivity.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }

    private void removeCourseFromUserEnrollments(String courseId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userId != null) {
            db.collection("users").document(userId)
                    .collection("enrollCourses").document(courseId)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Course removed from enrollments successfully"))
                    .addOnFailureListener(e -> Log.e(TAG, "Error removing course from enrollments", e));
        } else {
            Log.e(TAG, "User ID is null. Ensure the user is logged in.");
        }
    }

    private void showUnenrollConfirmationDialog(SharedPreferences.Editor editor, String courseId) {
        new AlertDialog.Builder(this)
                .setTitle("Unenroll from Course")
                .setMessage("Are you sure you want to unenroll from this course?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Snackbar.make(unenrollButton, "Unenrolled from course", Snackbar.LENGTH_SHORT).show();
                    enrollButton.setText("Enroll Course");
                    unenrollButton.setVisibility(View.GONE);

                    editor.putBoolean("isCourseStarted_" + courseId, false);
                    editor.apply();

                    removeCourseFromUserEnrollments(courseId);

                    updateMyCoursesPage(courseId, false);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void updateMyCoursesPage(String courseId, boolean isAdding) {
        Intent intent = new Intent("com.example.aura.UPDATE_MY_COURSES");
        intent.putExtra("courseId", courseId);
        intent.putExtra("isAdding", isAdding);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
