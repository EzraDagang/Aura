package com.example.aura.Courses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.aura.R;

import java.util.ArrayList;

public class CustomCourseAdapter extends ArrayAdapter<CourseModel> {

    private final Context context;
    private final ArrayList<CourseModel> courses;

    public CustomCourseAdapter(Context context, ArrayList<CourseModel> courses) {
        super(context, R.layout.item_course, courses);
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        }

        CourseModel course = courses.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvLessons = convertView.findViewById(R.id.tvLessons);
        TextView tvMentor = convertView.findViewById(R.id.tvDescription);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteCourseButton);

        tvTitle.setText(course.getTitle());
        tvLessons.setText(course.getNumLessons() + " Lessons");
        tvMentor.setText("Mentor: " + course.getMentorName());

        deleteButton.setOnClickListener(v -> {
            courses.remove(position);
            notifyDataSetChanged();

            // Notify activity about deletion
            Intent intent = new Intent("com.example.aura.UPDATE_MY_COURSES");
            intent.putExtra("courseId", course.getCourseId());
            intent.putExtra("isAdding", false);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        });

        return convertView;
    }
}
