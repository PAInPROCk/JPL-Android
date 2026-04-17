package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpl2.R;
import com.example.jpl2.viewmodel.TeamViewModel;
import com.example.jpl2.adapter.TeamAdapter;

import java.util.ArrayList;

public class TeamsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TeamAdapter adapter;
    TeamViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        recyclerView = findViewById(R.id.recyclerTeams);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        viewModel = new ViewModelProvider(this).get(TeamViewModel.class);

        viewModel.fetchTeams(this);

        viewModel.getTeams().observe(this, teams -> {
            if (teams != null && !teams.isEmpty()) {

                adapter = new TeamAdapter(this, teams);
                recyclerView.setAdapter(adapter);

            } else {
                Toast.makeText(this, "No teams found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}