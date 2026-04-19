package com.example.jpl2.activities;

import static com.example.jpl2.api.ApiClient.BASE_URL;

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
    TextView teamName, teamCaptain, teamBudget, totalTeamBudget, teamRank, playersBought;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);

        teamLogo = findViewById(R.id.teamImage);
        teamName = findViewById(R.id.teamName);
        teamCaptain = findViewById(R.id.teamCaptain);
        teamBudget = findViewById(R.id.teamBudget);
        totalTeamBudget = findViewById(R.id.teamTotalBudget);
        teamRank = findViewById(R.id.teamRank);
        playersBought = findViewById(R.id.playersBought);

        int teamId = getIntent().getIntExtra("team_id", -1);

        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        String name = getIntent().getStringExtra("team_name");
        String imagePath = getIntent().getStringExtra("team_logo");
        String captain = getIntent().getStringExtra("captain");
        String rank = getIntent().getStringExtra("rank");
        String totalBudget = getIntent().getStringExtra("total_budget");
        String currentBudget = getIntent().getStringExtra("current_budget");
        String bought = getIntent().getStringExtra("players_bought");
        int budget = getIntent().getIntExtra("team_budget", 0);


        teamName.setText(name != null ? name : "N/A");
        teamCaptain.setText(captain != null ? captain : "N/A");
        teamRank.setText(rank != null ? rank : "N/A");

        totalTeamBudget.setText("₹ " + (totalBudget != null ? totalBudget : "0"));
        teamBudget.setText("₹ " + (currentBudget != null ? currentBudget : "0"));

        playersBought.setText("Players Bought: " + (bought != null ? bought : "0"));

        //teamName.setText(team.getName());
        //captain.setText("Captain: " + (team.getCaptain() == null ? "N/A" : team.getCaptain()));
        //budget.setText("Budget: ₹ " + (team.getCurrentBudget() == null ? "0" : team.getCurrentBudget()));


        String imageUrl = (imagePath != null && !imagePath.isEmpty())
                ? BASE_URL + imagePath
                : null;

        Log.d("IMAGE_DEBUG", "DETAIL IMAGE = " + imageUrl);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.teams)
                .error(R.drawable.teams)
                .into(teamLogo);
    }
}