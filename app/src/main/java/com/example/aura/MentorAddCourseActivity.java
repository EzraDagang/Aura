package com.example.aura;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MentorAddCourseActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView courseImageView;
    private EditText titleEditText, descriptionEditText, lessonsEditText, imageUrlInput;
    private LinearLayout modulesLayout;
    private Button addModulesButton, doneButton, fetchImageButton;
    private Bitmap selectedImageBitmap;
    private boolean isEditMode = false;

    // Data structures for modules and lessons
    private List<String> groupList = new ArrayList<>();
    private HashMap<String, List<String>> childMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable navigation (back button)
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize views
        titleEditText = findViewById(R.id.courseTitle);
        descriptionEditText = findViewById(R.id.courseDescription);
        lessonsEditText = findViewById(R.id.tvLessons);
        modulesLayout = findViewById(R.id.modulesLayout);
        addModulesButton = findViewById(R.id.addModules);
        doneButton = findViewById(R.id.doneButton);
        courseImageView = findViewById(R.id.courseImageView);
        imageUrlInput = findViewById(R.id.imageUrlInput);
        fetchImageButton = findViewById(R.id.fetchImageButton);

        // Fetch image when button is clicked
        fetchImageButton.setOnClickListener(v -> {
            String imageUrl = imageUrlInput.getText().toString().trim();
            if (!imageUrl.isEmpty()) {
                loadImageFromUrl(imageUrl);
            } else {
                Toast.makeText(this, "Please enter a valid URL.", Toast.LENGTH_SHORT).show();
            }
        });

        addModulesButton.setOnClickListener(v -> addModulesField("")); // Dynamically add module row

        // Check if activity is launched in edit mode
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("editMode", false);

        if (isEditMode) {
            // Populate fields with current course data
            populateFields(intent);

            // Update the button text and color for "Save Changes"
            doneButton.setText("Save Changes");
            doneButton.setBackgroundTintList(getResources().getColorStateList(R.color.green));
        }

        doneButton.setOnClickListener(v -> saveCourseData());
    }

    // Load image from URL
    private void loadImageFromUrl(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "Error loading image", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(courseImageView);
    }

    // Dynamically add a new module field
    private void addModulesField(String module) {
        // Create a new LinearLayout row for the module
        LinearLayout moduleRow = new LinearLayout(this);
        moduleRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, 16, 0, 0); // Add spacing between rows
        moduleRow.setLayoutParams(rowParams);

        // Create an EditText for the module title
        EditText moduleField = new EditText(this);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1); // Weight = 1
        moduleField.setLayoutParams(editTextParams);
        moduleField.setHint("Module");
        moduleField.setText(module); // Set the module name if provided
        moduleField.setPadding(10, 15, 10, 10);
        moduleField.setBackground(getResources().getDrawable(R.drawable.edit_text_background));

        // Create the delete icon (ImageView)
        ImageView deleteIcon = new ImageView(this);
        deleteIcon.setImageResource(R.drawable.baseline_delete_24);
        deleteIcon.setPadding(10, 10, 10, 10);
        deleteIcon.setContentDescription("Delete Module");
        deleteIcon.setOnClickListener(v -> {
            // Confirmation dialog for deletion
            new AlertDialog.Builder(this)
                    .setTitle("Delete Module")
                    .setMessage("Are you sure you want to delete this module?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Remove the module row from the layout
                        modulesLayout.removeView(moduleRow);

                        // Remove the module from groupList and childMap if it exists
                        String moduleText = moduleField.getText().toString().trim();
                        if (!moduleText.isEmpty() && groupList.contains(moduleText)) {
                            groupList.remove(moduleText);
                            childMap.remove(moduleText);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Add the EditText and delete icon to the row
        moduleRow.addView(moduleField);
        moduleRow.addView(deleteIcon);

        // Add the row to the modules layout
        modulesLayout.addView(moduleRow);

        // Add the module to groupList and initialize childMap if it is valid
        if (!module.isEmpty() && !groupList.contains(module)) {
            groupList.add(module);
            childMap.put(module, new ArrayList<>()); // Initialize an empty list for lessons
        }
    }

    private void populateFields(Intent intent) {
        titleEditText.setText(intent.getStringExtra("courseTitle"));
        descriptionEditText.setText(intent.getStringExtra("courseDescription"));
        lessonsEditText.setText(String.valueOf(intent.getIntExtra("lessonsCount", 0)));
        imageUrlInput.setText(intent.getStringExtra("imageUrl"));
        loadImageFromUrl(intent.getStringExtra("imageUrl"));

        ArrayList<String> modules = intent.getStringArrayListExtra("modules");
        if (modules != null) {
            for (String module : modules) {
                addModulesField(module); // Add the pre-populated module rows
                if (!childMap.containsKey(module)) {
                    childMap.put(module, new ArrayList<>()); // Ensure childMap is initialized
                }
            }
        }
    }

    // Save or update course data
    private void saveCourseData() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String lessonsCountStr = lessonsEditText.getText().toString().trim();
        String imageUrl = imageUrlInput.getText().toString().trim();

        // Validate inputs
        if (title.isEmpty() || description.isEmpty() || lessonsCountStr.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!lessonsCountStr.matches("\\d+")) {
            Toast.makeText(this, "Please enter a valid number of lessons.", Toast.LENGTH_SHORT).show();
            return;
        }

        int lessonsCount = Integer.parseInt(lessonsCountStr);

        // Collect module data
        List<String> modules = new ArrayList<>();
        for (int i = 0; i < modulesLayout.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) modulesLayout.getChildAt(i);
            EditText moduleField = (EditText) row.getChildAt(0);
            String module = moduleField.getText().toString().trim();
            if (module.isEmpty()) {
                Toast.makeText(this, "Please fill in all module fields or remove empty rows.", Toast.LENGTH_SHORT).show();
                return;
            }
            modules.add(module);
        }

        // Pass data back to the previous activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("courseTitle", title);
        resultIntent.putExtra("courseDescription", description);
        resultIntent.putExtra("lessonsCount", lessonsCount);
        resultIntent.putStringArrayListExtra("modules", new ArrayList<>(modules));
        resultIntent.putExtra("imageUrl", imageUrl);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isEditMode) {
            // Return to CourseInfoActivity in edit mode
            setResult(RESULT_CANCELED);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
