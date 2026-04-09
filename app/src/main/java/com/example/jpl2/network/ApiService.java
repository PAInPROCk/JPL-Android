package com.example.jpl2.network;

import com.example.jpl2.model.LoginRequest;
import com.example.jpl2.model.LoginResponse;
import com.example.jpl2.model.PlayerResponse;
import com.example.jpl2.model.TeamResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

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

    @Multipart
    @POST("add-player")
    Call<ResponseBody> addPlayer(
            @Header("Cookie") String cookie,
            @Part("playerName") RequestBody playerName,
            @Part("fatherName") RequestBody fatherName,
            @Part("surName") RequestBody surName,
            @Part("nickName") RequestBody nickName,
            @Part("category") RequestBody category,
            @Part("style") RequestBody style,
            @Part("basePrice") RequestBody basePrice,
            @Part MultipartBody.Part image
    );
    @GET("teams")
    Call<TeamResponse> getTeams();

    @GET("team/{id}")
    Call<TeamResponse.Team> getTeamById(@Path("id") int id);

}