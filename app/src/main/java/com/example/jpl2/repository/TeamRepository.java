package com.example.jpl2.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.TeamResponse;
import com.example.jpl2.network.ApiService;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamRepository {

    public void getTeams(Context context,MutableLiveData<List<TeamResponse.Team>> liveData) {

        ApiService api = ApiClient.getClient(context).create(ApiService.class);

        api.getTeams().enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(response.body().teams);

                } else {
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                liveData.setValue(null);
            }
        });
    }
    public void addTeam(
            Context context,
            RequestBody teamName,
            RequestBody captain,
            RequestBody mobile,
            RequestBody email
    ){

        ApiService api = ApiClient.getClient(context).create(ApiService.class);

        api.addTeam(teamName, captain, mobile, email)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("TEAM_ADD", "Success: " + response.code());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("TEAM_ADD", "Error: " + t.getMessage());
                    }
                });
    }
}