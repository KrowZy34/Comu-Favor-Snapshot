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

import java.util.ArrayList;
import java.util.List;

public class RecruiterHomeActivity extends AppCompatActivity {

    private RecyclerView rvRecruiterJobs;
    private RecruiterJobAdapter adapter;
    private List<RecruiterJob> jobList;
    private TextView tvEmptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recruiter_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recruiterHomeMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        rvRecruiterJobs = findViewById(R.id.rvRecruiterJobs);
        rvRecruiterJobs.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadJobs();
    }

    private void loadJobs() {
        JobPreferences jobPrefs = new JobPreferences(this);
        jobList = jobPrefs.getAllJobs();

        // Ensure we have at least the initial mock data if no jobs exist yet
        if (jobList.isEmpty()) {
            jobList.add(new RecruiterJob("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25", 5));
        }
        
        if (jobList.isEmpty()) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            rvRecruiterJobs.setVisibility(View.GONE);
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
            rvRecruiterJobs.setVisibility(View.VISIBLE);
            adapter = new RecruiterJobAdapter(jobList, job -> {
                Intent intent = new Intent(this, JobProposalsActivity.class);
                intent.putExtra("SELECTED_JOB", job);
                startActivity(intent);
            });
            rvRecruiterJobs.setAdapter(adapter);
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.navPublicados).setOnClickListener(v -> {
            startActivity(new Intent(this, PublishedJobsActivity.class));
        });
        findViewById(R.id.navPublicar).setOnClickListener(v -> {
            startActivity(new Intent(this, CreateJobActivity.class));
        });
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            // Already here
        });
        findViewById(R.id.navHistorial).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
        });
        findViewById(R.id.navPerfil).setOnClickListener(v -> {
            // Could go to some account activity
        });
    }
}
