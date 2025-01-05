package com.example.aura.onBoarding;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.aura.LoginAndSignUp.LoginAndSignUpActivity;
import com.example.aura.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnBoardingPage3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnBoardingPage3Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OnBoardingPage3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnBoardingPage3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnBoardingPage3Fragment newInstance(String param1, String param2) {
        OnBoardingPage3Fragment fragment = new OnBoardingPage3Fragment();
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
        return inflater.inflate(R.layout.fragment_onboarding3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView BtnNextPage4 = view.findViewById(R.id.BtnNextPage4);

        BtnNextPage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // Set onboardingShown flag to true
                editor.putBoolean("onboardingShown", true);
                editor.apply();  // Apply the changes to SharedPreferences
                Intent intent = new Intent(getActivity(), LoginAndSignUpActivity.class);
                startActivity(intent);
            }
        });

        ImageView BtnBackPage2 = view.findViewById(R.id.BtnBackPage2);

        BtnBackPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.BackToPage2);
            }
        });
    }
}