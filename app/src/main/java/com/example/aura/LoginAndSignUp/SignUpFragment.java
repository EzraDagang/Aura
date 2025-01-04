package com.example.aura.LoginAndSignUp;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aura.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText signupUser, signupEmail, signupPasswordConfirm, signupPassword, signupPhoneNumber;
    private RadioGroup roleGroup;
    private Button signupButton;
    private TextView loginRedirectText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        signupUser = view.findViewById(R.id.ETUsername);
        signupEmail = view.findViewById(R.id.ETEmail);
        signupPasswordConfirm = view.findViewById(R.id.ETReconfirmPassword);
        signupPassword = view.findViewById(R.id.ETPassword);
        signupPhoneNumber = view.findViewById(R.id.ETPhoneNumber); // Add phone number input field
        roleGroup = view.findViewById(R.id.roleGroup); // Replace "roleGroup" with the actual ID in your layout
        signupButton = view.findViewById(R.id.BTNSignUp);
        loginRedirectText = view.findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(v -> {

            String user = signupUser.getText().toString().trim();
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            String confirmPassword = signupPasswordConfirm.getText().toString().trim();
            String phoneNumber = signupPhoneNumber.getText().toString().trim();
            int selectedRoleId = roleGroup.getCheckedRadioButtonId();

            // Validate input fields
            if(user.isEmpty()){
                signupUser.setError("User cannot be empty");
                return;
            }
            if (email.isEmpty()) {
                signupEmail.setError("Email cannot be empty");
                return;
            }
            if (password.isEmpty()) {
                signupPassword.setError("Password cannot be empty");
                return;
            }
            if (!password.equals(confirmPassword)) {
                signupPasswordConfirm.setError("Password does not match");
                return;
            }
            if (phoneNumber.isEmpty()) {
                signupPhoneNumber.setError("Phone number cannot be empty");
                return;
            }
            if (selectedRoleId == -1) {
                Toast.makeText(requireContext(), "Please select a role (mentee or mentor)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Determine the role based on the selected RadioButton
            String role;
            if (selectedRoleId == R.id.RBMentee) {
                role = "Mentee";
            } else if (selectedRoleId == R.id.RBMentor) {
                role = "Mentor";
            } else {
                Toast.makeText(requireContext(), "Invalid role selection", Toast.LENGTH_SHORT).show();
                return;
            }

            // Proceed with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();

                        // Prepare the user data for Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Create the Firestore user data map
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("username", user); // Adjust this if there’s a separate username field
                        userData.put("email", email);
                        userData.put("phoneNum", phoneNumber); // Save phone number
                        userData.put("password", password); // Save password
                        userData.put("role", role); // Save the selected role
                        userData.put("enrollCourses", new ArrayList<>()); // Initialize an empty list for enrolled courses

                        // Save user data into Firestore
                        db.collection("users").document(userId).set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(view).navigate(R.id.toEmailVerification);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(requireContext(), "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        loginRedirectText.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.toLoginFromSignUp));

        LinearLayout backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getView());
            navController.popBackStack();
        });
    }
}
