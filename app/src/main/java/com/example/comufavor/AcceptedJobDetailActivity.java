package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AcceptedJobDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accepted_job_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.acceptedJobDetailMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find Action Rows
        android.view.View actionConfirmadoRow = findViewById(R.id.actionConfirmadoRow);
        android.view.View actionEnProcesoRow = findViewById(R.id.actionEnProcesoRow);
        android.view.View actionFinalizadoRow = findViewById(R.id.actionFinalizadoRow);

        // Get Status
        String status = getIntent().getStringExtra("job_status");
        if ("en_proceso".equals(status)) {
            actionEnProcesoRow.setVisibility(android.view.View.VISIBLE);
        } else if ("finalizado".equals(status)) {
            actionFinalizadoRow.setVisibility(android.view.View.VISIBLE);
        } else {
            actionConfirmadoRow.setVisibility(android.view.View.VISIBLE);
        }

        // Setup Buttons
        findViewById(R.id.btnAccept).setOnClickListener(v -> {
            Toast.makeText(this, "Trabajo aceptado", Toast.LENGTH_SHORT).show();
            // Transition to en_proceso visually
            actionConfirmadoRow.setVisibility(android.view.View.GONE);
            actionEnProcesoRow.setVisibility(android.view.View.VISIBLE);
        });

        findViewById(R.id.btnCancel).setOnClickListener(v -> {
            showCancelConfirmationDialog();
        });

        findViewById(R.id.btnCancelEnProceso).setOnClickListener(v -> {
            showCancelConfirmationDialog();
        });

        findViewById(R.id.btnTrabajoCompleto).setOnClickListener(v -> {
            actionEnProcesoRow.setVisibility(android.view.View.GONE);
            actionFinalizadoRow.setVisibility(android.view.View.VISIBLE);
        });

        // Setup Contact Icons
        findViewById(R.id.ivPhone).setOnClickListener(v -> {
            Toast.makeText(this, "Llamando al reclutador...", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.ivMessage).setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo mensajes...", Toast.LENGTH_SHORT).show();
        });

        setupPhotoSection();
        setupBottomNav();
    }

    private void setupPhotoSection() {
        boolean hasImages = getIntent().getBooleanExtra("has_images", false);
        android.view.View photoSection = findViewById(R.id.photoSection);
        if (hasImages) {
            photoSection.setVisibility(android.view.View.VISIBLE);
        } else {
            photoSection.setVisibility(android.view.View.GONE);
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.navPostulado).setOnClickListener(v -> {
            startActivity(new Intent(AcceptedJobDetailActivity.this, EmployeePostuladosActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navRecomen).setOnClickListener(v -> {
            startActivity(new Intent(AcceptedJobDetailActivity.this, RecommendationsActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            startActivity(new Intent(AcceptedJobDetailActivity.this, FeedActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navGuardados).setOnClickListener(v -> {
            startActivity(new Intent(AcceptedJobDetailActivity.this, GuardadosActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navCuenta).setOnClickListener(v -> {
            startActivity(new Intent(AcceptedJobDetailActivity.this, EmployeeAccountActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
    }



    private void showCancelConfirmationDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_cancel_job, null);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            dialog.getWindow().setAttributes(params);
        }

        dialogView.findViewById(R.id.btnContinuar).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btnReportar).setOnClickListener(v -> {
            dialog.dismiss();
            showReportReasonDialog();
        });

        dialog.show();
    }

    private void showReportReasonDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_cancel_report_reason, null);
        builder.setView(dialogView);

        android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            android.view.WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            dialog.getWindow().setAttributes(params);
        }

        dialogView.findViewById(R.id.btnEnviarReporte).setOnClickListener(v -> {
            android.widget.RadioGroup rg = dialogView.findViewById(R.id.rgReportReasons);
            if (rg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Por favor seleccione un motivo", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Reporte enviado. Trabajo cancelado.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }
}
