package com.example.comufavor;

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

public class FeedActivity extends AppCompatActivity {

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed);

        rootView = findViewById(R.id.feedMain);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupRecruitedRecyclerView();
        setupRecyclerView();
        setupBottomNav();
    }

    private void setupRecruitedRecyclerView() {
        RecyclerView rvRecruited = findViewById(R.id.rvRecruitedJobs);
        rvRecruited.setLayoutManager(new LinearLayoutManager(this));

        List<String> recruitedTitles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            recruitedTitles.add("Limpieza de local 54 m²");
        }

        RecruitedJobAdapter recruitedAdapter = new RecruitedJobAdapter(recruitedTitles);
        rvRecruited.setAdapter(recruitedAdapter);
    }

    private void setupRecyclerView() {
        RecyclerView rvJobs = findViewById(R.id.rvJobs);
        rvJobs.setLayoutManager(new LinearLayoutManager(this));

        List<Job> mockJobs = new ArrayList<>();
        // Mock data matching the design
        for (int i = 0; i < 3; i++) {
            mockJobs.add(new Job(
                    "Limpieza de local 54 m²",
                    "Lima, Independencia",
                    "S/negociable",
                    "10/10/25"));
        }

        EmployeeJobAdapter adapter = new EmployeeJobAdapter(mockJobs);
        rvJobs.setAdapter(adapter);
    }

    private void setupBottomNav() {
        findViewById(R.id.navPostulado).setOnClickListener(v -> {
            startActivity(new android.content.Intent(FeedActivity.this, EmployeePostuladosActivity.class));
            overridePendingTransition(0, 0);
        });
        findViewById(R.id.navRecomen).setOnClickListener(v -> {
            startActivity(new android.content.Intent(FeedActivity.this, RecommendationsActivity.class));
            overridePendingTransition(0, 0);
        });
        findViewById(R.id.navInicio).setOnClickListener(v -> showSnackbar("Ya estás en Inicio"));
        findViewById(R.id.navGuardados).setOnClickListener(v -> {
            startActivity(new android.content.Intent(FeedActivity.this, GuardadosActivity.class));
            overridePendingTransition(0, 0);
        });
        findViewById(R.id.navCuenta).setOnClickListener(v -> {
            startActivity(new android.content.Intent(FeedActivity.this, EmployeeAccountActivity.class));
            overridePendingTransition(0, 0);
        });
    }



    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.purple_login, getTheme()));
        snackbar.show();
    }
}
