package com.example.jpl2.activities;

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

import android.widget.TextView;
import io.socket.client.Socket;
import okhttp3.ResponseBody;

import org.json.JSONObject;
import org.json.JSONException;

import com.example.jpl2.api.ApiClient;
import com.example.jpl2.network.ApiService;
import com.example.jpl2.network.SocketManager;

import com.example.jpl2.R;
import com.example.jpl2.adapter.NotificationAdapter;

import java.util.ArrayList;


public class AdminAuctionActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ArrayList<String> notifications;

    TextView tvTimer, tvPlayerName, tvCurrentPrice;
    Socket socket;

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

        notifications = new ArrayList<>();
        notifications.add("No Bids Yet");

        adapter = new NotificationAdapter(notifications);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ✅ THEN socket
        socket = SocketManager.getSocket();
        socket.connect();

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

                            int seconds = data.getInt("remaining_seconds");

                            int min = seconds / 60;
                            int sec = seconds % 60;

                            String time = String.format("%02d:%02d", min, sec);

                            tvPlayerName.setText(name);
                            tvCurrentPrice.setText("₹" + basePrice);
                            tvTimer.setText(time);

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

        socket.emit("join_auction");

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        // 🔥 TIMER UPDATE
        socket.on("timer_update", args -> {
            runOnUiThread(() -> {
                try {
                    JSONObject data = (JSONObject) args[0];

                    int seconds = data.getInt("remaining_seconds");

                    int min = seconds / 60;
                    int sec = seconds % 60;

                    String time = String.format("%02d:%02d", min, sec);

                    tvTimer.setText(time);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.off("auction_started");
            socket.off("timer_update");
            socket.off("auction_status");
            socket.disconnect();
        }
    }
}