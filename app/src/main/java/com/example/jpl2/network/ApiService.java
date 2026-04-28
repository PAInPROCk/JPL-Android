package com.example.jpl2.network;

import com.example.jpl2.model.AuctionStatusResponse;
import com.example.jpl2.model.AuthCheckResponse;
import com.example.jpl2.model.LoginRequest;
import com.example.jpl2.model.LoginResponse;
import com.example.jpl2.model.PlayerResponse;
import com.example.jpl2.model.StartAuctionRequest;
import com.example.jpl2.model.TeamResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.Part;

import com.example.jpl2.model.AuthCheckResponse;

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

            @Part("playerName") RequestBody playerName,
            @Part("fatherName") RequestBody fatherName,
            @Part("surName") RequestBody surName,
            @Part("nickName") RequestBody nickName,
            @Part("age") RequestBody age,
            @Part("mobile") RequestBody mobile,
            @Part("emailId") RequestBody email,
            @Part("gender") RequestBody gender,
            @Part("category") RequestBody category,
            @Part("style") RequestBody style,
            @Part("basePrice") RequestBody basePrice,
            @Part MultipartBody.Part image
    );

    @GET("teams")
    Call<TeamResponse> getTeams();


    @GET("team/{id}")
    Call<TeamResponse.Team> getTeamById(@Path("id") int id);

    @Multipart
    @POST("add-team")
    Call<ResponseBody> addTeam(

            @Part("teamName") RequestBody teamName,
            @Part("captain") RequestBody captain,
            @Part("mobile") RequestBody mobile,
            @Part("emailId") RequestBody emailId,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("upload-players")
    Call<ResponseBody> uploadBatch(@Part MultipartBody.Part file);
    @GET("check-auth")
    Call<AuthCheckResponse> checkAuth();

    @GET("auction-status")
    Call<AuctionStatusResponse> getAuctionStatus();

    @POST("start-auction")
    Call<ResponseBody> startAuction(@Body StartAuctionRequest request);

    @GET("current-auction")
    Call<ResponseBody> getCurrentAuction();

    @POST("cancel-auction")
    Call<ResponseBody> cancelAuction();

    @POST("pause-auction")
    Call<ResponseBody> pauseAuction();

    @POST("resume-auction")
    Call<ResponseBody> resumeAuction();

    @POST("next-auction")
    Call<ResponseBody> nextPlayer();



}