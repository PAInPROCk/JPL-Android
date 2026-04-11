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

    EditText etPlayerName, etFatherName, etSurname, etAge, etMobile;
    EditText etRole, etCategory, etStyle, etBasePrice, etNickName;;
    Button btnSubmit;

    PlayerViewModel viewModel;
    Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_register);

        etPlayerName = findViewById(R.id.etplayerName);
        etFatherName = findViewById(R.id.etfatherName);
        etSurname = findViewById(R.id.etsurName);
        etAge = findViewById(R.id.etage);
        etMobile = findViewById(R.id.etmobile);
        etNickName = findViewById(R.id.etnickName);

        etRole = findViewById(R.id.etrole);
        etCategory = findViewById(R.id.etcategory);
        etStyle = findViewById(R.id.etStyle);
        etBasePrice = findViewById(R.id.etBasePrice);

        genderSpinner = findViewById(R.id.etgenderspinner);
        btnSubmit = findViewById(R.id.btnSubmit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        btnSubmit.setOnClickListener(v -> {

            String name = etPlayerName.getText().toString();
            String father = etFatherName.getText().toString();
            String surname = etSurname.getText().toString();
            String age = etAge.getText().toString();
            String mobile = etMobile.getText().toString();
            String price = etBasePrice.getText().toString();
            String category = etCategory.getText().toString();
            String style = etStyle.getText().toString();
            String nickname = etNickName.getText().toString();

            String role = etRole.getText().toString();
            String selectedGender = genderSpinner.getSelectedItem().toString();

            if(name.isEmpty() || father.isEmpty() || role.isEmpty() || price.isEmpty()){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert to RequestBody
            RequestBody playerName = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody fatherName = RequestBody.create(MediaType.parse("text/plain"), father);
            RequestBody surName = RequestBody.create(MediaType.parse("text/plain"), surname);
            RequestBody nickName = RequestBody.create(MediaType.parse("text/plain"), nickname);

            RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category);
            RequestBody styleBody = RequestBody.create(MediaType.parse("text/plain"), style);
            RequestBody basePrice = RequestBody.create(MediaType.parse("text/plain"), price);

            // TEMP: No image
            MultipartBody.Part imagePart = null;

            viewModel.addPlayer(playerName, fatherName, surName,
                    nickName, categoryBody, styleBody, basePrice, imagePart);

            Toast.makeText(this, "Player Submitted", Toast.LENGTH_SHORT).show();
        });
    }
}