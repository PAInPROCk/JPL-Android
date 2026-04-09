package com.example.jpl2.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    public String message;
    public User user;

    public static class User {
        public int id;
        public String name;
        public String role;

        @SerializedName("team_id")
        public Integer teamId;

        @SerializedName("team_purse")
        public int teamPurse;

        @SerializedName("team_logo")
        public String teamLogo;
    }
}