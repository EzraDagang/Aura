package com.example.aura.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aura.R;

public class VideoActivity extends AppCompatActivity {

    private int currentLessonIndex; // Class-level variable for lesson index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Retrieve course details and lesson information from Intent
        CustomModel courseDetails = (CustomModel) getIntent().getSerializableExtra("courseDetails");
        String courseTitle = (courseDetails != null) ? courseDetails.getTitle() : "Unknown Course";
        String videoURL = getIntent().getStringExtra("videoURL");
        currentLessonIndex = getIntent().getIntExtra("currentLessonIndex", 0);
        int totalLessons = getIntent().getIntExtra("totalLessons", 1); // Optional

        // Initialize UI elements
        TextView courseTitleView = findViewById(R.id.courseTitle);
        WebView videoWebView = findViewById(R.id.videoWebView);
        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton nextButton = findViewById(R.id.nextButton);
        ImageButton prevButton = findViewById(R.id.prevButton);
        TextView progressText = findViewById(R.id.progressText);

        // Set course title and progress text
        courseTitleView.setText(courseTitle);
        progressText.setText(courseTitle + "\nLesson " + (currentLessonIndex + 1) + " of " + totalLessons);

        // Configure and load video
        if (videoURL != null && !videoURL.isEmpty()) {
            videoWebView.getSettings().setJavaScriptEnabled(true);
            videoWebView.loadUrl(videoURL);
        } else {
            Toast.makeText(this, "Video URL not available", Toast.LENGTH_SHORT).show();
        }

        // Back button functionality
        backButton.setOnClickListener(v -> navigateBackToMainActivity());

        // Next button navigates to QuizActivity
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(VideoActivity.this, QuizActivity.class);
            intent.putExtra("courseDetails", courseDetails);
            intent.putExtra("currentLessonIndex", currentLessonIndex);
            intent.putExtra("quizQuestions", getIntent().getSerializableExtra("quizQuestions")); // Pass quiz data
            startActivity(intent);
        });


        // Previous button navigates back to FactsActivity
        prevButton.setOnClickListener(v -> {
            if (currentLessonIndex > 0) {
                currentLessonIndex--; // Decrement the index
                Intent intent = new Intent(VideoActivity.this, FactsActivity.class);
                intent.putExtra("courseDetails", courseDetails);
                intent.putExtra("currentLessonIndex", currentLessonIndex);
                intent.putExtra("videoURL", videoURL); // Pass the current video URL
                intent.putExtra("totalLessons", totalLessons); // Optional
                startActivity(intent);
                finish(); // End the current activity
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
        Intent intent = new Intent(VideoActivity.this, MainActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
