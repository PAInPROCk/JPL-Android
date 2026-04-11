package com.example.jpl2.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.viewmodel.PlayerViewModel;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PlayerRegisterActivity extends AppCompatActivity {

    EditText etName, etRole, etPrice;
    Button btnSubmit;

    PlayerViewModel viewModel;
    Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_register);

        genderSpinner = findViewById(R.id.genderspinner);
        etName = findViewById(R.id.playerName);
        etRole = findViewById(R.id.role);
        etPrice = findViewById(R.id.pBasePrice);
        btnSubmit = findViewById(R.id.btnSubmit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        btnSubmit.setOnClickListener(v -> {

            String name = etName.getText().toString();
            String role = etRole.getText().toString();
            String price = etPrice.getText().toString();
            String selectedGender = genderSpinner.getSelectedItem().toString();

            if(name.isEmpty() || role.isEmpty() || price.isEmpty()){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert to RequestBody
            RequestBody playerName = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody fatherName = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody surName = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody nickName = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody category = RequestBody.create(MediaType.parse("text/plain"), "General");
            RequestBody style = RequestBody.create(MediaType.parse("text/plain"), role);
            RequestBody basePrice = RequestBody.create(MediaType.parse("text/plain"), price);

            // TEMP: No image
            MultipartBody.Part imagePart = null;

            viewModel.addPlayer(playerName, fatherName, surName,
                    nickName, category, style, basePrice, imagePart);

            Toast.makeText(this, "Player Submitted", Toast.LENGTH_SHORT).show();
        });
    }
}