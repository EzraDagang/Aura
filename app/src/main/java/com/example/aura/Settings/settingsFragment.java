package com.example.aura.Settings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aura.LoginAndSignUp.LoginAndSignUpActivity;
import com.example.aura.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class settingsFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private TextView TVUsername, TVEmail, TVPhoneNumber, TVRole;
    private ImageView profilePicture;

    public settingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        profilePicture = view.findViewById(R.id.profilePicture);
        TVUsername = view.findViewById(R.id.TVName);  // This TextView shows username
        TVEmail = view.findViewById(R.id.ETEmail);    // This TextView shows email
        TVPhoneNumber = view.findViewById(R.id.ETPhoneNumber); // This TextView shows phone number
        TVRole = view.findViewById(R.id.TVRole); // This TextView shows role
        ImageView EditProfile = view.findViewById(R.id.EditIcon);
        Button BTNLogOut = view.findViewById(R.id.BTNLogOut);

        // Fetch user data from Firebase
        loadUserData();

        // Navigate to Edit Profile screen
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.toEditProfile);
            }
        });

        // Handle log out
        BTNLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(requireContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), LoginAndSignUpActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    private void loadUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Set email
            TVEmail.setText(user.getEmail());

            // Fetch user data from Firestore
            firestore.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve username, phone number, and role from Firestore
                            String username = documentSnapshot.getString("username");
                            String phoneNum = documentSnapshot.getString("phoneNum");
                            String role = documentSnapshot.getString("role");

                            // Set data to UI elements
                            TVUsername.setText(username);
                            TVPhoneNumber.setText(phoneNum);
                            TVRole.setText(role);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors
                        Toast.makeText(requireContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                    });


            }
        }
    }

