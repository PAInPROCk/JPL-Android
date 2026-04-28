package com.example.jpl2.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.TeamResponse;
import com.example.jpl2.network.ApiService;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamRepository {

    // 🔹 GET TEAMS
    public void getTeams(Context context, MutableLiveData<List<TeamResponse.Team>> liveData) {

        ApiService api = ApiClient.getClient(context).create(ApiService.class);

        api.getTeams().enqueue(new Callback<TeamResponse>() {

            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {

                Log.d("API_DEBUG", "Code: " + response.code());
                Log.d("API_DEBUG", "Body: " + response.body());

                if (response.isSuccessful() && response.body() != null) {

                    List<TeamResponse.Team> teams = response.body().teams;

                    if (teams != null && !teams.isEmpty()) {

                        Toast.makeText(
                                context,
                                "Teams size: " + teams.size(),
                                Toast.LENGTH_LONG
                        ).show();

                        liveData.setValue(teams);

                    } else {

                        Toast.makeText(
                                context,
                                "No teams found",
                                Toast.LENGTH_LONG
                        ).show();

                        liveData.setValue(null);
                    }

                } else {

                    Toast.makeText(
                            context,
                            "Response failed",
                            Toast.LENGTH_LONG
                    ).show();

                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {

                Log.e("API_DEBUG", "Error: " + t.getMessage());

                Toast.makeText(
                        context,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();

                liveData.setValue(null);
            }
        });
    }

    // 🔹 ADD TEAM
    public void addTeam(
            Context context,
            RequestBody teamName,
            RequestBody captain,
            RequestBody mobile,
            RequestBody emailId,
            MultipartBody.Part image
    ) {

        ApiService api = ApiClient.getClient(context).create(ApiService.class);

        api.addTeam(teamName, captain, mobile, emailId, image)
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {

                            Log.d("API_DEBUG", "Team added successfully");

                            Toast.makeText(
                                    context,
                                    "Team added successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Log.e("API_DEBUG", "Failed to add team");

                            Toast.makeText(
                                    context,
                                    "Failed to add team",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Log.e("API_DEBUG", "Error: " + t.getMessage());

                        Toast.makeText(
                                context,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}