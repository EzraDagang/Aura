package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    private TextView heightValue, weightValue, bloodTypeValue;
    private TextView medicalConditions, allergies, medications;
    private TextView nameField, dateOfBirthValue;
    private ImageButton backButton;
    private Button editButton;
    private String documentId; // Firestore document ID for the selected emergency card

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

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
            finish(); // Optional: Closes the current activity
        });

        // Edit Button logic
        editButton.setOnClickListener(v -> navigateToEditProfile());
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
