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

        setupRecyclerView();
        setupLateralTabs();
        setupBottomNav();
        setupTopBar();
    }

    private void setupRecyclerView() {
        RecyclerView rvJobs = findViewById(R.id.rvJobs);
        rvJobs.setLayoutManager(new LinearLayoutManager(this));

        List<Job> mockJobs = new ArrayList<>();
        // Mock data to match the design (e.g. 6 items)
        for (int i = 0; i < 6; i++) {
            mockJobs.add(new Job(
                    "Limpieza de local 54 m²",
                    "Lima, Independencia",
                    "S/negociable",
                    "10/10/25"));
        }

        JobAdapter adapter = new JobAdapter(mockJobs);
        rvJobs.setAdapter(adapter);
    }

    private void setupLateralTabs() {
        findViewById(R.id.tabDestacados).setOnClickListener(v -> showSnackbar("Tab: destacados"));
        findViewById(R.id.tabRecomendados).setOnClickListener(v -> showSnackbar("Tab: Recomendados"));
        findViewById(R.id.tabRecientes).setOnClickListener(v -> showSnackbar("Tab: Recientes"));
        findViewById(R.id.tabHabilidades).setOnClickListener(v -> showSnackbar("Tab: Mis habilidades"));
        findViewById(R.id.tabAyuda).setOnClickListener(v -> showSnackbar("Tab: ayuda?"));
        findViewById(R.id.tabCerrarSesion).setOnClickListener(v -> {
            showSnackbar("Cerrar sesión");
            // Optionally, handle logout here in the future
        });
    }

    private void setupBottomNav() {
        findViewById(R.id.navRecPost).setOnClickListener(v -> {
            startActivity(new android.content.Intent(FeedActivity.this, RecPostActivity.class));
            overridePendingTransition(0, 0);
        });
        findViewById(R.id.navMensajes).setOnClickListener(v -> showSnackbar("Nav: Mensajes"));
        findViewById(R.id.navInicio).setOnClickListener(v -> showSnackbar("Ya estás en Inicio"));
        findViewById(R.id.navGuardados).setOnClickListener(v -> showSnackbar("Nav: Guardados"));

        findViewById(R.id.navPerfil).setOnClickListener(v -> showSnackbar("Nav: Perfil"));
    }

    private void setupTopBar() {
        // Just mock actions for menu and search if needed, Though not explicitly
        // requested as IDs, we can skip or add later
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
        snackbar.show();
    }
}
