package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jpl2.R;

public class sold_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold);

        TextView name = findViewById(R.id.splayerName);
        TextView price = findViewById(R.id.soldPrice);
        TextView team = findViewById(R.id.teamName);

        name.setText(getIntent().getStringExtra("player_name"));
        price.setText("₹" + getIntent().getDoubleExtra("sold_price", 0));
        team.setText(getIntent().getStringExtra("team_name"));

        // 🔥 AUTO RETURN AFTER 10 SEC
        new android.os.Handler().postDelayed(() -> {
            finish();
        }, 10000);
    }
}