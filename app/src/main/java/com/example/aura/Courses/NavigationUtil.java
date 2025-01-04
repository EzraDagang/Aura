package com.example.aura.Courses;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class NavigationUtil {
    public static void navigateToCourseList(AppCompatActivity activity, String category) {
        Intent intent = new Intent(activity, CourseListActivity.class);
        intent.putExtra("category", category);
        activity.startActivity(intent);
    }
}