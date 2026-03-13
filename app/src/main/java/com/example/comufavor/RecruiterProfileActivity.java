package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class RecruiterProfileActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName, etEmail, etPhone, etAddress;
    private TextView tvFullNameTitle;
    private UserPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recruiter_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recruiterProfileMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userPrefs = new UserPreferences(this);

        initViews();
        loadUserData();
        setupBottomNav();
        setupActionButtons();
    }

    private void initViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        tvFullNameTitle = findViewById(R.id.tvFullNameTitle);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadUserData() {
        String loggedInEmail = userPrefs.getLoggedInEmail();
        if (loggedInEmail == null) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show();
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

    private void setupActionButtons() {
        // Logout button
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });

        // Save button
        findViewById(R.id.btnSave).setOnClickListener(v -> {
            saveChanges();
            handleEmailUpdate();
            Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
        });
    }

    private void showLogoutConfirmationDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_logout_confirm, null);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialogView.findViewById(R.id.btnFinalizar).setOnClickListener(v -> {
            userPrefs.logout();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.btnCancelar).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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
            return;
        }

        if (userPrefs.userExists(newEmail)) {
            Toast.makeText(this, "El correo ya está en uso por otra cuenta.", Toast.LENGTH_SHORT).show();
            etEmail.setText(currentEmail);
        } else {
            userPrefs.updateUserEmail(currentEmail, newEmail);
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.navPublicados).setOnClickListener(v -> {
            startActivity(new Intent(this, PublishedJobsActivity.class));
            finish();
        });
        findViewById(R.id.navPublicar).setOnClickListener(v -> {
            startActivity(new Intent(this, CreateJobActivity.class));
            finish();
        });
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            startActivity(new Intent(this, RecruiterHomeActivity.class));
            finish();
        });
        findViewById(R.id.navHistorial).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
            finish();
        });
        findViewById(R.id.navPerfil).setOnClickListener(v -> {
            // Already here
        });
    }
}
