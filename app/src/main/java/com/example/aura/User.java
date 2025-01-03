package com.example.aura;

public class User {
    public String username;
    public String email;
    public String password;
    public String email_verification_code;
    public String image_id;
    public String last_login;
    public String emergency_contact;
    public String medication;
    public String allergies_and_reactions;
    public String full_name;
    public String date_of_birth;
    public String height;
    public String weight;
    public String blood_type;
    public String medical_condition;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String username, String email, String password, String email_verification_code,
                String image_id, String last_login, String emergency_contact, String medication,
                String allergies_and_reactions, String full_name, String date_of_birth, String height,
                String weight, String blood_type, String medical_condition) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.email_verification_code = email_verification_code;
        this.image_id = image_id;
        this.last_login = last_login;
        this.emergency_contact = emergency_contact;
        this.medication = medication;
        this.allergies_and_reactions = allergies_and_reactions;
        this.full_name = full_name;
        this.date_of_birth = date_of_birth;
        this.height = height;
        this.weight = weight;
        this.blood_type = blood_type;
        this.medical_condition = medical_condition;
    }
}
