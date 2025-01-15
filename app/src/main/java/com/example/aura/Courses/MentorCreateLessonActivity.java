package com.example.aura.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aura.R;

public class MentorCreateLessonActivity extends AppCompatActivity {
    private EditText lessonTitleEditText;
    private EditText lessonContentEditText;
    private LinearLayout questionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mentor_create_lesson);

        lessonTitleEditText = findViewById(R.id.lessonTitleEditText);
        lessonContentEditText = findViewById(R.id.lessonContentEditText);

        // Set up Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Lesson");
        }

        // Retrieve the module name from the intent
        String moduleName = getIntent().getStringExtra("moduleName");
        if (moduleName != null) {
            TextView selectedModuleText = findViewById(R.id.selectedModuleText);
            selectedModuleText.setText("Module: " + moduleName);
        }

        questionsContainer = findViewById(R.id.questionsLayout);
        Button addQuestionButton = findViewById(R.id.addQuestionButton);

        // Set up "Add Question" button
        addQuestionButton.setOnClickListener(v -> addNewQuestion());

        Button saveLessonButton = findViewById(R.id.saveLessonButton);

        // Set up Save button with validation
        saveLessonButton.setOnClickListener(v -> saveLesson());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveLesson() {
        String title = lessonTitleEditText.getText().toString().trim();
        String content = lessonContentEditText.getText().toString().trim();

        // Validate Title and Content
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a lesson title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "Please enter lesson content", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure at least one question is added
        if (questionsContainer.getChildCount() == 0) {
            Toast.makeText(this, "Please add at least one question", Toast.LENGTH_SHORT).show();
            return;
        }

        // Proceed with saving the lesson (e.g., return the data to the calling activity)
        Intent courseIntent = new Intent();
        courseIntent.putExtra("lessonTitle", title);
        courseIntent.putExtra("lessonContent", content);
        setResult(RESULT_OK, courseIntent);
        finish();
    }

    private void addNewQuestion() {
        // Create Question Layout
        LinearLayout questionLayout = new LinearLayout(this);
        questionLayout.setOrientation(LinearLayout.VERTICAL);
        questionLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        questionLayout.setPadding(20, 20, 20, 20);

        // Add Question EditText
        LinearLayout questionRow = new LinearLayout(this);
        questionRow.setOrientation(LinearLayout.HORIZONTAL);
        questionRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        questionRow.setPadding(10, 0, 10, 10);

        EditText questionEditText = new EditText(this);
        questionEditText.setHint("Enter Question");
        questionEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        questionEditText.setBackgroundResource(R.drawable.edit_text_background);
        questionRow.addView(questionEditText);

        // Delete Question Icon
        ImageView deleteQuestionImage = new ImageView(this);
        deleteQuestionImage.setImageResource(R.drawable.baseline_delete_24);
        deleteQuestionImage.setPadding(10, 10, 10, 10);
        deleteQuestionImage.setOnClickListener(v -> {
            // Remove the entire question layout
            questionsContainer.removeView(questionLayout);
        });
        questionRow.addView(deleteQuestionImage);

        questionLayout.addView(questionRow);

        // Add Fixed Number of Answers
        for (int i = 0; i < 3; i++) {
            addAnswerOption(questionLayout);
        }

        // Add Question Layout to Container
        questionsContainer.addView(questionLayout);
    }

    private void addAnswerOption(LinearLayout questionLayout) {
        // Create Answer Row
        LinearLayout answerRow = new LinearLayout(this);
        answerRow.setOrientation(LinearLayout.HORIZONTAL);
        answerRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        answerRow.setPadding(40, 10, 20, 10);

        // RadioButton for Correct Answer
        RadioButton correctAnswerRadio = new RadioButton(this);
        correctAnswerRadio.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        correctAnswerRadio.setOnClickListener(v -> {
            // Ensure only one RadioButton is selected in this question
            for (int i = 0; i < questionLayout.getChildCount(); i++) {
                View child = questionLayout.getChildAt(i);
                if (child instanceof LinearLayout) {
                    LinearLayout childLayout = (LinearLayout) child;
                    if (childLayout.getChildAt(0) instanceof RadioButton) {
                        ((RadioButton) childLayout.getChildAt(0)).setChecked(false);
                    }
                }
            }
            correctAnswerRadio.setChecked(true);
        });
        answerRow.addView(correctAnswerRadio);

        // EditText for Answer
        EditText answerEditText = new EditText(this);
        answerEditText.setHint("Enter Answer");
        answerEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        answerEditText.setBackgroundResource(R.drawable.edit_text_background);
        answerRow.addView(answerEditText);

        // Delete Answer Icon
        ImageView deleteAnswerImage = new ImageView(this);
        deleteAnswerImage.setImageResource(R.drawable.baseline_delete_24);
        deleteAnswerImage.setPadding(10, 10, 10, 10);
        deleteAnswerImage.setOnClickListener(v -> {
            // Show confirmation dialog before deleting the answer row
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this answer?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // If user confirms, remove the specific answer row
                        questionLayout.removeView(answerRow);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss(); // Close the dialog if user cancels
                    })
                    .show(); // Show the dialog
        });
        answerRow.addView(deleteAnswerImage);

// Add Answer Row to Question Layout
        questionLayout.addView(answerRow);
    }

        @Override
    public void onBackPressed() {
        // Confirm unsaved changes before navigating back
        if (hasUnsavedChanges()) {
            new AlertDialog.Builder(this)
                    .setTitle("Discard Changes?")
                    .setMessage("You have unsaved changes. Are you sure you want to discard them?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasUnsavedChanges() {
        return !lessonTitleEditText.getText().toString().trim().isEmpty() ||
                !lessonContentEditText.getText().toString().trim().isEmpty() ||
                questionsContainer.getChildCount() > 0;
    }
}
