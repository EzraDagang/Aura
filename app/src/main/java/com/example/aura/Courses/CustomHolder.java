package com.example.aura.Courses;

import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aura.R;

public class CustomHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView tvTitle,tvLesson,tvRating;
    CardView cardItemView;


    TextView tvAboutDetails, mentorName, mentorJob, tvDesc;
    ImageView mentorProfile;
    ExpandableListView lessonListView;
    Button enrollButton;
    String courseTitle;

    public CustomHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvLesson = itemView.findViewById(R.id.tvLesson);
        tvRating = itemView.findViewById(R.id.tvRating);
        cardItemView = itemView.findViewById((R.id.cardItemView));

        // Binding Views
/*
        tvAboutDetails = itemView.findViewById(R.id.tvAboutDetails);
        mentorName = itemView.findViewById(R.id.mentorName);
        mentorJob = itemView.findViewById(R.id.mentorJob);
        mentorProfile = itemView.findViewById(R.id.mentorProfile);
        lessonListView = itemView.findViewById(R.id.expandableListView);
        enrollButton = itemView.findViewById(R.id.enrollButton);


*/


    }
}