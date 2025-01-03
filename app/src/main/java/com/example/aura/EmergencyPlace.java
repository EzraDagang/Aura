/*package com.example.aura;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class EmergencyPlace extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private final int LOCATION_PERMISSION_REQUEST = 1;
    private LinearLayout listContainer;
    private final List<PoliceStation> policeStations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_place);

        final ImageButton btnBack = findViewById(R.id.BtnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Emergency.class);
                startActivity(i);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        listContainer = findViewById(R.id.listContainer);
        // Initialize location provider client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Add sample police stations (replace with dynamic data later)
        policeStations.add(new PoliceStation("Pantai Police Station", new LatLng(3.1286, 101.6789)));
        policeStations.add(new PoliceStation("Kerinchi Police Station", new LatLng(3.1189, 101.6770)));
        policeStations.add(new PoliceStation("Travers Police Station", new LatLng(3.1368, 101.6840)));
    }
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Request location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        // Enable user location and move camera
        mMap.setMyLocationEnabled(true);
        fetchAndDisplayLocation();
    }

    // Fetch user location and display nearby police stations
    private void fetchAndDisplayLocation() {
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                // Move the camera to user location
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14));

                // Add markers for police stations
                for (PoliceStation station : policeStations) {
                    mMap.addMarker(new MarkerOptions()
                            .position(station.getLocation())
                            .title(station.getName()));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onMapReady(mMap);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    // PoliceStation class to hold data
    private static class PoliceStation {
        private final String name;
        private final LatLng location;

        public PoliceStation(String name, LatLng location) {
            this.name = name;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public LatLng getLocation() {
            return location;
        }
    }
}*/