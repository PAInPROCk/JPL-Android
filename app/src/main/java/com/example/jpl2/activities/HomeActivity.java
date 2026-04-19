package com.example.jpl2.activities;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.viewmodel.AuthViewModel;

public class HomeActivity extends AppCompatActivity {

    private View btnTeams, btnPlayers, btnAuction, btnRegistration, btnAdmin, btnLogin;

    AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Find cards
        btnTeams = findViewById(R.id.btn_teams);
        btnPlayers = findViewById(R.id.btn_players);
        btnAuction = findViewById(R.id.btn_auction);
        btnRegistration = findViewById(R.id.btn_registration);
        btnAdmin = findViewById(R.id.btn_admin);
        btnLogin = findViewById(R.id.btn_login);

        try {

            // Normal cards
            setupCard(
                    btnTeams,
                    R.drawable.teamsicon,
                    "Teams",
                    TeamsActivity.class
            );

            setupCard(
                    btnPlayers,
                    R.drawable.player,
                    "Players",
                    PlayerActivity.class
            );

            setupCard(
                    btnAuction,
                    R.drawable.auctionicon,
                    "Auction",
                    Waiting_Activity.class
            );

            setupCard(
                    btnRegistration,
                    R.drawable.registrationicon,
                    "Register",
                    RegistrationActivity.class
            );

            setupCard(
                    btnLogin,
                    R.drawable.loginicon,
                    "Login",
                    LoginActivity.class
            );

            // Admin special logic
            setupAdminCard();

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), LENGTH_LONG).show();
        }
    }


    // -----------------------------
    // Reusable setup for normal cards
    // -----------------------------
    private void setupCard(View card, int iconRes, String title, Class<?> targetActivity) {

        ImageView icon = card.findViewById(R.id.icon);
        TextView text = card.findViewById(R.id.title);

        icon.setImageResource(iconRes);
        text.setText(title);

        card.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, targetActivity))
        );
    }

    // -----------------------------
    // Admin card with auth check
    // -----------------------------
    private void setupAdminCard() {

        ImageView icon = btnAdmin.findViewById(R.id.icon);
        TextView text = btnAdmin.findViewById(R.id.title);

        icon.setImageResource(R.drawable.adminicon);
        text.setText("Admin");

        AuthViewModel viewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);

        // Observe auth result
        viewModel.getAuthResult().observe(this, result -> {

            if (result == null || !result.authenticated || result.user == null) {
                Toast.makeText(this,
                        "Please login first",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if ("admin".equals(result.user.role)) {
                startActivity(
                        new Intent(HomeActivity.this, AdminActivity.class)
                );
            } else {
                Toast.makeText(this,
                        "Access Denied: Not Admin",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Click = check auth
        btnAdmin.setOnClickListener(v ->
                viewModel.checkAuth(this)
        );
    }
}