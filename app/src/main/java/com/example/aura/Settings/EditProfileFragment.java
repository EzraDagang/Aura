package com.example.aura.Settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aura.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        ImageView BackButton = view.findViewById(R.id.backButton);
        Button BTNUpdateProfile = view.findViewById(R.id.BTNUpdateProfile);
        EditText etName = view.findViewById(R.id.ETName);
        EditText etEmail = view.findViewById(R.id.ETEmail);
        EditText etPhoneNumber = view.findViewById(R.id.ETPhoneNumber);

        // Firebase setup
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = auth.getCurrentUser().getUid();

        // Fetch user data from Firestore
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve data and set to EditText fields
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");
                        String phoneNum = documentSnapshot.getString("phoneNum");
                        String role = documentSnapshot.getString("role");

                        etName.setText(username);
                        etEmail.setText(email);
                        etPhoneNumber.setText(phoneNum);
                    } else {
                        Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to fetch user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Back button functionality
        BackButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getView());
            navController.popBackStack();
        });

        // Update profile button functionality
        BTNUpdateProfile.setOnClickListener(v -> {
            // Add functionality for updating user data
            String updatedName = etName.getText().toString().trim();
            String updatedEmail = etEmail.getText().toString().trim();
            String updatedPhoneNumber = etPhoneNumber.getText().toString().trim();

            // Validate inputs before updating
            if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedPhoneNumber.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update Firestore with new data
            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("username", updatedName);
            updatedData.put("email", updatedEmail);
            updatedData.put("phoneNum", updatedPhoneNumber);

            db.collection("users").document(userId).update(updatedData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}