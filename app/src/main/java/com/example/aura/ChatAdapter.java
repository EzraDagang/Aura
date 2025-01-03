package com.example.aura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ChatMessage> chatMessages;
    private final Context context;

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_MENTOR = 2;

    public ChatAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).isSentByUser()) {
            return VIEW_TYPE_USER;
        } else {
            return VIEW_TYPE_MENTOR;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_mentor_message, parent, false);
            return new MentorMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).messageTextView.setText(chatMessage.getMessage());
        } else if (holder instanceof MentorMessageViewHolder) {
            ((MentorMessageViewHolder) holder).messageTextView.setText(chatMessage.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.userMessageTextView);
        }
    }

    static class MentorMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public MentorMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.mentorMessageTextView);
        }
    }
}
