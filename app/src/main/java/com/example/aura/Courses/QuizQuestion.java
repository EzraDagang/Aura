package com.example.aura.Courses;

import java.io.Serializable;
import java.util.List;

public class QuizQuestion implements Serializable {
    private String question; // The quiz question
    private List<String> options; // Options for the question
    private String correctAnswer; // The correct answer

    // Constructor
    public QuizQuestion(String question, List<String> options, String correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getters
    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    // Setters
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}