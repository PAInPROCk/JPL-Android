package com.example.jpl2.model;

public class Player {

    private int player_id;
    private String name;
    private String nickname;
    private String category;
    private String gender;
    private String teams_played;
    private String type;
    private int base_price;
    private int age;

    private int total_runs;
    private int highest_runs;
    private int wickets_taken;
    private int times_out;

    private String role;
    private String image_path;

    public int getPlayerId() { return player_id; }

    public String getName() { return name; }

    public String getNickName() { return nickname; }

    public String getCategory() { return category; }

    public String getGender() { return gender; }

    public String getTeamsPlayed() { return teams_played; }

    public String getType() { return type; }

    public int getBasePrice() { return base_price; }

    public int getAge() { return age; }

    public String getRole() { return role; }

    public String getImagePath() { return image_path; }

    public int getTotalRuns() { return total_runs; }

    public int getHighestRuns() { return highest_runs; }

    public int getWicketsTaken() { return wickets_taken; }

    public int getTimesOut() { return times_out; }
}