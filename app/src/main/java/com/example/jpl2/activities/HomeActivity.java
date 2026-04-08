package com.example.jpl2.activities;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jpl2.R;

public class HomeActivity extends AppCompatActivity {

    Button loginBtn, registerBtn, auctionBtn, bidBtn, web, adminbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        loginBtn = findViewById(R.id.btn_login);
        registerBtn = findViewById(R.id.btn_registration);
        auctionBtn = findViewById(R.id.btn_auction);
        bidBtn = findViewById(R.id.btn_players); // or change based on logic
        web = findViewById(R.id.btn_teams); // temporary mapping
        adminbtn = findViewById(R.id.btn_admin); //Takes to admin page

        try {

            loginBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, LoginActivity.class)));

            web.setOnClickListener(v ->
                    startActivity(new Intent(this, TeamsActivity.class)));

            bidBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, PlayerActivity.class)));
            adminbtn.setOnClickListener(v ->
                    startActivity(new Intent(this, AdminActivity.class))); //For testing there is no auth check //todo Add auth check so that only admin users can acces the page

        } catch (Exception e) {
            Toast.makeText(HomeActivity.this, e.toString(), LENGTH_LONG).show();
        }
    }
}