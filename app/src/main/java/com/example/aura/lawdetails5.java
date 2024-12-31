package com.example.aura;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class lawdetails5 extends AppCompatActivity {

    private ImageView thumbnaillaw5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawdetails5);

        setupToolbar();
        setupBackNavigation();

        ImageView thumbnaillaw5 = findViewById(R.id.thumbnaillaw5);

        String videoUrllaw5 = "https://www.youtube.com/watch?v=tVxKL2pHQac";

        thumbnaillaw5.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrllaw5));
            startActivity(intent);
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