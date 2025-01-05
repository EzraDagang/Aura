package com.example.aura.LoginAndSignUp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aura.Courses.DiscoverScreen;
import com.example.aura.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText, forgetPassword;
    private Button loginButton;
    private CheckBox CBRememberMe;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Customize status bar and navigation bar colors
        setStatusBarAndNavigationBarColors();

        auth = FirebaseAuth.getInstance();
        loginEmail = view.findViewById(R.id.ETEmail);
        loginPassword = view.findViewById(R.id.ETPassword);
        loginButton = view.findViewById(R.id.BTNLogin);
        signupRedirectText = view.findViewById(R.id.signUpRedirectText);
        forgetPassword = view.findViewById(R.id.ForgotPassword);
        CBRememberMe = view.findViewById(R.id.CBRememberMe);

        // Handle login button click
        loginButton.setOnClickListener(v -> handleLogin(view));

        // Redirect to sign-up screen
        signupRedirectText.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.toSignUpFromLogin));

        // Handle forget password redirection
        forgetPassword.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.toForgetPassword));

        // Back button handling
        LinearLayout backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack();
        });
    }

    /**
     * Handles the login functionality.
     *
     * @param view The current view.
     */
    private void handleLogin(View view) {
        String email = loginEmail.getText().toString().trim();
        String pass = loginPassword.getText().toString().trim();

        if (validateInputs(email, pass)) {
            auth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = auth.getCurrentUser();

                        if (user != null && user.isEmailVerified()) {
                            boolean rememberMe = CBRememberMe.isChecked();

                            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("rememberMe", rememberMe);
                            editor.apply();

                            Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireContext(), DiscoverScreen.class);
                            startActivity(intent);
                            requireActivity().finish();
                        } else {
                            auth.signOut();
                            Toast.makeText(requireContext(), "Please verify your email before logging in.", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * Validates the user inputs for login.
     *
     * @param email The entered email.
     * @param password The entered password.
     * @return True if inputs are valid, false otherwise.
     */
    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            loginEmail.setError("Email cannot be empty");
            loginEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please enter a valid email");
            loginEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            loginPassword.requestFocus();
            return false;
        }

        return true;
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
