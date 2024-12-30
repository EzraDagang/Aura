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
    private ImageView thumbnail2;
    private ImageView thumbnail3;
    private Button watchNowButton1;
    private Button watchNowButton2;
    private Button watchNowButton3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfdefense);

        setupToolbar();
        setupBackNavigation();

        thumbnail1 = findViewById(R.id.thumbnail1);
        thumbnail2 = findViewById(R.id.thumbnail2);
        thumbnail3 = findViewById(R.id.thumbnail3);
        watchNowButton1 = findViewById(R.id.watchNowButton1);
        watchNowButton2 = findViewById(R.id.watchNowButton2);
        watchNowButton3 = findViewById(R.id.watchNowButton3);

       // String thumbnailUrl1 = "https://img.youtube.com/vi/M4_8PoRQP8w/maxresdefault.jpg";
        String videoUrl1 = "https://www.youtube.com/watch?v=M4_8PoRQP8w";
        String videoUrl2 = "https://www.youtube.com/watch?v=HMtNKZEul3E";
        String videoUrl3 = "https://www.youtube.com/watch?v=-V4vEyhWDZ0";

        /*Glide.with(this)
                .load(thumbnailUrl1)
                .override(267, 150)// URL for the YouTube thumbnail
                .into(thumbnail1);
*/
        thumbnail1.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl1));
            startActivity(intent);  // This will open the YouTube video in the YouTube app or a browser
        });
        watchNowButton1.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl1));
            startActivity(intent);  // This will open the YouTube video in the YouTube app or a browser
        });

        thumbnail2.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl2));
            startActivity(intent);  // This will open the YouTube video in the YouTube app or a browser
        });
        watchNowButton2.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl2));
            startActivity(intent);  // This will open the YouTube video in the YouTube app or a browser
        });

        thumbnail3.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl3));
            startActivity(intent);  // This will open the YouTube video in the YouTube app or a browser
        });

        watchNowButton3.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl3));
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