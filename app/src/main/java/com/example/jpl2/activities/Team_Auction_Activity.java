package com.example.jpl2.activities;

import static com.example.jpl2.api.ApiClient.BASE_URL;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jpl2.R;
import com.example.jpl2.adapter.NotificationAdapter;
import com.example.jpl2.network.SocketManager;
import com.example.jpl2.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;

public class Team_Auction_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ArrayList<String> notifications;

    TextView pName, pCategory, pStyle, timerText, playerBasePrice, playerCurrentPrice;
    TextView teamBidPrice, teamPurse;

    Button bidButton;

    ImageView playerImage, uteamLogo;

    Socket socket;

    int currentBid = 0;

    public void addNotification(String msg) {
        notifications.add(0, msg);
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_auction);

        SessionManager session = new SessionManager(this);

        int teamId = session.getTeamId();
        String teamName = session.getTeamName();
        String teamLogo = session.getTeamLogo();
        int purse = session.getTeamPurse();



        // Views
        recyclerView = findViewById(R.id.notificationRecycler);
        timerText = findViewById(R.id.timerText);
        playerBasePrice = findViewById(R.id.playerBasePrice);
        playerCurrentPrice = findViewById(R.id.playerCurrentPrice);
        uteamLogo = findViewById(R.id.teamLogo);
        teamBidPrice = findViewById(R.id.teamBidPrice);
        teamPurse = findViewById(R.id.teamPurse);
        bidButton = findViewById(R.id.bidButton);
        playerImage = findViewById(R.id.playerImage);
        pName = findViewById(R.id.pName);
        pCategory = findViewById(R.id.pCategory);
        pStyle = findViewById(R.id.pStyle);

        // Recycler
        notifications = new ArrayList<>();
        notifications.add("Waiting for Auction...");

        adapter = new NotificationAdapter(notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Dummy purse
        teamPurse.setText("₹"+ purse);

        // Socket
        socket = SocketManager.getSocket();
        socket.connect();

        socket.emit("join_auction");

        // Auction Started
        socket.on("auction_started", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];

                JSONObject player = data.getJSONObject("player");
//                JSONObject team = data.getJSONObject("team");

                String name = player.optString("name", "N/A");
                String image = player.getString("image_path");
                String category = player.optString("category", "N/A");
                String style = player.optString("type", "N/A");

//                String teamLogo = team.optString("image_path", "");

                currentBid = player.optInt("base_price", 0);

                pName.setText(name);
                pCategory.setText(category);
                pStyle.setText(style);
                playerBasePrice.setText("₹" + currentBid);
                playerCurrentPrice.setText("₹" + currentBid);

                String imageUrl = player.optString("image","");

                Glide.with(this)
                        .load(BASE_URL + imageUrl)
                        .placeholder(R.drawable.player)
                        .into(playerImage);

                Glide.with(this)
                        .load(BASE_URL + teamLogo)
                        .placeholder(R.drawable.teams)
                        .error(R.drawable.teams)
                        .into(uteamLogo);

                addNotification(name + " is live now!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // Timer Update
        socket.on("timer_update", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];

                int sec = data.getInt("remaining_seconds");

                int min = sec / 60;
                int rem = sec % 60;

                timerText.setText(String.format("%02d:%02d", min, rem));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // New Bid Update
        socket.on("new_bid", args -> runOnUiThread(() -> {
            try {
                JSONObject data = (JSONObject) args[0];

                String team = data.getString("team_name");
                int amount = data.getInt("amount");

                currentBid = amount;

                playerCurrentPrice.setText("₹" + amount);

                addNotification(team + " bid ₹" + amount);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // Bid Button Click
        bidButton.setOnClickListener(v -> {

            try {
                JSONObject obj = new JSONObject();

                obj.put("team_id", teamId); // replace with login team id
                obj.put("amount", currentBid + 500);

                socket.emit("place_bid", obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socket != null) {
            socket.off("auction_started");
            socket.off("timer_update");
            socket.off("new_bid");
            socket.disconnect();
        }
    }
}