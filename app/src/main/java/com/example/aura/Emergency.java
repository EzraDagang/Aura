package com.example.aura;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.aura.Courses.DiscoverScreen;
import com.example.aura.Settings.SettingsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Emergency extends AppCompatActivity {

    private LinearLayout cancelLayout;
    private boolean isLongPressActive = false;
    private final Handler handler = new Handler();
    private Runnable callRunnable;
    private ImageView ivText;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_phone);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Navigate to DiscoverScreen or Home
                Intent intent = new Intent(Emergency.this, DiscoverScreen.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_phone) {
                // Current page is Emergency, no action required

                return true;
            } else if (itemId == R.id.nav_notifications) {
                // Handle notifications navigation
                Intent intent = new Intent(Emergency.this, education.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Handle profile navigation
                Intent intent = new Intent(Emergency.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        final ImageButton btnBack = findViewById(R.id.BtnBack);
        final Button btnECard = findViewById(R.id.BtnECard);
        final Button btnEPlace = findViewById(R.id.BtnEPlace);
        ImageButton sosButton = findViewById(R.id.BtnSOSCall);
        cancelLayout = findViewById(R.id.cancelLayout);
        ivText = findViewById(R.id.IVText);

        // Initialize location provider client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Starting.class);
                startActivity(i);
            }
        });

        btnEPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAndOpenGoogleMaps();
            }
        });

        btnECard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EmergencyCard.class);
                startActivity(i);
            }
        });

        sosButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ivText.setVisibility(View.GONE);
                        handler.postDelayed(() -> {
                            isLongPressActive = true;
                            cancelLayout.setVisibility(View.VISIBLE);
                        }, 10);

                        callRunnable = () -> makeEmergencyCall();
                        handler.postDelayed(callRunnable, 3000);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // Check if user swipes down to cancel
                        if (event.getY() > 200) { // Adjust threshold
                            cancelCall();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        if (isLongPressActive) {
                            handler.removeCallbacks(callRunnable);
                            makeEmergencyCall(); // Trigger call on release
                        }
                        reset();
                        break;
                }
                return true;
            }
        });
    }

    private void makeEmergencyCall() {
        String phoneNumber = "01158328125"; //999
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void cancelCall() {
        handler.removeCallbacks(callRunnable);
        cancelLayout.setVisibility(View.GONE);
        isLongPressActive = false;

        ivText.setVisibility(View.VISIBLE);
    }

    private void reset() {
        handler.removeCallbacks(callRunnable);
        cancelLayout.setVisibility(View.GONE);
        isLongPressActive = false;

        ivText.setVisibility(View.VISIBLE);
    }

    // Fetch user location and open Google Maps with a query for police stations
    private void fetchAndOpenGoogleMaps() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        @SuppressLint("MissingPermission")
        Task locationTask = fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Launch Google Maps with a query for nearby police stations
                String uri = String.format("geo:%s,%s?q=police+station", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            } else {
                // Handle case where location is unavailable
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchAndOpenGoogleMaps();
            }
        }
    }
}
