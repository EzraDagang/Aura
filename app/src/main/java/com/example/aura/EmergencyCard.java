package com.example.aura;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmergencyCard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmergencyCardAdapter adapter;
    private List<ECard> emergencyCards;
    private FirebaseFirestore db;
//    private String userId = "testUser123"; // Hardcoded user ID for testing

    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_card);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize the data list and adapter
        emergencyCards = new ArrayList<>();
        adapter = new EmergencyCardAdapter(emergencyCards, this);
        recyclerView.setAdapter(adapter);

        // Set up a real-time listener for Firestore updates
        setupRealTimeUpdates();

        // Back button functionality
        ImageButton btnBack = findViewById(R.id.BtnBack);
        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(EmergencyCard.this, Emergency.class);
            startActivity(i);
            finish();
        });

        // Add Card button functionality
        ImageButton btnAddCard = findViewById(R.id.BtnAddCard);
        btnAddCard.setOnClickListener(v -> {
            Intent i = new Intent(EmergencyCard.this, addECard.class);
            startActivityForResult(i, 1); // Request code 1 for adding a new card
        });
    }

    private void setupRealTimeUpdates() {
        db.collection("users").document(userId).collection("emergencyCards")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("EmergencyCard", "Firestore listener error", error);
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange change : value.getDocumentChanges()) {
                            DocumentSnapshot snapshot = change.getDocument();
                            ECard card = new ECard(
                                    snapshot.getId(),
                                    snapshot.getString("name"),
                                    snapshot.getString("bloodType"),
                                    snapshot.getString("dateOfBirth"),
                                    snapshot.getString("height"),
                                    snapshot.getString("weight"),
                                    snapshot.getString("medicalCondition"),
                                    snapshot.getString("medication"),
                                    snapshot.getString("allergies"),
                                    snapshot.getString("emergencyContact")
                            );

                            switch (change.getType()) {
                                case ADDED:
                                    emergencyCards.add(card);
                                    break;
                                case MODIFIED:
                                    updateCard(card);
                                    break;
                                case REMOVED:
                                    removeCard(card.getDocumentId());
                                    break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Reload the Firestore listener to fetch new cards
            emergencyCards.clear();
            setupRealTimeUpdates();
        }
    }

    private void updateCard(ECard updatedCard) {
        for (int i = 0; i < emergencyCards.size(); i++) {
            if (emergencyCards.get(i).getDocumentId().equals(updatedCard.getDocumentId())) {
                emergencyCards.set(i, updatedCard);
                break;
            }
        }
    }

    private void removeCard(String documentId) {
        emergencyCards.removeIf(card -> card.getDocumentId().equals(documentId));
    }

    // Adapter class to display emergency cards
    public static class EmergencyCardAdapter extends RecyclerView.Adapter<EmergencyCardAdapter.ViewHolder> {

        private final List<ECard> emergencyCards;
        private final Context context;

        public EmergencyCardAdapter(List<ECard> emergencyCards, Context context) {
            this.emergencyCards = emergencyCards;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emergency_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ECard card = emergencyCards.get(position);
            holder.profileName.setText(card.getFull_name());
            holder.profileBloodType.setText("Blood Type: " + card.getBlood_type());

            // Handle item click
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("DOCUMENT_ID", card.getDocumentId());
                intent.putExtra("NAME", card.getFull_name());
                intent.putExtra("DATE_OF_BIRTH", card.getDate_of_birth());
                intent.putExtra("HEIGHT", card.getHeight());
                intent.putExtra("WEIGHT", card.getWeight());
                intent.putExtra("BLOOD_TYPE", card.getBlood_type());
                intent.putExtra("MEDICAL_CONDITION", card.getMedical_condition());
                intent.putExtra("ALLERGIES", card.getAllergies_and_reactions());
                intent.putExtra("MEDICATION", card.getMedication());
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return emergencyCards.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView profileName, profileBloodType;
            ImageView profileIcon;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                profileName = itemView.findViewById(R.id.profileName);
                profileBloodType = itemView.findViewById(R.id.profileBloodType);
                profileIcon = itemView.findViewById(R.id.profileIcon);
            }
        }

    }

    // Model class for Emergency Card
    public static class ECard {
        private final String documentId; // Firestore document ID
        private final String full_name;
        private final String blood_type;
        private final String date_of_birth;
        private final String height;
        private final String weight;
        private final String medical_condition;
        private final String medication;
        private final String allergies_and_reactions;
        private final String emergency_contact;

        public ECard(String documentId, String full_name, String blood_type, String date_of_birth, String height, String weight,
                     String medical_condition, String medication, String allergies_and_reactions, String emergency_contact) {
            this.documentId = documentId;
            this.full_name = full_name;
            this.blood_type = blood_type;
            this.date_of_birth = date_of_birth;
            this.height = height;
            this.weight = weight;
            this.medical_condition = medical_condition;
            this.medication = medication;
            this.allergies_and_reactions = allergies_and_reactions;
            this.emergency_contact = emergency_contact;
        }

        public String getDocumentId() {
            return documentId;
        }

        public String getFull_name() {
            return full_name;
        }

        public String getBlood_type() {
            return blood_type;
        }

        public String getDate_of_birth() {
            return date_of_birth;
        }

        public String getHeight() {
            return height;
        }

        public String getWeight() {
            return weight;
        }

        public String getMedical_condition() {
            return medical_condition;
        }

        public String getMedication() {
            return medication;
        }

        public String getAllergies_and_reactions() {
            return allergies_and_reactions;
        }

        public String getEmergency_contact() {
            return emergency_contact;
        }
    }
}
