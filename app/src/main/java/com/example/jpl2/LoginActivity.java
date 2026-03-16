package com.example.jpl2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jpl2.api.ApiService;
import com.example.jpl2.api.LoginRequest;
import com.example.jpl2.api.LoginResponse;
import com.example.jpl2.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser(){

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        ApiService apiService =
                RetrofitClient.getClient().create(ApiService.class);

        LoginRequest request = new LoginRequest(email, password);

        Call<LoginResponse> call = apiService.login(request);

        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()){

                    Toast.makeText(LoginActivity.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(LoginActivity.this,
                            "Invalid Credentials",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Toast.makeText(LoginActivity.this,
                        "Server Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}