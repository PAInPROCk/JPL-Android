package com.example.jpl2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.utils.SessionManager;
import com.example.jpl2.viewmodel.PlayerViewModel;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PlayerRegisterActivity extends AppCompatActivity {

    EditText etPlayerName, etFatherName, etSurname, etAge, etMobile;
    EditText etRole, etCategory, etStyle, etBasePrice, etNickName;

    Button btnSubmit;

    Spinner genderSpinner;

    PlayerViewModel viewModel;
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

        setContentView(R.layout.activity_player_register);

        // ----------------------------------
        // Views
        // ----------------------------------
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

        // ----------------------------------
        // Spinner
        // ----------------------------------
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.gender_array,
                        R.layout.simple_spinner_item
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        genderSpinner.setAdapter(adapter);

        // ----------------------------------
        // ViewModel
        // ----------------------------------
        viewModel =
                new ViewModelProvider(this)
                        .get(PlayerViewModel.class);

        // ----------------------------------
        // Submit
        // ----------------------------------
        btnSubmit.setOnClickListener(v -> submitPlayer());
    }

    // ----------------------------------
    // Submit Logic
    // ----------------------------------
    private void submitPlayer() {

        String name =
                etPlayerName.getText().toString().trim();

        String father =
                etFatherName.getText().toString().trim();

        String surname =
                etSurname.getText().toString().trim();

        String age =
                etAge.getText().toString().trim();

        String mobile =
                etMobile.getText().toString().trim();

        String nickname =
                etNickName.getText().toString().trim();

        String role =
                etRole.getText().toString().trim();

        String category =
                etCategory.getText().toString().trim();

        String style =
                etStyle.getText().toString().trim();

        String price =
                etBasePrice.getText().toString().trim();

        String gender =
                genderSpinner.getSelectedItem()
                        .toString();

        // ----------------------------------
        // Validation
        // ----------------------------------
        if (name.isEmpty()
                || father.isEmpty()
                || role.isEmpty()
                || price.isEmpty()) {

            Toast.makeText(this,
                    "Fill all required fields",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        // ----------------------------------
        // Request Bodies
        // ----------------------------------
        RequestBody playerName =
                body(name);

        RequestBody fatherName =
                body(father);

        RequestBody surName =
                body(surname);

        RequestBody nickName =
                body(nickname);

        RequestBody categoryBody =
                body(category);

        RequestBody styleBody =
                body(style);

        RequestBody basePrice =
                body(price);

        // TEMP no image upload
        MultipartBody.Part imagePart = null;

        // ----------------------------------
        // API Call
        // ----------------------------------
        viewModel.addPlayer(
                this,
                playerName,
                fatherName,
                surName,
                nickName,
                categoryBody,
                styleBody,
                basePrice,
                imagePart
        );

        Toast.makeText(this,
                "Player Submitted",
                Toast.LENGTH_SHORT).show();

        clearForm();
    }

    // ----------------------------------
    // Helper RequestBody
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

        etPlayerName.setText("");
        etFatherName.setText("");
        etSurname.setText("");
        etAge.setText("");
        etMobile.setText("");
        etNickName.setText("");

        etRole.setText("");
        etCategory.setText("");
        etStyle.setText("");
        etBasePrice.setText("");

        genderSpinner.setSelection(0);
    }
}