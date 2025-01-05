package com.example.aura.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aura.R;

import java.util.ArrayList;

public class FactsActivity extends AppCompatActivity {

    private int currentLessonIndex = 0;
    private ArrayList<String> lessonTitles;
    private ArrayList<String> lessonContents;
    private ArrayList<String> videoURLs; // Add a list for video URLs

    private ArrayList<QuizQuestion> quizQuestions; // Add a variable for quiz questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        // Retrieve lesson data from Intent
        lessonTitles = getIntent().getStringArrayListExtra("lessonTitles");
        lessonContents = getIntent().getStringArrayListExtra("lessonContents");
        videoURLs = getIntent().getStringArrayListExtra("videoURLs");
        quizQuestions = (ArrayList<QuizQuestion>) getIntent().getSerializableExtra("quizQuestions"); // Retrieve quiz questions

        if (lessonTitles == null || lessonContents == null || videoURLs == null) {
            lessonTitles = new ArrayList<>();
            lessonContents = new ArrayList<>();
            videoURLs = new ArrayList<>();
            lessonTitles.add("No Lessons Available");
            lessonContents.add("Content not available.");
            videoURLs.add("");
        }

        // Initialize UI elements
        TextView lessonTitleView = findViewById(R.id.lessonTitle);
        ImageButton backButton = findViewById(R.id.backButton);
        TextView lessonContentView = findViewById(R.id.lessonContent);
        TextView progressText = findViewById(R.id.progressText);
        ImageButton nextButton = findViewById(R.id.nextButton);

        // Set initial content
        updateLessonContent(lessonTitleView, lessonContentView, progressText);

        backButton.setOnClickListener(v -> navigateBackToMainActivity());
        // Next button functionality
        nextButton.setOnClickListener(v -> {
            if (currentLessonIndex < lessonTitles.size() - 1) {
                currentLessonIndex++;
                updateLessonContent(lessonTitleView, lessonContentView, progressText);
            } else {
                // Navigate to VideoActivity with quiz questions
                Intent intent = new Intent(FactsActivity.this, VideoActivity.class);
                intent.putStringArrayListExtra("lessonTitles", lessonTitles);
                intent.putStringArrayListExtra("videoURLs", videoURLs);
                intent.putExtra("currentLessonIndex", currentLessonIndex);
                intent.putExtra("quizQuestions", quizQuestions); // Pass quiz data
                startActivity(intent);
            }
        });
    }

    private void updateLessonContent(TextView lessonTitleView, TextView lessonContentView, TextView progressText) {
        lessonTitleView.setText(lessonTitles.get(currentLessonIndex));
        lessonContentView.setText(lessonContents.get(currentLessonIndex));
        progressText.setText("Lesson " + (currentLessonIndex + 1) + " of " + lessonTitles.size());
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

}
