package com.example.jpl2.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;



public class AuctionTimerManager {
    private static AuctionTimerManager instance;

    private int timeLeft = 0;
    private boolean isPaused = false;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final MutableLiveData<Integer> timerLiveData = new MutableLiveData<>();

    private Runnable timerRunnable;

    private AuctionTimerManager() {
        startTimerLoop();
    }

    public static synchronized AuctionTimerManager getInstance() {
        if (instance == null) {
            instance = new AuctionTimerManager();
        }
        return instance;
    }

    public MutableLiveData<Integer> getTimer() {
        return timerLiveData;
    }

    // 🔥 Same as handleTimerUpdate
    public void updateFromServer(int seconds) {
        timeLeft = seconds;
        timerLiveData.postValue(timeLeft);
    }

    public void pause(int seconds) {
        isPaused = true;
        timeLeft = seconds;
        timerLiveData.postValue(timeLeft);
    }

    public void resume(int seconds) {
        isPaused = false;
        timeLeft = seconds;
        timerLiveData.postValue(timeLeft);
    }

    private void startTimerLoop() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {

                if (!isPaused && timeLeft > 0) {
                    timeLeft--;
                    timerLiveData.postValue(timeLeft);
                }

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(timerRunnable);
    }

    public void reset() {
        timeLeft = 0;
        isPaused = false;
        timerLiveData.postValue(0);
    }
}
