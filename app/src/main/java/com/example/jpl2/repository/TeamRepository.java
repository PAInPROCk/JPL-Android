package com.example.jpl2.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.TeamResponse;
import com.example.jpl2.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamRepository {

    public void getTeams(MutableLiveData<List<TeamResponse.Team>> liveData) {

        ApiService api = ApiClient.getClient().create(ApiService.class);

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
}