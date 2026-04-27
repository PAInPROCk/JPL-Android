package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.socket.client.Socket;
import okhttp3.ResponseBody;

import org.json.JSONObject;
import org.json.JSONException;

import com.example.jpl2.api.ApiClient;
import com.example.jpl2.network.ApiService;
import com.example.jpl2.network.SocketManager;

import com.example.jpl2.R;
import com.example.jpl2.adapter.NotificationAdapter;
import com.example.jpl2.utils.AuctionTimerManager;

import java.util.ArrayList;


public class AdminAuctionActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ArrayList<String> notifications;

    TextView tvTimer, tvPlayerName, tvCurrentPrice;
    Socket socket;
    boolean isCancelTriggered = false;

    public void addNotification(String message){
        notifications.add(0, message); // Set newest bid on top
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_auction);

        // ✅ FIRST: initialize views
        tvTimer = findViewById(R.id.tvTimer);
        tvPlayerName = findViewById(R.id.tvPlayerName);
        tvCurrentPrice = findViewById(R.id.playerCurrentPrice);

        recyclerView = findViewById(R.id.notificationRecycler);

        Button btnCancel = findViewById(R.id.cancel);
        Button btnPause = findViewById(R.id.pause);
        Button btnResume = findViewById(R.id.resume);
        Button btnNext = findViewById(R.id.nextPlayer);

        btnPause.setEnabled(true);
        btnResume.setEnabled(false);

        AuctionTimerManager.getInstance().getTimer().observe(this, seconds -> {

            int min = seconds / 60;
            int sec = seconds % 60;

            String time = String.format("%02d:%02d", min, sec);
            tvTimer.setText(time);

        });

        notifications = new ArrayList<>();
        notifications.add("No Bids Yet");

        adapter = new NotificationAdapter(notifications);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnCancel.setOnClickListener(v -> {

            ApiService api = ApiClient.getClient(this).create(ApiService.class);

            // 🔥 disable button (important)
            btnCancel.setEnabled(false);

            isCancelTriggered = true; // 🔥 ADD THIS LINE


            api.cancelAuction().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(AdminAuctionActivity.this, "Auction Cancelled", Toast.LENGTH_SHORT).show();

                    } else {
                        btnCancel.setEnabled(true); // re-enable on failure
                        Toast.makeText(AdminAuctionActivity.this, "Cancel failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    btnCancel.setEnabled(true);
                    Toast.makeText(AdminAuctionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        //Pause
        btnPause.setOnClickListener(v -> {

            ApiService api = ApiClient.getClient(this).create(ApiService.class);

            btnPause.setEnabled(false);

            api.pauseAuction().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(AdminAuctionActivity.this, "Auction Paused", Toast.LENGTH_SHORT).show();
                    } else {
                        btnPause.setEnabled(true);
                        Toast.makeText(AdminAuctionActivity.this, "Pause failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    btnPause.setEnabled(true);
                    Toast.makeText(AdminAuctionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        //Resume
        btnResume.setOnClickListener(v -> {

            ApiService api = ApiClient.getClient(this).create(ApiService.class);

            btnResume.setEnabled(false);

            api.resumeAuction().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        Toast.makeText(AdminAuctionActivity.this, "Auction Resumed", Toast.LENGTH_SHORT).show();
                    } else {
                        btnResume.setEnabled(true);
                        Toast.makeText(AdminAuctionActivity.this, "Resume failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    btnResume.setEnabled(true);
                    Toast.makeText(AdminAuctionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        btnNext.setOnClickListener(v -> {

            ApiService api = ApiClient.getClient(this).create(ApiService.class);

            btnNext.setEnabled(false);

            api.nextPlayer().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(AdminAuctionActivity.this, "Moving to next player...", Toast.LENGTH_SHORT).show();

                    } else {
                        btnNext.setEnabled(true);
                        Toast.makeText(AdminAuctionActivity.this, "Next player failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    btnNext.setEnabled(true);
                    Toast.makeText(AdminAuctionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });



        // ✅ THEN socket
        socket = SocketManager.getSocket();

        // 🔥 AUCTION STARTED
        socket.on("auction_started", args -> {
            runOnUiThread(() -> {
                try {
                    JSONObject data = (JSONObject) args[0];

                    JSONObject player = data.getJSONObject("player");

                    String name = player.getString("name");
                    double basePrice = player.getDouble("base_price");

                    tvPlayerName.setText(name);
                    tvCurrentPrice.setText("₹" + basePrice);

                    btnNext.setEnabled(true);
                    btnPause.setEnabled(true);
                    btnResume.setEnabled(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        // 🔥 TIMER UPDATE
        socket.on("timer_update", args -> {
            try {
                JSONObject data = (JSONObject) args[0];
                int seconds = data.getInt("remaining_seconds");

                AuctionTimerManager.getInstance().updateFromServer(seconds);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        socket.on("auction_paused", args -> {
            try {
                JSONObject data = (JSONObject) args[0];
                int seconds = data.getInt("remaining_seconds");

                AuctionTimerManager.getInstance().pause(seconds);

                runOnUiThread(() -> {
                    btnPause.setEnabled(false);
                    btnResume.setEnabled(true);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        socket.on("auction_resumed", args -> {
            try {
                JSONObject data = (JSONObject) args[0];
                int seconds = data.getInt("remaining_seconds");

                AuctionTimerManager.getInstance().resume(seconds);

                runOnUiThread(() -> {
                    btnPause.setEnabled(true);
                    btnResume.setEnabled(false);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        socket.on("auction_status", args -> {
            runOnUiThread(() -> {
                try {
                    JSONObject data = (JSONObject) args[0];

                    JSONObject player = data.getJSONObject("player");

                    String name = player.getString("name");
                    double basePrice = player.getDouble("base_price");

                    tvPlayerName.setText(name);
                    tvCurrentPrice.setText("₹" + basePrice);

                    // 🔥 HANDLE PAUSE STATE
                    boolean paused = data.optBoolean("paused", false);

                    if (paused) {
                        btnPause.setEnabled(false);
                        btnResume.setEnabled(true);
                    } else {
                        btnPause.setEnabled(true);
                        btnResume.setEnabled(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        socket.on("auction_state", args -> {
            runOnUiThread(() -> {
                try {
                    JSONObject data = (JSONObject) args[0];

                    if (data.getString("status").equals("no_active_auction")) {

                        Log.d("AUCTION", "No active auction, retrying...");

                        // 🔥 Retry after delay
                        new android.os.Handler().postDelayed(() -> {
                            socket.emit("join_auction");
                        }, 1000);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        socket.on("next_player_loading", args -> {

            runOnUiThread(() -> {
                try {
                    JSONObject data = (JSONObject) args[0];
                    int delay = data.getInt("delay");

                    Toast.makeText(this, "Next player in " + delay + " sec", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        });

        socket.on(Socket.EVENT_CONNECT, args -> {
            Log.d("SOCKET", "Connected ✅");

            // 🔥 NOW SAFE TO JOIN
            socket.emit("join_auction");

            ApiService api = ApiClient.getClient(this).create(ApiService.class);

            api.getCurrentAuction().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().string();
                            JSONObject data = new JSONObject(res);

                            if (data.getString("status").equals("auction_active")) {

                                JSONObject player = data.getJSONObject("player");

                                String name = player.getString("name");
                                double basePrice = player.getDouble("base_price");

                                tvPlayerName.setText(name);
                                tvCurrentPrice.setText("₹" + basePrice);

                                // 🔥 ADD THIS BLOCK (IMPORTANT)
                                boolean paused = data.optBoolean("paused", false);

                                if (paused) {
                                    btnPause.setEnabled(false);
                                    btnResume.setEnabled(true);
                                } else {
                                    btnPause.setEnabled(true);
                                    btnResume.setEnabled(false);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        });

        socket.on("auction_ended", args -> {

            runOnUiThread(() -> {
                try {
                    JSONObject data = (JSONObject) args[0];

                    String status = data.getString("status");

                    AuctionTimerManager.getInstance().reset();

                    Intent intent;

                    if (status.equals("sold")) {
                        intent = new Intent(AdminAuctionActivity.this, sold_activity.class);
                    } else {
                        intent = new Intent(AdminAuctionActivity.this, unsold_activity.class);
                    }

                    intent.putExtra("player_name", data.getJSONObject("player").getString("name"));
                    intent.putExtra("team_name", data.optString("team_name", ""));
                    intent.putExtra("sold_price", data.optDouble("sold_price", 0));

                    // 🔥 PASS CANCEL FLAG
                    intent.putExtra("is_cancel", isCancelTriggered);

                    isCancelTriggered = false; // reset AFTER passing

                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });


        socket.connect();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.off("auction_started");
            socket.off("timer_update");
            socket.off("auction_status");
            socket.off("auction_paused");
            socket.off("auction_resumed");
            socket.off("next_player_loading");
            socket.off("auction_ended");
            socket.disconnect();
        }
    }
}