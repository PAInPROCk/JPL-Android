package com.example.jpl2.model;

import java.util.List;

public class PlayerResponse {
    private boolean success;
    private int count;
    private List<Player> players;

    public boolean isSuccess() {
        return success;
    }

    public int getCount() {
        return count;
    }

    public List<Player> getPlayers() {
        return players;
    }
}