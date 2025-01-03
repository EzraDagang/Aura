package com.example.aura;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder> {

    Context context;
    ArrayList<CustomModel> customModelArrayList = new ArrayList<>();

    CardView cardView;

    public interface OnCourseSelectedListener {
        void onCourseSelected(CustomModel selectedCourse);
    }

    public CustomAdapter(Context context, ArrayList<CustomModel> customModelArrayList) {
        this.context = context;
        this.customModelArrayList = customModelArrayList;
        //this.courseDetailModelArrayList = courseDetailModelArrayList;
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(context).inflate(R.layout.card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        holder.imageView.setImageResource(customModelArrayList.get(position).getImage());
        holder.tvTitle.setText(customModelArrayList.get(position).getTitle());
        holder.tvLesson.setText(customModelArrayList.get(position).getLesson());
        holder.tvRating.setText(customModelArrayList.get(position).getRating());


        holder.cardItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    CustomModel currentItem = customModelArrayList.get(currentPosition);


                    Intent intent = new Intent(context, MainActivity2.class);
                    intent.putExtra("courseDetails", currentItem); // Pass the entire CustomModel object
                    context.startActivity(intent);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return customModelArrayList.size();
    }

    public static class CustomHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle, tvLesson, tvRating;
        View cardItemView;


        public CustomHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvLesson = itemView.findViewById(R.id.tvLesson);
            tvRating = itemView.findViewById(R.id.tvRating);
            cardItemView = itemView.findViewById(R.id.cardItemView);

        }
    }

}