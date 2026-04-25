package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.jpl2.R;
import com.example.jpl2.api.ApiClient;
import com.example.jpl2.utils.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage, btnBack;
    TextView name, email, role;
    Button logoutBtn;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        session = new SessionManager(this);

        profileImage = findViewById(R.id.profileImage);
        btnBack = findViewById(R.id.btnBack);

        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        role = findViewById(R.id.profileRole);

        logoutBtn = findViewById(R.id.logoutBtn);

        btnBack.setOnClickListener(v -> finish());

        // Set real data
        name.setText(session.getUserName());
        email.setText(session.getUserEmail());
        role.setText(session.getRole());

        // Team logo if exists
        String logo = session.getTeamLogo();

        if (logo != null && !logo.isEmpty()) {
            Glide.with(this)
                    .load(ApiClient.BASE_URL + logo)
                    .placeholder(R.drawable.profileicon)
                    .into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.profileicon);
        }

        // Logout
        logoutBtn.setOnClickListener(v -> {

            session.logout();

            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish();
        });
    }
}