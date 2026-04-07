package com.example.jpl2.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.Player;
import com.example.jpl2.model.PlayerResponse;
import com.example.jpl2.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerRepository {
    public void getPlayers(MutableLiveData<List<Player>>liveData){
        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getPlayers().enqueue(new Callback<PlayerResponse>() {
            @Override
            public void onResponse(Call<PlayerResponse> call, Response<PlayerResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    Log.d("PLAYER_DEBUG", "Players: " + response.body().getPlayers());
                    liveData.setValue(response.body().getPlayers());
                } else {
                    Log.d("PLAYER_DEBUG", "Failed response");
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PlayerResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        });
    }
}
