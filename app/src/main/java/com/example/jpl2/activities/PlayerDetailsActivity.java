package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.jpl2.R;

public class PlayerDetailsActivity extends AppCompatActivity {

    ImageView playerImage;
    TextView playerName, playerRole, playerPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        playerImage = findViewById(R.id.playerImage);
        playerName = findViewById(R.id.playerName);
        playerRole = findViewById(R.id.playerRole);
        playerPrice = findViewById(R.id.playerPrice);

        // GET DATA FROM INTENT
        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int price = getIntent().getIntExtra("price", 0);
        String role = getIntent().getStringExtra("role");

        playerName.setText(name);
        playerRole.setText("Role: " + role);
        playerPrice.setText("₹ " + price);

        String imageUrl = "http://192.168.0.103:5000/" + image;

        Glide.with(this)
                .load(imageUrl)
                .into(playerImage);
    }
}