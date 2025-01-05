package com.example.aura.onBoarding;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.aura.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnBoardingPage1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnBoardingPage1Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OnBoardingPage1Fragment() {
        // Required empty public constructor
    }

    public static OnBoardingPage1Fragment newInstance(String param1, String param2) {
        OnBoardingPage1Fragment fragment = new OnBoardingPage1Fragment();
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
        return inflater.inflate(R.layout.fragment_onboarding1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Button to navigate to Page 2
        Button BtnStartPage1 = view.findViewById(R.id.BtnStartPage1);

        BtnStartPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.ToPage2);
            }
        });

        // Change the status bar and navigation bar colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();

            // Make status bar and navigation bar fully transparent
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

            // Change status bar and navigation bar colors
            window.setStatusBarColor(getResources().getColor(R.color.pink)); // Set status bar to pink
            window.setNavigationBarColor(getResources().getColor(R.color.black)); // Set navigation bar to black
        }
    }
}
