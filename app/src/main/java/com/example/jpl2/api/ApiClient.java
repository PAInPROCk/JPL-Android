package com.example.jpl2.api;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class ApiClient {

    public static final String BASE_URL = "https://jpl-backend-6ecq.onrender.com/";

    public static Retrofit getClient(Context context){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {

                    Request original = chain.request();

                    String token = context
                            .getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)
                            .getString("TOKEN", "");

                    Request.Builder builder = original.newBuilder();

                    if(token != null && !token.isEmpty()){
                        builder.addHeader("Authorization", "Bearer " + token);
                    }

                    return chain.proceed(builder.build());
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}