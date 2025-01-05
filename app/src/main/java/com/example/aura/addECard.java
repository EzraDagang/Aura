package com.example.aura;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class addECard extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image selection
    private ImageView profileImage;
    private Uri profileImageUri;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ecard);

        // Initialize Firestore and get user ID
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId == null) {
            Toast.makeText(this, "User not authenticated. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Back button functionality
        ImageButton btnBack = findViewById(R.id.BtnBack);
        btnBack.setOnClickListener(v -> finish()); // Return to the previous screen

        // Profile picture setup
        profileImage = findViewById(R.id.profileImage);
        ImageButton btnAddProfilePicture = findViewById(R.id.btnAddProfilePicture);
        btnAddProfilePicture.setOnClickListener(v -> openImagePicker());

        // Done button functionality
        Button btnDone = findViewById(R.id.BtnSaveNewECard);
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
                Toast.makeText(this, "Please fill in all the required fields.", Toast.LENGTH_SHORT).show();
            } else {
                // Create a map of data to store in Firestore
                Map<String, Object> eCardDetails = new HashMap<>();
                eCardDetails.put("name", name);
                eCardDetails.put("bloodType", bloodType);
                eCardDetails.put("dateOfBirth", dob);
                eCardDetails.put("height", height);
                eCardDetails.put("weight", weight);
                eCardDetails.put("medicalCondition", medicalCondition);
                eCardDetails.put("medication", medication);
                eCardDetails.put("allergies", allergies);

                if (profileImageUri != null) {
                    eCardDetails.put("profileImageUri", profileImageUri.toString());
                }

                // Save data to Firestore under the user's ID
                db.collection("users").document(userId).collection("emergencyCards")
                        .add(eCardDetails)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Emergency card details have been successfully saved!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK); // Notify the calling activity that the card was added
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to save emergency card details. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("addECard", "Error saving card: ", e);
                        });
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
