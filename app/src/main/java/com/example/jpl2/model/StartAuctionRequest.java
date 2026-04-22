package com.example.jpl2.model;

public class StartAuctionRequest {

    private String mode;
    private int duration;

    public StartAuctionRequest(String mode, int duration) {
        this.mode = mode;
        this.duration = duration;
    }
}