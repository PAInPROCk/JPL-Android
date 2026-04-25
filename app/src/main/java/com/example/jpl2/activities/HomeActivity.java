package com.example.jpl2.activities;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.jpl2.R;
import com.example.jpl2.api.ApiClient;
import com.example.jpl2.utils.SessionManager;
import com.example.jpl2.viewmodel.AuthViewModel;

public class HomeActivity extends AppCompatActivity {

    private View btnTeams, btnPlayers, btnAuction, btnRegistration, btnAdmin, btnLogin;
    private ImageView profileIcon;

    private AuthViewModel viewModel;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new SessionManager(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Views
        btnTeams = findViewById(R.id.btn_teams);
        btnPlayers = findViewById(R.id.btn_players);
        btnAuction = findViewById(R.id.btn_auction);
        btnRegistration = findViewById(R.id.btn_registration);
        btnAdmin = findViewById(R.id.btn_admin);
        btnLogin = findViewById(R.id.btn_login);
        profileIcon = findViewById(R.id.profileIcon);

        try {

            // Normal cards
            setupCard(btnTeams, R.drawable.teamsicon, "Teams", TeamsActivity.class);
            setupCard(btnPlayers, R.drawable.player, "Players", PlayerActivity.class);
            setupCard(btnRegistration, R.drawable.registrationicon, "Register", RegistrationActivity.class);

            // Special cards
            setupAuctionCard();
            setupLoginCard();
            setupProfileIcon();
            setupAdminCard();

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh latest session state after login/logout/profile return
        session = new SessionManager(this);

        setupLoginCard();
        setupProfileIcon();
    }

    // ------------------------------------------------
    // Reusable Card Setup
    // ------------------------------------------------
    private void setupCard(View card, int iconRes, String title, Class<?> targetActivity) {

        ImageView icon = card.findViewById(R.id.icon);
        TextView text = card.findViewById(R.id.title);

        if (icon != null) icon.setImageResource(iconRes);
        if (text != null) text.setText(title);

        card.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, targetActivity))
        );
    }

    // ------------------------------------------------
    // Auction Button
    // ------------------------------------------------
    private void setupAuctionCard() {

        ImageView icon = btnAuction.findViewById(R.id.icon);
        TextView text = btnAuction.findViewById(R.id.title);

        icon.setImageResource(R.drawable.auctionicon);
        text.setText("Auction");

        btnAuction.setOnClickListener(v -> {

            if (!session.isLoggedIn()) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (session.isTeam()) {
                startActivity(new Intent(this, Waiting_Activity.class));
            } else {
                Toast.makeText(this,
                        "Only team users can join auction",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ------------------------------------------------
    // Login / Profile Button
    // ------------------------------------------------
    private void setupLoginCard() {

        ImageView icon = btnLogin.findViewById(R.id.icon);
        TextView text = btnLogin.findViewById(R.id.title);

        if (session.isLoggedIn()) {

            icon.setImageResource(R.drawable.profileicon);
            text.setText("Profile");

            btnLogin.setOnClickListener(v ->
                    startActivity(new Intent(this, ProfileActivity.class))
            );

        } else {

            icon.setImageResource(R.drawable.loginicon);
            text.setText("Login");

            btnLogin.setOnClickListener(v ->
                    startActivity(new Intent(this, LoginActivity.class))
            );
        }
    }

    // ------------------------------------------------
    // Top Right Profile Icon
    // ------------------------------------------------
    private void setupProfileIcon() {

        if (profileIcon == null) return;

        // Always reset first
        profileIcon.setImageResource(R.drawable.profileicon);
        profileIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);

        profileIcon.setOnClickListener(v -> {

            if (session.isLoggedIn()) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });

        if (session.isLoggedIn()) {

            String imagePath = session.getTeamLogo();

            if (imagePath != null && !imagePath.isEmpty()) {

                Glide.with(this)
                        .load(ApiClient.BASE_URL + imagePath)
                        .placeholder(R.drawable.profileicon)
                        .error(R.drawable.profileicon)
                        .into(profileIcon);
            }
        }
    }

    // ------------------------------------------------
    // Admin Button
    // ------------------------------------------------
    private void setupAdminCard() {

        ImageView icon = btnAdmin.findViewById(R.id.icon);
        TextView text = btnAdmin.findViewById(R.id.title);

        icon.setImageResource(R.drawable.adminicon);
        text.setText("Admin");

        btnAdmin.setOnClickListener(v -> {

            if (!session.isLoggedIn()) {
                Toast.makeText(this,
                        "Please login first",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (session.isAdmin()) {
                startActivity(new Intent(this, AdminActivity.class));
            } else {
                Toast.makeText(this,
                        "Access Denied",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}