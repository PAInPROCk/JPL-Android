package com.example.jpl2.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jpl2.R;
import com.example.jpl2.utils.SessionManager;
import com.example.jpl2.viewmodel.PlayerViewModel;

import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PlayerRegisterActivity extends AppCompatActivity {

    EditText etPlayerName, etFatherName, etSurname, etAge, etMobile, etEmail;
    EditText etRole, etCategory, etStyle, etBasePrice, etNickName;

    ImageView playerImage;

    Button btnSubmit, btnUploadImage;

    Uri selectedImageUri;

    Spinner genderSpinner;

    PlayerViewModel viewModel;
    SessionManager session;

    // File Picker Launcher

    private final ActivityResultLauncher<String> imagePicker = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null){
                    selectedImageUri = uri;
                    playerImage.setImageURI(uri);
                }
            }
    );

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
        playerImage = findViewById(R.id.playerImage);
        etPlayerName = findViewById(R.id.etplayerName);
        etFatherName = findViewById(R.id.etfatherName);
        etSurname = findViewById(R.id.etsurName);
        etAge = findViewById(R.id.etage);
        etMobile = findViewById(R.id.etmobile);
        etEmail = findViewById(R.id.etEmail);
        etNickName = findViewById(R.id.etnickName);

        etRole = findViewById(R.id.etrole);
        etCategory = findViewById(R.id.etcategory);
        etStyle = findViewById(R.id.etStyle);
        etBasePrice = findViewById(R.id.etBasePrice);

        genderSpinner = findViewById(R.id.etgenderspinner);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnUploadImage.setOnClickListener(v -> imagePicker.launch("image/*"));
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

        String emailId = etEmail.getText().toString().trim();

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

        RequestBody ageBody = body(age);

        RequestBody mobileBody = body(mobile);

        RequestBody genderBody = body(gender);

        RequestBody emailBody = body(emailId);

        RequestBody categoryBody =
                body(category);

        RequestBody styleBody =
                body(style);

        RequestBody basePrice =
                body(price);

        // TEMP no image upload
        MultipartBody.Part imagePart = null;

        try {
            if (selectedImageUri != null) {

                InputStream inputStream =
                        getContentResolver().openInputStream(selectedImageUri);

                if (inputStream != null) {

                    byte[] bytes = new byte[inputStream.available()];
                    int read = inputStream.read(bytes);
                    inputStream.close();

                    if (read > 0) {
                        RequestBody requestFile =
                                RequestBody.create(
                                        MediaType.parse("image/*"),
                                        bytes
                                );

                        imagePart =
                                MultipartBody.Part.createFormData(
                                        "image",
                                        "player.jpg",
                                        requestFile
                                );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ----------------------------------
        // API Call
        // ----------------------------------
        viewModel.addPlayer(
                this,
                playerName,
                fatherName,
                surName,
                nickName,      // 4th
                ageBody,       // 5th
                mobileBody,
                emailBody,
                genderBody,
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