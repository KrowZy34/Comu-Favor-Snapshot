package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class GuardadosActivity extends AppCompatActivity {

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guardados);

        rootView = findViewById(R.id.guardadosMain);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRecyclerView();
        setupBottomNav();
    }

    private void setupRecyclerView() {
        RecyclerView rvGuardados = findViewById(R.id.rvGuardadoCards);
        rvGuardados.setLayoutManager(new LinearLayoutManager(this));

        List<Job> mockJobs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mockJobs.add(new Job(
                    "Limpieza de local 54 m²",
                    "Lima, Independencia",
                    "S/negociable",
                    "10/10/25"));
        }

        GuardadosJobAdapter adapter = new GuardadosJobAdapter(mockJobs);
        rvGuardados.setAdapter(adapter);
    }

    private void setupBottomNav() {
        findViewById(R.id.navPostulado).setOnClickListener(v -> {
            startActivity(new Intent(GuardadosActivity.this, EmployeePostuladosActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navRecomen).setOnClickListener(v -> {
            startActivity(new Intent(GuardadosActivity.this, RecommendationsActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            startActivity(new Intent(GuardadosActivity.this, FeedActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navGuardados).setOnClickListener(v -> showSnackbar("Ya estás en Guardados"));
        findViewById(R.id.navPerfil).setOnClickListener(v -> {
            startActivity(new Intent(GuardadosActivity.this, EmployeeAccountActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.purple_login, getTheme()));
        snackbar.show();
    }
}
