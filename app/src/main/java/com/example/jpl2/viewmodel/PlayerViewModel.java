package com.example.jpl2.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jpl2.model.Player;
import com.example.jpl2.repository.PlayerRepository;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PlayerViewModel extends ViewModel {
    private MutableLiveData<List<Player>> playerList = new MutableLiveData<>();
    private PlayerRepository repository = new PlayerRepository();

    public LiveData<List<Player>> getPlayers(){
        return playerList;
    }

    public  void loadPlayers(Context context){
        repository.getPlayers(context,playerList);
    }

    public void addPlayer(
            Context context,
            RequestBody playerName,
            RequestBody fatherName,
            RequestBody surName,
            RequestBody nickName,
            RequestBody age,
            RequestBody mobile,
            RequestBody email,
            RequestBody gender,
            RequestBody category,
            RequestBody style,
            RequestBody basePrice,
            MultipartBody.Part image
    ){
        repository.addPlayer(
                context,
                playerName,
                fatherName,
                surName,
                nickName,
                age,
                mobile,
                email,
                gender,
                category,
                style,
                basePrice,
                image
        );
    }
}
