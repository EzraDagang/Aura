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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPasswordConfirm, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        signupEmail = view.findViewById(R.id.ETEmail);
        signupPasswordConfirm = view.findViewById(R.id.ETReconfirmPassword);
        signupPassword = view.findViewById(R.id.ETPassword);
        signupButton = view.findViewById(R.id.BTNSignUp);
        loginRedirectText = view.findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String pass2 = signupPasswordConfirm.getText().toString().trim();

                if(user.isEmpty()){
                    signupEmail.setError("Email cannot be empty");
                }
                if(pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                }
                if(pass.length() < 6){
                    signupPassword.setError("Password must be at least 6 characters long");
                }
                if(!(pass.equals(pass2))) {
                    signupPasswordConfirm.setError("Password does not match");
                }

                else{
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String userId = auth.getCurrentUser().getUid();

                                // Prepare the user data for Firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                // Create the Firestore user data map
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("username", user); // You can adjust this to get a separate username field
                                userData.put("email", user);
                                userData.put("password", pass); // Storing raw passwords is not secure; hash it if possible
                                userData.put("phoneNum", ""); // Add a placeholder if no phone number is collected
                                userData.put("enrollCourses", new ArrayList<>()); // Initialize an empty list for enrolled courses

                                // Save user data into Firestore
                                db.collection("users").document(userId).set(userData)
                                        .addOnSuccessListener(aVoid -> {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("userEmail", user);
                                            Toast.makeText(requireContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                            Navigation.findNavController(view).navigate(R.id.toEmailVerification, bundle);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(requireContext(), "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(requireContext(), "Sign Up Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.toLoginFromSignUp);
            }
        });

        LinearLayout backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getView());
                navController.popBackStack();
            }
        });
    }
}