package com.example.jpl2.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.Player;
import com.example.jpl2.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerRepository {
    public void getPlayers(MutableLiveData<List<Player>>liveData){
        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getPlayers().enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                if(response.isSuccessful()){
                    liveData.setValue(response.body());
                }else{
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                liveData.setValue(null);
            }
        });
    }
}
