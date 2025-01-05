package com.example.aura.Courses;

import java.io.Serializable;
import java.util.List;

public class CustomModel implements Serializable {
    private String courseId;
    private static final long serialVersionUID = 1L;
    int image;
    String title, lesson, rating, about, mentorName, mentorJob,courseID;
    int mentorProfileImage;
    List<String> modules; // List of module titles
    List<List<String>> lessons; // Nested list of lessons for each module
    List<List<String>> lessonContents; // Nested list of lesson content for each module
    List<QuizQuestion> quizQuestions;//new
    String videoURL;
    private boolean isEnrolled; // Add enrollment status



    public CustomModel(int image, String title, String lesson, String rating, String about, String mentorName, String mentorJob, int mentorProfileImage, List<String> modules, List<List<String>> lessons, List<List<String>> lessonContents, List<QuizQuestion> quizQuestions, String videoURL) {
        this.image = image;
        this.title = title;
        this.lesson = lesson;
        this.rating = rating;
        this.about = about;
        this.mentorName = mentorName;
        this.mentorJob = mentorJob;
        this.mentorProfileImage = mentorProfileImage;
        this.modules = modules;
        this.lessons = lessons;
        this.lessonContents = lessonContents;
        this.quizQuestions = quizQuestions;
        this.videoURL = videoURL;
        this.isEnrolled = false;
    }

    public CustomModel(int image, String title, String lesson, String rating, String courseId) {
        this.image = image;
        this.title = title;
        this.lesson = lesson;
        this.rating = rating;
        this.courseId = courseId;
    }

    // Getters and Setters
    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    public List<List<String>> getLessonContents() {
        return lessonContents;
    }

    public List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestion> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getMentorName() { return mentorName; }
    public String getMentorJob() { return mentorJob; }
    public int getMentorProfileImage() { return mentorProfileImage; }

    public List<String> getModules() {
        return modules;
    }

    public List<List<String>> getLessons() {
        return lessons;
    }

}
