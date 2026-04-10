package com.example.jpl2.repository;

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
    public void addTeam(RequestBody teamName, RequestBody captain, RequestBody mobile, RequestBody email){

        ApiService api = ApiClient.getClient().create(ApiService.class);

        String cookie = "access_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiZW1haWwiOiJhZG1pbkBleGFtcGxlLmNvbSIsInJvbGUiOiJhZG1pbiIsInRlYW1faWQiOm51bGwsIm5hbWUiOiJQcmF0aGFtZXNoIEFkbWluIiwidGVhbV9wdXJzZSI6MCwidGVhbV9sb2dvIjpudWxsLCJleHAiOjE3NzU4NDczNDh9.of4WEIIBTynuMKBsYxiCr-GtbQqiMNj4_k8PicFL9u8";

        api.addTeam(cookie, teamName, captain, mobile, email)
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