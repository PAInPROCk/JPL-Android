package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jpl2.R;

public class unsold_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsold);

        TextView name = findViewById(R.id.uplayerName);

        name.setText(getIntent().getStringExtra("player_name"));

        boolean isCancel = getIntent().getBooleanExtra("is_cancel", false);

        new android.os.Handler().postDelayed(() -> {

            if (isCancel) {
                startActivity(new Intent(this, AdminActivity.class));
            }

            finish();
        }, 10000);
    }
}