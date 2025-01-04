package com.example.aura.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aura.R;

import java.util.List;

public class FactsActivity extends AppCompatActivity {

    private int currentLessonIndex = 0;
    private String[] lessonTitles;
    private String[] lessonContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        // Retrieve course details and lesson data from Intent
        CustomModel courseDetails = (CustomModel) getIntent().getSerializableExtra("courseDetails");
        String courseTitle = (courseDetails != null) ? courseDetails.getTitle() : "Unknown Course";

        if (courseDetails != null) {
            List<List<String>> courseLessons = courseDetails.getLessons();
            List<List<String>> courseLessonContents = courseDetails.getLessonContents();

            if (courseLessons != null && !courseLessons.isEmpty() &&
                    courseLessonContents != null && !courseLessonContents.isEmpty()) {
                lessonTitles = courseLessons.get(0).toArray(new String[0]);
                lessonContents = courseLessonContents.get(0).toArray(new String[0]);
            } else {
                lessonTitles = new String[]{"No Lessons Available"};
                lessonContents = new String[]{"Content not available for this course."};
            }
        } else {
            lessonTitles = new String[]{"No Lessons Available"};
            lessonContents = new String[]{"Content not available for this course."};
        }

        // Retrieve additional data from Intent
        String lessonTitle = getIntent().getStringExtra("lessonTitle");
        String lessonContent = getIntent().getStringExtra("lessonContent");
        String videoURL = getIntent().getStringExtra("videoURL");

        // Initialize UI elements
        TextView courseTitleView = findViewById(R.id.courseTitle);
        TextView lessonTitleView = findViewById(R.id.lessonTitle);
        TextView lessonContentView = findViewById(R.id.lessonContent);
        TextView progressText = findViewById(R.id.progressText);
        ImageButton nextButton = findViewById(R.id.nextButton);
        ImageButton prevButton = findViewById(R.id.prevButton);
        ImageButton backButton = findViewById(R.id.backButton);

        // Set initial content
        lessonTitleView.setText(lessonTitle != null ? lessonTitle : lessonTitles[currentLessonIndex]);
        lessonContentView.setText(lessonContent != null ? lessonContent : lessonContents[currentLessonIndex]);
        courseTitleView.setText(courseTitle);
        updateLessonContent(lessonTitleView, lessonContentView, progressText, courseTitle);

        // Back button functionality
        backButton.setOnClickListener(v -> navigateBackToMainActivity());

        // Next button functionality
        nextButton.setOnClickListener(v -> {
            if (currentLessonIndex < lessonTitles.length - 1) {
                currentLessonIndex++;
                updateLessonContent(lessonTitleView, lessonContentView, progressText, courseTitle);
            } else {
                // Navigate to VideoActivity
                Intent intent = new Intent(FactsActivity.this, VideoActivity.class);
                intent.putExtra("courseDetails", courseDetails);
                intent.putExtra("videoURL", videoURL);
                intent.putExtra("lessonTitle", lessonTitle);
                startActivity(intent);
            }
        });

        // Previous button functionality
        prevButton.setOnClickListener(v -> {
            if (currentLessonIndex > 0) {
                currentLessonIndex--;
                updateLessonContent(lessonTitleView, lessonContentView, progressText, courseTitle);
            } else {
                Toast.makeText(this, "You are already at the first lesson.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        navigateBackToMainActivity();
        super.onBackPressed();
    }

    private void navigateBackToMainActivity() {
        Intent intent = new Intent(FactsActivity.this, MainActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void updateLessonContent(TextView lessonTitle, TextView lessonContent, TextView progressText, String courseTitle) {
        lessonTitle.setText(lessonTitles[currentLessonIndex]);
        lessonContent.setText(lessonContents[currentLessonIndex]);
        progressText.setText(courseTitle + "\nLesson " + (currentLessonIndex + 1) + " of " + lessonTitles.length);
    }
}


    /*
    private void updateLessonProgress(String courseId, int lessonIndex, int progress) {
        SharedPreferences sharedPreferences = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String lessonKey = courseId + "_lesson_" + lessonIndex;
        editor.putInt(lessonKey, progress);
        editor.apply();

        Toast.makeText(this, "Lesson progress updated to " + progress + "%", Toast.LENGTH_SHORT).show();
    }

     */

