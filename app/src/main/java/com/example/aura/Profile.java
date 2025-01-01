package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    private TextView heightValue, weightValue, bloodTypeValue;
    private TextView medicalConditions, allergies, medications;
    private TextView nameField, dateOfBirthValue;
    private ImageButton backButton;

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
        medicalConditions = findViewById(R.id.medicalConditions);
        allergies = findViewById(R.id.allergies);
        medications = findViewById(R.id.medications);

        // Initialize Back Button
        backButton = findViewById(R.id.BtnBack);

        // Set Back Button Click Listener
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, EmergencyCard.class);
            startActivity(intent);
            finish(); // Optional: Closes the current activity
        });

        // Populate the UI with details passed from the Intent
        populateDetailsFromIntent();
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
}
