package com.example.aura.Settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.aura.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private ImageView profilePicture, backButton, editIcon;
    private EditText ETName, ETEmail, ETPhoneNumber;
    private Button BTNUpdateProfile;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private Uri selectedImageUri;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize views
        backButton = view.findViewById(R.id.backButton);
        profilePicture = view.findViewById(R.id.profilePicture);
        editIcon = view.findViewById(R.id.EditIcon);
        ETName = view.findViewById(R.id.ETName);
        ETEmail = view.findViewById(R.id.ETEmail);
        ETPhoneNumber = view.findViewById(R.id.ETPhoneNumber);
        BTNUpdateProfile = view.findViewById(R.id.BTNUpdateProfile);

        // Fetch user data from the database
        loadUserData();

        // Back button click listener
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack();
        });

        // Profile picture edit click listener
        editIcon.setOnClickListener(v -> openImagePicker());

        // Update profile button click listener
        BTNUpdateProfile.setOnClickListener(v -> updateUserProfile());
    }

    private void loadUserData() {
        String userId = auth.getCurrentUser().getUid();

        // Fetch user data from Firestore
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Populate fields with user data
                String username = documentSnapshot.getString("username");
                String email = documentSnapshot.getString("email");
                String phoneNum = documentSnapshot.getString("phoneNum");
                String profilePicUrl = documentSnapshot.getString("profilePicture");

                ETName.setText(username);
                ETEmail.setText(email);
                ETPhoneNumber.setText(phoneNum);

                // Load profile picture with Picasso
                if (!TextUtils.isEmpty(profilePicUrl)) {
                    Picasso.get().load(profilePicUrl).into(profilePicture);
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            Log.e("EditProfileFragment", "Error fetching user data", e);
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profilePicture.setImageURI(selectedImageUri);
        }
    }

    private void updateUserProfile() {
        String userId = auth.getCurrentUser().getUid();
        String name = ETName.getText().toString().trim();
        String email = ETEmail.getText().toString().trim();
        String phone = ETPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("username", name);
        updates.put("email", email);
        updates.put("phoneNum", phone);

        if (selectedImageUri != null) {
            // Upload image to Firebase Storage first
            uploadImageToStorage(selectedImageUri, url -> {
                updates.put("profilePicture", url);
                saveUpdatesToDatabase(userId, updates); // Replace the entire document
            });
        } else {
            saveUpdatesToDatabase(userId, updates); // Replace the entire document
        }
    }

    private void saveUpdatesToDatabase(String userId, Map<String, Object> updates) {
        db.collection("users").document(userId)
                .set(updates) // Overwrites the entire document
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                    Log.e("EditProfileFragment", "Error updating profile", e);
                });
    }

    private void uploadImageToStorage(Uri imageUri, OnImageUploadCallback callback) {
        // Implement Firebase Storage logic here to upload image and return URL
        // Call callback.onSuccess(uploadedImageUrl) once upload is complete
    }

    interface OnImageUploadCallback {
        void onSuccess(String uploadedImageUrl);
    }
}
