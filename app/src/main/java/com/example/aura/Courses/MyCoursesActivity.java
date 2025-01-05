package com.example.aura.Courses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.aura.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyCoursesActivity extends AppCompatActivity {

    private ListView coursesListView;
    private CustomCourseAdapter coursesAdapter;
    private ArrayList<CourseModel> enrolledCourses;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        coursesListView = findViewById(R.id.coursesListView);
        enrolledCourses = new ArrayList<>();
        coursesAdapter = new CustomCourseAdapter(this, enrolledCourses);
        coursesListView.setAdapter(coursesAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Fetch the logged-in user's courses
        fetchEnrolledCourses();

        // Register local broadcast receiver for updates
        LocalBroadcastManager.getInstance(this).registerReceiver(courseUpdateReceiver,
                new IntentFilter("com.example.aura.UPDATE_MY_COURSES"));
    }

    private void fetchEnrolledCourses() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Fetch user document from Firestore
            firebaseFirestore.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            ArrayList<String> courseIDs = (ArrayList<String>) documentSnapshot.get("enrollCourses");

                            if (courseIDs != null && !courseIDs.isEmpty()) {
                                for (String courseId : courseIDs) {
                                    fetchCourseDetails(courseId);
                                }
                            } else {
                                Toast.makeText(MyCoursesActivity.this, "No enrolled courses found", Toast.LENGTH_SHORT).show();
                                findViewById(R.id.emptyTextView).setVisibility(View.VISIBLE);
                                coursesListView.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(MyCoursesActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("MyCoursesActivity", "Error fetching user data: ", e);
                        Toast.makeText(MyCoursesActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCourseDetails(String courseId) {
        firebaseFirestore.collection("courses").document(courseId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String courseTitle = documentSnapshot.getString("courseTitle");
                        String mentorName = documentSnapshot.getString("mentorName");
                        Long numLessons = documentSnapshot.getLong("numLessons");

                        enrolledCourses.add(new CourseModel(courseId, courseTitle, mentorName, numLessons));
                        coursesAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("MyCoursesActivity", "Course data not found for ID: " + courseId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MyCoursesActivity", "Error fetching course data: ", e);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(courseUpdateReceiver);
    }

    private final BroadcastReceiver courseUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String courseId = intent.getStringExtra("courseId");
            boolean isAdding = intent.getBooleanExtra("isAdding", false);

            if (isAdding) {
                fetchCourseDetails(courseId);
            } else {
                enrolledCourses.removeIf(course -> course.getCourseId().equals(courseId));
                coursesAdapter.notifyDataSetChanged();
            }
        }
    };
}
