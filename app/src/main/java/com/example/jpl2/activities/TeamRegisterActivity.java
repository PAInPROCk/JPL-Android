package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.utils.SessionManager;
import com.example.jpl2.viewmodel.TeamViewModel;

import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TeamRegisterActivity extends AppCompatActivity {

    EditText etTeamName, etCaptain, etMobile, etEmail;
    Button btnSubmit, btnUploadImage;

    Uri selectedImageUri;
    ImageView teamImage;
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
        teamImage = findViewById(R.id.playerImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);

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
        btnUploadImage.setOnClickListener(v -> imagePicker.launch("image/*"));
    }

    //Team image picker logic
    private final ActivityResultLauncher<String> imagePicker = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if(uri != null){
                    selectedImageUri = uri;

                    teamImage.setImageURI(uri);
                }
            }
    );
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
        MultipartBody.Part imagePart = null;

        try {
            if (selectedImageUri != null) {

                InputStream inputStream =
                        getContentResolver()
                                .openInputStream(selectedImageUri);

                if (inputStream != null) {

                    byte[] bytes =
                            new byte[inputStream.available()];

                    inputStream.read(bytes);
                    inputStream.close();

                    RequestBody requestFile =
                            RequestBody.create(
                                    MediaType.parse("image/*"),
                                    bytes
                            );

                    imagePart =
                            MultipartBody.Part.createFormData(
                                    "image",
                                    "team.jpg",
                                    requestFile
                            );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                emailBody,
                imagePart
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