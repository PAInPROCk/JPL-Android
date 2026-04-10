package com.example.jpl2.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jpl2.model.TeamResponse;
import com.example.jpl2.repository.TeamRepository;

import java.util.List;

import okhttp3.RequestBody;

public class TeamViewModel extends ViewModel {

    private final MutableLiveData<List<TeamResponse.Team>> teamsLiveData = new MutableLiveData<>();
    private final TeamRepository repository = new TeamRepository();

    public void fetchTeams() {
        repository.getTeams(teamsLiveData);
    }

    public MutableLiveData<List<TeamResponse.Team>> getTeams() {
        return teamsLiveData;
    }

    public void addTeam(RequestBody teamName, RequestBody owner, RequestBody purse){
        repository.addTeam(teamName, owner, purse);
    }
}