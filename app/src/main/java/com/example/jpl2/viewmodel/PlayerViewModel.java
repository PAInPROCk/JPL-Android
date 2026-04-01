package com.example.jpl2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jpl2.model.Player;
import com.example.jpl2.repository.PlayerRepository;

import java.util.List;

public class PlayerViewModel extends ViewModel {
    private MutableLiveData<List<Player>> playerList = new MutableLiveData<>();
    private PlayerRepository repository = new PlayerRepository();

    public LiveData<List<Player>> getPlayers(){
        return playerList;
    }

    public  void loadPlayers(){
        repository.getPlayers(playerList);
    }
}
