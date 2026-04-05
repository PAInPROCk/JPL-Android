package com.example.jpl2.model;

import java.util.List;

public class TeamResponse {

    public boolean success;
    public int count;
    public List<Team> teams;

    public static class Team {
        public int team_id;
        public String name;
        public String captain;
        public String current_budget;
        public String image_path;

        public int getTeamId() { return team_id; }
        public String getName() { return name; }
        public String getCaptain() { return captain; }
        public String getCurrentBudget() { return current_budget; }
        public String getImagePath() { return image_path; }
    }
}