package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.AuctionStatusResponse;
import com.example.jpl2.network.ApiService;
import com.example.jpl2.viewmodel.AuthViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Waiting_Activity extends AppCompatActivity {

    Handler handler;
    Runnable checkAuctionStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler(getMainLooper());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_waiting);

        checkAuctionStatus = new Runnable() {
            @Override
            public void run() {
                checkStatusFromServer();
                handler.postDelayed(this,3000);
            }
        };
        handler.post(checkAuctionStatus);
    }

    private void checkStatusFromServer(){
        ApiService api = ApiClient.getClient(this).create(ApiService.class);

        Call<AuctionStatusResponse> call = api.getAuctionStatus();

        call.enqueue(new Callback<AuctionStatusResponse>() {
            @Override
            public void onResponse(Call<AuctionStatusResponse> call, Response<AuctionStatusResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isStarted()){
                        handler.removeCallbacks(checkAuctionStatus);
                        Intent intent = new Intent(Waiting_Activity.this, Team_Auction_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuctionStatusResponse> call, Throwable t) {

            }
        });
    }
}