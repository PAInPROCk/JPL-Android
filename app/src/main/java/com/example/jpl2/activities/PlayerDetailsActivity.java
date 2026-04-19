package com.example.jpl2.activities;

import static com.example.jpl2.api.ApiClient.BASE_URL;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.jpl2.R;

public class PlayerDetailsActivity extends AppCompatActivity {

    ImageView playerImage;
    TextView playerName, playerRole, playerPrice, playerNickName, playerStyle, playerAge, playerGender, playerTeam, playerCategory, playerTotalRuns, playerHighestRuns, playerWicketsTaken, playerOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        //Image
        playerImage = findViewById(R.id.playerImage);

        //text views
        playerName = findViewById(R.id.playerName);
        playerNickName = findViewById(R.id.playerNickName);
        playerCategory = findViewById(R.id.playerCategory);
        playerStyle = findViewById(R.id.playerStyle);
        playerRole = findViewById(R.id.playerRole);
        playerAge = findViewById(R.id.playerAge);
        playerPrice = findViewById(R.id.playerBasePrice);
        playerGender = findViewById(R.id.playerGender);
        playerTeam = findViewById(R.id.playerTeams);
        playerTotalRuns = findViewById(R.id.playerTotalRuns);
        playerHighestRuns = findViewById(R.id.playerHighestRuns);
        playerWicketsTaken = findViewById(R.id.playerWicketsTaken);
        playerOut = findViewById(R.id.playerOut);

        // GET DATA FROM INTENT
        String name = getIntent().getStringExtra("name");
        String nickName = getIntent().getStringExtra("nickname");
        String category = getIntent().getStringExtra("category");
        String type = getIntent().getStringExtra("type");
        String role = getIntent().getStringExtra("role");
        int totalRuns = getIntent().getIntExtra("total_runs", 0);
        int highestRuns = getIntent().getIntExtra("highest_runs", 0);
        int wicketsTaken = getIntent().getIntExtra("wickets_taken", 0);
        int out = getIntent().getIntExtra("times_out", 0);
        int age = getIntent().getIntExtra("age", 0);
        int price = getIntent().getIntExtra("price", 0);
        String gender = getIntent().getStringExtra("gender");
        String teams = getIntent().getStringExtra("teams");
        String image = getIntent().getStringExtra("image");

        playerName.setText(name);
        playerNickName.setText(nickName);
        playerCategory.setText(category);
        playerStyle.setText(type);
        playerAge.setText(String.valueOf(age));
        playerGender.setText(gender);
        playerTeam.setText(TextUtils.isEmpty(teams) ? "No teams recorded" : teams);
        playerRole.setText(role);
        playerPrice.setText("₹ " + price);
        playerOut.setText(String.valueOf(out));
        playerWicketsTaken.setText(String.valueOf(wicketsTaken));
        playerTotalRuns.setText(String.valueOf(totalRuns));
        playerHighestRuns.setText(String.valueOf(highestRuns));

        String imageUrl;

        if(image != null && !image.isEmpty()){
            imageUrl = BASE_URL + image;
        }else {
            imageUrl = null;
        }

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.player)
                .error(R.drawable.player)
                .circleCrop()
                .into(playerImage);
    }
}