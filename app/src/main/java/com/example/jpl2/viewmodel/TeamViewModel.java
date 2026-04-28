package com.example.jpl2.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jpl2.model.TeamResponse;
import com.example.jpl2.repository.TeamRepository;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TeamViewModel extends ViewModel {

    private final MutableLiveData<List<TeamResponse.Team>> teamsLiveData = new MutableLiveData<>();
    private final TeamRepository repository = new TeamRepository();

    public void fetchTeams(Context context) {
        repository.getTeams(context,teamsLiveData);
    }

    public MutableLiveData<List<TeamResponse.Team>> getTeams() {
        return teamsLiveData;
    }

    public void addTeam(
            Context context,
            RequestBody teamName,
            RequestBody captain,
            RequestBody mobile,
            RequestBody emailId,
            MultipartBody.Part image
    ){
        repository.addTeam(context, teamName, captain, mobile, emailId, image);
    }
}