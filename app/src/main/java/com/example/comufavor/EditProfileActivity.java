package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName, etEmail, etPhone, etAddress;
    private TextView tvFullNameTitle;
    private UserPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.editProfileMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userPrefs = new UserPreferences(this);

        initViews();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        tvFullNameTitle = findViewById(R.id.tvFullNameTitle);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadUserData() {
        String loggedInEmail = userPrefs.getLoggedInEmail();
        if (loggedInEmail == null) {
            Toast.makeText(this, "Session expired", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String fn = userPrefs.getUserFirstName(loggedInEmail);
        String ln = userPrefs.getUserLastName(loggedInEmail);

        etFirstName.setText(fn);
        etLastName.setText(ln);
        etPhone.setText(userPrefs.getUserPhone(loggedInEmail));
        etAddress.setText(userPrefs.getUserAddress(loggedInEmail));
        etEmail.setText(loggedInEmail);

        updateTitle(fn, ln, loggedInEmail);
    }

    private void updateTitle(String fn, String ln, String email) {
        if (!fn.trim().isEmpty() || !ln.trim().isEmpty()) {
            tvFullNameTitle.setText((fn + " " + ln).trim());
        } else {
            tvFullNameTitle.setText(userPrefs.getUserName(email));
        }
    }

    private void setupListeners() {
        TextWatcher saveWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                saveChanges();
            }
        };

        etFirstName.addTextChangedListener(saveWatcher);
        etLastName.addTextChangedListener(saveWatcher);
        etPhone.addTextChangedListener(saveWatcher);
        etAddress.addTextChangedListener(saveWatcher);

        // For email, we might only want to save on focus lost to avoid constant migrations while typing
        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                handleEmailUpdate();
            }
        });
    }

    private void saveChanges() {
        String currentEmail = userPrefs.getLoggedInEmail();
        if (currentEmail == null) return;

        String fn = etFirstName.getText().toString().trim();
        String ln = etLastName.getText().toString().trim();
        String ph = etPhone.getText().toString().trim();
        String ad = etAddress.getText().toString().trim();

        userPrefs.updateProfileFields(currentEmail, fn, ln, ph, ad);
        updateTitle(fn, ln, currentEmail);
    }

    private void handleEmailUpdate() {
        String currentEmail = userPrefs.getLoggedInEmail();
        String newEmail = etEmail.getText().toString().trim();

        if (currentEmail == null || newEmail.isEmpty() || currentEmail.equalsIgnoreCase(newEmail)) {
            return; // No auth, empty input, or no change
        }

        if (userPrefs.userExists(newEmail)) {
            Toast.makeText(this, "El correo ya está en uso por otra cuenta.", Toast.LENGTH_SHORT).show();
            etEmail.setText(currentEmail); // Revert
        } else {
            boolean success = userPrefs.updateUserEmail(currentEmail, newEmail);
            if (success) {
                Toast.makeText(this, "Correo actualizado correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar el correo.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
