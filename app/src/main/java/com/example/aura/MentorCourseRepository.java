package com.example.aura;

import java.util.ArrayList;
import java.util.List;

public class MentorCourseRepository {
    private static final List<MentorCourse> COURS = new ArrayList<>();

    public static void addCourse(MentorCourse mentorCourse) {
        COURS.add(mentorCourse);
    }

    public static List<MentorCourse> getCourses() {
        return new ArrayList<>(COURS);
    }
}