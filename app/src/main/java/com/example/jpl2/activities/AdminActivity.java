package com.example.jpl2.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jpl2.R;
import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.StartAuctionRequest;
import com.example.jpl2.network.ApiService;
import com.example.jpl2.utils.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private Button btnChooseFile, btnSubmit;
    private TextView tvFileName;
    private Uri fileUri;

    private SessionManager session;

    // -----------------------------------
    // File Picker
    // -----------------------------------
    private final ActivityResultLauncher<String> filePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {

                if (uri != null) {

                    String name = uri.getLastPathSegment();

                    if (name == null || !name.toLowerCase().endsWith(".zip")) {
                        Toast.makeText(this,
                                "Only ZIP files allowed",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    fileUri = uri;
                    tvFileName.setText(name);

                    Toast.makeText(this,
                            "File Selected",
                            Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);

        // -----------------------------------
        // Protect Page (Admin Only)
        // -----------------------------------
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

        setContentView(R.layout.activity_admin);

        Button btnPlayer = findViewById(R.id.btnPlayerReg);
        Button btnTeam = findViewById(R.id.btnTeamReg);
        Button startAuction = findViewById(R.id.startAuction);

        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnSubmit = findViewById(R.id.btnSubmitBatch);
        tvFileName = findViewById(R.id.tvFileName);

        CheckBox checkRandom = findViewById(R.id.checkRandom);

        // -----------------------------------
        // Navigation
        // -----------------------------------
        btnPlayer.setOnClickListener(v ->
                startActivity(new Intent(this, PlayerRegisterActivity.class)));

        btnTeam.setOnClickListener(v ->
                startActivity(new Intent(this, TeamRegisterActivity.class)));

        // -----------------------------------
        // Start Auction
        // -----------------------------------
        startAuction.setOnClickListener(v -> {

            if (!checkRandom.isChecked()) {
                Toast.makeText(this,
                        "Please select auction mode",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            ApiService api =
                    ApiClient.getClient(this).create(ApiService.class);

            StartAuctionRequest request =
                    new StartAuctionRequest("random", 120);

            api.startAuction(request).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(AdminActivity.this,
                                "Auction Started",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(
                                AdminActivity.this,
                                AdminAuctionActivity.class));

                    } else {

                        try {
                            String error = response.errorBody().string();

                            if (error.contains("Auction already running")) {

                                Toast.makeText(AdminActivity.this,
                                        "Auction already running",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(
                                        AdminActivity.this,
                                        AdminAuctionActivity.class));

                            } else {
                                Toast.makeText(AdminActivity.this,
                                        error,
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {

                            Toast.makeText(AdminActivity.this,
                                    "Failed to start auction",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call,
                                      Throwable t) {

                    Toast.makeText(AdminActivity.this,
                            "Error: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        // -----------------------------------
        // Choose ZIP
        // -----------------------------------
        btnChooseFile.setOnClickListener(v ->
                filePicker.launch("*/*"));

        // -----------------------------------
        // Upload ZIP
        // -----------------------------------
        btnSubmit.setOnClickListener(v -> {

            if (fileUri == null) {
                Toast.makeText(this,
                        "Please select ZIP file first",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                InputStream inputStream =
                        getContentResolver().openInputStream(fileUri);

                if (inputStream == null) {
                    Toast.makeText(this,
                            "Cannot read file",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ByteArrayOutputStream buffer =
                        new ByteArrayOutputStream();

                byte[] data = new byte[1024];
                int nRead;

                while ((nRead = inputStream.read(data)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                byte[] bytes = buffer.toByteArray();
                inputStream.close();

                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse("application/zip"),
                                bytes
                        );

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData(
                                "file",
                                "players.zip",
                                requestFile
                        );

                ApiService api =
                        ApiClient.getClient(this)
                                .create(ApiService.class);

                api.uploadBatch(body)
                        .enqueue(new Callback<ResponseBody>() {

                            @Override
                            public void onResponse(
                                    Call<ResponseBody> call,
                                    Response<ResponseBody> response) {

                                if (response.isSuccessful()) {

                                    Toast.makeText(
                                            AdminActivity.this,
                                            "Upload Success",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    fileUri = null;
                                    tvFileName.setText("No file chosen");

                                } else {

                                    Toast.makeText(
                                            AdminActivity.this,
                                            "Upload Failed",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }

                            @Override
                            public void onFailure(
                                    Call<ResponseBody> call,
                                    Throwable t) {

                                Log.e("UPLOAD",
                                        t.getMessage());

                                Toast.makeText(
                                        AdminActivity.this,
                                        "Upload Failed",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        });

            } catch (Exception e) {

                Log.e("UPLOAD", "Exception", e);

                Toast.makeText(this,
                        "File Error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}