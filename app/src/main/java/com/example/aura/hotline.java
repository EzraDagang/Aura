package com.example.aura;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class hotline extends AppCompatActivity {

    private SearchView searchView;
    private LinearLayout hotlineLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotline);

        setupToolbar();
        setupBackNavigation();

        // Initialize views
        searchView = findViewById(R.id.searchView);
        hotlineLayout = findViewById(R.id.hotlineLayout);

        // Add search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterHotlines(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterHotlines(newText); // Restore or filter dynamically
                return false;
            }
        });
    }

    private void filterHotlines(String query) {
        query = query.toLowerCase().trim(); // Convert query to lowercase for case-insensitive matching

        // Iterate through all child views in the hotlineLayout
        for (int i = 0; i < hotlineLayout.getChildCount(); i++) {
            View view = hotlineLayout.getChildAt(i);
            if (view instanceof CardView) {
                boolean matches = false;

                // Find all TextViews within the CardView dynamically
                for (int j = 0; j < ((CardView) view).getChildCount(); j++) {
                    View child = ((CardView) view).getChildAt(j);
                    if (child instanceof LinearLayout) {
                        for (int k = 0; k < ((LinearLayout) child).getChildCount(); k++) {
                            View grandChild = ((LinearLayout) child).getChildAt(k);
                            if (grandChild instanceof TextView) {
                                TextView textView = (TextView) grandChild;
                                if (textView.getText().toString().toLowerCase().contains(query)) {
                                    matches = true;
                                    break; // Exit loop if a match is found
                                }
                            }
                        }
                    }
                }

                // Show or hide the CardView based on the match
                view.setVisibility(matches ? View.VISIBLE : View.GONE);
            }
        }

        // Restore all cards if the query is empty
        if (query.isEmpty()) {
            for (int i = 0; i < hotlineLayout.getChildCount(); i++) {
                View view = hotlineLayout.getChildAt(i);
                if (view instanceof CardView) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void setupBackNavigation() {
        findViewById(R.id.backArrow).setOnClickListener(v -> finish());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }
}