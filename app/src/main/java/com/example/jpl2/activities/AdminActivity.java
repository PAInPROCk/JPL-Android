package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jpl2.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);

        Button btnPlayer = findViewById(R.id.btnPlayerReg);
        Button btnTeam = findViewById(R.id.btnTeamReg);
        Button startAuction = findViewById(R.id.startAuction);

        btnPlayer.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, PlayerRegisterActivity.class));
        });

        btnTeam.setOnClickListener(v -> {
            startActivity(new Intent(this, TeamRegisterActivity.class));
        });
        startAuction.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, AdminAuctionActivity.class));
        });
    }
}