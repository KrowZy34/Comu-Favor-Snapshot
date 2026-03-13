package com.example.comufavor;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class ApplicantDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_applicant_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.applicantDetailMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Applicant applicant = (Applicant) getIntent().getSerializableExtra("SELECTED_APPLICANT");
        if (applicant != null) {
            setupUI(applicant);
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        View llActionStep = findViewById(R.id.llActionStep);
        View llPendingStep = findViewById(R.id.llPendingStep);
        View llFinalizedStep = findViewById(R.id.llFinalizedStep);

        findViewById(R.id.btnAccept).setOnClickListener(v -> {
            llActionStep.setVisibility(View.GONE);
            llPendingStep.setVisibility(View.VISIBLE);
        });

        // Simulate employee confirmation by tapping the purple banner
        llPendingStep.findViewById(R.id.llPendingStep).setOnClickListener(v -> {
            llPendingStep.setVisibility(View.GONE);
            llFinalizedStep.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.btnCounterProposal).setOnClickListener(v -> {
            showCounterProposalDialog();
        });

        findViewById(R.id.tvCancelProposal).setOnClickListener(v -> {
            llPendingStep.setVisibility(View.GONE);
            llActionStep.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.tvFinalizeJob).setOnClickListener(v -> {
            // End of process
            finish();
        });
    }

    private void showCounterProposalDialog() {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.dialog_counterproposal);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        android.widget.EditText etPrice = dialog.findViewById(R.id.etCounterPrice);
        TextView btnCancelar = dialog.findViewById(R.id.btnDialogCancelar);
        TextView btnConfirmar = dialog.findViewById(R.id.btnDialogConfirmar);

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnConfirmar.setOnClickListener(v -> {
            String price = etPrice.getText().toString().trim();
            if (price.isEmpty()) {
                android.widget.Toast.makeText(this, "Por favor, ingresa un precio", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            android.widget.Toast.makeText(this, "Contrapropuesta enviada: S/" + price, android.widget.Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            
            findViewById(R.id.llActionStep).setVisibility(View.GONE);
            findViewById(R.id.llPendingStep).setVisibility(View.VISIBLE);
        });

        dialog.show();
    }

    private void setupUI(Applicant applicant) {
        TextView tvName = findViewById(R.id.tvApplicantName);
        TextView tvRating = findViewById(R.id.tvRating);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvDescription = findViewById(R.id.tvDescription);
        ChipGroup chipGroup = findViewById(R.id.chipGroupSkills);

        tvName.setText(applicant.getName());
        tvRating.setText(String.valueOf(applicant.getRating()));
        tvPrice.setText(applicant.getPrice());
        tvDescription.setText(applicant.getDescription());

        List<String> skills = applicant.getSkills();
        if (skills != null) {
            for (String skill : skills) {
                addChipToGroup(chipGroup, skill);
            }
        }
    }

    private void addChipToGroup(ChipGroup group, String skillText) {
        TextView chip = new TextView(this);
        chip.setText(skillText);
        chip.setTextColor(getResources().getColor(R.color.black, getTheme()));
        chip.setTextSize(14);
        chip.setPadding(24, 12, 24, 12);
        chip.setBackgroundResource(R.drawable.bg_rounded_corner);
        chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getRandomChipColor()));

        // Add tag icon
        chip.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tag_outline, 0);
        chip.setCompoundDrawablePadding(12);

        group.addView(chip);
    }

    private int getRandomChipColor() {
        int[] colors = {
                R.color.habilidad_chip_orange,
                R.color.habilidad_chip_purple,
                R.color.habilidad_chip_blue,
                R.color.habilidad_chip_red,
                R.color.habilidad_chip_green
        };
        int randomIndex = new java.util.Random().nextInt(colors.length);
        return getResources().getColor(colors[randomIndex], getTheme());
    }
}
