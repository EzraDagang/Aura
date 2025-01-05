package com.example.aura.Courses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

    private int currentQuestionIndex = 0;
    private List<QuizQuestion> quizQuestions;
    private String courseTitleText;
    private ImageButton nextButton;
    private int[] selectedAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizQuestions = (ArrayList<QuizQuestion>) getIntent().getSerializableExtra("quizQuestions");
        CustomModel courseDetails = (CustomModel) getIntent().getSerializableExtra("courseDetails");
        int currentLessonIndex = getIntent().getIntExtra("currentLessonIndex", 0);

        // Fetch quiz questions from courseDetails if available
        if (courseDetails != null && courseDetails.getQuizQuestions() != null) {
            quizQuestions = courseDetails.getQuizQuestions();
            courseTitleText = courseDetails.getTitle();
        }

        if (quizQuestions == null || quizQuestions.isEmpty()) {
            Toast.makeText(this, "No quiz questions available.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        selectedAnswers = new int[quizQuestions.size()];
        for (int i = 0; i < selectedAnswers.length; i++) {
            selectedAnswers[i] = -1;
        }

        TextView courseTitle = findViewById(R.id.courseTitle);
        TextView quizQuestionText = findViewById(R.id.quizQuestion);
        RadioGroup quizOptions = findViewById(R.id.quizOptions);
        Button submitButton = findViewById(R.id.submitButton);
        nextButton = findViewById(R.id.nextButton);
        ImageButton prevButton = findViewById(R.id.prevButton);
        ImageButton backButton = findViewById(R.id.backButton);
        TextView progressText = findViewById(R.id.progressText);

        courseTitle.setText(courseTitleText != null ? courseTitleText : "Quiz");

        loadQuizQuestion(quizQuestions.get(currentQuestionIndex), quizQuestionText, quizOptions);

        backButton.setOnClickListener(v -> navigateBackToMainActivity(currentLessonIndex, courseDetails));
        submitButton.setOnClickListener(v -> handleSubmission(quizOptions));
        nextButton.setOnClickListener(v -> handleNextQuestion(quizOptions, quizQuestionText, quizOptions, progressText));

        prevButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                loadQuizQuestion(quizQuestions.get(currentQuestionIndex), quizQuestionText, quizOptions);
                updateProgressText(progressText);
            } else {
                Toast.makeText(this, "You are already at the first question.", Toast.LENGTH_SHORT).show();
            }
        });

        updateProgressText(progressText);
    }

    private void loadQuizQuestion(QuizQuestion quizQuestion, TextView quizQuestionText, RadioGroup quizOptions) {
        quizQuestionText.setText(quizQuestion.getQuestion());
        quizOptions.removeAllViews();

        for (String option : quizQuestion.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setTextColor(Color.WHITE);
            quizOptions.addView(radioButton);
        }

        if (selectedAnswers[currentQuestionIndex] != -1) {
            ((RadioButton) quizOptions.getChildAt(selectedAnswers[currentQuestionIndex])).setChecked(true);
        } else {
            quizOptions.clearCheck();
        }

        if (currentQuestionIndex == quizQuestions.size() - 1) {
            nextButton.setImageResource(R.drawable.baseline_check_24);
            nextButton.setOnClickListener(v -> {
                Toast.makeText(this, "Congratulations! Quiz Completed!", Toast.LENGTH_LONG).show();
                navigateBackToMainActivity(0, null);
            });
        }
    }

    private void handleSubmission(RadioGroup quizOptions) {
        if (quizOptions.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedOptionId = quizOptions.getCheckedRadioButtonId();
        RadioButton selectedOption = findViewById(selectedOptionId);

        selectedAnswers[currentQuestionIndex] = quizOptions.indexOfChild(selectedOption);

        String selectedAnswer = selectedOption.getText().toString();
        String correctAnswer = quizQuestions.get(currentQuestionIndex).getCorrectAnswer();

        if (selectedAnswer.equals(correctAnswer)) {
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect Answer. Correct: " + correctAnswer, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNextQuestion(RadioGroup quizOptions, TextView quizQuestionText, RadioGroup optionsGroup, TextView progressText) {
        if (quizOptions.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select an answer before proceeding.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentQuestionIndex < quizQuestions.size() - 1) {
            currentQuestionIndex++;
            loadQuizQuestion(quizQuestions.get(currentQuestionIndex), quizQuestionText, optionsGroup);
            updateProgressText(progressText);
        }
    }

    private void updateProgressText(TextView progressText) {
        progressText.setText("Question " + (currentQuestionIndex + 1) + " of " + quizQuestions.size());
    }

    private void navigateBackToMainActivity(int currentLessonIndex, CustomModel courseDetails) {
        Intent intent = new Intent(QuizActivity.this, VideoActivity.class);
        intent.putExtra("currentLessonIndex", currentLessonIndex);
        if (courseDetails != null) {
            intent.putExtra("courseDetails", courseDetails);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        navigateBackToMainActivity(0, null);
        super.onBackPressed();
    }
}
