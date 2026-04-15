package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.viewmodel.AuthViewModel;

public class AdminActivity extends AppCompatActivity {

    AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // ✅ INIT VIEWMODEL HERE
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // ✅ CHECK AUTH HERE
        viewModel.checkAuth(this);

        viewModel.getAuthResult().observe(this, result -> {
            if(result == null || !result.authenticated || result.user == null || !result.user.role.equals("admin")){
                Toast.makeText(this, "Unauthorized", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // UI BUTTONS
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