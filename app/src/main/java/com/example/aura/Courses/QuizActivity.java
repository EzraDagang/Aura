package com.example.aura.Courses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aura.R;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private int currentQuestionIndex = 0; // Class-level variable for question index
    private List<QuizQuestion> quizQuestions;
    private String courseTitleText;
    private ImageButton nextButton;
    private int[] selectedAnswers; // Array to store selected answers for each question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Retrieve quiz questions and course details from Intent
        quizQuestions = (ArrayList<QuizQuestion>) getIntent().getSerializableExtra("quizQuestions");
        CustomModel courseDetails = (CustomModel) getIntent().getSerializableExtra("courseDetails");
        int currentLessonIndex = getIntent().getIntExtra("currentLessonIndex", 0);

        if (courseDetails != null) {
            quizQuestions = courseDetails.getQuizQuestions();
            courseTitleText = courseDetails.getTitle();
        }

        // Initialize the array to store answers
        selectedAnswers = new int[quizQuestions.size()];
        for (int i = 0; i < selectedAnswers.length; i++) {
            selectedAnswers[i] = -1; // Default value indicating no selection
        }

        // Initialize views
        TextView courseTitle = findViewById(R.id.courseTitle);
        TextView quizQuestionText = findViewById(R.id.quizQuestion);
        RadioGroup quizOptions = findViewById(R.id.quizOptions);
        Button submitButton = findViewById(R.id.submitButton);
        nextButton = findViewById(R.id.nextButton);
        ImageButton prevButton = findViewById(R.id.prevButton);
        ImageButton backButton = findViewById(R.id.backButton);
        TextView progressText = findViewById(R.id.progressText);

        // Set course title dynamically
        courseTitle.setText(courseTitleText != null ? courseTitleText : "Quiz");

        // Load the first question
        if (quizQuestions != null && !quizQuestions.isEmpty()) {
            loadQuizQuestion(quizQuestions.get(currentQuestionIndex), quizQuestionText, quizOptions);
        } else {
            Toast.makeText(this, "No quiz questions available", Toast.LENGTH_SHORT).show();
            finish(); // Exit activity if no questions are available
        }

        // Back button functionality
        backButton.setOnClickListener(v -> navigateBackToMainActivity(currentLessonIndex, courseDetails));

        // Submit button functionality
        submitButton.setOnClickListener(v -> handleSubmission(quizOptions));

        // Next button functionality
        nextButton.setOnClickListener(v -> handleNextQuestion(quizOptions, quizQuestionText, quizOptions, progressText));

        // Previous button functionality
        prevButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                loadQuizQuestion(quizQuestions.get(currentQuestionIndex), quizQuestionText, quizOptions);
                updateProgressText(progressText);
            } else {
                Toast.makeText(this, "You are already at the first question.", Toast.LENGTH_SHORT).show();
            }
        });

        updateProgressText(progressText); // Set initial progress
    }

    private void loadQuizQuestion(QuizQuestion quizQuestion, TextView quizQuestionText, RadioGroup quizOptions) {
        // Set the question text
        quizQuestionText.setText(quizQuestion.getQuestion());

        // Clear previous options
        quizOptions.removeAllViews();

        // Populate new options
        for (String option : quizQuestion.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setTextColor(Color.WHITE);
            quizOptions.addView(radioButton);
        }

        // Restore the user's previous selection
        if (selectedAnswers[currentQuestionIndex] != -1) {
            ((RadioButton) quizOptions.getChildAt(selectedAnswers[currentQuestionIndex])).setChecked(true);
        } else {
            quizOptions.clearCheck(); // Clear selection if no answer was selected before
        }

        // Reset the Next button for all questions except the last
        if (currentQuestionIndex < quizQuestions.size() - 1) {
            nextButton.setImageResource(R.drawable.baseline_keyboard_arrow_right_24); // Replace with your next icon resource
        }
    }

    private void handleSubmission(RadioGroup quizOptions) {
        int selectedOptionId = quizOptions.getCheckedRadioButtonId();
        RadioButton selectedOption = findViewById(selectedOptionId);

        if (selectedOption == null) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the user's selected answer
        selectedAnswers[currentQuestionIndex] = quizOptions.indexOfChild(selectedOption);

        String selectedAnswer = selectedOption.getText().toString();
        String correctAnswer = quizQuestions.get(currentQuestionIndex).getCorrectAnswer();

        if (selectedAnswer.equals(correctAnswer)) {
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect Answer. Correct: " + correctAnswer, Toast.LENGTH_SHORT).show();
        }

        // If it's the last question, update the Next button to show a tick
        if (currentQuestionIndex == quizQuestions.size() - 1) {
            nextButton.setImageResource(R.drawable.baseline_check_24); // Replace with your tick icon resource
            nextButton.setOnClickListener(v1 -> {
                Toast.makeText(this, "Congratulations! You have completed the quiz.", Toast.LENGTH_LONG).show();

                // Navigate back to MainActivity2
                Intent intent = new Intent(QuizActivity.this, MainActivity2.class);
                intent.putExtra("courseDetails", (CustomModel) getIntent().getSerializableExtra("courseDetails"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // End the QuizActivity
            });
        }
    }

    private void handleNextQuestion(RadioGroup quizOptions, TextView quizQuestionText, RadioGroup optionsGroup, TextView progressText) {
        int selectedOptionId = quizOptions.getCheckedRadioButtonId();
        if (selectedOptionId == -1) {
            Toast.makeText(this, "Please answer the question before proceeding.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentQuestionIndex < quizQuestions.size() - 1) {
            currentQuestionIndex++;
            loadQuizQuestion(quizQuestions.get(currentQuestionIndex), quizQuestionText, optionsGroup);
            updateProgressText(progressText);
        }
    }

    private void updateProgressText(TextView progressText) {
        progressText.setText(courseTitleText + "\nQuestion " + (currentQuestionIndex + 1) + " of " + quizQuestions.size());
    }

    private void navigateBackToMainActivity(int currentLessonIndex, CustomModel courseDetails) {
        Intent intent = new Intent(QuizActivity.this, VideoActivity.class);
        intent.putExtra("currentLessonIndex", currentLessonIndex);
        intent.putExtra("courseDetails", courseDetails);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        navigateBackToMainActivity(0, null);
        super.onBackPressed();
    }
}
