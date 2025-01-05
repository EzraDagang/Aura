package com.example.aura.onBoarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.aura.LoginAndSignUp.LoginAndSignUpActivity;
import com.example.aura.R;

public class OnBoardingPage2Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OnBoardingPage2Fragment() {
        // Required empty public constructor
    }

    public static OnBoardingPage2Fragment newInstance(String param1, String param2) {
        OnBoardingPage2Fragment fragment = new OnBoardingPage2Fragment();
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
        return inflater.inflate(R.layout.fragment_onboarding2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Next button to navigate to Page 3
        ImageView BtnNextPage2 = view.findViewById(R.id.BtnNextPage2);
        BtnNextPage2.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.ToPage3)
        );

        // Change the status bar and navigation bar colors
        setStatusBarAndNavigationBarColors();

        // Skip button to jump to the Login and Sign-Up screen
        Button BtnSkip = view.findViewById(R.id.BtnSkip);
        BtnSkip.setOnClickListener(v -> {
            // Save to SharedPreferences that onboarding has been completed
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("onboardingShown", true);
            editor.apply(); // Apply changes to SharedPreferences

            // Navigate to LoginAndSignUpActivity
            Intent intent = new Intent(getActivity(), LoginAndSignUpActivity.class);
            startActivity(intent);

            // Optionally finish the current activity to prevent going back
            requireActivity().finish();
        });
    }

    /**
     * Sets the status bar and navigation bar colors for the fragment.
     */
    private void setStatusBarAndNavigationBarColors() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.pink)); // Change to pink
            window.setNavigationBarColor(getResources().getColor(R.color.black)); // Change to black
        }
    }
}
