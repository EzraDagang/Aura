package com.example.aura.Courses;

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

import com.example.aura.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder> {

    private Context context;
    private ArrayList<CustomModel> customModelArrayList = new ArrayList<>();
    private String categoryDocumentId; // To store the category ID

    public CustomAdapter(Context context, ArrayList<CustomModel> customModelArrayList, String categoryDocumentId) {
        this.context = context;
        this.customModelArrayList = customModelArrayList;
        this.categoryDocumentId = categoryDocumentId; // Initialize the categoryDocumentId
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

        holder.cardItemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                CustomModel currentItem = customModelArrayList.get(currentPosition);

                // Create intent to navigate to MainActivity2
                Intent intent = new Intent(context, MainActivity2.class);
                intent.putExtra("courseID", currentItem.getCourseId()); // Pass course ID
                intent.putExtra("category", categoryDocumentId); // Pass category ID
                context.startActivity(intent);
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
