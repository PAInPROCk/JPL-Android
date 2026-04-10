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

    EditText etTeamName, etCaptain, etMobile, etEmail;
    Button btnSubmit;
    TeamViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_register);

        etTeamName = findViewById(R.id.etTeamName);
        etCaptain = findViewById(R.id.etCaptain);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.email);
        btnSubmit = findViewById(R.id.btnSubmit);

        viewModel = new ViewModelProvider(this).get(TeamViewModel.class);

        btnSubmit.setOnClickListener(v -> {

            String teamName = etTeamName.getText().toString();
            String captain = etCaptain.getText().toString();
            String mobile = etMobile.getText().toString();
            String email = etEmail.getText().toString();

            if(teamName.isEmpty() || captain.isEmpty() || mobile.isEmpty() || email.isEmpty()){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody teamNameBody = RequestBody.create(MediaType.parse("text/plain"), teamName);
            RequestBody captainBody = RequestBody.create(MediaType.parse("text/plain"), captain);
            RequestBody mobileBody = RequestBody.create(MediaType.parse("text/plain"), mobile);
            RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);

            viewModel.addTeam(teamNameBody, captainBody, mobileBody, emailBody);

            Toast.makeText(this, "Team Submitted", Toast.LENGTH_SHORT).show();
        });
    }
}