package com.example.jpl2.model;

import com.google.gson.annotations.SerializedName;

public class User {

    public int id;
    public String email;
    public String role;
    public String name;

    @SerializedName("team_logo")
    public String teamLogo;

    @SerializedName("team_id")
    public Integer teamId;

    @SerializedName("team_purse")
    public int teamPurse;
}