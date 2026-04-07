package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpl2.R;
import com.example.jpl2.model.Player;
import com.example.jpl2.adapter.PlayerAdapter;
import com.example.jpl2.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PlayerAdapter playerAdapter;
    PlayerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        viewModel.getPlayers().observe(this, players -> {
            if(players != null){
                playerAdapter = new PlayerAdapter(players, player -> {
                    Toast.makeText(this, player.getName(), Toast.LENGTH_SHORT).show();
                });
                recyclerView.setAdapter(playerAdapter);
            }
        });
        viewModel.loadPlayers();
    }
}