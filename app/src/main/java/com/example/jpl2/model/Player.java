package com.example.jpl2.model;

public class Player {
    private int player_id;
    private String name;
    private int base_price;
    private String type;
    private String image_path;

    public int getPlayerId() {
        return player_id;
    }

    public String getName() {
        return name;
    }

    public int getBasePrice() {
        return base_price;
    }

    public String getRole() {
        return type;
    }

    public String getImagePath() {
        return image_path;
    }
}
