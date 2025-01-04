package com.example.aura;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {

    private TextView heightValue, weightValue, bloodTypeValue;
    private TextView medicalConditions, allergies, medications;
    private TextView nameField, dateOfBirthValue;
    private ImageButton backButton;
    private Button editButton;
    private FirebaseFirestore db;
    private String userId; // Firebase Authentication user ID
    private String documentId; // Firestore document ID for the selected emergency card

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        // Initialize Firestore and Firebase Auth
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (userId == null) {
            Toast.makeText(this, "User not authenticated. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize TextViews
        nameField = findViewById(R.id.nameField);
        dateOfBirthValue = findViewById(R.id.dateBirthValue);
        heightValue = findViewById(R.id.heightValue);
        weightValue = findViewById(R.id.weightValue);
        bloodTypeValue = findViewById(R.id.bloodTypeValue);
        medicalConditions = findViewById(R.id.medicalConditionsField);
        allergies = findViewById(R.id.allergies);
        medications = findViewById(R.id.medicationsField);

        // Initialize Buttons
        backButton = findViewById(R.id.BtnBack);
        editButton = findViewById(R.id.BtnEditProfile);

        // Get Firestore document ID passed from EmergencyCard
        documentId = getIntent().getStringExtra("DOCUMENT_ID");
        if (documentId == null) {
            Toast.makeText(this, "Error: Document ID not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate the UI with details passed from the Intent
        populateDetailsFromIntent();

        // Back Button logic
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, EmergencyCard.class);
            startActivity(intent);
            finish();
        });

        // Edit Button logic
        editButton.setOnClickListener(v -> navigateToEditProfile());

        // Floating Delete Button logic
        findViewById(R.id.fabDelete).setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void populateDetailsFromIntent() {
        // Extract details from the Intent
        String name = getIntent().getStringExtra("NAME");
        String dateOfBirth = getIntent().getStringExtra("DATE_OF_BIRTH");
        String height = getIntent().getStringExtra("HEIGHT");
        String weight = getIntent().getStringExtra("WEIGHT");
        String bloodType = getIntent().getStringExtra("BLOOD_TYPE");
        String medicalCond = getIntent().getStringExtra("MEDICAL_CONDITION");
        String allergyDetails = getIntent().getStringExtra("ALLERGIES");
        String medicationDetails = getIntent().getStringExtra("MEDICATION");

        // Set the extracted details in TextViews
        nameField.setText(name != null ? name : "---");
        dateOfBirthValue.setText(dateOfBirth != null ? dateOfBirth : "---");
        heightValue.setText(height != null ? height : "---");
        weightValue.setText(weight != null ? weight : "---");
        bloodTypeValue.setText(bloodType != null ? bloodType : "---");
        medicalConditions.setText(medicalCond != null ? medicalCond : "---");
        allergies.setText(allergyDetails != null ? allergyDetails : "---");
        medications.setText(medicationDetails != null ? medicationDetails : "---");
    }

    private void navigateToEditProfile() {
        // Extract current details to pass to the edit screen
        Intent editIntent = new Intent(Profile.this, editProfileDetails.class);
        editIntent.putExtra("DOCUMENT_ID", documentId); // Pass Firestore document ID
        editIntent.putExtra("NAME", nameField.getText().toString());
        editIntent.putExtra("DATE_OF_BIRTH", dateOfBirthValue.getText().toString());
        editIntent.putExtra("HEIGHT", heightValue.getText().toString());
        editIntent.putExtra("WEIGHT", weightValue.getText().toString());
        editIntent.putExtra("BLOOD_TYPE", bloodTypeValue.getText().toString());
        editIntent.putExtra("MEDICAL_CONDITION", medicalConditions.getText().toString());
        editIntent.putExtra("ALLERGIES", allergies.getText().toString());
        editIntent.putExtra("MEDICATION", medications.getText().toString());
        startActivityForResult(editIntent, 1); // Request code for editing
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Emergency Card")
                .setMessage("Do you want to delete this emergency card?")
                .setPositiveButton("Yes", (dialog, which) -> deleteEmergencyCard())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteEmergencyCard() {
        // Delete the card from Firestore
        db.collection("users")
                .document(userId) // Use the logged-in user's ID dynamically
                .collection("emergencyCards")
                .document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Emergency card deleted successfully.", Toast.LENGTH_SHORT).show();
                    finish(); // Close the current activity and return to the previous one
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete emergency card. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Update the profile with edited details
            String name = data.getStringExtra("NAME");
            String dateOfBirth = data.getStringExtra("DATE_OF_BIRTH");
            String height = data.getStringExtra("HEIGHT");
            String weight = data.getStringExtra("WEIGHT");
            String bloodType = data.getStringExtra("BLOOD_TYPE");
            String medicalCond = data.getStringExtra("MEDICAL_CONDITION");
            String allergyDetails = data.getStringExtra("ALLERGIES");
            String medicationDetails = data.getStringExtra("MEDICATION");

            nameField.setText(name != null ? name : "---");
            dateOfBirthValue.setText(dateOfBirth != null ? dateOfBirth : "---");
            heightValue.setText(height != null ? height : "---");
            weightValue.setText(weight != null ? weight : "---");
            bloodTypeValue.setText(bloodType != null ? bloodType : "---");
            medicalConditions.setText(medicalCond != null ? medicalCond : "---");
            allergies.setText(allergyDetails != null ? allergyDetails : "---");
            medications.setText(medicationDetails != null ? medicationDetails : "---");
        }
    }
}
