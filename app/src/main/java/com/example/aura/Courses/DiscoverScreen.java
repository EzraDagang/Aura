package com.example.aura.Courses;
import com.example.aura.Courses.DiscoverScreen;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.aura.Emergency;
import com.example.aura.R;
import com.example.aura.Settings.SettingsActivity;
import com.example.aura.databinding.ActivityDiscoverScreen2Binding;
import com.example.aura.education;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscoverScreen extends AppCompatActivity {
    private LinearLayout courseContainer;
    private ActivityDiscoverScreen2Binding binding;
    // List to store all courses
    private List<CustomModel> allCourses;
    private List<CustomModel> currentRecommendations;
    private List<CustomModel> recommendationCourses;
    private Button myCoursesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_screen2);


        // Initialize UI elements
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        TextView tvViewAll = findViewById(R.id.tvViewAll);
        Button careerAdvancementButton = findViewById(R.id.careerAdvancementButton);
        Button personalGrowthButton = findViewById(R.id.personalGrowthButton);
        Button myCoursesButton = findViewById(R.id.myCourseButton);
        Button refreshButton = findViewById(R.id.refreshButton);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Discover");
        }

        // Navigation bar setup
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                Log.d("Navigation", "Home selected");

                return true;
            } else if (itemId == R.id.nav_phone) {
                Log.d("Navigation", "Phone selected");
                startActivity(new Intent(DiscoverScreen.this, Emergency.class));
                return true;
            } else if (itemId == R.id.nav_notifications) {
                Log.d("Navigation", "Notifications selected");
                startActivity(new Intent(DiscoverScreen.this, education.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                Log.d("Navigation", "Profile selected");
                startActivity(new Intent(DiscoverScreen.this, SettingsActivity.class));
                return true;
            }
            return false;
        });


        // Set up other click listeners
        tvViewAll.setOnClickListener(v -> {
            Intent intent = new Intent(DiscoverScreen.this, MainActivity.class);
            startActivity(intent);
        });

        careerAdvancementButton.setOnClickListener(v -> {
            String categoryTitle = "Career Advancement";

            // Query Firestore to get the document ID for "Career Advancement"
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("categories")
                    .whereEqualTo("categoryTitle", categoryTitle)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            // Assume the first document matches the category
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String categoryDocumentId = document.getId(); // Get the document ID

                            // Navigate to CourseListActivity with the fetched document ID
                            Intent intent = new Intent(DiscoverScreen.this, CourseListActivity.class);
                            intent.putExtra("categoryTitle", categoryTitle);
                            intent.putExtra("documentId", categoryDocumentId);
                            startActivity(intent);
                        } else {
                            // Handle case where no document is found
                            Toast.makeText(DiscoverScreen.this, "Category not found for: " + categoryTitle, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors during Firestore query
                        Toast.makeText(DiscoverScreen.this, "Error fetching category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        personalGrowthButton.setOnClickListener(v -> {
            String categoryTitle = "Personal Growth";

            // Query Firestore to get the document ID for "Career Advancement"
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("categories")
                    .whereEqualTo("categoryTitle", categoryTitle)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            // Assume the first document matches the category
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String categoryDocumentId = document.getId(); // Get the document ID

                            // Navigate to CourseListActivity with the fetched document ID
                            Intent intent = new Intent(DiscoverScreen.this, CourseListActivity.class);
                            intent.putExtra("categoryTitle", categoryTitle);
                            intent.putExtra("documentId", categoryDocumentId);
                            startActivity(intent);
                        } else {
                            // Handle case where no document is found
                            Toast.makeText(DiscoverScreen.this, "Category not found for: " + categoryTitle, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors during Firestore query
                        Toast.makeText(DiscoverScreen.this, "Error fetching category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });


        // Career Advancement button navigation logic
//        careerAdvancementButton.setOnClickListener(v -> {
//            // Navigate to Career Advancement course list
//            Intent intent = new Intent(DiscoverScreen.this, CourseListActivity.class);
//            intent.putExtra("category", "Career Advancement"); // Pass category information if needed
//            startActivity(intent);
//        });
//
//
//        // Personal Growth button navigation logic
//        personalGrowthButton.setOnClickListener(v -> {
//            // Navigate to Personal Growth course list
//            Intent intent = new Intent(DiscoverScreen.this, CourseListActivity.class);
//            intent.putExtra("category", "Personal Growth"); // Pass category information if needed
//            startActivity(intent);
//        });

        // My Courses button navigation logic
        myCoursesButton.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

            if (currentUser != null) {
                String userId = currentUser.getUid();

                // Fetch user details from Firestore
                firebaseFirestore.collection("users").document(userId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String role = documentSnapshot.getString("role");

                                Log.d("DiscoverScreen", "User role retrieved: " + role);

                                if ("Mentor".equalsIgnoreCase(role)) {
                                    Log.d("DiscoverScreen", "Navigating to MentorMyCourseActivity");
                                    Intent intent = new Intent(DiscoverScreen.this, MentorMyCourseActivity.class);
                                    startActivity(intent);
                                } else if ("Mentee".equalsIgnoreCase(role)) {
                                    Log.d("DiscoverScreen", "Navigating to MyCoursesActivity");
                                    Intent intent = new Intent(DiscoverScreen.this, MyCoursesActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(DiscoverScreen.this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.e("DiscoverScreen", "User document does not exist in Firestore");
                                Toast.makeText(DiscoverScreen.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("DiscoverScreen", "Error fetching user data: " + e.getMessage());
                            Toast.makeText(DiscoverScreen.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Log.e("DiscoverScreen", "User not authenticated");
                Toast.makeText(DiscoverScreen.this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        });



        // Refresh Recommendations button logic
        refreshButton.setOnClickListener(v -> {
            // Refresh recommendations by updating the course list or UI
            updateRecommendations();
            Toast.makeText(DiscoverScreen.this, "Recommendations refreshed!", Toast.LENGTH_SHORT).show();
        });

        /*
        // Inflate the layout using view binding
        binding = ActivityDiscoverScreen2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;

        if (bottomNavigationView == null) {
            Log.e("DiscoverScreen", "BottomNavigationView is null");
        } else {
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    // Stay on the Discover page
                    return true;
                } else if (itemId == R.id.nav_phone) {
                    // Navigate to Emergency Activity
                    Log.d("DiscoverScreen", "Navigating to Emergency");
                    Intent intent = new Intent(DiscoverScreen.this, Emergency.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    // Handle notifications navigation
                    Intent intent = new Intent(DiscoverScreen.this, education.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    // Handle profile navigation
                    Intent intent = new Intent(DiscoverScreen.this, Emergency.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            });
        }

        // Other initialization code (UI elements, toolbar, etc.)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Discover");
        }

         */


/*
        // Inflate the layout using view binding
        binding = ActivityDiscoverScreen2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.tvViewAll.setOnClickListener(v -> {
            Intent intent = new Intent(DiscoverScreen.this, MainActivity.class);
            startActivity(intent);
        });

        // Set up the Career Advancement button
        binding.careerAdvancementButton.setOnClickListener(v ->
                NavigationUtil.navigateToCourseList(DiscoverScreen.this, "Career Advancement")
        );

        // Set up the Personal Growth button
        binding.personalGrowthButton.setOnClickListener(v ->
                NavigationUtil.navigateToCourseList(DiscoverScreen.this, "Personal Growth")
        );


 */
        // Set click listener for "My Courses" button

        /*

        // Set up the View All button to navigate to MainActivity
        TextView viewAll = findViewById(R.id.tvViewAll);
        viewAll.setOnClickListener(v -> {
            Intent intent = new Intent(DiscoverScreen.this, MainActivity.class);
            startActivity(intent);
        });

        // Retrieve all courses
        allCourses = consolidateAllCourses();


         */
        // Set up "Refresh Recommendations" button

        // binding.refreshButton.setOnClickListener(v -> updateRecommendations());

        // Initialize allCourses (ensure it's not null)
        if (allCourses == null) {
            allCourses = consolidateAllCourses();
        }


        // Update the Recommendation Section
        updateRecommendations();
    }

    private List<CustomModel> consolidateAllCourses() {
        List<CustomModel> allCourses = new ArrayList<>();
        allCourses.addAll(getCourseForCategory("Career Advancement"));
        allCourses.addAll(getCourseForCategory("Self-Development"));
        allCourses.addAll(getCourseForCategory("Personal Growth"));
        allCourses.addAll(getCourseForCategory("Health and Wellness"));
        allCourses.addAll(getCourseForCategory("Advocacy and Strength"));
        allCourses.addAll(getCourseForCategory("Relationship and Support"));
        return allCourses;
    }


    private void updateRecommendations() {
        if (recommendationCourses == null) {
            recommendationCourses = new ArrayList<>();
        }

        // Clear existing recommendations
        recommendationCourses.clear();

        // Shuffle all courses and pick 4
        Collections.shuffle(allCourses);
        for (int i = 0; i < Math.min(4, allCourses.size()); i++) {
            recommendationCourses.add(allCourses.get(i));
        }

        // Update UI
        for (int i = 0; i < recommendationCourses.size(); i++) {
            CustomModel course = recommendationCourses.get(i);
            switch (i) {
                case 0:
                    displayCourse(course, R.id.image1, R.id.title1, R.id.lesson1, R.id.rating1, R.id.card1);
                    break;
                case 1:
                    displayCourse(course, R.id.image2, R.id.title2, R.id.lesson2, R.id.rating2, R.id.card2);
                    break;
                case 2:
                    displayCourse(course, R.id.image3, R.id.title3, R.id.lesson3, R.id.rating3, R.id.card3);
                    break;
                case 3:
                    displayCourse(course, R.id.image4, R.id.title4, R.id.lesson4, R.id.rating4, R.id.card4);
                    break;
            }
        }
    }



    private void displayCourse(CustomModel course, int imageViewId, int titleViewId, int lessonViewId, int ratingViewId,int cardViewId) {
        ImageView imageView = findViewById(imageViewId);
        TextView titleView = findViewById(titleViewId);
        TextView lessonView = findViewById(lessonViewId);
        TextView ratingView = findViewById(ratingViewId);
        View cardView = findViewById(cardViewId);

        imageView.setImageResource(course.getImage());
        titleView.setText(course.getTitle());
        lessonView.setText(course.getLesson());
        ratingView.setText(course.getRating());

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(DiscoverScreen.this, MainActivity2.class);
            intent.putExtra("courseDetails", course);
            startActivity(intent);
        });
    }

    private ArrayList<CustomModel> getCourseForCategory(String category) {
        ArrayList<CustomModel> courses = new ArrayList<>();
        List<String> modules = new ArrayList<>();
        List<List<String>> lessons = new ArrayList<>();

        switch (category) {
            case "Career Advancement":
                // Course 1: Resume Building
                modules = new ArrayList<>();
                modules.add("Module 1: Resume Writing");
                modules.add("Module 2: Formatting");

                // Lesson titles and content for Resume Building
                lessons = new ArrayList<>();
                List<String> resumeModule1 = new ArrayList<>();
                resumeModule1.add("Lesson 1: Writing Basics");
                resumeModule1.add("Lesson 2: Tailoring to Job Descriptions");
                List<String> resumeModule2 = new ArrayList<>();
                resumeModule2.add("Lesson 1: Formatting Tips");
                resumeModule2.add("Lesson 2: Finalizing and Proofreading");
                lessons.add(resumeModule1);
                lessons.add(resumeModule2);

                List<List<String>> resumeLessonContents = new ArrayList<>();
                List<String> resumeModule1Content = new ArrayList<>();
                resumeModule1Content.add("Learn how to write a compelling resume that highlights your skills and experience.");
                resumeModule1Content.add("Understand how to tailor your resume to specific job descriptions for better results.");
                List<String> resumeModule2Content = new ArrayList<>();
                resumeModule2Content.add("Formatting tips to make your resume visually appealing and professional.");
                resumeModule2Content.add("How to proofread and finalize your resume for submission.");
                resumeLessonContents.add(resumeModule1Content);
                resumeLessonContents.add(resumeModule2Content);

                // Quiz questions for Resume Building
                List<QuizQuestion> resumeQuizQuestions = new ArrayList<>();
                resumeQuizQuestions.add(new QuizQuestion("What is the primary purpose of a resume?",
                        List.of("To highlight your skills and experience", "To show off your hobbies", "To write a story about your life"),
                        "To highlight your skills and experience"));
                resumeQuizQuestions.add(new QuizQuestion("What is important when tailoring a resume?",
                        List.of("Using a generic format", "Addressing the job description", "Ignoring keywords"),
                        "Addressing the job description"));

                courses.add(new CustomModel(R.drawable.resume, "Resume Building", "4 Lessons", "4.5 / 5.0",
                        "Learn how to craft an effective resume.",
                        "John Doe", "Career Mentor", R.drawable.profile_girl2,
                        modules, lessons, resumeLessonContents, resumeQuizQuestions, "https://youtu.be/Tt08KmFfIYQ?si=UP7MD-yFU8GYGPoN"));

                // Course 2: Interview Preparation
                modules = new ArrayList<>();
                modules.add("Module 1: Preparing for Interviews");
                modules.add("Module 2: Answering Questions");

                lessons = new ArrayList<>();
                List<String> interviewModule1 = new ArrayList<>();
                interviewModule1.add("Lesson 1: Researching the Company");
                interviewModule1.add("Lesson 2: Common Questions");
                List<String> interviewModule2 = new ArrayList<>();
                interviewModule2.add("Lesson 1: Behavioral Questions");
                interviewModule2.add("Lesson 2: Asking Questions");
                lessons.add(interviewModule1);
                lessons.add(interviewModule2);

                List<List<String>> interviewLessonContents = new ArrayList<>();
                List<String> interviewModule1Content = new ArrayList<>();
                interviewModule1Content.add("Learn how to research the company and understand their values.");
                interviewModule1Content.add("Prepare answers for the most commonly asked interview questions.");
                List<String> interviewModule2Content = new ArrayList<>();
                interviewModule2Content.add("Understand how to respond effectively to behavioral questions.");
                interviewModule2Content.add("Learn the importance of asking meaningful questions to the interviewer.");
                interviewLessonContents.add(interviewModule1Content);
                interviewLessonContents.add(interviewModule2Content);

                List<QuizQuestion> interviewQuizQuestions = new ArrayList<>();
                interviewQuizQuestions.add(new QuizQuestion("What is the first step in preparing for an interview?",
                        List.of("Wearing formal clothes", "Researching the company", "Memorizing answers"),
                        "Researching the company"));
                interviewQuizQuestions.add(new QuizQuestion("What type of questions should you ask at the end of an interview?",
                        List.of("Salary-related questions", "Meaningful questions about the role or company", "No questions"),
                        "Meaningful questions about the role or company"));

                courses.add(new CustomModel(R.drawable.interview, "Interview Preparation", "4 Lessons", "4.7 / 5.0",
                        "Master the art of preparing and excelling in interviews.",
                        "Emily Su", "Career Expert", R.drawable.profile_girl1,
                        modules, lessons, interviewLessonContents, interviewQuizQuestions, "https://youtu.be/HG68Ymazo18?si=1zbNfVSNdKXaVees"));

                // Add similar logic for Leadership Development, Networking, and other courses.


                // Course 3: Leadership Development
                modules = new ArrayList<>();
                modules.add("Module 1: Leadership Fundamentals");
                modules.add("Module 2: Team Management");

// Lesson titles and content for Leadership Development
                lessons = new ArrayList<>();
                List<String> leadershipModule1 = new ArrayList<>();
                leadershipModule1.add("Lesson 1: Understanding Leadership Styles");
                leadershipModule1.add("Lesson 2: Communication Skills");
                List<String> leadershipModule2 = new ArrayList<>();
                leadershipModule2.add("Lesson 1: Delegation Techniques");
                leadershipModule2.add("Lesson 2: Conflict Resolution");
                lessons.add(leadershipModule1);
                lessons.add(leadershipModule2);

                List<List<String>> leadershipLessonContents = new ArrayList<>();
                List<String> leadershipModule1Content = new ArrayList<>();
                leadershipModule1Content.add("Learn about different leadership styles and how they impact teams.");
                leadershipModule1Content.add("Understand effective communication techniques for leaders.");
                List<String> leadershipModule2Content = new ArrayList<>();
                leadershipModule2Content.add("Learn how to delegate tasks effectively to team members.");
                leadershipModule2Content.add("Discover methods to resolve conflicts and maintain team harmony.");
                leadershipLessonContents.add(leadershipModule1Content);
                leadershipLessonContents.add(leadershipModule2Content);

// Quiz questions for Leadership Development
                List<QuizQuestion> leadershipQuizQuestions = new ArrayList<>();
                leadershipQuizQuestions.add(new QuizQuestion("What is a key trait of effective leaders?",
                        List.of("Inflexibility", "Poor communication", "Empathy", "Indifference"),
                        "Empathy"));
                leadershipQuizQuestions.add(new QuizQuestion("Which leadership style focuses on team collaboration?",
                        List.of("Autocratic", "Laissez-faire", "Democratic", "Transactional"),
                        "Democratic"));
                leadershipQuizQuestions.add(new QuizQuestion("What is an important skill for conflict resolution?",
                        List.of("Ignoring the problem", "Active listening", "Blaming others", "Avoiding discussions"),
                        "Active listening"));

                courses.add(new CustomModel(R.drawable.leadership, "Leadership Development", "4 Lessons", "4.8 / 5.0",
                        "Learn to lead effectively and manage teams with confidence.",
                        "James Wong", "Life Mentor", R.drawable.profile_mentor_boy,
                        modules, lessons, leadershipLessonContents, leadershipQuizQuestions, "https://youtu.be/aUYSDEYdmzw?si=DI8YC9UhtJca0FGO"));

                // Course 4: Networking for Career Growth
                modules = new ArrayList<>();
                modules.add("Module 1: Building Connections");
                modules.add("Module 2: Leveraging Networks");

// Lesson titles and content for Networking
                lessons = new ArrayList<>();
                List<String> networkingModule1 = new ArrayList<>();
                networkingModule1.add("Lesson 1: Identifying Opportunities");
                networkingModule1.add("Lesson 2: Building Rapport");
                List<String> networkingModule2 = new ArrayList<>();
                networkingModule2.add("Lesson 1: Professional Platforms");
                networkingModule2.add("Lesson 2: Following Up");
                lessons.add(networkingModule1);
                lessons.add(networkingModule2);

                List<List<String>> networkingLessonContents = new ArrayList<>();
                List<String> networkingModule1Content = new ArrayList<>();
                networkingModule1Content.add("Learn how to identify networking opportunities in various settings.");
                networkingModule1Content.add("Understand how to build rapport and meaningful connections.");
                List<String> networkingModule2Content = new ArrayList<>();
                networkingModule2Content.add("Discover how to use professional platforms like LinkedIn effectively.");
                networkingModule2Content.add("Learn the importance of following up after networking events.");
                networkingLessonContents.add(networkingModule1Content);
                networkingLessonContents.add(networkingModule2Content);

                // Quiz questions for Networking for Career Growth
                List<QuizQuestion> networkingQuizQuestions = new ArrayList<>();
                networkingQuizQuestions.add(new QuizQuestion("What is the primary goal of networking?",
                        List.of("To make friends", "To find job opportunities", "To attend social events", "To collect business cards"),
                        "To find job opportunities"));
                networkingQuizQuestions.add(new QuizQuestion("Which platform is commonly used for professional networking?",
                        List.of("Instagram", "Facebook", "LinkedIn", "TikTok"),
                        "LinkedIn"));
                networkingQuizQuestions.add(new QuizQuestion("What is a good way to follow up after meeting someone at a networking event?",
                        List.of("Ignore them", "Send a thank-you email", "Call them repeatedly", "Post about them on social media"),
                        "Send a thank-you email"));

                courses.add(new CustomModel(R.drawable.networking, "Networking for Career Growth", "4 Lessons", "4.6 / 5.0",
                        "Master the art of networking to accelerate your career growth.",
                        "Qi Yi", "Professor", R.drawable.profile_mentor_girl,
                        modules, lessons, networkingLessonContents, networkingQuizQuestions, "https://youtu.be/-30m8D6gTrg?si=SHLmO9nL-kk3JUUn"));

                break;

            case "Self-Development":
                // Course 1: Time Management
                modules = new ArrayList<>();
                modules.add("Module 1: Basics of Time Management");
                modules.add("Module 2: Advanced Techniques");

                // Lesson titles and content for Time Management
                lessons = new ArrayList<>();
                List<String> timeManagementModule1 = new ArrayList<>();
                timeManagementModule1.add("Lesson 1: Understanding Prioritization");
                timeManagementModule1.add("Lesson 2: Setting SMART Goals");
                List<String> timeManagementModule2 = new ArrayList<>();
                timeManagementModule2.add("Lesson 1: Using Time Blocks Effectively");
                timeManagementModule2.add("Lesson 2: Minimizing Distractions");
                lessons.add(timeManagementModule1);
                lessons.add(timeManagementModule2);

                List<List<String>> timeManagementLessonContents = new ArrayList<>();
                List<String> timeManagementModule1Content = new ArrayList<>();
                timeManagementModule1Content.add("Learn how to prioritize tasks effectively to maximize productivity.");
                timeManagementModule1Content.add("Discover how to set Specific, Measurable, Achievable, Relevant, and Time-bound goals.");
                List<String> timeManagementModule2Content = new ArrayList<>();
                timeManagementModule2Content.add("Master the use of time blocking to organize your day effectively.");
                timeManagementModule2Content.add("Learn strategies to identify and reduce distractions in your workflow.");
                timeManagementLessonContents.add(timeManagementModule1Content);
                timeManagementLessonContents.add(timeManagementModule2Content);

                // Quiz questions for Time Management
                List<QuizQuestion> timeManagementQuizQuestions = new ArrayList<>();
                timeManagementQuizQuestions.add(new QuizQuestion("What is the main benefit of setting SMART goals?",
                        List.of("They are easier to achieve", "They help prioritize tasks", "They provide clarity and focus"),
                        "They provide clarity and focus"));
                timeManagementQuizQuestions.add(new QuizQuestion("Which technique is effective in organizing your daily schedule?",
                        List.of("Procrastinating tasks", "Time blocking", "Random task selection"),
                        "Time blocking"));

                courses.add(new CustomModel(R.drawable.timemanagement, "Time Management", "4 Lessons", "4.8 / 5.0",
                        "Master the art of managing your time effectively.",
                        "Alex Smith", "Life Coach", R.drawable.profile_mentor_boy,
                        modules, lessons, timeManagementLessonContents, timeManagementQuizQuestions, "https://youtu.be/TimeMgmtTips"));

                // Course 2: Emotional Intelligence
                modules = new ArrayList<>();
                modules.add("Module 1: Understanding Emotional Intelligence");
                modules.add("Module 2: Applying Emotional Intelligence");

                lessons = new ArrayList<>();
                List<String> emotionalIntelligenceModule1 = new ArrayList<>();
                emotionalIntelligenceModule1.add("Lesson 1: Self-Awareness");
                emotionalIntelligenceModule1.add("Lesson 2: Empathy");
                List<String> emotionalIntelligenceModule2 = new ArrayList<>();
                emotionalIntelligenceModule2.add("Lesson 1: Managing Emotions");
                emotionalIntelligenceModule2.add("Lesson 2: Building Relationships");
                lessons.add(emotionalIntelligenceModule1);
                lessons.add(emotionalIntelligenceModule2);

                List<List<String>> emotionalIntelligenceLessonContents = new ArrayList<>();
                List<String> emotionalIntelligenceModule1Content = new ArrayList<>();
                emotionalIntelligenceModule1Content.add("Learn the importance of self-awareness and recognizing your emotions.");
                emotionalIntelligenceModule1Content.add("Understand how empathy strengthens personal and professional relationships.");
                List<String> emotionalIntelligenceModule2Content = new ArrayList<>();
                emotionalIntelligenceModule2Content.add("Discover techniques to manage emotions effectively under stress.");
                emotionalIntelligenceModule2Content.add("Learn how to build and maintain strong interpersonal relationships.");
                emotionalIntelligenceLessonContents.add(emotionalIntelligenceModule1Content);
                emotionalIntelligenceLessonContents.add(emotionalIntelligenceModule2Content);

                // Quiz questions for Emotional Intelligence
                List<QuizQuestion> emotionalIntelligenceQuizQuestions = new ArrayList<>();
                emotionalIntelligenceQuizQuestions.add(new QuizQuestion("What is the first step in developing emotional intelligence?",
                        List.of("Ignoring emotions", "Self-awareness", "Focusing on others"),
                        "Self-awareness"));
                emotionalIntelligenceQuizQuestions.add(new QuizQuestion("What is a key benefit of empathy?",
                        List.of("Improved relationships", "Avoiding conflict", "Ignoring emotions"),
                        "Improved relationships"));

                courses.add(new CustomModel(R.drawable.emotions, "Emotional Intelligence", "4 Lessons", "4.9 / 5.0",
                        "Enhance your ability to understand and manage emotions.",
                        "Sophia Lee", "Emotional Intelligence Expert", R.drawable.profile_girl2,
                        modules, lessons, emotionalIntelligenceLessonContents, emotionalIntelligenceQuizQuestions, "https://youtu.be/EITips"));

                // Add similar logic for courses like Stress Management, Goal Setting, and others.

                break;

            case "Personal Growth":
                // Course 1: Mindfulness Practice
                modules = new ArrayList<>();
                modules.add("Module 1: Introduction to Mindfulness");
                modules.add("Module 2: Daily Mindfulness Techniques");

                lessons = new ArrayList<>();
                List<String> mindfulnessModule1 = new ArrayList<>();
                mindfulnessModule1.add("Lesson 1: What is Mindfulness?");
                mindfulnessModule1.add("Lesson 2: Benefits of Mindfulness");
                List<String> mindfulnessModule2 = new ArrayList<>();
                mindfulnessModule2.add("Lesson 1: Breathing Exercises");
                mindfulnessModule2.add("Lesson 2: Staying Present in Daily Life");
                lessons.add(mindfulnessModule1);
                lessons.add(mindfulnessModule2);

                List<List<String>> mindfulnessLessonContents = new ArrayList<>();
                List<String> mindfulnessModule1Content = new ArrayList<>();
                mindfulnessModule1Content.add("Understand the basics of mindfulness and its importance.");
                mindfulnessModule1Content.add("Learn about the mental and physical benefits of practicing mindfulness.");
                List<String> mindfulnessModule2Content = new ArrayList<>();
                mindfulnessModule2Content.add("Practice breathing exercises to enhance mindfulness.");
                mindfulnessModule2Content.add("Discover techniques to stay present in everyday activities.");
                mindfulnessLessonContents.add(mindfulnessModule1Content);
                mindfulnessLessonContents.add(mindfulnessModule2Content);

                List<QuizQuestion> mindfulnessQuizQuestions = new ArrayList<>();
                mindfulnessQuizQuestions.add(new QuizQuestion("What is the primary goal of mindfulness?",
                        List.of("To stay present", "To multitask effectively", "To achieve perfection"),
                        "To stay present"));
                mindfulnessQuizQuestions.add(new QuizQuestion("Which technique can help with mindfulness?",
                        List.of("Breathing exercises", "Overthinking", "Ignoring thoughts"),
                        "Breathing exercises"));

                courses.add(new CustomModel(R.drawable.mentalhealth, "Mindfulness Practice", "4 Lessons", "4.9 / 5.0",
                        "Learn how to incorporate mindfulness into your daily life.",
                        "Sophia Green", "Mindfulness Coach", R.drawable.profile_girl1,
                        modules, lessons, mindfulnessLessonContents, mindfulnessQuizQuestions, "https://youtu.be/MindfulnessPractice"));

                // Course 2: Personal Resilience
                modules = new ArrayList<>();
                modules.add("Module 1: Understanding Resilience");
                modules.add("Module 2: Building Resilience");

                lessons = new ArrayList<>();
                List<String> resilienceModule1 = new ArrayList<>();
                resilienceModule1.add("Lesson 1: What is Resilience?");
                resilienceModule1.add("Lesson 2: Factors Affecting Resilience");
                List<String> resilienceModule2 = new ArrayList<>();
                resilienceModule2.add("Lesson 1: Coping Strategies");
                resilienceModule2.add("Lesson 2: Developing a Growth Mindset");
                lessons.add(resilienceModule1);
                lessons.add(resilienceModule2);

                List<List<String>> resilienceLessonContents = new ArrayList<>();
                List<String> resilienceModule1Content = new ArrayList<>();
                resilienceModule1Content.add("Learn the meaning of resilience and why it matters.");
                resilienceModule1Content.add("Explore factors that influence resilience in individuals.");
                List<String> resilienceModule2Content = new ArrayList<>();
                resilienceModule2Content.add("Discover effective coping strategies for overcoming challenges.");
                resilienceModule2Content.add("Understand how a growth mindset contributes to resilience.");
                resilienceLessonContents.add(resilienceModule1Content);
                resilienceLessonContents.add(resilienceModule2Content);

                List<QuizQuestion> resilienceQuizQuestions = new ArrayList<>();
                resilienceQuizQuestions.add(new QuizQuestion("What does resilience help you with?",
                        List.of("Avoid challenges", "Adapt and recover", "Ignore problems"),
                        "Adapt and recover"));
                resilienceQuizQuestions.add(new QuizQuestion("What mindset is essential for building resilience?",
                        List.of("Growth mindset", "Fixed mindset", "Avoidant mindset"),
                        "Growth mindset"));

                courses.add(new CustomModel(R.drawable.challenge, "Personal Resilience", "4 Lessons", "4.8 / 5.0",
                        "Develop resilience to navigate life's challenges.",
                        "James Miller", "Resilience Expert", R.drawable.profile_mentor_boy,
                        modules, lessons, resilienceLessonContents, resilienceQuizQuestions, "https://youtu.be/ResilienceTips"));

                // Course 3: Effective Communication
                modules = new ArrayList<>();
                modules.add("Module 1: Foundations of Communication");
                modules.add("Module 2: Advanced Communication Skills");

                lessons = new ArrayList<>();
                List<String> communicationModule1 = new ArrayList<>();
                communicationModule1.add("Lesson 1: The Basics of Effective Communication");
                communicationModule1.add("Lesson 2: Active Listening");
                List<String> communicationModule2 = new ArrayList<>();
                communicationModule2.add("Lesson 1: Giving Constructive Feedback");
                communicationModule2.add("Lesson 2: Conflict Resolution");
                lessons.add(communicationModule1);
                lessons.add(communicationModule2);

                List<List<String>> communicationLessonContents = new ArrayList<>();
                List<String> communicationModule1Content = new ArrayList<>();
                communicationModule1Content.add("Understand the principles of clear and effective communication.");
                communicationModule1Content.add("Learn the art of active listening to improve interactions.");
                List<String> communicationModule2Content = new ArrayList<>();
                communicationModule2Content.add("Discover how to provide constructive feedback effectively.");
                communicationModule2Content.add("Explore techniques for resolving conflicts peacefully.");
                communicationLessonContents.add(communicationModule1Content);
                communicationLessonContents.add(communicationModule2Content);

                List<QuizQuestion> communicationQuizQuestions = new ArrayList<>();
                communicationQuizQuestions.add(new QuizQuestion("What is a key component of effective communication?",
                        List.of("Clear expression", "Interrupting often", "Ignoring others"),
                        "Clear expression"));
                communicationQuizQuestions.add(new QuizQuestion("What skill is essential for resolving conflicts?",
                        List.of("Active listening", "Avoiding the issue", "Arguing more"),
                        "Active listening"));

                courses.add(new CustomModel(R.drawable.effectivecommunication, "Effective Communication", "4 Lessons", "4.7 / 5.0",
                        "Enhance your communication skills for personal and professional success.",
                        "Emma Johnson", "Communication Coach", R.drawable.profile_mentor_girl,
                        modules, lessons, communicationLessonContents, communicationQuizQuestions, "https://youtu.be/CommSkills"));

                break;

            case "Health and Wellness":
                // Course 1: Healthy Eating Habits
                modules = new ArrayList<>();
                modules.add("Module 1: Basics of Nutrition");
                modules.add("Module 2: Meal Planning and Preparation");

                lessons = new ArrayList<>();
                List<String> healthyEatingModule1 = new ArrayList<>();
                healthyEatingModule1.add("Lesson 1: Understanding Macronutrients");
                healthyEatingModule1.add("Lesson 2: Importance of Hydration");
                List<String> healthyEatingModule2 = new ArrayList<>();
                healthyEatingModule2.add("Lesson 1: Weekly Meal Planning");
                healthyEatingModule2.add("Lesson 2: Healthy Recipe Ideas");
                lessons.add(healthyEatingModule1);
                lessons.add(healthyEatingModule2);

                List<List<String>> healthyEatingContents = new ArrayList<>();
                healthyEatingContents.add(List.of("Learn about carbohydrates, proteins, and fats.", "Understand why staying hydrated is vital."));
                healthyEatingContents.add(List.of("Tips for creating a balanced meal plan.", "Explore quick and healthy recipes."));

                List<QuizQuestion> healthyEatingQuiz = new ArrayList<>();
                healthyEatingQuiz.add(new QuizQuestion("What are macronutrients?",
                        List.of("Proteins, fats, carbs", "Vitamins and minerals", "Water and fiber"), "Proteins, fats, carbs"));
                healthyEatingQuiz.add(new QuizQuestion("What is essential for hydration?",
                        List.of("Water", "Caffeine", "Sugary drinks"), "Water"));

                courses.add(new CustomModel(R.drawable.healthy_eating, "Healthy Eating Habits", "4 Lessons", "4.9 / 5.0",
                        "Develop sustainable and healthy eating habits.", "Laura Brown", "Dietitian", R.drawable.profile_girl2,
                        modules, lessons, healthyEatingContents, healthyEatingQuiz, "https://youtu.be/HealthyEating"));

                // Course 2: Mental Well-Being
                modules = new ArrayList<>();
                modules.add("Module 1: Introduction to Mental Health");
                modules.add("Module 2: Coping Strategies");

                lessons = new ArrayList<>();
                List<String> mentalWellBeingModule1 = new ArrayList<>();
                mentalWellBeingModule1.add("Lesson 1: Understanding Anxiety and Depression");
                mentalWellBeingModule1.add("Lesson 2: Recognizing Burnout");
                List<String> mentalWellBeingModule2 = new ArrayList<>();
                mentalWellBeingModule2.add("Lesson 1: Meditation and Mindfulness");
                mentalWellBeingModule2.add("Lesson 2: Seeking Professional Help");
                lessons.add(mentalWellBeingModule1);
                lessons.add(mentalWellBeingModule2);

                List<List<String>> mentalWellBeingContents = new ArrayList<>();
                mentalWellBeingContents.add(List.of("Identify signs of anxiety and depression.", "Learn about burnout and its effects."));
                mentalWellBeingContents.add(List.of("Practice meditation to reduce stress.", "Know when and how to seek help."));

                List<QuizQuestion> mentalWellBeingQuiz = new ArrayList<>();
                mentalWellBeingQuiz.add(new QuizQuestion("What are common signs of burnout?",
                        List.of("Exhaustion and detachment", "Increased energy", "Better concentration"), "Exhaustion and detachment"));
                mentalWellBeingQuiz.add(new QuizQuestion("What is a mindfulness practice?",
                        List.of("Meditation", "Multitasking", "Ignoring stress"), "Meditation"));

                courses.add(new CustomModel(R.drawable.mental_wellbeing, "Mental Well-Being", "4 Lessons", "4.8 / 5.0",
                        "Focus on mental health and stress management.", "Dr. Michael Green", "Psychologist", R.drawable.profile_mentor_boy,
                        modules, lessons, mentalWellBeingContents, mentalWellBeingQuiz, "https://youtu.be/MentalHealthTips"));

                // Course 3: Physical Fitness
                modules = new ArrayList<>();
                modules.add("Module 1: Building a Fitness Routine");
                modules.add("Module 2: Staying Consistent");

                lessons = new ArrayList<>();
                List<String> fitnessModule1 = new ArrayList<>();
                fitnessModule1.add("Lesson 1: Setting Fitness Goals");
                fitnessModule1.add("Lesson 2: Types of Exercises");
                List<String> fitnessModule2 = new ArrayList<>();
                fitnessModule2.add("Lesson 1: Tracking Progress");
                fitnessModule2.add("Lesson 2: Overcoming Challenges");
                lessons.add(fitnessModule1);
                lessons.add(fitnessModule2);

                List<List<String>> fitnessContents = new ArrayList<>();
                fitnessContents.add(List.of("Set achievable fitness goals.", "Explore strength, cardio, and flexibility workouts."));
                fitnessContents.add(List.of("Track your progress effectively.", "Learn strategies to stay motivated."));

                List<QuizQuestion> fitnessQuiz = new ArrayList<>();
                fitnessQuiz.add(new QuizQuestion("What is essential for fitness progress?",
                        List.of("Consistency", "Skipping sessions", "Avoiding feedback"), "Consistency"));
                fitnessQuiz.add(new QuizQuestion("What is the best way to track progress?",
                        List.of("Measurements and logs", "Ignoring results", "Only cardio"), "Measurements and logs"));

                courses.add(new CustomModel(R.drawable.weightlifting, "Physical Fitness", "4 Lessons", "4.7 / 5.0",
                        "Achieve your fitness goals effectively.", "Sarah Davis", "Fitness Trainer", R.drawable.profile_mentor_girl,
                        modules, lessons, fitnessContents, fitnessQuiz, "https://youtu.be/FitnessTips"));

                break;

            case "Advocacy and Strength":
                // Course 1: Public Speaking for Advocacy
                modules = new ArrayList<>();
                modules.add("Module 1: Fundamentals of Public Speaking");
                modules.add("Module 2: Crafting an Advocacy Speech");

                lessons = new ArrayList<>();
                List<String> publicSpeakingModule1 = new ArrayList<>();
                publicSpeakingModule1.add("Lesson 1: Overcoming Stage Fright");
                publicSpeakingModule1.add("Lesson 2: Engaging Your Audience");
                List<String> publicSpeakingModule2 = new ArrayList<>();
                publicSpeakingModule2.add("Lesson 1: Structuring Your Message");
                publicSpeakingModule2.add("Lesson 2: Delivering with Confidence");
                lessons.add(publicSpeakingModule1);
                lessons.add(publicSpeakingModule2);

                List<List<String>> publicSpeakingContents = new ArrayList<>();
                publicSpeakingContents.add(List.of("Learn strategies to conquer stage fear.", "Understand techniques to captivate listeners."));
                publicSpeakingContents.add(List.of("Organize your advocacy speech effectively.", "Practice confident and impactful delivery."));

                List<QuizQuestion> publicSpeakingQuiz = new ArrayList<>();
                publicSpeakingQuiz.add(new QuizQuestion("What is key to overcoming stage fright?",
                        List.of("Practice", "Avoiding speeches", "Reading silently"), "Practice"));
                publicSpeakingQuiz.add(new QuizQuestion("What makes a speech engaging?",
                        List.of("Clear structure", "Monotone delivery", "Ignoring the audience"), "Clear structure"));

                courses.add(new CustomModel(R.drawable.advocacy_speaking, "Public Speaking for Advocacy", "4 Lessons", "4.9 / 5.0",
                        "Master public speaking to amplify your advocacy efforts.", "John Taylor", "Advocacy Trainer", R.drawable.profile_mentor_boy,
                        modules, lessons, publicSpeakingContents, publicSpeakingQuiz, "https://youtu.be/PublicSpeaking"));

                // Course 2: Advocacy Strategy
                modules = new ArrayList<>();
                modules.add("Module 1: Planning Your Advocacy");
                modules.add("Module 2: Building Alliances");

                lessons = new ArrayList<>();
                List<String> advocacyStrategyModule1 = new ArrayList<>();
                advocacyStrategyModule1.add("Lesson 1: Setting Advocacy Goals");
                advocacyStrategyModule1.add("Lesson 2: Identifying Key Stakeholders");
                List<String> advocacyStrategyModule2 = new ArrayList<>();
                advocacyStrategyModule2.add("Lesson 1: Forming Coalitions");
                advocacyStrategyModule2.add("Lesson 2: Sustaining Partnerships");
                lessons.add(advocacyStrategyModule1);
                lessons.add(advocacyStrategyModule2);

                List<List<String>> advocacyStrategyContents = new ArrayList<>();
                advocacyStrategyContents.add(List.of("Learn to define clear advocacy objectives.", "Identify individuals and groups to influence."));
                advocacyStrategyContents.add(List.of("Collaborate with like-minded organizations.", "Maintain long-term partnerships."));

                List<QuizQuestion> advocacyStrategyQuiz = new ArrayList<>();
                advocacyStrategyQuiz.add(new QuizQuestion("What is the first step in advocacy?",
                        List.of("Setting goals", "Avoiding action", "Ignoring stakeholders"), "Setting goals"));
                advocacyStrategyQuiz.add(new QuizQuestion("How can you sustain partnerships?",
                        List.of("Regular communication", "Ignoring allies", "Focusing only on yourself"), "Regular communication"));

                courses.add(new CustomModel(R.drawable.startegy, "Advocacy Strategy", "4 Lessons", "4.8 / 5.0",
                        "Plan and execute effective advocacy campaigns.", "Emily Brown", "Advocacy Expert", R.drawable.profile_mentor_girl,
                        modules, lessons, advocacyStrategyContents, advocacyStrategyQuiz, "https://youtu.be/AdvocacyStrategy"));

                // Course 3: Resilience in Advocacy
                modules = new ArrayList<>();
                modules.add("Module 1: Facing Advocacy Challenges");
                modules.add("Module 2: Staying Motivated");

                lessons = new ArrayList<>();
                List<String> resilienceAdvocacyModule1 = new ArrayList<>();
                resilienceAdvocacyModule1.add("Lesson 1: Handling Opposition");
                resilienceAdvocacyModule1.add("Lesson 2: Dealing with Burnout");
                List<String> resilienceAdvocacyModule2 = new ArrayList<>();
                resilienceAdvocacyModule2.add("Lesson 1: Celebrating Small Wins");
                resilienceAdvocacyModule2.add("Lesson 2: Building a Support Network");
                lessons.add(resilienceAdvocacyModule1);
                lessons.add(resilienceAdvocacyModule2);

                List<List<String>> resilienceAdvocacyContents = new ArrayList<>();
                resilienceAdvocacyContents.add(List.of("Learn to navigate resistance in advocacy.", "Understand burnout and recovery strategies."));
                resilienceAdvocacyContents.add(List.of("Recognize and celebrate milestones.", "Build a supportive advocacy community."));

                List<QuizQuestion> resilienceAdvocacyQuiz = new ArrayList<>();
                resilienceAdvocacyQuiz.add(new QuizQuestion("What helps overcome advocacy challenges?",
                        List.of("Support networks", "Avoiding challenges", "Ignoring issues"), "Support networks"));
                resilienceAdvocacyQuiz.add(new QuizQuestion("How can you stay motivated in advocacy?",
                        List.of("Celebrating wins", "Avoiding feedback", "Working in isolation"), "Celebrating wins"));

                courses.add(new CustomModel(R.drawable.conflictresolution, "Resilience in Advocacy", "4 Lessons", "4.7 / 5.0",
                        "Stay resilient and motivated in your advocacy journey.", "Sophia Green", "Advocacy Coach", R.drawable.profile_girl2,
                        modules, lessons, resilienceAdvocacyContents, resilienceAdvocacyQuiz, "https://youtu.be/AdvocacyResilience"));

                break;

            case "Relationship and Support":
                // Course 1: Building Healthy Relationships
                modules = new ArrayList<>();
                modules.add("Module 1: Foundations of Healthy Relationships");
                modules.add("Module 2: Effective Communication in Relationships");

                lessons = new ArrayList<>();
                List<String> healthyRelationshipsModule1 = new ArrayList<>();
                healthyRelationshipsModule1.add("Lesson 1: Understanding Trust and Respect");
                healthyRelationshipsModule1.add("Lesson 2: Setting Boundaries");
                List<String> healthyRelationshipsModule2 = new ArrayList<>();
                healthyRelationshipsModule2.add("Lesson 1: Active Listening Techniques");
                healthyRelationshipsModule2.add("Lesson 2: Managing Conflicts");
                lessons.add(healthyRelationshipsModule1);
                lessons.add(healthyRelationshipsModule2);

                List<List<String>> healthyRelationshipsContents = new ArrayList<>();
                healthyRelationshipsContents.add(List.of("Learn the importance of trust and mutual respect.", "Understand how to set personal boundaries."));
                healthyRelationshipsContents.add(List.of("Practice active listening for better communication.", "Discover strategies for resolving conflicts effectively."));

                List<QuizQuestion> healthyRelationshipsQuiz = new ArrayList<>();
                healthyRelationshipsQuiz.add(new QuizQuestion("What is key to a healthy relationship?",
                        List.of("Trust and respect", "Ignoring boundaries", "Conflict avoidance"), "Trust and respect"));
                healthyRelationshipsQuiz.add(new QuizQuestion("What helps in managing conflicts?",
                        List.of("Effective communication", "Avoiding discussions", "Holding grudges"), "Effective communication"));

                courses.add(new CustomModel(R.drawable.healthyrelationship, "Building Healthy Relationships", "4 Lessons", "4.9 / 5.0",
                        "Foster trust and respect in your personal relationships.", "Dr. Lisa Hart", "Relationship Counselor", R.drawable.profile_mentor_girl,
                        modules, lessons, healthyRelationshipsContents, healthyRelationshipsQuiz, "https://youtu.be/HealthyRelationships"));

                // Course 2: Emotional Support Strategies
                modules = new ArrayList<>();
                modules.add("Module 1: Understanding Emotional Needs");
                modules.add("Module 2: Providing Support Effectively");

                lessons = new ArrayList<>();
                List<String> emotionalSupportModule1 = new ArrayList<>();
                emotionalSupportModule1.add("Lesson 1: Recognizing Emotional Cues");
                emotionalSupportModule1.add("Lesson 2: Empathy in Action");
                List<String> emotionalSupportModule2 = new ArrayList<>();
                emotionalSupportModule2.add("Lesson 1: Supporting a Friend in Crisis");
                emotionalSupportModule2.add("Lesson 2: Self-Care While Supporting Others");
                lessons.add(emotionalSupportModule1);
                lessons.add(emotionalSupportModule2);

                List<List<String>> emotionalSupportContents = new ArrayList<>();
                emotionalSupportContents.add(List.of("Identify emotional needs in relationships.", "Learn the importance of showing empathy."));
                emotionalSupportContents.add(List.of("Provide effective support during tough times.", "Understand how to care for yourself as a supporter."));

                List<QuizQuestion> emotionalSupportQuiz = new ArrayList<>();
                emotionalSupportQuiz.add(new QuizQuestion("What is essential for emotional support?",
                        List.of("Empathy", "Judgment", "Indifference"), "Empathy"));
                emotionalSupportQuiz.add(new QuizQuestion("Why is self-care important for supporters?",
                        List.of("To stay balanced", "To ignore others", "To focus only on oneself"), "To stay balanced"));

                courses.add(new CustomModel(R.drawable.support, "Emotional Support Strategies", "4 Lessons", "4.8 / 5.0",
                        "Learn how to support others emotionally while maintaining self-care.", "James Carter", "Support Specialist", R.drawable.profile_mentor_boy,
                        modules, lessons, emotionalSupportContents, emotionalSupportQuiz, "https://youtu.be/EmotionalSupport"));

                // Course 3: Strengthening Family Bonds
                modules = new ArrayList<>();
                modules.add("Module 1: Understanding Family Dynamics");
                modules.add("Module 2: Building Stronger Connections");

                lessons = new ArrayList<>();
                List<String> familyBondsModule1 = new ArrayList<>();
                familyBondsModule1.add("Lesson 1: Roles and Responsibilities in Families");
                familyBondsModule1.add("Lesson 2: Dealing with Family Challenges");
                List<String> familyBondsModule2 = new ArrayList<>();
                familyBondsModule2.add("Lesson 1: Quality Time Activities");
                familyBondsModule2.add("Lesson 2: Celebrating Achievements Together");
                lessons.add(familyBondsModule1);
                lessons.add(familyBondsModule2);

                List<List<String>> familyBondsContents = new ArrayList<>();
                familyBondsContents.add(List.of("Understand roles and dynamics within families.", "Learn strategies for addressing family challenges."));
                familyBondsContents.add(List.of("Discover activities to strengthen family bonds.", "Celebrate milestones to create shared memories."));

                List<QuizQuestion> familyBondsQuiz = new ArrayList<>();
                familyBondsQuiz.add(new QuizQuestion("What strengthens family bonds?",
                        List.of("Quality time together", "Avoiding interaction", "Ignoring roles"), "Quality time together"));
                familyBondsQuiz.add(new QuizQuestion("How can families celebrate achievements?",
                        List.of("Acknowledging successes", "Ignoring milestones", "Competing with each other"), "Acknowledging successes"));

                courses.add(new CustomModel(R.drawable.family, "Strengthening Family Bonds", "4 Lessons", "4.7 / 5.0",
                        "Enhance family relationships through communication and activities.", "Emma Johnson", "Family Coach", R.drawable.profile_girl2,
                        modules, lessons, familyBondsContents, familyBondsQuiz, "https://youtu.be/FamilyBonds"));

                break;

        }



        // Add more categories here
        return courses;
    }
}