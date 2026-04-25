package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpl2.R;
import com.example.jpl2.adapter.NotificationAdapter;
import com.example.jpl2.api.ApiClient;
import com.example.jpl2.network.ApiService;
import com.example.jpl2.network.SocketManager;
import com.example.jpl2.utils.AuctionTimerManager;
import com.example.jpl2.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAuctionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ArrayList<String> notifications;

    TextView tvTimer, tvPlayerName, tvCurrentPrice;

    Button btnCancel, btnPause, btnResume, btnNext;

    Socket socket;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        // ---------------------------
        // Protect Admin Page
        // ---------------------------
        if (!session.isLoggedIn()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!session.isAdmin()) {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_auction);

        initViews();
        initRecycler();
        initTimer();
        setupButtons();

        socket = SocketManager.getSocket();
        setupSocketListeners();

        socket.connect();

        loadCurrentAuction();
    }

    // ---------------------------------------------------
    // Init Views
    // ---------------------------------------------------
    private void initViews() {

        tvTimer = findViewById(R.id.tvTimer);
        tvPlayerName = findViewById(R.id.tvPlayerName);
        tvCurrentPrice = findViewById(R.id.playerCurrentPrice);

        recyclerView = findViewById(R.id.notificationRecycler);

        btnCancel = findViewById(R.id.cancel);
        btnPause = findViewById(R.id.pause);
        btnResume = findViewById(R.id.resume);
        btnNext = findViewById(R.id.nextPlayer);

        btnPause.setEnabled(true);
        btnResume.setEnabled(false);
    }

    // ---------------------------------------------------
    // Recycler
    // ---------------------------------------------------
    private void initRecycler() {

        notifications = new ArrayList<>();
        notifications.add("Waiting for Auction...");

        adapter = new NotificationAdapter(notifications);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(adapter);
    }

    // ---------------------------------------------------
    // Timer LiveData
    // ---------------------------------------------------
    private void initTimer() {

        AuctionTimerManager.getInstance()
                .getTimer()
                .observe(this, seconds -> {

                    tvTimer.setText(
                            formatTime(seconds)
                    );
                });
    }

    // ---------------------------------------------------
    // Buttons
    // ---------------------------------------------------
    private void setupButtons() {

        btnCancel.setOnClickListener(v -> cancelAuction());

        btnPause.setOnClickListener(v -> pauseAuction());

        btnResume.setOnClickListener(v -> resumeAuction());

        btnNext.setOnClickListener(v -> nextPlayer());
    }

    // ---------------------------------------------------
    // Socket Events
    // ---------------------------------------------------
    private void setupSocketListeners() {

        socket.on(Socket.EVENT_CONNECT, args ->
                runOnUiThread(() -> {

                    socket.emit("join_auction");
                }));

        socket.on("auction_started", args ->
                runOnUiThread(() -> {

                    try {

                        JSONObject data = (JSONObject) args[0];
                        JSONObject player = data.getJSONObject("player");

                        String name = player.getString("name");
                        double price = player.getDouble("base_price");
                        int duration = data.getInt("duration");

                        updateAuctionUI(name, price, duration);

                        btnPause.setEnabled(true);
                        btnResume.setEnabled(false);
                        btnNext.setEnabled(true);

                        addNotification(name + " is live now!");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        socket.on("timer_update", args ->
                runOnUiThread(() -> {

                    try {

                        JSONObject data = (JSONObject) args[0];
                        int sec = data.getInt("remaining_seconds");

                        AuctionTimerManager
                                .getInstance()
                                .updateFromServer(sec);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        socket.on("new_bid", args ->
                runOnUiThread(() -> {

                    try {

                        JSONObject data = (JSONObject) args[0];

                        String team =
                                data.getString("team_name");

                        int amount =
                                data.getInt("amount");

                        tvCurrentPrice.setText(
                                "₹" + amount
                        );

                        addNotification(
                                team + " bid ₹" + amount
                        );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        socket.on("auction_paused", args ->
                runOnUiThread(() -> {

                    btnPause.setEnabled(false);
                    btnResume.setEnabled(true);

                    try {
                        JSONObject data = (JSONObject) args[0];

                        int sec =
                                data.getInt(
                                        "remaining_seconds"
                                );

                        AuctionTimerManager
                                .getInstance()
                                .pause(sec);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        socket.on("auction_resumed", args ->
                runOnUiThread(() -> {

                    btnPause.setEnabled(true);
                    btnResume.setEnabled(false);

                    try {

                        JSONObject data = (JSONObject) args[0];

                        int sec =
                                data.getInt(
                                        "remaining_seconds"
                                );

                        AuctionTimerManager
                                .getInstance()
                                .resume(sec);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

        socket.on("auction_ended", args ->
                runOnUiThread(() -> {

                    try {

                        JSONObject data =
                                (JSONObject) args[0];

                        String status =
                                data.getString("status");

                        AuctionTimerManager
                                .getInstance()
                                .reset();

                        Intent intent;

                        if (status.equals("sold")) {

                            intent = new Intent(
                                    this,
                                    sold_activity.class
                            );

                        } else {

                            intent = new Intent(
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

    // ---------------------------------------------------
    // API CALLS
    // ---------------------------------------------------
    private void cancelAuction() {

        btnCancel.setEnabled(false);

        getApi().cancelAuction()
                .enqueue(defaultCallback(
                        "Auction Cancelled",
                        btnCancel
                ));
    }

    private void pauseAuction() {

        btnPause.setEnabled(false);

        getApi().pauseAuction()
                .enqueue(defaultCallback(
                        "Auction Paused",
                        btnPause
                ));
    }

    private void resumeAuction() {

        btnResume.setEnabled(false);

        getApi().resumeAuction()
                .enqueue(defaultCallback(
                        "Auction Resumed",
                        btnResume
                ));
    }

    private void nextPlayer() {

        btnNext.setEnabled(false);

        getApi().nextPlayer()
                .enqueue(defaultCallback(
                        "Loading Next Player...",
                        btnNext
                ));
    }

    // ---------------------------------------------------
    // Load Existing Auction
    // ---------------------------------------------------
    private void loadCurrentAuction() {

        getApi().getCurrentAuction()
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(
                            Call<ResponseBody> call,
                            Response<ResponseBody> response) {

                        try {

                            if (!response.isSuccessful()
                                    || response.body() == null)
                                return;

                            String res =
                                    response.body().string();

                            JSONObject data =
                                    new JSONObject(res);

                            if (!data.getString("status")
                                    .equals("auction_active"))
                                return;

                            JSONObject player =
                                    data.getJSONObject("player");

                            String name =
                                    player.getString("name");

                            double price =
                                    player.getDouble("base_price");

                            int sec =
                                    data.getInt(
                                            "remaining_seconds"
                                    );

                            updateAuctionUI(
                                    name,
                                    price,
                                    sec
                            );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ResponseBody> call,
                            Throwable t) {
                    }
                });
    }

    // ---------------------------------------------------
    // Helpers
    // ---------------------------------------------------
    private ApiService getApi() {
        return ApiClient
                .getClient(this)
                .create(ApiService.class);
    }

    private void updateAuctionUI(
            String name,
            double price,
            int sec
    ) {
        tvPlayerName.setText(name);
        tvCurrentPrice.setText("₹" + price);
        tvTimer.setText(formatTime(sec));
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

    private void addNotification(String msg) {

        notifications.add(0, msg);

        adapter.notifyItemInserted(0);

        recyclerView.scrollToPosition(0);
    }

    private Callback<ResponseBody> defaultCallback(
            String successMsg,
            Button btn
    ) {

        return new Callback<ResponseBody>() {

            @Override
            public void onResponse(
                    Call<ResponseBody> call,
                    Response<ResponseBody> response
            ) {

                btn.setEnabled(true);

                if (response.isSuccessful()) {

                    Toast.makeText(
                            AdminAuctionActivity.this,
                            successMsg,
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    Toast.makeText(
                            AdminAuctionActivity.this,
                            "Action failed",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<ResponseBody> call,
                    Throwable t
            ) {

                btn.setEnabled(true);

                Toast.makeText(
                        AdminAuctionActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        };
    }

    // ---------------------------------------------------
    // Cleanup
    // ---------------------------------------------------
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socket != null) {

            socket.off("auction_started");
            socket.off("timer_update");
            socket.off("new_bid");
            socket.off("auction_paused");
            socket.off("auction_resumed");
            socket.off("auction_ended");

            socket.disconnect();
        }
    }
}