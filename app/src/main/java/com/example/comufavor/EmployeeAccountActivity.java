package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EmployeeAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_account);

        View rootView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupMenu();
        setupBottomNav();
    }

    private void setupMenu() {
        findViewById(R.id.menuLogout).setOnClickListener(v -> showLogoutConfirmationDialog());
        findViewById(R.id.menuHistorial).setOnClickListener(v -> {
            startActivity(new Intent(EmployeeAccountActivity.this, HistoryActivity.class));
        });
        findViewById(R.id.menuHabilidades).setOnClickListener(v -> {
            startActivity(new Intent(EmployeeAccountActivity.this, ProfileHabilidadesActivity.class));
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
            android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(params);
        }

        dialogView.findViewById(R.id.btnFinalizar).setOnClickListener(v -> {
            UserPreferences userPrefs = new UserPreferences(this);
            userPrefs.logout();
            userPrefs.clearRemembered();

            Intent intent = new Intent(EmployeeAccountActivity.this, MainActivity.class);
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.btnCancelar).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void setupBottomNav() {
        findViewById(R.id.navPostulado).setOnClickListener(v -> {
            startActivity(new Intent(EmployeeAccountActivity.this, EmployeePostuladosActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.menuPerfil).setOnClickListener(v -> {
            startActivity(new Intent(EmployeeAccountActivity.this, EditProfileActivity.class));
        });
        findViewById(R.id.navRecomen).setOnClickListener(v -> {
            startActivity(new Intent(EmployeeAccountActivity.this, RecommendationsActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            startActivity(new Intent(EmployeeAccountActivity.this, FeedActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navGuardados).setOnClickListener(v -> {
            startActivity(new Intent(EmployeeAccountActivity.this, GuardadosActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        // navCuenta is already active here
    }
}
