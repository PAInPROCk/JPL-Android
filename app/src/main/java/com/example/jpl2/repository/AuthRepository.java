package com.example.jpl2.repository;


import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.AuthCheckResponse;
import com.example.jpl2.model.LoginRequest;
import com.example.jpl2.model.LoginResponse;
import com.example.jpl2.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    public void login(Context context,String email, String password, MutableLiveData<LoginResponse>liveData){
        ApiService api = ApiClient.getClient(context).create(ApiService.class);
        LoginRequest request = new LoginRequest(email, password);
        api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                System.out.println("RAW RESPONSE: " + response.body());

                if(response.isSuccessful() && response.body() != null){

                    LoginResponse res = response.body();

                    System.out.println("MESSAGE: " + res.message);
                    System.out.println("USER OBJECT: " + res.user);

                    if(res.user != null){
                        System.out.println("ROLE: " + res.user.role);
                    }

                    liveData.setValue(res);

                } else {
                    System.out.println("LOGIN FAILED RESPONSE");
                    liveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                liveData.setValue(null);
                t.printStackTrace();
            }
        });
    }
    public void checkAuth(Context context, MutableLiveData<AuthCheckResponse> liveData){
        ApiService api = ApiClient.getClient(context).create(ApiService.class);

        api.checkAuth().enqueue(new Callback<AuthCheckResponse>() {
            @Override
            public void onResponse(Call<AuthCheckResponse> call, Response<AuthCheckResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    liveData.setValue(response.body());
                } else {
                    AuthCheckResponse res = new AuthCheckResponse();
                    res.authenticated = false;
                    liveData.setValue(res);
                }
            }

            @Override
            public void onFailure(Call<AuthCheckResponse> call, Throwable t) {
                AuthCheckResponse res = new AuthCheckResponse();
                res.authenticated = false;
                liveData.setValue(res);
            }
        });
    }

}
