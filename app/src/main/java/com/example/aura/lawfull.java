package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.core.view.WindowInsetsCompat;

public class lawfull extends AppCompatActivity {
    ArrayList<LawItem> lawFullItem = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawfull);
        setupToolbar();
        setupBackNavigation();

        RecyclerView recyclerView = findViewById(R.id.lawRecyclerView);

        setUpLawItem();

        LawAdapter adapter = new LawAdapter(this,lawFullItem);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

    private void setUpLawItem(){
        String[] lawTitle = getResources().getStringArray(R.array.title);
        String[] lawDes = getResources().getStringArray(R.array.description);
        String[] lawYear = getResources().getStringArray(R.array.year);

        for(int i = 0; i < lawTitle.length;i++){
            lawFullItem.add(new LawItem(lawTitle[i], lawDes[i],lawYear[i]));
        }
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Navigate to EducationActivity when the back button is pressed
            Intent intent = new Intent(lawfull.this,education.class);
            startActivity(intent);
            finish(); // Optionally call finish to close LawfulActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            SearchView searchView = findViewById(R.id.search_view);

            List<LawItem> lawList = new ArrayList<>();
            lawList.add(new LawItem("Domestic Violence Act", "Protects women from abuse", "1994"));
            lawList.add(new LawItem("Employment Act", "Ensures workplace equality", "1955"));
            lawList.add(new LawItem("Islamic Family Law", "Addresses marriage issues", "1984"));

            LawAdapter adapter = new LawAdapter(lawList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText);
                    return true;
                }
            });
        }
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lawfull);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

     */

}