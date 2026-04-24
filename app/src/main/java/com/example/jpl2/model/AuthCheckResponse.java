package com.example.jpl2.model;

import com.google.gson.annotations.SerializedName;

public class AuthCheckResponse {

    @SerializedName("authenticated")
    public boolean authenticated;

    @SerializedName("user")
    public User user;

    public static class User {

        @SerializedName("id")
        public int id;

        @SerializedName("email")
        public String email;

        @SerializedName("role")
        public String role;

        @SerializedName("team_id")
        public Integer teamId;

        @SerializedName("name")
        public String name;

        @SerializedName("team_purse")
        public int teamPurse;

        @SerializedName("team_logo")
        public String teamLogo;

        @SerializedName("exp")
        public long exp;
    }
}