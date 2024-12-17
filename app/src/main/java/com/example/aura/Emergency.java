package com.example.aura;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class Emergency extends AppCompatActivity {

    private LinearLayout cancelLayout;
    private boolean isLongPressActive = false;
    private final Handler handler = new Handler();
    private Runnable callRunnable;
    private ImageView ivText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        final ImageButton btnBack = findViewById(R.id.BtnBack);
        final Button btnECard = findViewById(R.id.BtnECard);
        final Button btnEPlace = findViewById(R.id.BtnEPlace);
        ImageButton sosButton = findViewById(R.id.BtnSOSCall);
        cancelLayout = findViewById(R.id.cancelLayout);
        ivText = findViewById(R.id.IVText);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Starting.class);
                startActivity(i);
            }
        });

        sosButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
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

        /*btnECard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EmergencyCard.class);
            }
        });*/



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
}