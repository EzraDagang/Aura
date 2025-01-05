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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aura.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        signupUser = view.findViewById(R.id.ETUsername);
        signupEmail = view.findViewById(R.id.ETEmail);
        signupPasswordConfirm = view.findViewById(R.id.ETReconfirmPassword);
        signupPassword = view.findViewById(R.id.ETPassword);
        signupPhoneNumber = view.findViewById(R.id.ETPhoneNumber);
        roleGroup = view.findViewById(R.id.roleGroup);
        signupButton = view.findViewById(R.id.BTNSignUp);
        loginRedirectText = view.findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(v -> {
            String user = signupUser.getText().toString().trim();
            String email = signupEmail.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();
            String confirmPassword = signupPasswordConfirm.getText().toString().trim();
            String phoneNumber = signupPhoneNumber.getText().toString().trim();
            int selectedRoleId = roleGroup.getCheckedRadioButtonId();

            if (user.isEmpty()) {
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

            String role = selectedRoleId == R.id.RBMentee ? "Mentee" : "Mentor";

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("username", user);
                        userData.put("email", email);
                        userData.put("phoneNum", phoneNumber);
                        userData.put("role", role);

                        db.collection("users").document(userId).set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    if (role.equals("Mentee")) {
                                        db.collection("users")
                                                .document(userId)
                                                .collection("enrollCourses")
                                                .add(new HashMap<>())
                                                .addOnSuccessListener(subcollectionSuccess -> {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("userEmail", email);
                                                    Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                                    Navigation.findNavController(view).navigate(R.id.toEmailVerification, bundle);
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to initialize enrollCourses: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                    } else if (role.equals("Mentor")) {
                                        db.collection("users")
                                                .document(userId)
                                                .collection("myCourses")
                                                .add(new HashMap<>())
                                                .addOnSuccessListener(subcollectionSuccess -> {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("userEmail", email);
                                                    Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                                    Navigation.findNavController(view).navigate(R.id.toEmailVerification, bundle);
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to initialize myCourses: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                    }
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
