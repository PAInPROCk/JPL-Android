package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.utils.SessionManager;
import com.example.jpl2.viewmodel.TeamViewModel;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class TeamRegisterActivity extends AppCompatActivity {

    EditText etTeamName, etCaptain, etMobile, etEmail;
    Button btnSubmit;

    TeamViewModel viewModel;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        // ----------------------------------
        // Protect Page (Admin Only)
        // ----------------------------------
        if (!session.isLoggedIn()) {
            Toast.makeText(this,
                    "Please login first",
                    Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        if (!session.isAdmin()) {
            Toast.makeText(this,
                    "Access Denied",
                    Toast.LENGTH_SHORT).show();

            finish();
            return;
        }

        setContentView(R.layout.activity_team_register);

        // ----------------------------------
        // Views
        // ----------------------------------
        etTeamName = findViewById(R.id.etTeamName);
        etCaptain = findViewById(R.id.etCaptain);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.email);
        btnSubmit = findViewById(R.id.btnSubmit);

        // ----------------------------------
        // ViewModel
        // ----------------------------------
        viewModel =
                new ViewModelProvider(this)
                        .get(TeamViewModel.class);

        // ----------------------------------
        // Submit Button
        // ----------------------------------
        btnSubmit.setOnClickListener(v -> submitTeam());
    }

    // ----------------------------------
    // Submit Logic
    // ----------------------------------
    private void submitTeam() {

        String teamName =
                etTeamName.getText().toString().trim();

        String captain =
                etCaptain.getText().toString().trim();

        String mobile =
                etMobile.getText().toString().trim();

        String email =
                etEmail.getText().toString().trim();

        // Validation
        if (teamName.isEmpty()
                || captain.isEmpty()
                || mobile.isEmpty()
                || email.isEmpty()) {

            Toast.makeText(this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        RequestBody teamNameBody = body(teamName);
        RequestBody captainBody = body(captain);
        RequestBody mobileBody = body(mobile);
        RequestBody emailBody = body(email);

        viewModel.addTeam(
                this,
                teamNameBody,
                captainBody,
                mobileBody,
                emailBody
        );

        Toast.makeText(this,
                "Team Submitted",
                Toast.LENGTH_SHORT).show();

        clearForm();
    }

    // ----------------------------------
    // Helper
    // ----------------------------------
    private RequestBody body(String value) {
        return RequestBody.create(
                MediaType.parse("text/plain"),
                value
        );
    }

    // ----------------------------------
    // Clear Form
    // ----------------------------------
    private void clearForm() {
        etTeamName.setText("");
        etCaptain.setText("");
        etMobile.setText("");
        etEmail.setText("");
    }
}