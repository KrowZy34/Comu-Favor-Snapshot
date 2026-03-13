package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class JobDetailActivity extends AppCompatActivity {

    private View rootView;
    private LinearLayout actionRow;
    private LinearLayout propuestaEnviadaRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_detail);

        rootView = findViewById(R.id.jobDetailMain);
        actionRow = findViewById(R.id.actionRow);
        propuestaEnviadaRow = findViewById(R.id.propuestaEnviadaRow);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        populateJobData();
        setupPhotoSection();
        setupActions();
        setupBottomNav();
    }

    private void populateJobData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("job_title");
        String location = intent.getStringExtra("job_location");
        String price = intent.getStringExtra("job_price");
        String date = intent.getStringExtra("job_date");

        if (title != null) {
            ((TextView) findViewById(R.id.tvJobTitle)).setText(title);
        }
        if (location != null) {
            ((TextView) findViewById(R.id.tvJobLocation)).setText(location);
        }
        if (price != null) {
            ((TextView) findViewById(R.id.tvJobPrice)).setText(price);
        }
        if (date != null) {
            ((TextView) findViewById(R.id.tvJobDate)).setText(date);
        }

        String status = intent.getStringExtra("job_status");
        if ("propuesta_enviada".equals(status) || "en_proceso".equals(status) || "finalizado".equals(status)) {
            // Simulate that the proposal was already sent or a state that shouldn't show the "Postular" button
            actionRow.setVisibility(View.GONE);
            propuestaEnviadaRow.setVisibility(View.VISIBLE);
        }
    }

    private void setupPhotoSection() {
        boolean hasImages = getIntent().getBooleanExtra("has_images", false);
        LinearLayout photoSection = findViewById(R.id.photoSection);
        if (hasImages) {
            photoSection.setVisibility(View.VISIBLE);
        } else {
            photoSection.setVisibility(View.GONE);
        }
    }

    private void setupActions() {
        findViewById(R.id.btnPostular).setOnClickListener(v -> showPostularDialog());
    }

    private void showPostularDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_postular_confirm, null);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setDimAmount(0.6f);
            android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes(params);
        }

        EditText etPrice = dialogView.findViewById(R.id.etPostularPrice);

        // Set the permanent "S/" prefix and position cursor after it
        etPrice.setText("S/");
        etPrice.setSelection(etPrice.getText().length());

        // Prevent deleting the "S/" prefix
        etPrice.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) return;
                String text = s.toString();
                if (!text.startsWith("S/")) {
                    isUpdating = true;
                    etPrice.setText("S/");
                    etPrice.setSelection(etPrice.getText().length());
                    isUpdating = false;
                }
            }
        });

        dialogView.findViewById(R.id.btnDialogCancelar).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnDialogConfirmar).setOnClickListener(v -> {
            String fullText = etPrice.getText().toString().trim();
            // Extract the numeric part after "S/"
            String numericPart = fullText.startsWith("S/") ? fullText.substring(2).trim() : fullText;

            if (numericPart.isEmpty()) {
                showSnackbar("Ingresa un precio válido");
                return;
            }

            try {
                double amount = Double.parseDouble(numericPart);
                if (amount <= 0) {
                    showSnackbar("Ingresa un precio válido");
                    return;
                }
                // Format with 2 decimals
                String formatted = String.format("S/%.2f", amount);
                etPrice.setText(formatted);
            } catch (NumberFormatException e) {
                showSnackbar("Ingresa un precio válido");
                return;
            }

            dialog.dismiss();
            // Switch to "Propuesta enviada" state
            actionRow.setVisibility(View.GONE);
            propuestaEnviadaRow.setVisibility(View.VISIBLE);
        });

        dialog.show();
    }

    private void setupBottomNav() {
        findViewById(R.id.navPostulado).setOnClickListener(v -> {
            startActivity(new Intent(JobDetailActivity.this, EmployeePostuladosActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navRecomen).setOnClickListener(v -> {
            startActivity(new Intent(JobDetailActivity.this, RecommendationsActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            startActivity(new Intent(JobDetailActivity.this, FeedActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navGuardados).setOnClickListener(v -> {
            startActivity(new Intent(JobDetailActivity.this, GuardadosActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navCuenta).setOnClickListener(v -> {
            startActivity(new Intent(JobDetailActivity.this, EmployeeAccountActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
    }



    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.white, getTheme()));
        // Anchor above bottom nav so it doesn't overlap
        snackbar.setAnchorView(findViewById(R.id.bottomNavMain));
        snackbar.show();
    }
}
