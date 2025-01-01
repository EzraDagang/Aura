package com.example.aura;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class EmergencyCard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmergencyCardAdapter adapter;
    private List<ECard> emergencyCards;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_card);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the data list with hardcoded data
        emergencyCards = new ArrayList<>();
        emergencyCards.add(new ECard("Jane Doe", "A+", "1995-05-05", "170cm", "60kg", "Asthma", "Inhaler", "None", "987654321"));
        emergencyCards.add(new ECard("John Smith", "O-", "1990-03-22", "180cm", "75kg", "Diabetes", "Insulin", "Peanuts", "123456789"));
        emergencyCards.add(new ECard("Alice Brown", "B+", "1988-12-12", "165cm", "55kg", "Hypertension", "Beta Blockers", "Latex", "555555555"));

        // Initialize the adapter
        adapter = new EmergencyCardAdapter(emergencyCards, this);
        recyclerView.setAdapter(adapter);

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

    // Handle the result from addECard activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Retrieve the data from the intent
            String name = data.getStringExtra("NAME");
            String bloodType = data.getStringExtra("BLOOD_TYPE");
            String dob = data.getStringExtra("DATE_OF_BIRTH");
            String height = data.getStringExtra("HEIGHT");
            String weight = data.getStringExtra("WEIGHT");
            String medicalCondition = data.getStringExtra("MEDICAL_CONDITION");
            String medication = data.getStringExtra("MEDICATION");
            String allergies = data.getStringExtra("ALLERGIES");
            String emergencyContact = data.getStringExtra("EMERGENCY_CONTACT");

            // Add the new ECard
            emergencyCards.add(new ECard(name, bloodType, dob, height, weight, medicalCondition, medication, allergies, emergencyContact));
            adapter.notifyDataSetChanged(); // Update the RecyclerView
        }
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
        private final String full_name;
        private final String blood_type;
        private final String date_of_birth;
        private final String height;
        private final String weight;
        private final String medical_condition;
        private final String medication;
        private final String allergies_and_reactions;
        private final String emergency_contact;

        public ECard(String full_name, String blood_type, String date_of_birth, String height, String weight, String medical_condition, String medication, String allergies_and_reactions, String emergency_contact) {
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
