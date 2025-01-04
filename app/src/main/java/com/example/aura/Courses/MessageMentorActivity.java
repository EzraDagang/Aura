package com.example.aura.Courses;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aura.R;

import java.util.ArrayList;
import java.util.List;

public class MessageMentorActivity extends AppCompatActivity {

    private List<ChatMessage> chatMessages = new ArrayList<>();
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_mentor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Remove the default.png title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Retrieve the mentor's name passed via the intent
        String mentorName = getIntent().getStringExtra("mentorName");

        // Set the mentor's name in the TextView
        TextView mentorNameTextView = findViewById(R.id.mentorNameTextView);
        mentorNameTextView.setText(mentorName);
        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        EditText messageInput = findViewById(R.id.messageInput);
        ImageView sendButton = findViewById(R.id.sendButton);

        // Initialize chat adapter
        chatAdapter = new ChatAdapter(this, chatMessages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Handle send button click
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                chatMessages.add(new ChatMessage(message, true)); // User message
                chatMessages.add(new ChatMessage("Mentor's reply to: " + message, false)); // Simulate mentor reply
                chatAdapter.notifyDataSetChanged();
                messageInput.setText("");
                chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
            }
        });
    }
}
