package com.example.jpl2.network;

import com.example.jpl2.model.LoginRequest;
import com.example.jpl2.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

}