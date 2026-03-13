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

public class EmployeePostuladosActivity extends AppCompatActivity {

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_postulados);

        rootView = findViewById(R.id.employeePostuladosMain);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRecyclerView();
        setupBottomNav();
    }

    private void setupRecyclerView() {
        RecyclerView rvPostulados = findViewById(R.id.rvPostuladoCards);
        rvPostulados.setLayoutManager(new LinearLayoutManager(this));

        List<Job> mockJobs = new ArrayList<>();
        mockJobs.add(new Job("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25", "en_proceso"));
        mockJobs.add(new Job("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25", "confirmado"));
        mockJobs.add(new Job("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25", "propuesta_enviada"));
        mockJobs.add(new Job("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25", "finalizado"));
        mockJobs.add(new Job("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25"));

        PostuladoCardAdapter adapter = new PostuladoCardAdapter(mockJobs);
        rvPostulados.setAdapter(adapter);
    }

    private void setupBottomNav() {
        findViewById(R.id.navPostulado).setOnClickListener(v -> showSnackbar("Ya estás en Postulado"));
        findViewById(R.id.navRecomen).setOnClickListener(v -> {
            startActivity(new Intent(EmployeePostuladosActivity.this, RecommendationsActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            startActivity(new Intent(EmployeePostuladosActivity.this, FeedActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navGuardados).setOnClickListener(v -> {
            startActivity(new Intent(EmployeePostuladosActivity.this, GuardadosActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navPerfil).setOnClickListener(v -> {
            startActivity(new Intent(EmployeePostuladosActivity.this, EmployeeAccountActivity.class));
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
