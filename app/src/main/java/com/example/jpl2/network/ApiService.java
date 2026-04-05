package com.example.jpl2.network;

import com.example.jpl2.model.LoginRequest;
import com.example.jpl2.model.LoginResponse;
import com.example.jpl2.model.PlayerResponse;
import com.example.jpl2.model.TeamResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("players")
    Call<PlayerResponse> getPlayers();

    @GET("teams")
    Call<TeamResponse> getTeams();

    @GET("team/{id}")
    Call<TeamResponse.Team> getTeamById(@Path("id") int id);

}