package com.example.jpl2.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.Player;
import com.example.jpl2.model.PlayerResponse;
import com.example.jpl2.network.ApiService;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerRepository {
    public void getPlayers(Context context,MutableLiveData<List<Player>>liveData){
        ApiService api = ApiClient.getClient(context).create(ApiService.class);

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

    public void addPlayer(
            Context context,
            RequestBody playerName,
            RequestBody fatherName,
            RequestBody surName,
            RequestBody nickName,
            RequestBody category,
            RequestBody style,
            RequestBody basePrice,
            MultipartBody.Part image
    ){
        ApiService api = ApiClient.getClient(context).create(ApiService.class);

        api.addPlayer(playerName, fatherName, surName, nickName,
                        category, style, basePrice, image)

                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("PLAYER_ADD", "Success: " + response.code());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("PLAYER_ADD", "Error: " + t.getMessage());
                    }
                });
    }
}
