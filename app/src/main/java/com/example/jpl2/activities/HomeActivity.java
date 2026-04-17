package com.example.jpl2.activities;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.viewmodel.AuthViewModel;

public class HomeActivity extends AppCompatActivity {

    Button teamsBtn, playersBtn, auctionBtn, registrationBtn, adminBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Match IDs with your XML
        teamsBtn = findViewById(R.id.btn_teams);
        playersBtn = findViewById(R.id.btn_players);
        auctionBtn = findViewById(R.id.btn_auction);
        registrationBtn = findViewById(R.id.btn_registration);
        adminBtn = findViewById(R.id.btn_admin);
        loginBtn = findViewById(R.id.btn_login);

        try {
            // Example: Login button opens LoginActivity
            loginBtn.setOnClickListener(v ->
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class)));

            // Example: Auction button opens Waiting_activity which checks live auction and then redirect to auction page
            auctionBtn.setOnClickListener(v ->
                    startActivity(new Intent(HomeActivity.this, Waiting_Activity.class)));

            // Example: Teams button opens TeamsActivity
            teamsBtn.setOnClickListener(v ->
                    startActivity(new Intent(HomeActivity.this, TeamsActivity.class)));

            // Example: Players button opens PlayersActivity
//            playersBtn.setOnClickListener(v ->
//                    startActivity(new Intent(HomeActivity.this, PlayerActivity.class)));

            // Example: Registration button opens RegistrationActivity
            registrationBtn.setOnClickListener(v ->
                    startActivity(new Intent(HomeActivity.this, RegistrationActivity.class)));

            // Example: Admin button opens AdminActivity
            AuthViewModel viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

// ✅ OBSERVER (OUTSIDE CLICK)
            viewModel.getAuthResult().observe(this, result -> {

                if(result == null || !result.authenticated || result.user == null){
                    Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                    return;
                }

                String role = result.user.role;

                if(role.equals("admin")){
                    startActivity(new Intent(HomeActivity.this, AdminActivity.class));
                } else {
                    Toast.makeText(this, "Access Denied: Not Admin", Toast.LENGTH_SHORT).show();
                }
            });

// ✅ BUTTON CLICK (ONLY API CALL)
            adminBtn.setOnClickListener(v -> {
                viewModel.checkAuth(this);
            });

        } catch (Exception e) {
            Toast.makeText(HomeActivity.this, e.toString(), LENGTH_LONG).show();
        }
    }
}