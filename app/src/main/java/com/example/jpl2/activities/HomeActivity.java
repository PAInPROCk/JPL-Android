package com.example.jpl2.activities;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.jpl2.R;
import com.example.jpl2.viewmodel.AuthViewModel;

public class HomeActivity extends AppCompatActivity {

    private View btnTeams, btnPlayers, btnAuction, btnRegistration, btnAdmin, btnLogin;
    private ImageView profileIcon;

    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // ViewModel
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Find views
        btnTeams = findViewById(R.id.btn_teams);
        btnPlayers = findViewById(R.id.btn_players);
        btnAuction = findViewById(R.id.btn_auction);
        btnRegistration = findViewById(R.id.btn_registration);
        btnAdmin = findViewById(R.id.btn_admin);
        btnLogin = findViewById(R.id.btn_login);
        profileIcon = findViewById(R.id.profileIcon);

        try {

            // Setup cards
            setupCard(btnTeams, R.drawable.teamsicon, "Teams", TeamsActivity.class);
            setupCard(btnPlayers, R.drawable.player, "Players", PlayerActivity.class);
            setupCard(btnAuction, R.drawable.auctionicon, "Auction", Waiting_Activity.class);
            setupCard(btnRegistration, R.drawable.registrationicon, "Register", RegistrationActivity.class);
            setupCard(btnLogin, R.drawable.loginicon, "Login", LoginActivity.class);

            // ✅ PROFILE ICON LOGIC
            if (profileIcon != null) {

                // Default image first
                profileIcon.setImageResource(R.drawable.profileicon);

                // Click → Profile page
                profileIcon.setOnClickListener(v ->
                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class))
                );

                // Observe user data
                viewModel.getAuthResult().observe(this, result -> {

                    if (result != null && result.authenticated && result.user != null) {

                        String imagePath = result.user.teamLogo;

                        if (imagePath != null && !imagePath.isEmpty()) {

                            String imageUrl = "http://192.168.0.103:5000/" + imagePath;

                            Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.profileicon)
                                    .error(R.drawable.profileicon)
                                    .into(profileIcon);

                        } else {
                            profileIcon.setImageResource(R.drawable.profileicon);
                        }

                    } else {
                        profileIcon.setImageResource(R.drawable.profileicon);
                    }
                });
            }

            // Admin logic
            setupAdminCard();

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), LENGTH_LONG).show();
        }
    }

    // -----------------------------
    // Card setup
    // -----------------------------
    private void setupCard(View card, int iconRes, String title, Class<?> targetActivity) {

        ImageView icon = card.findViewById(R.id.icon);
        TextView text = card.findViewById(R.id.title);

        if (icon != null) icon.setImageResource(iconRes);
        if (text != null) text.setText(title);

        card.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, targetActivity))
        );
    }

    // -----------------------------
    // Admin card
    // -----------------------------
    private void setupAdminCard() {

        ImageView icon = btnAdmin.findViewById(R.id.icon);
        TextView text = btnAdmin.findViewById(R.id.title);

        if (icon != null) icon.setImageResource(R.drawable.adminicon);
        if (text != null) text.setText("Admin");

        viewModel.getAuthResult().observe(this, result -> {

            if (result == null || !result.authenticated || result.user == null) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("admin".equals(result.user.role)) {
                startActivity(new Intent(HomeActivity.this, AdminActivity.class));
            } else {
                Toast.makeText(this, "Access Denied: Not Admin", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdmin.setOnClickListener(v ->
                viewModel.checkAuth(this)
        );
    }
}