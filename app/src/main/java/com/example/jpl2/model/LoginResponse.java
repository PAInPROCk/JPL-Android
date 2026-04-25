package com.example.jpl2.model;

public class LoginResponse {
    public  String message;
    public String token;
    public User user;

    public static class User{
        public int id;
        public String name;
        public String email;
        public String role;

        public Integer team_id;
        public int team_purse;
        public String team_logo;
    }
}