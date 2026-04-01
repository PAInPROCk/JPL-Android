package com.example.jpl2.network;

import com.example.jpl2.model.LoginRequest;
import com.example.jpl2.model.LoginResponse;
import com.example.jpl2.model.Player;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("players")
    Call<List<Player>> getPlayers();

}