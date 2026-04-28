package com.example.jpl2.model;

public class AuctionStatusResponse {
    private boolean active;

    public boolean isActive(){
        return  active;
    }

    public boolean isStarted(){ return active; }
}
