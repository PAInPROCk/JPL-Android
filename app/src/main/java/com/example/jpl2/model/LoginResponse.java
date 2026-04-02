package com.example.jpl2.model;

public class LoginResponse {

    public String message;
    public User user;

    public static class User {
        public int id;
        public String name;
        public String role;
        public Integer team_id;
        public double team_purse;
        public String team_logo;
    }
}