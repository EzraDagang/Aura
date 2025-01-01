package com.example.aura;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class editProfileDetails extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image selection
    private ImageView profileImage;
    private Uri profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_details);

        EditText nameField = findViewById(R.id.nameField);
        EditText dateOfBirthField = findViewById(R.id.dateBirthField);
        EditText heightField = findViewById(R.id.heightField);
        EditText weightField = findViewById(R.id.weightField);
        EditText bloodTypeField = findViewById(R.id.bloodTypeField);
        EditText medicalConditionsField = findViewById(R.id.medicalConditionsField);
        EditText allergiesField = findViewById(R.id.allergiesField);
        EditText medicationsField = findViewById(R.id.medicationsField);

        ImageButton btnBack = findViewById(R.id.BtnBack);
        ImageButton editButton = findViewById(R.id.btnAddProfilePicture);
        ImageButton btnAddProfilePicture = findViewById(R.id.btnAddProfilePicture);
        btnAddProfilePicture.setOnClickListener(v -> openImagePicker());


        // Populate fields with data passed from Profile activity
        Intent intent = getIntent();
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
        findViewById(R.id.BtnUpdateEditedDetails).setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("NAME", nameField.getText().toString());
            resultIntent.putExtra("DATE_OF_BIRTH", dateOfBirthField.getText().toString());
            resultIntent.putExtra("HEIGHT", heightField.getText().toString());
            resultIntent.putExtra("WEIGHT", weightField.getText().toString());
            resultIntent.putExtra("BLOOD_TYPE", bloodTypeField.getText().toString());
            resultIntent.putExtra("MEDICAL_CONDITION", medicalConditionsField.getText().toString());
            resultIntent.putExtra("ALLERGIES", allergiesField.getText().toString());
            resultIntent.putExtra("MEDICATION", medicationsField.getText().toString());

            // Display a success message
            Toast.makeText(this, "Successfully updated.", Toast.LENGTH_SHORT).show();

            // Return the result and finish the activity
            setResult(RESULT_OK, resultIntent);
            finish();
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
