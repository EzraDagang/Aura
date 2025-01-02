package com.example.aura;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class editProfileDetails extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image selection
    private ImageView profileImage;
    private Uri profileImageUri;
    private FirebaseFirestore db;
    private String documentId; // Firestore document ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_details);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get references to the fields
        EditText nameField = findViewById(R.id.nameField);
        EditText dateOfBirthField = findViewById(R.id.dateBirthField);
        EditText heightField = findViewById(R.id.heightField);
        EditText weightField = findViewById(R.id.weightField);
        EditText bloodTypeField = findViewById(R.id.bloodTypeField);
        EditText medicalConditionsField = findViewById(R.id.medicalConditionsField);
        EditText allergiesField = findViewById(R.id.allergiesField);
        EditText medicationsField = findViewById(R.id.medicationsField);

        profileImage = findViewById(R.id.profileImage);
        ImageButton btnBack = findViewById(R.id.BtnBack);
        ImageButton btnAddProfilePicture = findViewById(R.id.btnAddProfilePicture);
        btnAddProfilePicture.setOnClickListener(v -> openImagePicker());

        // Populate fields with data passed from the Profile activity
        Intent intent = getIntent();
        documentId = intent.getStringExtra("DOCUMENT_ID"); // Firestore document ID
        nameField.setText(intent.getStringExtra("NAME"));
        dateOfBirthField.setText(intent.getStringExtra("DATE_OF_BIRTH"));
        heightField.setText(intent.getStringExtra("HEIGHT"));
        weightField.setText(intent.getStringExtra("WEIGHT"));
        bloodTypeField.setText(intent.getStringExtra("BLOOD_TYPE"));
        medicalConditionsField.setText(intent.getStringExtra("MEDICAL_CONDITION"));
        allergiesField.setText(intent.getStringExtra("ALLERGIES"));
        medicationsField.setText(intent.getStringExtra("MEDICATION"));

        // Back button logic
        btnBack.setOnClickListener(v -> onBackPressed());

        // Save button logic
        findViewById(R.id.BtnSaveNewECard).setOnClickListener(v -> {
            // Collect updated details
            String name = nameField.getText().toString().trim();
            String dateOfBirth = dateOfBirthField.getText().toString().trim();
            String height = heightField.getText().toString().trim();
            String weight = weightField.getText().toString().trim();
            String bloodType = bloodTypeField.getText().toString().trim();
            String medicalConditions = medicalConditionsField.getText().toString().trim();
            String allergies = allergiesField.getText().toString().trim();
            String medications = medicationsField.getText().toString().trim();

            // Validate input fields
            if (name.isEmpty() || dateOfBirth.isEmpty() || height.isEmpty() || weight.isEmpty() || bloodType.isEmpty()) {
                Toast.makeText(this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a map with updated data
            Map<String, Object> updatedDetails = new HashMap<>();
            updatedDetails.put("name", name);
            updatedDetails.put("dateOfBirth", dateOfBirth);
            updatedDetails.put("height", height);
            updatedDetails.put("weight", weight);
            updatedDetails.put("bloodType", bloodType);
            updatedDetails.put("medicalCondition", medicalConditions);
            updatedDetails.put("allergies", allergies);
            updatedDetails.put("medication", medications);
            if (profileImageUri != null) {
                updatedDetails.put("profileImageUri", profileImageUri.toString());
            }

            // Update the data in Firestore
            db.collection("users")
                    .document("testUser123") // Replace with the actual user ID or hardcoded user ID
                    .collection("emergencyCards")
                    .document(documentId)
                    .update(updatedDetails)
                    .addOnSuccessListener(aVoid -> {
                        // Display a success message
                        Toast.makeText(this, "Details successfully updated.", Toast.LENGTH_SHORT).show();

                        // Return updated details to the previous activity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("NAME", name);
                        resultIntent.putExtra("DATE_OF_BIRTH", dateOfBirth);
                        resultIntent.putExtra("HEIGHT", height);
                        resultIntent.putExtra("WEIGHT", weight);
                        resultIntent.putExtra("BLOOD_TYPE", bloodType);
                        resultIntent.putExtra("MEDICAL_CONDITION", medicalConditions);
                        resultIntent.putExtra("ALLERGIES", allergies);
                        resultIntent.putExtra("MEDICATION", medications);

                        setResult(RESULT_OK, resultIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Display an error message
                        Toast.makeText(this, "Failed to update details. Please try again.", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public void onBackPressed() {
        // Set the result to canceled and finish the activity
        setResult(RESULT_CANCELED);
        super.onBackPressed();
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
