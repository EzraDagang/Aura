package com.example.aura.Courses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.aura.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MentorCourseInfoActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private List<String> moduleList; // Modules
    private HashMap<String, List<String>> lessonMap; // Lessons under each module
    private ExpandableListAdapter expandableListAdapter;
    private ImageView courseImageView;
    private TextView titleTextView, descriptionTextView, lessonsTextView;
    private String selectedModuleName = null; // To store the selected module name
    private View createLessonsButton; // Reference to the "Create Lessons" button

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize views
        courseImageView = findViewById(R.id.courseImageView);
        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.about);
        lessonsTextView = findViewById(R.id.lessonsTextView);
        expandableListView = findViewById(R.id.expandableListView);
        createLessonsButton = findViewById(R.id.createLessonsButton);
        View editCourseInfoButton = findViewById(R.id.editCourseInfoButton);

        // Retrieve data from Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("courseTitle");
        String description = intent.getStringExtra("courseDescription");
        int lessonsCount = intent.getIntExtra("lessonsCount", 0);
        String imageUrl = intent.getStringExtra("imageUrl");
        ArrayList<String> modules = intent.getStringArrayListExtra("modules");

        // Set course details
        titleTextView.setText(title);
        descriptionTextView.setText(description);
        lessonsTextView.setText(String.format("Lessons: %d", lessonsCount));
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(courseImageView);
        } else {
            courseImageView.setImageResource(R.drawable.error);
        }

        // Initialize modules and lessons data
        initializeModulesAndLessons(modules);

        // Set up ExpandableListView with adapter
        expandableListAdapter = new MentorCustomExpandableListAdapter(this, moduleList, lessonMap);
        expandableListView.setAdapter(expandableListAdapter);

        // Handle group clicks (only for modules)
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            selectedModuleName = moduleList.get(groupPosition); // Store selected module name
            Toast.makeText(this, "Selected Module: " + selectedModuleName, Toast.LENGTH_SHORT).show();
            return false; // Allow default expand/collapse behavior
        });

        // Set up "Create Lessons" button
        createLessonsButton.setOnClickListener(v -> {
            if (selectedModuleName != null) {
                Intent createLessonIntent = new Intent(MentorCourseInfoActivity.this, MentorCreateLessonActivity.class);
                createLessonIntent.putExtra("moduleName", selectedModuleName);
                startActivityForResult(createLessonIntent, 2);
            } else {
                Toast.makeText(this, "Please select a module first", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle "Edit Course Info" button click
        editCourseInfoButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(MentorCourseInfoActivity.this, MentorAddCourseActivity.class);

            // Pass the current course data to AddCourseActivity
            editIntent.putExtra("editMode", true);
            editIntent.putExtra("courseTitle", titleTextView.getText().toString());
            editIntent.putExtra("courseDescription", descriptionTextView.getText().toString());
            editIntent.putExtra("lessonsCount", Integer.parseInt(lessonsTextView.getText().toString().replace("Lessons: ", "")));
            editIntent.putExtra("imageUrl", imageUrl);

            if (moduleList != null) {
                editIntent.putStringArrayListExtra("modules", new ArrayList<>(moduleList));
            }

            startActivityForResult(editIntent, 1); // Use requestCode 1 for editing
        });


    }

    // Method to initialize module and lesson data
    private void initializeModulesAndLessons(ArrayList<String> modules) {
        moduleList = new ArrayList<>();
        lessonMap = new HashMap<>();

        if (modules != null) {
            for (String module : modules) {
                moduleList.add(module);
                lessonMap.put(module, new ArrayList<>()); // Start with no lessons
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                // Handle course details when requestCode == 1
                if (requestCode == 1) {
                    String title = data.getStringExtra("courseTitle");
                    String description = data.getStringExtra("courseDescription");
                    int lessonsCount = data.getIntExtra("lessonsCount", 0);
                    String imageUrl = data.getStringExtra("imageUrl");
                    ArrayList<String> modules = data.getStringArrayListExtra("modules");

                    // Log the data to check if it's received
                    Log.d("ReceivedData", "Title: " + title);
                    Log.d("ReceivedData", "Description: " + description);
                    Log.d("ReceivedData", "Lessons Count: " + lessonsCount);
                    Log.d("ReceivedData", "Modules: " + modules);
                    Log.d("ReceivedData", "Image URL: " + imageUrl);

                    // Update the views with the received data
                    titleTextView.setText(title);
                    descriptionTextView.setText(description);
                    lessonsTextView.setText(String.valueOf(lessonsCount));

                    // Load the image into an ImageView, for example using Glide or Picasso
                    Glide.with(this).load(imageUrl).into(courseImageView);

                    // Optionally, update the module list dynamically (if needed)
                    for (String module : modules) {
                        // Add each module to the UI (e.g., in a LinearLayout or a ListView)
                    }
                }

                // Handle lesson details
                String lessonTitle = data.getStringExtra("lessonTitle");
                String lessonContent = data.getStringExtra("lessonContent");

                // Ensure a module is selected and the lesson data is valid
                if (selectedModuleName != null && lessonTitle != null) {
                    // Update the lessonMap (local for ExpandableListView)
                    List<String> lessons = lessonMap.get(selectedModuleName);
                    if (lessons == null) {
                        lessons = new ArrayList<>();
                        lessonMap.put(selectedModuleName, lessons);
                    }
                    lessons.add(lessonTitle);

                    // Retrieve the current course object (update lessonsByModule)
                    MentorCourse mentorCourse = retrieveCurrentCourse();
                    if (mentorCourse != null) {
                        mentorCourse.addLesson(selectedModuleName, lessonTitle); // Update course data
                    }

                    // Notify the ExpandableListAdapter of data changes
                    ((MentorCustomExpandableListAdapter) expandableListAdapter).notifyDataSetChanged();
                }
            }
        }
    }

    private MentorCourse retrieveCurrentCourse() {
        // Assuming you have a way to identify the course being edited (e.g., a title or ID)
        String courseTitle = titleTextView.getText().toString(); // Ensure the title matches exactly
        for (MentorCourse mentorCourse : MentorCourseRepository.getCourses()) {
            if (mentorCourse.getTitle().equals(courseTitle)) {
                return mentorCourse;
            }
        }
        return null; // If no matching course is found
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle back button click
            Intent intent = new Intent(MentorCourseInfoActivity.this, MentorMyCourseActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}