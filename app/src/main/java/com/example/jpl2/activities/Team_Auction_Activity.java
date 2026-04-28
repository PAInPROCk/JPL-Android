package com.example.jpl2.activities;

import static com.example.jpl2.api.ApiClient.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jpl2.R;
import com.example.jpl2.adapter.NotificationAdapter;
import com.example.jpl2.network.SocketManager;
import com.example.jpl2.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;

public class Team_Auction_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ArrayList<String> notifications;

    TextView pName, pCategory, pStyle;
    TextView timerText, playerBasePrice, playerCurrentPrice;
    TextView teamBidPrice, teamPurse;

    ImageView playerImage, teamLogo;

    Button bidButton;

    Socket socket;
    SessionManager session;

    int currentBid = 0;
    int teamPurseValue = 0;
    int currentPlayerId = 0;

    boolean paused = false;

    final int MIN_INCREMENT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        // -------------------------
        // Protect Page (Team Only)
        // -------------------------
        if (!session.isLoggedIn()) {
            Toast.makeText(this,
                    "Please login first",
                    Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        if (!session.isTeam()) {
            Toast.makeText(this,
                    "Only team users allowed",
                    Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_auction);

        initViews();
        initRecycler();
        loadSessionData();

        socket = SocketManager.getSocket();

        setupSocketListeners();

        socket.connect();

        updateBidButton();
    }

    // -----------------------------------
    // Views
    // -----------------------------------
    private void initViews() {

        recyclerView = findViewById(R.id.notificationRecycler);

        pName = findViewById(R.id.pName);
        pCategory = findViewById(R.id.pCategory);
        pStyle = findViewById(R.id.pStyle);

        timerText = findViewById(R.id.timerText);

        playerBasePrice =
                findViewById(R.id.playerBasePrice);

        playerCurrentPrice =
                findViewById(R.id.playerCurrentPrice);

//        teamBidPrice =
//                findViewById(R.id.teamBidPrice);

        teamPurse =
                findViewById(R.id.teamPurse);

        playerImage =
                findViewById(R.id.playerImage);

        teamLogo =
                findViewById(R.id.teamLogo);

        bidButton =
                findViewById(R.id.bidButton);

        bidButton.setOnClickListener(v ->
                placeBid());
    }

    // -----------------------------------
    // Recycler
    // -----------------------------------
    private void initRecycler() {

        notifications = new ArrayList<>();
        notifications.add("Waiting for Auction...");

        adapter =
                new NotificationAdapter(
                        notifications
                );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(adapter);
    }

    // -----------------------------------
    // Session Data
    // -----------------------------------
    private void loadSessionData() {

        teamPurseValue =
                session.getTeamPurse();

        teamPurse.setText(
                "₹" + teamPurseValue
        );

        String logo =
                session.getTeamLogo();

        if (logo != null && !logo.isEmpty()) {

            Glide.with(this)
                    .load(BASE_URL + logo)
                    .placeholder(R.drawable.teams)
                    .error(R.drawable.teams)
                    .into(teamLogo);
        }
    }

    // -----------------------------------
    // Socket Events
    // -----------------------------------
    private void setupSocketListeners() {

        socket.on(Socket.EVENT_CONNECT,
                args -> runOnUiThread(() -> {

                    try {

                        JSONObject obj =
                                new JSONObject();

                        obj.put(
                                "team_id",
                                session.getTeamId()
                        );

                        socket.emit(
                                "join_auction",
                                obj
                        );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        // Current auction state
        socket.on("auction_status",
                args -> runOnUiThread(() -> {

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        if (!data.getString("status")
                                .equals("auction_active"))
                            return;

                        JSONObject player =
                                data.getJSONObject("player");

                        int base = (int)
                                player.getDouble(
                                        "base_price"
                                );

                        int current =
                                data.has("highest_bid")
                                        && !data.isNull("highest_bid")
                                        ? data.getJSONObject(
                                                "highest_bid")
                                        .getInt(
                                                "bid_amount")
                                        : base;

                        currentBid = current;

                        currentPlayerId =
                                player.getInt("id");

                        bindPlayer(player);

                        playerCurrentPrice.setText(
                                "₹" + current
                        );

                        int purse =
                                data.optInt(
                                        "team_purse",
                                        teamPurseValue
                                );

                        updatePurse(purse);

                        paused = false;

                        updateBidButton();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        // New player started
        socket.on("auction_started",
                args -> runOnUiThread(() -> {

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        JSONObject player =
                                data.getJSONObject("player");

                        currentPlayerId =
                                player.getInt("id");

                        currentBid = (int)
                                data.getDouble(
                                        "current_bid"
                                );

                        bindPlayer(player);

                        timerText.setText(
                                formatTime(
                                        data.getInt(
                                                "duration"
                                        )
                                )
                        );

                        paused = false;

                        notifications.clear();
                        notifications.add(
                                player.getString("name")
                                        + " is live now!"
                        );

                        adapter.notifyDataSetChanged();

                        updateBidButton();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        // Bid updates + history
        socket.on("auction_update",
                args -> runOnUiThread(() -> {

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        currentBid =
                                data.optInt(
                                        "current_bid",
                                        currentBid
                                );

                        playerCurrentPrice.setText(
                                "₹" + currentBid
                        );

                        teamBidPrice.setText(
                                "₹" + currentBid
                        );

                        if (data.has("history")) {

                            notifications.clear();

                            JSONArray arr =
                                    data.getJSONArray(
                                            "history"
                                    );

                            for (int i = 0;
                                 i < arr.length();
                                 i++) {

                                JSONObject item =
                                        arr.getJSONObject(i);

                                notifications.add(
                                        item.getString(
                                                "team_name")
                                                + " bid ₹"
                                                + item.getInt(
                                                "bid_amount")
                                );
                            }

                            adapter.notifyDataSetChanged();
                        }

                        updateBidButton();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        // Timer
        socket.on("timer_update",
                args -> runOnUiThread(() -> {

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        int sec =
                                data.getInt(
                                        "remaining_seconds"
                                );

                        timerText.setText(
                                formatTime(sec)
                        );

                        if (sec <= 0) {
                            bidButton.setEnabled(false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        // Paused
        socket.on("auction_paused",
                args -> runOnUiThread(() -> {

                    paused = true;
                    updateBidButton();

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        timerText.setText(
                                formatTime(
                                        data.getInt(
                                                "remaining_seconds"
                                        )
                                )
                        );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        // Resumed
        socket.on("auction_resumed",
                args -> runOnUiThread(() -> {

                    paused = false;
                    updateBidButton();

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        timerText.setText(
                                formatTime(
                                        data.getInt(
                                                "remaining_seconds"
                                        )
                                )
                        );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        // Purse update
        socket.on("purse_update",
                args -> runOnUiThread(() -> {

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        int purse =
                                data.getInt("purse");

                        updatePurse(purse);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        // Bid rejected
        socket.on("bid_rejected",
                args -> runOnUiThread(() -> {

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        Toast.makeText(
                                this,
                                data.optString(
                                        "error",
                                        "Bid Rejected"
                                ),
                                Toast.LENGTH_SHORT
                        ).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        // Auction ended
        socket.on("auction_ended",
                args -> runOnUiThread(() -> {

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        Intent intent;

                        if (data.getString("status")
                                .equals("sold")) {

                            intent =
                                    new Intent(
                                            this,
                                            sold_activity.class
                                    );

                        } else {

                            intent =
                                    new Intent(
                                            this,
                                            unsold_activity.class
                                    );
                        }

                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
    }

    // -----------------------------------
    // Bind Player
    // -----------------------------------
    private void bindPlayer(JSONObject player)
            throws Exception {

        pName.setText(
                player.optString("name")
        );

        pCategory.setText(
                player.optString("category")
        );

        pStyle.setText(
                player.optString("type")
        );

        int base = (int)
                player.getDouble("base_price");

        playerBasePrice.setText(
                "₹" + base
        );

        playerCurrentPrice.setText(
                "₹" + currentBid
        );

        teamBidPrice.setText(
                "₹" + currentBid
        );

        String image =
                player.optString(
                        "image_path"
                );

        Glide.with(this)
                .load(BASE_URL + image)
                .placeholder(R.drawable.player)
                .error(R.drawable.player)
                .into(playerImage);

        Log.d("PLAYER_DATA", player.toString());
    }

    // -----------------------------------
    // Place Bid
    // -----------------------------------
    private void placeBid() {

        if (paused) {
            Toast.makeText(this,
                    "Auction Paused",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int nextBid =
                currentBid + MIN_INCREMENT;

        if (nextBid > teamPurseValue) {

            Toast.makeText(this,
                    "Insufficient Purse",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        try {

            JSONObject obj =
                    new JSONObject();

            obj.put(
                    "team_id",
                    session.getTeamId()
            );

            obj.put(
                    "player_id",
                    currentPlayerId
            );

            obj.put(
                    "bid_amount",
                    nextBid
            );

            socket.emit(
                    "place_bid",
                    obj
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------
    // Helpers
    // -----------------------------------
    private void updateBidButton() {

        int nextBid =
                currentBid + MIN_INCREMENT;

        bidButton.setText(
                "₹" + nextBid
        );

        bidButton.setEnabled(
                !paused &&
                        nextBid <= teamPurseValue &&
                        currentPlayerId != 0
        );
    }

    private void updatePurse(int purse) {

        teamPurseValue = purse;

        session.updateTeamPurse(purse);

        teamPurse.setText(
                "₹" + purse
        );

        updateBidButton();
    }

    private String formatTime(int sec) {

        int min = sec / 60;
        int rem = sec % 60;

        return String.format(
                "%02d:%02d",
                min,
                rem
        );
    }

    // -----------------------------------
    // Cleanup
    // -----------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socket != null) {

            socket.off("auction_status");
            socket.off("auction_started");
            socket.off("auction_update");
            socket.off("timer_update");
            socket.off("auction_paused");
            socket.off("auction_resumed");
            socket.off("auction_ended");
            socket.off("purse_update");
            socket.off("bid_rejected");

            socket.disconnect();
        }
    }
}