package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jpl2.R;
import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.AuctionStatusResponse;
import com.example.jpl2.network.ApiService;
import com.example.jpl2.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Waiting_Activity extends AppCompatActivity {

    private Handler handler;
    private Runnable checkAuctionStatus;

    private static final int POLL_DELAY = 3000;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        // -------------------------------
        // Protect Page (Only Team Users)
        // -------------------------------
        if (!session.isLoggedIn()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (!session.isTeam()) {
            Toast.makeText(this, "Only team users can access auction", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_waiting);

        handler = new Handler(Looper.getMainLooper());

        checkAuctionStatus = new Runnable() {
            @Override
            public void run() {
                checkStatusFromServer();
                handler.postDelayed(this, POLL_DELAY);
            }
        };

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        handler.removeCallbacks(checkAuctionStatus);
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (handler != null && checkAuctionStatus != null) {
            handler.post(checkAuctionStatus);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (handler != null) {
            handler.removeCallbacks(checkAuctionStatus);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null) {
            handler.removeCallbacks(checkAuctionStatus);
        }
    }

    private void checkStatusFromServer() {

        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        Call<AuctionStatusResponse> call = api.getAuctionStatus();

        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(
                    @NonNull Call<AuctionStatusResponse> call,
                    @NonNull Response<AuctionStatusResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    if (response.body().isStarted()) {

                        handler.removeCallbacks(checkAuctionStatus);

                        startActivity(new Intent(
                                Waiting_Activity.this,
                                Team_Auction_Activity.class
                        ));

                        finish();
                    }
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<AuctionStatusResponse> call,
                    @NonNull Throwable t) {
            }
        });
    }
}
