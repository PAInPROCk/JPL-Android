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

    // File picker
    private final ActivityResultLauncher<String> filePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    fileUri = uri;

                    String name = uri.getLastPathSegment();
                    tvFileName.setText(name);

                    Toast.makeText(this, "File Selected", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btnPlayer = findViewById(R.id.btnPlayerReg);
        Button btnTeam = findViewById(R.id.btnTeamReg);
        Button startAuction = findViewById(R.id.startAuction);

        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnSubmit = findViewById(R.id.btnSubmitBatch);
        tvFileName = findViewById(R.id.tvFileName);

        // Navigation
        btnPlayer.setOnClickListener(v ->
                startActivity(new Intent(this, PlayerRegisterActivity.class)));

        btnTeam.setOnClickListener(v ->
                startActivity(new Intent(this, TeamRegisterActivity.class)));

        CheckBox checkRandom = findViewById(R.id.checkRandom);

        startAuction.setOnClickListener(v -> {

            // ❌ If checkbox not selected
            if (!checkRandom.isChecked()) {
                Toast.makeText(this, "Please select auction mode", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Call API
            ApiService api = ApiClient.getClient(this).create(ApiService.class);

            StartAuctionRequest request = new StartAuctionRequest("random", 120);

            api.startAuction(request).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(AdminActivity.this, "Auction Started", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(AdminActivity.this, AdminAuctionActivity.class));

                    } else {
                        try {
                            String error = response.errorBody().string();

                            if (error.contains("Auction already running")) {

                                Toast.makeText(AdminActivity.this, "Auction already running - opening...", Toast.LENGTH_SHORT).show();

                                // 🔥 REDIRECT ANYWAY
                                startActivity(new Intent(AdminActivity.this, AdminAuctionActivity.class));

                            } else {
                                Toast.makeText(AdminActivity.this, error, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(AdminActivity.this, "Failed to start auction", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(AdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Choose file
        btnChooseFile.setOnClickListener(v -> filePicker.launch("*/*"));

        // Upload file
        btnSubmit.setOnClickListener(v -> {

            if (fileUri == null) {
                Toast.makeText(this, "Please select file first", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                InputStream inputStream = getContentResolver().openInputStream(fileUri);

                if (inputStream == null) {
                    Toast.makeText(this, "Cannot read file", Toast.LENGTH_SHORT).show();
                    return;
                }

// 🔥 Better way to read file
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();

// ✅ FIXED LINE (correct order for new OkHttp)
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("application/octet-stream"), bytes);

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", "upload.csv", requestFile);
                ApiService api = ApiClient.getClient(this).create(ApiService.class);

                api.uploadBatch(body).enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AdminActivity.this, "Upload Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("UPLOAD", "Error: " + t.getMessage());
                        Toast.makeText(AdminActivity.this, "Error uploading file", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                Log.e("UPLOAD", "Exception", e);
                Toast.makeText(this, "File error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}