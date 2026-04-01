package com.example.jpl2.activities;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jpl2.R;

public class HomeActivity extends AppCompatActivity {

    Button loginBtn, registerBtn, auctionBtn, bidBtn, web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        auctionBtn = findViewById(R.id.auctionBtn);
        bidBtn = findViewById(R.id.bidBtn);
        web = findViewById(R.id.web);

        try {

            loginBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, LoginActivity.class)));

            web.setOnClickListener(v ->
                    startActivity(new Intent(this, WebsiteActivity.class)));

        } catch (Exception e) {
            Toast.makeText(HomeActivity.this, e.toString(), LENGTH_LONG).show();
        }
    }
}