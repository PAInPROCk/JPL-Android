package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.viewmodel.TeamViewModel;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TeamRegisterActivity extends AppCompatActivity {

    EditText etTeamName, etOwner, etPurse;
    Button btnSubmit;

    TeamViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_register);

        etTeamName = findViewById(R.id.etTeamName);
        etOwner = findViewById(R.id.etOwner);
        etPurse = findViewById(R.id.etPurse);
        btnSubmit = findViewById(R.id.btnSubmitTeam);

        viewModel = new ViewModelProvider(this).get(TeamViewModel.class);

        btnSubmit.setOnClickListener(v -> {

            String teamName = etTeamName.getText().toString();
            String owner = etOwner.getText().toString();
            String purse = etPurse.getText().toString();

            if(teamName.isEmpty() || owner.isEmpty() || purse.isEmpty()){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody teamNameBody = RequestBody.create(MediaType.parse("text/plain"), teamName);
            RequestBody ownerBody = RequestBody.create(MediaType.parse("text/plain"), owner);
            RequestBody purseBody = RequestBody.create(MediaType.parse("text/plain"), purse);

            viewModel.addTeam(teamNameBody, ownerBody, purseBody);

            Toast.makeText(this, "Team Submitted", Toast.LENGTH_SHORT).show();
        });
    }
}