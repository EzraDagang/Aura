package com.example.aura.LoginAndSignUp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private EditText signupEmail, signupPasswordConfirm, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set system bar colors
        setStatusBarAndNavigationBarColors();

        auth = FirebaseAuth.getInstance();
        signupEmail = view.findViewById(R.id.ETEmail);
        signupPasswordConfirm = view.findViewById(R.id.ETReconfirmPassword);
        signupPassword = view.findViewById(R.id.ETPassword);
        signupButton = view.findViewById(R.id.BTNSignUp);
        loginRedirectText = view.findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(v -> handleSignUp(view));

        loginRedirectText.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.toLoginFromSignUp));

        LinearLayout backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack();
        });
    }

    /**
     * Handles the sign-up process.
     *
     * @param view The current view.
     */
    private void handleSignUp(View view) {
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String confirmPassword = signupPasswordConfirm.getText().toString().trim();

        if (validateInputs(email, password, confirmPassword)) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            saveUserDataToFirestore(view, email, password);
                        } else {
                            Toast.makeText(requireContext(), "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * Validates user input for the sign-up form.
     *
     * @param email           The email address entered by the user.
     * @param password        The password entered by the user.
     * @param confirmPassword The password confirmation entered by the user.
     * @return True if all inputs are valid, false otherwise.
     */
    private boolean validateInputs(String email, String password, String confirmPassword) {
        if (email.isEmpty()) {
            signupEmail.setError("Email cannot be empty");
            signupEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            signupPassword.setError("Password cannot be empty");
            signupPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            signupPassword.setError("Password must be at least 6 characters long");
            signupPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            signupPasswordConfirm.setError("Passwords do not match");
            signupPasswordConfirm.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Saves user data to Firestore after successful authentication.
     *
     * @param view     The current view.
     * @param email    The email address of the user.
     * @param password The password of the user.
     */
    private void saveUserDataToFirestore(View view, String email, String password) {
        String userId = auth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", email); // Optional: Replace with actual username if collected
        userData.put("email", email);
        userData.put("phoneNum", ""); // Placeholder for phone number
        userData.put("enrollCourses", new ArrayList<>()); // Empty list for enrolled courses

        db.collection("users").document(userId).set(userData)
                .addOnSuccessListener(aVoid -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("userEmail", email);
                    Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.toEmailVerification, bundle);
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * Sets the status bar and navigation bar colors to match the app's theme.
     */
    private void setStatusBarAndNavigationBarColors() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();

            // Make the status bar and navigation bar fully transparent
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );

            // Change the status bar and navigation bar colors
            window.setStatusBarColor(getResources().getColor(R.color.pink)); // Set to pink
            window.setNavigationBarColor(getResources().getColor(R.color.pink)); // Set to pink
        }
    }
}
