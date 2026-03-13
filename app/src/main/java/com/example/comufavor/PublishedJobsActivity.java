package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PublishedJobsActivity extends AppCompatActivity {

    private RecyclerView rvPublishedJobs;
    private RecruiterJobAdapter adapter;
    private List<RecruiterJob> jobList;
    private TextView tvEmptyMessage;
    private JobPreferences jobPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_published_jobs);

        jobPrefs = new JobPreferences(this);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        rvPublishedJobs = findViewById(R.id.rvPublishedJobs);
        rvPublishedJobs.setLayoutManager(new LinearLayoutManager(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.publishedJobsMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadJobs();
    }

    private void loadJobs() {
        jobList = jobPrefs.getAllJobs();
        
        // Mocking some jobs with 0 proposals for style demonstration if list is empty
        if (jobList.isEmpty()) {
            jobList.add(new RecruiterJob("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25", 5));
            jobList.add(new RecruiterJob("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25", 0));
            jobList.add(new RecruiterJob("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25", 0));
        }

        if (jobList.isEmpty()) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            rvPublishedJobs.setVisibility(View.GONE);
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
            rvPublishedJobs.setVisibility(View.VISIBLE);
            adapter = new RecruiterJobAdapter(jobList, job -> {
                Intent intent = new Intent(this, JobProposalsActivity.class);
                intent.putExtra("SELECTED_JOB", job);
                startActivity(intent);
            });
            rvPublishedJobs.setAdapter(adapter);
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.navPublicados).setOnClickListener(v -> {
            // Already here
        });
        findViewById(R.id.navPublicar).setOnClickListener(v -> {
            startActivity(new Intent(this, CreateJobActivity.class));
        });
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            Intent intent = new Intent(this, RecruiterHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        findViewById(R.id.navHistorial).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
        });
        findViewById(R.id.navPerfil).setOnClickListener(v -> {
            // Profile action
        });
    }
}
