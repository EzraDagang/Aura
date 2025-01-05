package com.example.aura.Courses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.aura.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MyCoursesActivity extends AppCompatActivity {

    private ListView coursesListView;
    private ArrayAdapter<String> coursesAdapter;
    private ArrayList<String> enrolledCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        coursesListView = findViewById(R.id.coursesListView);
        TextView emptyTextView = findViewById(R.id.emptyTextView);

        enrolledCourses = new ArrayList<>(loadEnrolledCourses());
        coursesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, enrolledCourses);

        coursesListView.setAdapter(coursesAdapter);
        coursesListView.setEmptyView(emptyTextView); // Show emptyTextView if no courses

        LocalBroadcastManager.getInstance(this).registerReceiver(courseUpdateReceiver,
                new IntentFilter("com.example.aura.UPDATE_MY_COURSES"));
    }

    private Set<String> loadEnrolledCourses() {
        SharedPreferences prefs = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        return prefs.getStringSet("enrolledCourses", new HashSet<>());
    }
    private void saveEnrolledCourses() {
        SharedPreferences prefs = getSharedPreferences("CoursePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("enrolledCourses", new HashSet<>(enrolledCourses));
        boolean success = editor.commit();
        Log.d("MyCoursesActivity", "Courses saved: " + enrolledCourses + ", success=" + success);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveEnrolledCourses();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(courseUpdateReceiver);
    }

    private final BroadcastReceiver courseUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String courseTitle = intent.getStringExtra("courseTitle");
            boolean isAdding = intent.getBooleanExtra("isAdding", false);

            if (isAdding) {
                if (!enrolledCourses.contains(courseTitle)) {
                    enrolledCourses.add(courseTitle);
                    saveEnrolledCourses(); // Save the updated list
                    coursesAdapter.notifyDataSetChanged(); // Refresh the ListView
                }
            } else {
                if (enrolledCourses.remove(courseTitle)) {
                    saveEnrolledCourses(); // Save the updated list
                    coursesAdapter.notifyDataSetChanged(); // Refresh the ListView
                }
            }
        }
    };
}