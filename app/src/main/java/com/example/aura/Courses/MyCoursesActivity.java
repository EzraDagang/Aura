package com.example.aura.Courses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aura.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyCoursesActivity extends AppCompatActivity {

    private static final String TAG = "MyCoursesActivity";

    private RecyclerView coursesRecyclerView;
    private CustomAdapter coursesAdapter;
    private ArrayList<CustomModel> enrolledCourses;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);
        enrolledCourses = new ArrayList<>();
        coursesAdapter = new CustomAdapter(this, enrolledCourses, "desiredCategory");
        coursesRecyclerView.setAdapter(coursesAdapter);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("CoursePrefs", MODE_PRIVATE);

        fetchEnrolledCourses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("com.example.aura.UPDATE_MY_COURSES");
        LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver);
    }

    private final BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String courseId = intent.getStringExtra("courseId");
            boolean isAdding = intent.getBooleanExtra("isAdding", false);
            if (isAdding) {
                fetchEnrolledCourses();
            } else {
                removeCourseFromList(courseId);
            }
        }
    };

    private void removeCourseFromList(String courseId) {
        for (int i = 0; i < enrolledCourses.size(); i++) {
            if (enrolledCourses.get(i).getCourseId().equals(courseId)) {
                enrolledCourses.remove(i);
                coursesAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    private void fetchEnrolledCourses() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d(TAG, "Fetching enrolled courses for userId: " + userId);

            firebaseFirestore.collection("users").document(userId)
                    .collection("enrollCourses")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            enrolledCourses.clear();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                fetchCourseDetails(document);
                            }
                        } else {
                            Toast.makeText(MyCoursesActivity.this, "No enrolled courses found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error fetching enrolled courses: ", e);
                        Toast.makeText(MyCoursesActivity.this, "Failed to fetch courses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchCourseDetails(DocumentSnapshot document) {
        String courseId = document.getId();
        String courseTitle = document.getString("courseTitle");
        String mentorName = document.getString("mentorName");
        Long numLessons = document.getLong("numLessons");

        if (courseTitle != null && mentorName != null && numLessons != null) {
            enrolledCourses.add(new CustomModel(R.drawable.confident, courseTitle, numLessons + " Lessons", "4.5 / 5.0", courseId));
            coursesAdapter.notifyDataSetChanged();
        } else {
            Log.w(TAG, "Incomplete course data for ID: " + courseId);
        }
    }

    private void onCourseSelected(String courseId) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Intent intent = new Intent(this, MainActivity3.class);
            intent.putExtra("courseID", courseId); // Pass courseID
            intent.putExtra("userID", userId); // Pass userID
            startActivity(intent);
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

}
