package com.example.aura.Courses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aura.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MentorMyCourseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MentorCourseAdapter mentorCourseAdapter;
    private ArrayList<MentorCourse> mentorCourseList;
    private SharedPreferences sharedPreferences;
    private Button addCourseButton;

    private static final String COURSE_LIST_KEY = "courseList";
    private static final String LESSON_MAP_KEY = "lessonMap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        addCourseButton = findViewById(R.id.addCourseButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("CourseData", MODE_PRIVATE);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> {
            finish(); // Close this activity and go back to the previous screen
        });
        // Load courses and lesson maps from SharedPreferences
        mentorCourseList = loadCourses();

        // Initialize adapter with delete functionality
        mentorCourseAdapter = new MentorCourseAdapter(this, mentorCourseList, position -> {
            mentorCourseList.remove(position); // Remove course from the list
            saveCourses(); // Save updated list to SharedPreferences
            mentorCourseAdapter.notifyItemRemoved(position); // Notify adapter about the removed item
            mentorCourseAdapter.notifyItemRangeChanged(position, mentorCourseList.size()); // Refresh the list
        });

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mentorCourseAdapter);
        Log.d("MyCourseActivity", "Loaded Courses: " + mentorCourseList);

        // Handle add course button click
        addCourseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MentorMyCourseActivity.this, MentorAddCourseActivity.class);
            startActivityForResult(intent, 1); // Request code 1 to handle the result
        });

        saveCourses(); // Ensure updated courses are saved
    }

    // Load courses and their lesson maps from SharedPreferences
    private ArrayList<MentorCourse> loadCourses() {
        Gson gson = new Gson();

        // Load course list
        String courseListJson = sharedPreferences.getString(COURSE_LIST_KEY, "");
        ArrayList<MentorCourse> cours = new ArrayList<>();
        if (!courseListJson.isEmpty()) {
            Type courseListType = new TypeToken<ArrayList<MentorCourse>>() {}.getType();
            cours = gson.fromJson(courseListJson, courseListType);
        }

        // Load lesson maps for each course
        for (MentorCourse mentorCourse : cours) {
            String lessonMapJson = sharedPreferences.getString(LESSON_MAP_KEY + "_" + mentorCourse.getTitle(), "");
            if (!lessonMapJson.isEmpty()) {
                Type lessonMapType = new TypeToken<HashMap<String, List<String>>>() {}.getType();
                HashMap<String, List<String>> lessonMap = gson.fromJson(lessonMapJson, lessonMapType);
                mentorCourse.setLessonsByModule(lessonMap);
            }
        }

        return cours;
    }

    // Save courses and their lesson maps to SharedPreferences
    private void saveCourses() {
        Gson gson = new Gson();

        // Save course list
        String courseListJson = gson.toJson(mentorCourseList);
        sharedPreferences.edit().putString(COURSE_LIST_KEY, courseListJson).apply();

        // Save lesson maps for each course
        for (MentorCourse mentorCourse : mentorCourseList) {
            String lessonMapJson = gson.toJson(mentorCourse.getLessonsByModule());
            sharedPreferences.edit().putString(LESSON_MAP_KEY + "_" + mentorCourse.getTitle(), lessonMapJson).apply();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String courseTitle = data.getStringExtra("courseTitle");
            String courseDescription = data.getStringExtra("courseDescription");
            int lessonsCount = data.getIntExtra("lessonsCount", 0);
            String imageUrl = data.getStringExtra("imageUrl");
            ArrayList<String> modules = data.getStringArrayListExtra("modules");

            // Check if it's an edit or a new course
            if (data.getBooleanExtra("isEditMode", false)) {
                // Handle updating an existing course
                for (int i = 0; i < mentorCourseList.size(); i++) {
                    MentorCourse existingCourse = mentorCourseList.get(i);
                    if (existingCourse.getTitle().equals(courseTitle)) {
                        // Update the course fields
                        existingCourse.setDescription(courseDescription);
                        existingCourse.setLessonsCount(lessonsCount);
                        existingCourse.setImageUrl(imageUrl);
                        existingCourse.setModules(modules);

                        // Update the lesson map if needed
                        existingCourse.setLessonsByModule(new HashMap<>());
                        for (String module : modules) {
                            existingCourse.getLessonsByModule().put(module, new ArrayList<>());
                        }

                        mentorCourseAdapter.notifyItemChanged(i);  // Notify the adapter that the item has been updated
                        break;
                    }
                }
            } else {
                // Add new course if it's not an edit mode
                MentorCourse newMentorCourse = new MentorCourse(courseTitle, courseDescription, lessonsCount, imageUrl, modules);

                // Initialize empty lesson map for the new course
                newMentorCourse.setLessonsByModule(new HashMap<>());
                for (String module : modules) {
                    newMentorCourse.getLessonsByModule().put(module, new ArrayList<>());
                }

                mentorCourseList.add(newMentorCourse);
                mentorCourseAdapter.notifyItemInserted(mentorCourseList.size() - 1);  // Notify that a new course was added
            }

            // Save updated courses to SharedPreferences
            saveCourses();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload courses from SharedPreferences
        mentorCourseList.clear();
        mentorCourseList.addAll(loadCourses());
        mentorCourseAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Finish the current activity to return to the previous one
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
