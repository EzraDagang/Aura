package com.example.aura;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class addECard extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image selection
    private ImageView profileImage;
    private Uri profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ecard);

        // Back button functionality
        ImageButton btnBack = findViewById(R.id.BtnBack);
        btnBack.setOnClickListener(v -> finish()); // Return to the previous screen

        // Profile picture setup
        profileImage = findViewById(R.id.profileImage);
        ImageButton btnAddProfilePicture = findViewById(R.id.btnAddProfilePicture);
        btnAddProfilePicture.setOnClickListener(v -> openImagePicker());

        // Done button functionality
        Button btnDone = findViewById(R.id.BtnUpdateEditedDetails);
        btnDone.setOnClickListener(v -> {
            // Collect user input
            String name = ((EditText) findViewById(R.id.nameField)).getText().toString().trim();
            String bloodType = ((EditText) findViewById(R.id.bloodTypeField)).getText().toString().trim();
            String dob = ((EditText) findViewById(R.id.dateBirthField)).getText().toString().trim();
            String height = ((EditText) findViewById(R.id.heightField)).getText().toString().trim();
            String weight = ((EditText) findViewById(R.id.weightField)).getText().toString().trim();
            String medicalCondition = ((EditText) findViewById(R.id.medicalConditionsField)).getText().toString().trim();
            String medication = ((EditText) findViewById(R.id.medicationsField)).getText().toString().trim();
            String allergies = ((EditText) findViewById(R.id.allergiesField)).getText().toString().trim();

            // Validate all required fields
            if (name.isEmpty() || bloodType.isEmpty() || dob.isEmpty() || height.isEmpty() || weight.isEmpty()
                    || medicalCondition.isEmpty() || medication.isEmpty() || allergies.isEmpty()) {
                // Display error message
                Toast.makeText(this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show();
            } else {
                // Prepare the result to send back
                Intent resultIntent = new Intent();
                resultIntent.putExtra("NAME", name);
                resultIntent.putExtra("BLOOD_TYPE", bloodType);
                resultIntent.putExtra("DATE_OF_BIRTH", dob);
                resultIntent.putExtra("HEIGHT", height);
                resultIntent.putExtra("WEIGHT", weight);
                resultIntent.putExtra("MEDICAL_CONDITION", medicalCondition);
                resultIntent.putExtra("MEDICATION", medication);
                resultIntent.putExtra("ALLERGIES", allergies);
                resultIntent.putExtra("EMERGENCY_CONTACT", "No contact provided"); // Add this if needed
                if (profileImageUri != null) {
                    resultIntent.putExtra("PROFILE_IMAGE_URI", profileImageUri.toString());
                }

                // Display success message
                Toast.makeText(this, "You have successfully added a new Emergency Card!", Toast.LENGTH_SHORT).show();

                // Set result and finish activity
                setResult(RESULT_OK, resultIntent);
                finish(); // Return to EmergencyCard activity
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profileImageUri);
                profileImage.setImageBitmap(bitmap); // Display the selected image
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
