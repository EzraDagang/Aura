// Updated MainActivity2.java

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity2 extends AppCompatActivity {

    private ImageView courseImageView, mentorProfile;
    private TextView courseTitleTextView, courseDetailsTextView, mentorName, mentorJob;
    private ExpandableListView lessonsExpandableListView;
    private Button enrollButton, unenrollButton;
    private boolean isCourseEnrolled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize UI elements
        initializeUI();

        // Retrieve course details passed from previous activity
        CustomModel courseDetails = (CustomModel) getIntent().getSerializableExtra("courseDetails");
        if (courseDetails == null) {
            Log.e("MainActivity2", "No course details provided");
            finish();
            return;
        }

        String courseId = courseDetails.getTitle();

        // Load SharedPreferences for course state
        SharedPreferences sharedPreferences = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        Set<String> enrolledCourses = sharedPreferences.getStringSet("enrolledCourses", new HashSet<>());

        // Determine if the course is enrolled
        isCourseEnrolled = enrolledCourses.contains(courseId);

        // Update UI based on course state
        updateEnrollmentState(isCourseEnrolled, courseDetails);

        // Populate course details
        populateCourseDetails(courseDetails);

        // Setup lessons and modules in ExpandableListView
        setupExpandableListView(courseDetails);
    }

    private void initializeUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        courseImageView = findViewById(R.id.courseImageView);
        courseTitleTextView = findViewById(R.id.courseTitle);
        courseDetailsTextView = findViewById(R.id.tvAboutDetails);
        lessonsExpandableListView = findViewById(R.id.expandableListView);
        enrollButton = findViewById(R.id.enrollButton);
        unenrollButton = new Button(this);
        unenrollButton.setText("Unenroll");
        unenrollButton.setVisibility(View.GONE);
        unenrollButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        ((ViewGroup) enrollButton.getParent()).addView(unenrollButton);

        mentorProfile = findViewById(R.id.mentorProfile);
        mentorName = findViewById(R.id.mentorName);
        mentorJob = findViewById(R.id.mentorJob);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void updateEnrollmentState(boolean isEnrolled, CustomModel courseDetails) {
        if (isEnrolled) {
            enrollButton.setText("Start Lesson");
            unenrollButton.setVisibility(View.VISIBLE);
        } else {
            enrollButton.setText("Enroll Course");
            unenrollButton.setVisibility(View.GONE);
        }

        enrollButton.setOnClickListener(v -> {
            if (!isCourseEnrolled) {
                // Enroll in the course
                Snackbar.make(v, "Enrolled in course", Snackbar.LENGTH_SHORT).show();
                enrollButton.setText("Start Lesson");
                unenrollButton.setVisibility(View.VISIBLE);

                isCourseEnrolled = true;
                updateEnrollmentStatus(courseDetails.getTitle(), true);

            } else {
                // Start lesson
                navigateToLessonActivity(courseDetails);
            }
        });

        unenrollButton.setOnClickListener(v -> showUnenrollConfirmationDialog(courseDetails));
    }

    private void showUnenrollConfirmationDialog(CustomModel courseDetails) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unenroll from Course");
        builder.setMessage("Are you sure you want to unenroll from this course?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Snackbar.make(unenrollButton, "Unenrolled from course", Snackbar.LENGTH_SHORT).show();
            enrollButton.setText("Enroll Course");
            unenrollButton.setVisibility(View.GONE);

            isCourseEnrolled = false;
            updateEnrollmentStatus(courseDetails.getTitle(), false);
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void updateEnrollmentStatus(String courseTitle, boolean isAdding) {
        SharedPreferences prefs = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> enrolledCourses = prefs.getStringSet("enrolledCourses", new HashSet<>());

        if (isAdding) {
            enrolledCourses.add(courseTitle); // Add course if enrolling
        } else {
            enrolledCourses.remove(courseTitle); // Remove course if unenrolling
        }

        editor.putStringSet("enrolledCourses", enrolledCourses); // Save updated set
        editor.apply();

        // Notify MyCoursesActivity via Broadcast
        Intent intent = new Intent("com.example.aura.UPDATE_MY_COURSES");
        intent.putExtra("courseTitle", courseTitle);
        intent.putExtra("isAdding", isAdding);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void populateCourseDetails(CustomModel courseDetails) {
        courseImageView.setImageResource(courseDetails.getImage());
        courseTitleTextView.setText(courseDetails.getTitle());
        courseDetailsTextView.setText(courseDetails.getAbout());
        mentorProfile.setImageResource(courseDetails.getMentorProfileImage());
        mentorName.setText(courseDetails.getMentorName());
        mentorJob.setText(courseDetails.getMentorJob());

        mentorProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MessageMentorActivity.class);
            intent.putExtra("mentorName", courseDetails.getMentorName());
            startActivity(intent);
        });
    }

    private void setupExpandableListView(CustomModel courseDetails) {
        CourseDetailsExpandableListAdapter adapter = new CourseDetailsExpandableListAdapter(
                this,
                courseDetails.getModules(),
                courseDetails.getLessons()
        );
        lessonsExpandableListView.setAdapter(adapter);

        lessonsExpandableListView.setOnChildClickListener((parent, view, groupPosition, childPosition, id) -> {
            String selectedLessonTitle = courseDetails.getLessons().get(groupPosition).get(childPosition);
            String selectedLessonContent = courseDetails.getLessonContents().get(groupPosition).get(childPosition);
            navigateToLessonActivity(courseDetails, selectedLessonTitle, selectedLessonContent, courseDetails.getQuizQuestions());
            return true;
        });
    }

    private void navigateToLessonActivity(CustomModel courseDetails) {
        Intent intent = new Intent(MainActivity2.this, FactsActivity.class);
        intent.putExtra("courseDetails", courseDetails);
        startActivity(intent);
    }

    private void navigateToLessonActivity(CustomModel courseDetails, String lessonTitle, String lessonContent, List<QuizQuestion> quizQuestions) {
        Intent intent = new Intent(MainActivity2.this, FactsActivity.class);
        intent.putExtra("courseDetails", courseDetails);
        intent.putExtra("lessonTitle", lessonTitle);
        intent.putExtra("lessonContent", lessonContent);
        intent.putExtra("quizQuestions", new ArrayList<>(quizQuestions));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
