package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jpl2.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage, btnBack;
    private TextView name, email, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 🔙 Back Button
        btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 📌 Initialize Views
        profileImage = findViewById(R.id.profileImage);
        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        role = findViewById(R.id.profileRole);

        // 🧪 Dummy Data (Replace later with API data)
        if (name != null) {
            name.setText("Prathamesh Admin");
        }

        if (email != null) {
            email.setText("admin@example.com");
        }

        if (role != null) {
            role.setText("Admin");
        }

        if (profileImage != null) {
            profileImage.setImageResource(R.drawable.profileicon);
        }
    }
}