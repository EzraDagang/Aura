package com.example.aura.LoginAndSignUp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.aura.Courses.DiscoverScreen;
import com.example.aura.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginAndSignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginAndSignUpFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LoginAndSignUpFragment() {
        // Required empty public constructor
    }

    public static LoginAndSignUpFragment newInstance(String param1, String param2) {
        LoginAndSignUpFragment fragment = new LoginAndSignUpFragment();
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
        return inflater.inflate(R.layout.fragment_login_and_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Change status bar and navigation bar colors
        setStatusBarAndNavigationBarColors();

        Button BtnSignUp = view.findViewById(R.id.BtnSignUp);
        Button BtnLogin = view.findViewById(R.id.BtnLogin);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);

        if (user != null && rememberMe) {
            // User is logged in, redirect to DiscoverScreen
            startActivity(new Intent(getActivity(), DiscoverScreen.class));
            requireActivity().finish();
        } else {
            // User is not logged in, show login/signup options
            BtnSignUp.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.toSignUp));

            BtnLogin.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.toLogin));
        }
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

            // Change the status bar and navigation bar colors to pink
            window.setStatusBarColor(getResources().getColor(R.color.pink)); // Set to pink
            window.setNavigationBarColor(getResources().getColor(R.color.pink)); // Set to pink
        }
    }
}
