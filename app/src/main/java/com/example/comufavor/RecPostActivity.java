package com.example.comufavor;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RecPostActivity extends AppCompatActivity {

    private View rootView;
    private TextView tvReclutando, tvPostulando, tvLastWeek;
    private Button btnAddJob;
    private RecyclerView rvJobsHoy, rvJobsPastWeek;

    private boolean isReclutando = true;
    private static List<Job> mockJobs = new ArrayList<>();

    private ActivityResultLauncher<Intent> addJobLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rec_post);

        rootView = findViewById(R.id.recPostMain);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupLauncher();
        initViews();
        setupMockData();
        setupToggle();
        setupBottomNav();

        // Initial state
        updateUIState();
    }

    private void setupLauncher() {
        addJobLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String title = data.getStringExtra("EXTRA_TITLE");
                        String location = data.getStringExtra("EXTRA_LOCATION");
                        String payment = data.getStringExtra("EXTRA_PAYMENT");

                        if (title != null && location != null && payment != null) {
                            mockJobs.add(0, new Job(title, location, payment, "Hoy"));
                            if (isReclutando && rvJobsHoy.getAdapter() != null) {
                                rvJobsHoy.getAdapter().notifyItemInserted(0);
                                rvJobsHoy.scrollToPosition(0);
                            }
                            showSnackbar("¡Trabajo '" + title + "' publicado exitosamente!");
                        }
                    }
                });
    }

    private void initViews() {
        tvReclutando = findViewById(R.id.tvReclutando);
        tvPostulando = findViewById(R.id.tvPostulando);
        tvLastWeek = findViewById(R.id.tvLastWeek);
        btnAddJob = findViewById(R.id.btnAddJob);

        rvJobsHoy = findViewById(R.id.rvJobsHoy);
        rvJobsHoy.setLayoutManager(new LinearLayoutManager(this));

        rvJobsPastWeek = findViewById(R.id.rvJobsPastWeek);
        rvJobsPastWeek.setLayoutManager(new LinearLayoutManager(this));

        btnAddJob.setOnClickListener(v -> {
            Intent intent = new Intent(RecPostActivity.this, AgregarPropuestaActivity.class);
            addJobLauncher.launch(intent);
        });
    }

    private void setupMockData() {
        if (mockJobs.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                mockJobs.add(new Job("Limpieza de local 54 m²", "Lima, Independencia", "S/negociable", "10/10/25"));
            }
        }
    }

    private void setupToggle() {
        tvReclutando.setOnClickListener(v -> {
            if (!isReclutando) {
                isReclutando = true;
                updateUIState();
            }
        });

        tvPostulando.setOnClickListener(v -> {
            if (isReclutando) {
                isReclutando = false;
                updateUIState();
            }
        });
    }

    private void updateUIState() {
        if (isReclutando) {
            tvReclutando.setTextColor(getResources().getColor(R.color.rec_cyan, getTheme()));
            tvReclutando.setTypeface(null, Typeface.BOLD_ITALIC);

            tvPostulando.setTextColor(getResources().getColor(R.color.white, getTheme()));
            tvPostulando.setTypeface(null, Typeface.ITALIC);

            btnAddJob.setVisibility(View.VISIBLE);
            tvLastWeek.setVisibility(View.VISIBLE);
            rvJobsPastWeek.setVisibility(View.VISIBLE);

            rvJobsHoy.setAdapter(new RecJobAdapter(mockJobs));
            rvJobsPastWeek.setAdapter(new RecJobAdapter(mockJobs)); // Using same mock list for demo
        } else {
            tvReclutando.setTextColor(getResources().getColor(R.color.white, getTheme()));
            tvReclutando.setTypeface(null, Typeface.ITALIC);

            tvPostulando.setTextColor(getResources().getColor(R.color.post_red, getTheme()));
            tvPostulando.setTypeface(null, Typeface.BOLD_ITALIC);

            btnAddJob.setVisibility(View.GONE);
            tvLastWeek.setVisibility(View.GONE);
            rvJobsPastWeek.setVisibility(View.GONE);

            rvJobsHoy.setAdapter(new PostJobAdapter(mockJobs));
            rvJobsPastWeek.setAdapter(null);
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.navRecPost).setOnClickListener(v -> showSnackbar("Ya estás en Rec & Post"));
        findViewById(R.id.navMensajes).setOnClickListener(v -> showSnackbar("Nav: Mensajes"));
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            startActivity(new Intent(RecPostActivity.this, FeedActivity.class));
            overridePendingTransition(0, 0); // Disable animation for instantaneous switch
            finish();
        });
        findViewById(R.id.navGuardados).setOnClickListener(v -> showSnackbar("Nav: Guardados"));
        findViewById(R.id.navPerfil).setOnClickListener(v -> showSnackbar("Nav: Perfil"));
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
        snackbar.show();
    }
}
