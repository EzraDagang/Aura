package com.example.aura.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aura.R;

import java.util.ArrayList;

public class MentorLessonDetailsActivity extends AppCompatActivity {
    private EditText lessonTitleEditText;
    private EditText lessonContentEditText;
    private EditText lessonVideoEditText;
    private LinearLayout questionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_create_lesson); // Reuse the same layout

        // Initialize Views
        lessonTitleEditText = findViewById(R.id.lessonTitleEditText);
        lessonContentEditText = findViewById(R.id.lessonContentEditText);
        lessonVideoEditText = findViewById(R.id.lessonVideo);
        questionsContainer = findViewById(R.id.questionsLayout);

        // Retrieve data from Intent
        Intent intent = getIntent();
        String moduleName = intent.getStringExtra("moduleName");
        String lessonTitle = intent.getStringExtra("lessonTitle");
        String lessonContent = intent.getStringExtra("lessonContent");
        String lessonVideo = intent.getStringExtra("lessonVideo");
        String[] quizQuestions = intent.getStringArrayExtra("quizQuestions");
        ArrayList<ArrayList<String>> quizAnswers = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("quizAnswers");
        int[] correctAnswerIndices = intent.getIntArrayExtra("correctAnswerIndices");

        // Set data to views
        if (moduleName != null) {
            TextView selectedModuleText = findViewById(R.id.selectedModuleText);
            selectedModuleText.setText("Module: " + moduleName);
        }
        if (lessonTitle != null) lessonTitleEditText.setText(lessonTitle);
        if (lessonContent != null) lessonContentEditText.setText(lessonContent);
        if (lessonVideo != null) lessonVideoEditText.setText(lessonVideo);

        // Populate quiz questions and answers
        if (quizQuestions != null && quizAnswers != null && correctAnswerIndices != null) {
            for (int i = 0; i < quizQuestions.length; i++) {
                addQuestionWithAnswers(quizQuestions[i], quizAnswers.get(i), correctAnswerIndices[i]);
            }
        }
    }

    private void addQuestionWithAnswers(String question, ArrayList<String> answers, int correctIndex) {
        // Create a layout for each question
        LinearLayout questionLayout = new LinearLayout(this);
        questionLayout.setOrientation(LinearLayout.VERTICAL);
        questionLayout.setPadding(20, 20, 20, 20);

        // Add the question EditText
        EditText questionEditText = new EditText(this);
        questionEditText.setText(question);
        questionEditText.setHint("Enter Question");
        questionLayout.addView(questionEditText);

        // Add answers
        for (int i = 0; i < answers.size(); i++) {
            LinearLayout answerLayout = new LinearLayout(this);
            answerLayout.setOrientation(LinearLayout.HORIZONTAL);

            // Correct answer radio button
            RadioButton radioButton = new RadioButton(this);
            radioButton.setChecked(i == correctIndex);
            answerLayout.addView(radioButton);

            // Answer EditText
            EditText answerEditText = new EditText(this);
            answerEditText.setText(answers.get(i));
            answerEditText.setHint("Enter Answer");
            answerLayout.addView(answerEditText);

            questionLayout.addView(answerLayout);

            // Ensure only one RadioButton is selected
            radioButton.setOnClickListener(v -> {
                for (int j = 1; j < questionLayout.getChildCount(); j++) {
                    LinearLayout layout = (LinearLayout) questionLayout.getChildAt(j);
                    RadioButton rb = (RadioButton) layout.getChildAt(0);
                    rb.setChecked(false);
                }
                radioButton.setChecked(true);
            });
        }

        // Add the question layout to the container
        questionsContainer.addView(questionLayout);
    }
}
