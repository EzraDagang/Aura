package com.example.aura.LoginAndSignUp;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aura.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private TextView emailTextView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EmailVerificationFragment() {
        // Required empty public constructor
    }

    public static EmailVerificationFragment newInstance(String param1, String param2) {
        EmailVerificationFragment fragment = new EmailVerificationFragment();
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
        return inflater.inflate(R.layout.fragment_email_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Change the status bar and navigation bar colors
        setStatusBarAndNavigationBarColors();

        firebaseAuth = FirebaseAuth.getInstance();
        emailTextView = view.findViewById(R.id.TVEmail);
        Button BTNVerify = view.findViewById(R.id.BTNVerify);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        emailTextView.setText(getArguments().getString("userEmail"));

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Verification email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Failed to send email: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "No user logged in", Toast.LENGTH_SHORT).show();
        }

        BTNVerify.setOnClickListener(v -> {
            if (user != null) {
                user.reload().addOnCompleteListener(task -> {
                    if (user.isEmailVerified()) {
                        Toast.makeText(requireContext(), "Email is verified!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Navigation.findNavController(view).navigate(R.id.toLoginFromEmailVerification);
                    } else {
                        Toast.makeText(requireContext(), "Email is not verified.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        LinearLayout backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            NavController navController = Navigation.findNavController(view);
            navController.popBackStack();
        });
    }

    /**
     * Sets the status bar and navigation bar colors to pink.
     */
    private void setStatusBarAndNavigationBarColors() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();

            // Make the status bar and navigation bar fully transparent
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

            // Change the status bar and navigation bar colors to pink
            window.setStatusBarColor(getResources().getColor(R.color.pink)); // Set to pink
            window.setNavigationBarColor(getResources().getColor(R.color.pink)); // Set to pink
        }
    }
}
