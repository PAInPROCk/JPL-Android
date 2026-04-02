package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jpl2.R;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.viewmodel.AuthViewModel;
import com.example.jpl2.model.LoginResponse;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginBtn;
    AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);

        // Initialize viewModel
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Button click call the ViewModel
        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, password);
        });

        // observing live data from the result
        viewModel.getLoginResult().observe(this, result -> {
            if(result != null){
                Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }else{
                Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
            }
        });
    }


}