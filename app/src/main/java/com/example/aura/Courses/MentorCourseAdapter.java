package com.example.aura.Courses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aura.R;

import java.util.ArrayList;
import java.util.List;

public class MentorCourseAdapter extends RecyclerView.Adapter<MentorCourseAdapter.CourseViewHolder> {
    private List<MentorCourse> mentorCourseList;
    private Context context;
    private OnCourseDeleteListener deleteListener;

    public interface OnCourseDeleteListener {
        void onCourseDelete(int position);
    }

    public MentorCourseAdapter(Context context, List<MentorCourse> mentorCourseList, OnCourseDeleteListener deleteListener) {
        this.context = context;
        this.mentorCourseList = mentorCourseList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        MentorCourse mentorCourse = mentorCourseList.get(position);

        holder.titleTextView.setText(mentorCourse.getTitle());
        holder.lessonsTextView.setText(String.format("Lessons: %d", mentorCourse.getLessonsCount()));
        holder.descriptionTextView.setText(mentorCourse.getDescription());

        Glide.with(holder.itemView.getContext())
                .load(mentorCourse.getImageUrl())
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(holder.courseImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MentorCourseInfoActivity.class);
            intent.putExtra("courseTitle", mentorCourse.getTitle());
            intent.putExtra("courseDescription", mentorCourse.getDescription());
            intent.putExtra("lessonsCount", mentorCourse.getLessonsCount());
            intent.putExtra("imageUrl", mentorCourse.getImageUrl());
            intent.putStringArrayListExtra("modules", new ArrayList<>(mentorCourse.getModules()));
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onCourseDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mentorCourseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, lessonsTextView, descriptionTextView;
        ImageView courseImage;
        ImageButton deleteButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvTitle);
            lessonsTextView = itemView.findViewById(R.id.tvLessons);
            descriptionTextView = itemView.findViewById(R.id.tvDescription);
            courseImage = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.deleteCourseButton);
        }
    }
}