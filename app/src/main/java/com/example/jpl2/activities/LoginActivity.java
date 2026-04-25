package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.utils.SessionManager;
import com.example.jpl2.viewmodel.AuthViewModel;

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

            viewModel.login(this,email, password);
        });

        // observing live data from the result
        viewModel.getLoginResult().observe(this, result -> {

            if (result != null && result.user != null) {

                SessionManager session = new SessionManager(this);

                session.saveLoginSession(
                        result.token,
                        result.user.role,
                        result.user.name,
                        result.user.email,
                        result.user.team_id != null ? result.user.team_id : 0,
                        result.user.team_logo,
                        result.user.name,
                        result.user.team_purse
                );

                String role = result.user.role;


                if (role.equals("admin")) {
                    Toast.makeText(LoginActivity.this, "Admin Login", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (role.equals("team")) {
                    Toast.makeText(LoginActivity.this, "Team Login", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Unknown role", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}