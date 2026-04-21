package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jpl2.R;
import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.AuctionStatusResponse;
import com.example.jpl2.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Waiting_Activity extends AppCompatActivity {

    private Handler handler;
    private Runnable checkAuctionStatus;

    private static final int POLL_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        handler.post(checkAuctionStatus);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(checkAuctionStatus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkAuctionStatus);
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
                                Team_Auction_Activity.class));

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