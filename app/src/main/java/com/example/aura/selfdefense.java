package com.example.aura;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;

public class selfdefense extends AppCompatActivity {

    private ImageView thumbnail1;
    private TextView description1;
    private TextView descriptionTitle1;
    private Button watchNowButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfdefense);

        setupToolbar();
        setupBackNavigation();

        thumbnail1 = findViewById(R.id.thumbnail1);
        watchNowButton1 = findViewById(R.id.watchNowButton1);

        String thumbnailUrl1 = "https://img.youtube.com/vi/M4_8PoRQP8w/maxresdefault.jpg";
        String videoUrl1 = "https://www.youtube.com/watch?v=M4_8PoRQP8w";

        Glide.with(this)
                .load(thumbnailUrl1)  // URL for the YouTube thumbnail
                .into(thumbnail1);

        watchNowButton1.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl1));
            startActivity(intent);  // This will open the YouTube video in the YouTube app or a browser
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);  // Disable default back arrow since we have custom
        }
    }

    private void setupBackNavigation() {
        // Initialize back navigation views
        ImageView backArrow = findViewById(R.id.backArrow);
        Button backButton = findViewById(R.id.backButton);

        View.OnClickListener backClickListener = v -> finish();  // Using lambda for cleaner code

        // Set click listeners
        backArrow.setOnClickListener(backClickListener);
        backButton.setOnClickListener(backClickListener);

        // Register back press callback
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();  // Properly finish the activity
            }
        });
    }
}