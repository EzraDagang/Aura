package com.example.aura.Courses;

public class CourseModel {
    private String courseId;
    private String title;
    private String mentorName;
    private Long numLessons;

    public CourseModel(String courseId, String title, String mentorName, Long numLessons) {
        this.courseId = courseId;
        this.title = title;
        this.mentorName = mentorName;
        this.numLessons = numLessons;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public String getMentorName() {
        return mentorName;
    }

    public Long getNumLessons() {
        return numLessons;
    }
}

