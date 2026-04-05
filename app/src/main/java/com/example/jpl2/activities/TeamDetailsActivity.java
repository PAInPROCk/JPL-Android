package com.example.jpl2.activities;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.jpl2.api.ApiClient;
import com.example.jpl2.network.ApiService;
import com.example.jpl2.model.TeamResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.jpl2.R;

public class TeamDetailsActivity extends AppCompatActivity {

    ImageView teamLogo;
    TextView teamName, captain, budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);

        teamLogo = findViewById(R.id.teamLogo);
        teamName = findViewById(R.id.teamName);
        captain = findViewById(R.id.captain);
        budget = findViewById(R.id.budget);

        int teamId = getIntent().getIntExtra("team_id", -1);

        ApiService api = ApiClient.getClient().create(ApiService.class);

        String name = getIntent().getStringExtra("team_name");
        String imagePath = getIntent().getStringExtra("team_logo");

        teamName.setText(name);
        captain.setText("Captain: N/A");
        budget.setText("Budget: ₹ 0");

        //teamName.setText(team.getName());
        //captain.setText("Captain: " + (team.getCaptain() == null ? "N/A" : team.getCaptain()));
        //budget.setText("Budget: ₹ " + (team.getCurrentBudget() == null ? "0" : team.getCurrentBudget()));


        String imageUrl = "http://192.168.0.101:5000/" + imagePath;

        Log.d("IMAGE_DEBUG", "DETAIL IMAGE = " + imageUrl);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(teamLogo);
    }
}